package com.techeersalon.moitda.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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
import com.techeersalon.moitda.domain.user.dto.request.UserTokenReq;
import com.techeersalon.moitda.domain.user.dto.response.UserIdRes;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.Role;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.exception.UnAuthorizedAccessException;
import com.techeersalon.moitda.domain.user.exception.UserAlreadyRegisteredException;
import com.techeersalon.moitda.domain.user.exception.UserNotFoundException;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import com.techeersalon.moitda.global.jwt.JwtToken;
import com.techeersalon.moitda.global.jwt.Service.JwtService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AmazonS3 amazonS3;
    private final JwtService jwtService;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${baseProfilePath}")
    private String baseProfilePath;

    @Value("${baseBannerPath}")
    private String baseBannerPath;

    public void signup(SignUpReq signUpReq) {
        User user = this.getLoginUser();
        if (user.getRole().equals(Role.GUEST)) {
            user.signupUser(signUpReq);
            userRepository.save(user);
        } else {
            throw new UserAlreadyRegisteredException();
        }
    }

    public void logout() {
        User user = this.getLoginUser();
        user.onLogout();
        userRepository.save(user);
    }

    public UserIdRes findCurrentUserProfile() {
        User user = this.getLoginUser();
        return userMapper.toUserId(user);
    }

    public UserProfileRes findUserProfileForChat(Long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser
                .orElseThrow(UserNotFoundException::new);
        return userMapper.toUserProfileForChat(user);
    }


    public UserProfileRes findUserProfile(Long userId) {

        // userId를 통해 찾은 유저 정보.
        Optional<User> optionalUser = userRepository.findById(userId);
        User foundUser = optionalUser
                .orElseThrow(UserNotFoundException::new);

        User user = this.getLoginUser();

        boolean owner = user == foundUser;

        return userMapper.toUserProfile(foundUser, owner);
    }

    public void updateUserProfile(UpdateUserReq updateUserReq, String profileUrl, String bannerUrl, MultipartFile profileImage, MultipartFile bannerImage) throws IOException {
        User user = this.getLoginUser();
        String[] urls = new String[2];

        // URL 디코딩 처리
        profileUrl = profileUrl != null ? URLDecoder.decode(profileUrl, StandardCharsets.UTF_8.name()) : null;
        bannerUrl = bannerUrl != null ? URLDecoder.decode(bannerUrl, StandardCharsets.UTF_8.name()) : null;

        // 프로필 이미지 처리
        if (profileUrl == null && (profileImage != null && !profileImage.isEmpty())) {
            if (user.getProfileImage() != null) {
                deleteExistingImage(user.getProfileImage(), baseProfilePath, "user/custom/profile/");
            }
            urls[0] = processImage(profileImage, "user/custom/profile/");
        } else {
            urls[0] = profileUrl != null ? profileUrl : user.getProfileImage();
        }

        // 배너 이미지 처리
        if (bannerUrl == null && (bannerImage != null && !bannerImage.isEmpty())) {
            if (user.getBannerImage() != null) {
                deleteExistingImage(user.getBannerImage(), baseBannerPath, "user/custom/banner/");
            }
            urls[1] = processImage(bannerImage, "user/custom/banner/");
        } else {
            urls[1] = bannerUrl != null ? bannerUrl : user.getBannerImage();
        }

        user.updateProfile(updateUserReq, urls[0], urls[1]);
        userRepository.save(user);

        List<Meeting> meetings = meetingRepository.getMeetingsByUserId(user.getId());
        List<MeetingParticipant> meetingParticipantList = meetingParticipantRepository.getParticipantsByUserId(user.getId());

        meetings.forEach(meeting -> {
            meeting.updateUsername(user.getUsername());
            meetingRepository.save(meeting);
        });

        meetingParticipantList.forEach(meetingParticipant -> {
            meetingParticipant.updateUsername(user.getUsername());
            meetingParticipantRepository.save(meetingParticipant);
        });
    }

    void deleteExistingImage(String imageUrl, String basePath, String s3Folder) throws UnsupportedEncodingException {
        String encodedString = imageUrl.replace(basePath, "");
        String decodedString = URLDecoder.decode(encodedString, "UTF-8");
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Folder + decodedString));
    }

    String processImage(MultipartFile image, String s3Folder) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file must not be null or empty");
        }

        String imageFileName = image.getOriginalFilename();
        if (imageFileName == null) {
            throw new IllegalArgumentException("Image file name must not be null");
        }

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
    }

    public User getLoginUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loginUser = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        return loginUser;
    }

    public JwtToken reissueToken(UserTokenReq userTokenReq) {
        // Refresh Token 검증
        if (!jwtService.isTokenValid(userTokenReq.getRefreshToken())) {
            throw new UnAuthorizedAccessException();
        }

        Object[] emailAndSocialType = jwtService.extractEmailAndSocialType(userTokenReq.getAccessToken());
        if (emailAndSocialType.length >= 2) {
            String email = (String) emailAndSocialType[0];
            SocialType socialType = (SocialType) emailAndSocialType[1];
            User user = userRepository.findBySocialTypeAndEmail(socialType, email)
                    .orElseThrow(UserNotFoundException::new);
            String refreshToken = user.getRefreshToken();

            // db에 리프레시 토큰 없을 경우(logout)
            if (refreshToken == null || !refreshToken.equals(userTokenReq.getRefreshToken())) {
                throw new UnAuthorizedAccessException();
            }

            String reissueAccessToken = jwtService.createAccessToken(email, socialType);
            String reissueRefreshToken = jwtService.createRefreshToken();
            user.updateRefreshToken(reissueRefreshToken);

            return JwtToken.builder()
                    .accessToken(reissueAccessToken)
                    .refreshToken(reissueRefreshToken)
                    .build();
        }

        throw new UnAuthorizedAccessException();
    }
}
