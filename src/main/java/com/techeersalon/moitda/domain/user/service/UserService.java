package com.techeersalon.moitda.domain.user.service;

import com.techeersalon.moitda.domain.user.dto.mapper.UserMapper;
import com.techeersalon.moitda.domain.user.dto.request.SignUpReq;
import com.techeersalon.moitda.domain.user.dto.request.UpdateUserReq;
import com.techeersalon.moitda.domain.user.dto.response.UserProfileRes;
import com.techeersalon.moitda.domain.user.entity.SocialType;
import com.techeersalon.moitda.domain.user.entity.User;
import com.techeersalon.moitda.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    //나중에 리펙토링하기
    private final UserMapper userMapper;

    public void signup(SignUpReq signUpReq) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername()).get();
        user.signupUser(signUpReq);

        userRepository.save(user);
    }

    public void logout() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername()).get();
        user.onLogout();
        userRepository.save(user);
    }


    public UserProfileRes findUserProfile(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        User existingUser = optionalUser.get();

        return userMapper.toUserProfile(existingUser);
    }

    public void updateUserProfile(UpdateUserReq updateUserReq) {
        User user = getLoginUser();
        user.updateProfile(updateUserReq);

        userRepository.save(user);
    }

    public User getLoginUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loginUser = userRepository.findBySocialTypeAndEmail(SocialType.valueOf(userDetails.getPassword()), userDetails.getUsername()).get();
        return loginUser;
    }
}