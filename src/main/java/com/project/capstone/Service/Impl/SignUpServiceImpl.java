package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Models.UserFriends;
import com.project.capstone.Repositories.Service.UserFriendsRepoService;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.SignUpService;
import com.project.capstone.Utils.EncryptionAndDecryption;
import com.project.capstone.Utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    UserFriendsRepoService userFriendsRepoService;

    @Autowired
    EncryptionAndDecryption encryptionAndDecryption;

    @Autowired
    MailService mailService;

    @Override
    public String SignUp(User user) throws Exception {
        if (!userRepositoryService.existsById(user.getEmail())) {
            String decrypt = encryptionAndDecryption.decrypt(user.getPassword());
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            String hashPass = bCrypt.encode(decrypt);
            user.setPassword(hashPass);
            user.setFollowing(new ArrayList<>());
            user.setFollowers(new ArrayList<>());
            user.setFriendRequests(new ArrayList<>());
            user.setFriends(new ArrayList<>());
            UserFriends userFriends = new UserFriends();
            userFriends.setEmail(user.getEmail());
            userFriends.setFriends(new ArrayList<>());
            userRepositoryService.save(user);
            mailService.register(user);
            userFriendsRepoService.save(userFriends);
            return "Verification mail sent successfully.";
        } else {
            Optional<User> userDetails = userRepositoryService.findByEmail(user.getEmail());
            if (userDetails.get().getEnabled()) {
                return "Already exists, try different email.";
            } else {
                return "Waiting for verification";
            }
        }
    }

    @Override
    public Optional<User> findById(String email) {
        return Optional.empty();
    }

    @Override
    public void req() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> all = userRepositoryService.findAll();
        userRepositoryService.deleteAll(all);
    }

}
