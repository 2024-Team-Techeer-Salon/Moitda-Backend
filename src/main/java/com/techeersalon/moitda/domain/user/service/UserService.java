package com.techeersalon.moitda.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.dto.mapper.UserMapper;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.response.RecordsRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.exception.UserNotFoundException;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.s3.exception.S3Exception;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingRepository meetingRepository;
    private final UserMapper userMapper;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void signup(SignUpReq signUpReq) {
//        수정필요
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        user.signupUser(signUpReq);

        userRepository.save(user);
    }

    public void logout() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        user.onLogout();

        userRepository.save(user);
    }


    public UserProfileRes findUserProfile(Long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toUserProfile(user);
    }

    public void updateUserProfile(UpdateUserReq updateUserReq, MultipartFile profileImage, MultipartFile bannerImage) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        // 저장되어 있는 url이 같은 경우 패스.
        // 그냥 지우고 저장..?

        String[] urls = new String[2];
        // 프로필 이미지 처리
        if (profileImage.isEmpty()) {
            urls[0] = "https://moitda-bucket.s3.ap-northeast-2.amazonaws.com/user/default/DefaultProfileImage.png"; //그냥 url 넘기
        } else {
            // 이미지가 비어있지 않은 경우 이미지를 S3에 업로드
            String profileFileName = profileImage.getOriginalFilename();
            String extension = profileFileName.substring(profileFileName.lastIndexOf("."));
            String s3ProfileFileName = "user/custom/profile/" + UUID.randomUUID().toString().substring(0, 10) + profileFileName;
            InputStream is1 = profileImage.getInputStream();
            byte[] bytes1 = IOUtils.toByteArray(is1);
            ObjectMetadata metadata1 = new ObjectMetadata();
            metadata1.setContentType("image/" + extension);
            metadata1.setContentLength(bytes1.length);
            ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(bytes1);

            try {
                PutObjectRequest putObjectRequest1 =
                        new PutObjectRequest(bucketName, s3ProfileFileName, byteArrayInputStream1, metadata1)
                                .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3.putObject(putObjectRequest1);

                // 첫 번째 이미지의 URL을 저장
                urls[0] = amazonS3.getUrl(bucketName, s3ProfileFileName).toString();
            } catch (Exception e) {
                throw new S3Exception();
            } finally {
                byteArrayInputStream1.close();
                is1.close();
            }
        }

        // 배너 이미지 처리
        if (bannerImage.isEmpty()) {
            urls[1] = "https://moitda-bucket.s3.ap-northeast-2.amazonaws.com/user/default/DefaultProfileImage.png";
        } else {
            // 이미지가 비어있지 않은 경우 이미지를 S3에 업로드
            String bannerFileName = bannerImage.getOriginalFilename();
            String extension2 = bannerFileName.substring(bannerFileName.lastIndexOf("."));
            String s3BannerFileName = "user/custom/banner/" + UUID.randomUUID().toString().substring(0, 10) + bannerFileName;
            InputStream is2 = bannerImage.getInputStream();
            byte[] bytes2 = IOUtils.toByteArray(is2);
            ObjectMetadata metadata2 = new ObjectMetadata();
            metadata2.setContentType("image/" + extension2);
            metadata2.setContentLength(bytes2.length);
            ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bytes2);

            try {
                PutObjectRequest putObjectRequest2 =
                        new PutObjectRequest(bucketName, s3BannerFileName, byteArrayInputStream2, metadata2)
                                .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3.putObject(putObjectRequest2);

                // 두 번째 이미지의 URL을 저장
                urls[1] = amazonS3.getUrl(bucketName, s3BannerFileName).toString();
            } catch (Exception e) {
                throw new S3Exception();
            } finally {
                byteArrayInputStream2.close();
                is2.close();
            }
        }
        user.updateProfile(updateUserReq, urls[0], urls[1]);

        userRepository.save(user);
    }

    public User getLoginUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loginUser = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return loginUser;
    }

    public RecordsRes getUserMeetingRecords(Long userId) {

        if (userRepository.existsById(userId)) {
            List<MeetingParticipant> meetingParticipantList =
                    meetingParticipantRepository.findByUserIdAndIsWaiting(userId, false);
            List<Long> meetingIds = meetingParticipantList
                    .stream()
                    .map(MeetingParticipant::getMeetingId)
                    .collect(Collectors.toList());
            List<Meeting> userMeetings = meetingRepository.findByIdIn(meetingIds);

            return userMapper.toUserMeetingRecord(userMeetings);
        }

        throw new UserNotFoundException();
    }
}