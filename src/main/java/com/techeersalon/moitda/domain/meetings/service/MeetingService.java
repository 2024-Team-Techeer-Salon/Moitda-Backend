
package com.techeersalon.moitda.domain.meetings.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantListMapper;
import com.techeersalon.moitda.domain.meetings.dto.mapper.MeetingParticipantMapper;
import com.techeersalon.moitda.domain.meetings.dto.request.ApprovalParticipantReq;
import com.techeersalon.moitda.domain.meetings.dto.request.ChangeMeetingInfoReq;
import com.techeersalon.moitda.domain.meetings.dto.request.CreateMeetingReq;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateMeetingRes;
import com.techeersalon.moitda.domain.meetings.dto.response.CreateParticipantRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetLatestMeetingListRes;
import com.techeersalon.moitda.domain.meetings.dto.response.GetMeetingDetailRes;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MeetingIsFullException;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MeetingNotFoundException;
import com.techeersalon.moitda.domain.meetings.exception.meeting.MeetingPageNotFoundException;
import com.techeersalon.moitda.domain.meetings.exception.participant.AlreadyParticipatingOrAppliedException;
import com.techeersalon.moitda.domain.meetings.exception.participant.MeetingParticipantNotFoundException;
import com.techeersalon.moitda.domain.meetings.exception.participant.NotAuthorizedToAppproveException;
import com.techeersalon.moitda.domain.meetings.repository.MeetingImageRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.service.UserService;
import com.techeersalon.moitda.global.s3.exception.S3Exception;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final UserService userService;
    private final MeetingImageRepository meetingImageRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 한 페이지당 meeting 데이터 개수
    //private final int pageSize = 32;

    /*
     * 미팅 생성 메소드
     * Meeting, MeetingParticipant 생성
     * Meeting에 참가자 1명 추가
     * */
    public CreateMeetingRes createMeeting(CreateMeetingReq dto, List<MultipartFile> images) throws IOException {
        User loginUser = userService.getLoginUser();
        Meeting entity = dto.toEntity(loginUser);

        // 최대 참가 인원 유효성 검사
        entity.validateMaxParticipantsCount();
        Meeting meeting = meetingRepository.save(entity);

        // MeetingParticipant 엔티티 생성 및 저장
        MeetingParticipant participant = MeetingParticipantMapper.toEntity(meeting);
        participant.notNeedToApprove();
        meetingParticipantRepository.save(participant);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageName = image.getOriginalFilename();
                String extension = imageName.substring(imageName.lastIndexOf(".") + 1);
                String s3imageFileName = "meeting/" + UUID.randomUUID().toString().substring(0, 10) + "_" + imageName;
                InputStream is = image.getInputStream();
                byte[] bytes = IOUtils.toByteArray(is);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/" + extension);
                metadata.setContentLength(bytes.length);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                try {
                    PutObjectRequest putObjectRequest =
                            new PutObjectRequest(bucketName, s3imageFileName, byteArrayInputStream, metadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead);
                    amazonS3.putObject(putObjectRequest);

                    // 이미지의 URL을 저장
                    String imageUrl = amazonS3.getUrl(bucketName, s3imageFileName).toString();

                    // MeetingImage 엔티티에 저장
                    MeetingImage meetingImage = new MeetingImage(imageUrl, entity.getId()); // 생성자를 사용하여 설정

                    // MeetingImage 저장
                    meetingImageRepository.save(meetingImage);

                } catch (Exception e) {
                    throw new S3Exception();
                } finally {
                    byteArrayInputStream.close();
                    is.close();
                }
            }
        }

        return CreateMeetingRes.from(meeting.getId());
    }

    /*
     * 미팅 상세 조회 메소드
     * 미팅 데이터 값 중 필요한 내용과 미팅 참가자 리스트 reponse에 담음
     * */
    public GetMeetingDetailRes findMeetingById(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);

        List<MeetingImage> imageList = meetingImageRepository.findByMeetingId(meetingId);

        Optional<MeetingParticipant> participantOptional = meetingParticipantRepository.findByMeetingIdAndIsWaiting(meetingId, Boolean.FALSE);
        participantOptional.orElseThrow(MeetingParticipantNotFoundException::new);

        List<MeetingParticipantListMapper> participantDtoList = participantOptional
                .stream()
                .map(MeetingParticipantListMapper::from)
                .collect(Collectors.toList());
        return GetMeetingDetailRes.of(meeting, participantDtoList, imageList);
    }

//    private MeetingParticipantMapper mapToDto(MeetingParticipant meetingParticipant) {
//
//        return new MeetingParticipantMapper(
//                meetingParticipant.getId()
//        );
//    }

    /*
     * 참가자 신청 메소드
     * 미팅에 선착순인 경우와 승인이 필요한 경우
     * */
    public CreateParticipantRes addParticipantOfMeeting(Long meetingId) {
        User loginUser = userService.getLoginUser();
        Long loginUserId = loginUser.getId();
        Meeting meeting = null;

        // 이미 유저가 미팅에 참가자 거나 참가 신청을 했을 경우 예외처리
        if (meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUserId)) {
            throw new AlreadyParticipatingOrAppliedException();
        }
        //log.info(String.valueOf(meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUserId)));
        meeting = this.getMeetingById(meetingId);

        // 미팅 참가자가 이미 최대 참가자수를 넘을 경우 예외처리
        if (meeting.getParticipantsCount() >= meeting.getMaxParticipantsCount()) {
            throw new MeetingIsFullException();
        }

        MeetingParticipant entity = MeetingParticipantMapper.toEntity(meeting, loginUserId);

        // 미팅이 선착순일 경우 바로 미팅 참가자로 변경
        if (!meeting.getApprovalRequired()) {
            entity.notNeedToApprove();
            meeting.increaseParticipantsCnt();
        }

        MeetingParticipant participant = meetingParticipantRepository.save(entity);

        return CreateParticipantRes.from(participant.getId());
