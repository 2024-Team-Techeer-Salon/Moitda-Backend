package com.techeersalon.moitda.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.techeersalon.moitda.domain.meetings.entity.Meeting;
import com.techeersalon.moitda.domain.meetings.entity.MeetingImage;
import com.techeersalon.moitda.domain.meetings.entity.MeetingParticipant;
import com.techeersalon.moitda.domain.meetings.repository.MeetingImageRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingParticipantRepository;
import com.techeersalon.moitda.domain.meetings.repository.MeetingRepository;
import com.techeersalon.moitda.domain.user.dto.mapper.UserMapper;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.response.RecordsRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.Role;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.exception.UserAlreadyRegisteredException;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingImageRepository meetingImageRepository;
    private final UserMapper userMapper;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${defaultProfileUrl}")
    private String defaultProfileUrl;

    @Value("${defaultBannerUrl}")
    private String defaultBannerUrl;

    public void signup(SignUpReq signUpReq) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (user.getRole().equals(Role.GUEST)) {

            user.signupUser(signUpReq);
            userRepository.save(user);

        } else {
            throw new UserAlreadyRegisteredException();
        }

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
        User user = this.getLoginUser();

        String[] urls = new String[2];

        // 프로필 이미지 처리
        urls[0] = processImage(profileImage, user.getProfileImage(), "user/custom/profile/", defaultProfileUrl);

        // 배너 이미지 처리
        urls[1] = processImage(bannerImage, user.getBannerImage(), "user/custom/banner/", defaultBannerUrl);

        user.updateProfile(updateUserReq, urls[0], urls[1]);
        userRepository.save(user);
    }

    private String processImage(MultipartFile image, String currentImageUrl, String s3Folder, String defaultUrl) throws IOException {
        if (image == null) {
            return defaultUrl;
        }

        String imageFileName = image.getOriginalFilename();
        if (!currentImageUrl.equals(imageFileName)) {
            String extension = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);
            String s3FileName = s3Folder + UUID.randomUUID().toString().substring(0, 10) + imageFileName;

            byte[] bytes = IOUtils.toByteArray(image.getInputStream());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(bytes.length);

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3.putObject(putObjectRequest);
                return amazonS3.getUrl(bucketName, s3FileName).toString();
            } catch (Exception e) {
                throw new S3Exception();
            }
        } else {
            return currentImageUrl;
        }
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
            // 각 회의에 대한 이미지 가져오기
            List<List<MeetingImage>> meetingImages = new ArrayList<>();
            for (Meeting meeting : userMeetings) {
                List<MeetingImage> meetingImage = meetingImageRepository.findByMeetingId(meeting.getId());
                meetingImages.add(meetingImage);
            }

            return userMapper.toUserMeetingRecord(userMeetings, meetingImages);
        }

        throw new UserNotFoundException();
    }


}