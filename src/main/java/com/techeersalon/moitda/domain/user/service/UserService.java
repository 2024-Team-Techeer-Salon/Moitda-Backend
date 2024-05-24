package com.techeersalon.moitda.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.techeersalon.moitda.domain.user.dto.mapper.UserMapper;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

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

    public UserProfileRes findCurrentUserProfile() {
        User user = this.getLoginUser();
        return userMapper.toUserProfile(user);
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
//        urls[0] = processImage(profileImage, user.getProfileImage(), "user/custom/profile/");
        urls[0] = processImage(profileImage, "user/custom/profile/");
        // 배너 이미지 처리
        urls[1] = processImage(bannerImage, "user/custom/banner/");

        user.updateProfile(updateUserReq, urls[0], urls[1]);
        userRepository.save(user);
    }

    private String processImage(MultipartFile image, String s3Folder) throws IOException {

        String imageFileName = image.getOriginalFilename();
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
        /*
        String imageFileName = image.getOriginalFilename();
        // 기존의 url과 다른 경우 혹은 같은 경우
        // 서버에서 저장하는 것은 url. 클라이언트에서 오는 것은 파일 형태이기 때문에 의미가 없음.
        if (!currentImageUrl.equals(imageFileName)) {
            // 기존 파일 s3에서 삭제
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Folder + currentImageUrl));

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
         */
    }

    public User getLoginUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loginUser = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        return loginUser;
    }
}