//        if (!meetingParticipantRepository.existsByMeetingIdAndUserId(meetingId, loginUser.getId())) {
//            meeting = this.getMeetingById(meetingId);
//            if (meeting.getParticipantsCount() < meeting.getMaxParticipantsCount()) {
//
//                MeetingParticipant participant = MeetingParticipantMapper.toEntity(meeting);
//
//                if (!meeting.getApprovalRequired()) {
//                    participant.notNeedToApprove();
//                    meeting.increaseParticipantsCnt();
//                }
//
//                meetingParticipantRepository.save(participant);
//
//            } else {
//                throw new MeetingIsFullException();
//            }
//
//        } else {
//            throw new AlreadyParticipatingOrAppliedException();
//        }
    }

    public void approvalParticipant(ApprovalParticipantReq dto) {
        // 참가자 존재 예외처리
        MeetingParticipant participant = meetingParticipantRepository.findById(dto.getParticipantId()).orElseThrow(MeetingParticipantNotFoundException::new);
        // 승인한 미팅이 참가자 미팅이 다른 경우 예외처리
        if (participant.getMeetingId().longValue() != dto.getMeetingId().longValue()) {
            throw new NotAuthorizedToAppproveException();
        }
        participant.notNeedToApprove();

        if (dto.getIsApproval()) { // 승인 할 경우
            Meeting meeting = this.getMeetingById(participant.getMeetingId());
            meeting.increaseParticipantsCnt();
            meetingParticipantRepository.save(participant);
        } else { // 거절 할 경우
            meetingParticipantRepository.delete(participant);
            //participant.delete();
        }
    }

    //    public List<Meeting> getUserMeetingList(){
//        Long loginUserId = userService.getLoginUser().getId();
//        return meetingRepository.findByUserId(loginUserId);
//    }
    /*
     * 미팅 리스트 조회 메소드
     * 간략화 된 미팅 내용을 최대 32개인 한 페이지로 준다.
     * */
    public List<GetLatestMeetingListRes> latestMeetings(int page, int pageSize) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createAt"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Page<Meeting> meetings = meetingRepository.findAll(pageable);
        if (meetings.isEmpty()) {
            throw new MeetingPageNotFoundException();
        }

        List<GetLatestMeetingListRes> meetingListResList = new ArrayList<>();
        for (Meeting meeting : meetings) {
            Long meetingId = meeting.getId();
            List<MeetingImage> images = meetingImageRepository.findByMeetingId(meetingId);
            GetLatestMeetingListRes meetingListRes = GetLatestMeetingListRes.from(meeting, images);
            meetingListResList.add(meetingListRes);
        }

        return meetingListResList;
    }

    /*
     * 미팅 삭제 메소드
     * 미팅, 미팅의 참가자 모두 softDelete
     * */
    public void deleteMeeting(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);

        Optional<MeetingParticipant> participantOptional = meetingParticipantRepository.findByMeetingId(meetingId);
        participantOptional.orElseThrow(MeetingParticipantNotFoundException::new);

        meetingRepository.delete(meeting);
        meetingImageRepository.deleteByMeetingId(meetingId);
        //meetingParticipantRepository.save(participant);
        meetingParticipantRepository.deleteAll(participantOptional.stream().toList());
        //meetingRepository.save(meeting);
    }

    private Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
    }

    /*
     * 미팅 수정 메소드
     * 미팅 전체 내용 수정
     * */
    public void modifyMeeting(Long meetingId, ChangeMeetingInfoReq dto, List<MultipartFile> images) throws IOException {
        Meeting meeting = this.getMeetingById(meetingId);
        meeting.updateInfo(dto);
        // 일단 db에 있는 내역 다 날림.
        // s3는 일단 건드리지 않고 진행..
        meetingImageRepository.deleteByMeetingId(meetingId);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageName = image.getOriginalFilename();
                String extension = imageName.substring(imageName.lastIndexOf(".") + 1);
                String s3imageFileName = "meeting/" + UUID.randomUUID().toString().substring(0, 10) + "_" + imageName;
                InputStream is = image.getInputStream();
                byte[] bytes = IOUtils.toByteArray(is);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/" + extension);
                metadata.setContentLength(bytes.length);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                try {
                    PutObjectRequest putObjectRequest =
                            new PutObjectRequest(bucketName, s3imageFileName, byteArrayInputStream, metadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead);
                    amazonS3.putObject(putObjectRequest);

                    // 이미지의 URL을 저장
                    String imageUrl = amazonS3.getUrl(bucketName, s3imageFileName).toString();

                    // MeetingImage 엔티티에 저장
                    MeetingImage meetingImage = new MeetingImage(imageUrl, meetingId); // 생성자를 사용하여 설정

                    // MeetingImage 저장
                    meetingImageRepository.save(meetingImage);

                } catch (Exception e) {
                    throw new S3Exception();
                } finally {
                    byteArrayInputStream.close();
                    is.close();
                }
            }
        }

        meetingRepository.save(meeting);
    }

    public void endMeeting(Long meetingId) {
        Meeting meeting = this.getMeetingById(meetingId);
        meeting.updateEndTime(LocalDateTime.now().toString());
    }
}