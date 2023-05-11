package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Models.UserFriends;
import com.project.capstone.Repositories.Service.UserFriendsRepoService;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.UserService;
import com.project.capstone.UserDto.MsgDto;
import com.project.capstone.UserDto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    UserFriendsRepoService userFriendsRepoService;

    @Override
    public ResponseEntity<MsgDto> profileUpdate(UserDto userDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepositoryService.existsById(email)){
            Optional<User> user = userRepositoryService.findByEmail(email);
            if (user.get().getEnabled()){
                user.get().setFullname(userDto.getFullname());
                user.get().setAccVisibility(userDto.getAccVisibility());
                userRepositoryService.save(user.get());
                return ResponseEntity.ok(new MsgDto("User has been updated.",HttpStatus.OK));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("User not enabled.",HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("User not found.",HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<?> follow(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepositoryService.findByEmail(email);
        Optional<User> requestedUser = userRepositoryService.findByEmail(id);
        if (requestedUser.isPresent() && requestedUser.get().getEnabled()) {
            switch (requestedUser.get().getAccVisibility()) {
                case "Public":
                    if (!currentUser.get().getFollowing().contains(id)) {
                        if (requestedUser.get().getFollowing().contains(email)) {
                            currentUser.get().getFollowing().add(id);
                            currentUser.get().getFriends().add(id);
                            requestedUser.get().getFollowers().add(email);
                            requestedUser.get().getFriends().add(email);
                            userRepositoryService.saveAll(Arrays.asList(currentUser.get(), requestedUser.get()));
                            Optional<UserFriends> user = userFriendsRepoService.findByEmail(email);
                            Optional<UserFriends> reqUser = userFriendsRepoService.findByEmail(id);
                            if ((!user.get().getFriends().contains(id) || user.get().getFriends().isEmpty()) && (!reqUser.get().getFriends().contains(email) || reqUser.get().getFriends().isEmpty())) {
                                user.get().getFriends().add(id);
                                reqUser.get().getFriends().add(email);
                                userFriendsRepoService.saveAll(Arrays.asList(user.get(), reqUser.get()));
                                return ResponseEntity.ok("You and " + requestedUser.get().getFullname() + " are now friends.");
                            }
                            return ResponseEntity.badRequest().body("Friends list error in mongodb friends list.");
                        }
                        currentUser.get().getFollowing().add(id);
                        requestedUser.get().getFollowers().add(email);
                        userRepositoryService.saveAll(Arrays.asList(currentUser.get(), requestedUser.get()));
                        return ResponseEntity.ok("You're following " + requestedUser.get().getFullname());
                    }return ResponseEntity.badRequest().body("You're already follow this account.");
                case "Private":
                    if (!currentUser.get().getFollowing().contains(id)){
                        if (!requestedUser.get().getFriendRequests().contains(email)){
                            requestedUser.get().getFriendRequests().add(email);
                            userRepositoryService.save(requestedUser.get());
                            return ResponseEntity.ok("Friend request sent.");
                        }return ResponseEntity.badRequest().body("Already friend request sent");
                    }return ResponseEntity.badRequest().body("You already follow this PRIVATE account");
            }
        } return ResponseEntity.badRequest().body("Requested account is not found or requested account might be inactive.");
    }

    @Override
    public ResponseEntity<?> accept(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepositoryService.findByEmail(email);
        Optional<User> requestedUser = userRepositoryService.findByEmail(id);
        if (requestedUser.isPresent() && requestedUser.get().getEnabled()) {
            if (currentUser.get().getFriendRequests().contains(id)) {
                if (currentUser.get().getFollowing().contains(id) && requestedUser.get().getFollowers().contains(email)){
                    currentUser.get().getFriendRequests().remove(id);
                    requestedUser.get().getFollowing().add(email);
                    currentUser.get().getFollowers().add(id);
                    currentUser.get().getFriends().add(id);
                    requestedUser.get().getFriends().add(email);
                    userRepositoryService.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                    Optional<UserFriends> curUser = userFriendsRepoService.findByEmail(email);
                    Optional<UserFriends> reqUser = userFriendsRepoService.findByEmail(id);
                    if ((!curUser.get().getFriends().contains(id) || curUser.get().getFriends().isEmpty())&& (!reqUser.get().getFriends().contains(email)||reqUser.get().getFriends().isEmpty())) {
                        curUser.get().getFriends().add(id);
                        reqUser.get().getFriends().add(email);
                        userFriendsRepoService.saveAll(Arrays.asList(curUser.get(),reqUser.get()));
                        return ResponseEntity.ok("You and "+requestedUser.get().getFullname()+" are now friends.");
                    }return ResponseEntity.badRequest().body("Friends list error in mongodb");
                }
                currentUser.get().getFriendRequests().remove(id);
                currentUser.get().getFollowers().add(id);
                requestedUser.get().getFollowing().add(email);
                userRepositoryService.saveAll(Arrays.asList(currentUser.get(), requestedUser.get()));
                return ResponseEntity.ok("Requested " + requestedUser.get().getFullname()+" account accepted.");
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested account not found in current user's request list.");
        }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested account not enabled.");
    }

    @Override
    public ResponseEntity<?> unfollow(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepositoryService.findByEmail(email);
        Optional<User> requestedUser = userRepositoryService.findByEmail(id);
        if (requestedUser.isPresent()&&requestedUser.get().getEnabled()) {
            if (currentUser.get().getFollowing().contains(id)){
                Optional<UserFriends> user = userFriendsRepoService.findByEmail(email);
                Optional<UserFriends> reqUser = userFriendsRepoService.findByEmail(id);
                switch (requestedUser.get().getAccVisibility()) {
                    case "Public":
                        currentUser.get().getFollowing().remove(id);
                        requestedUser.get().getFollowers().remove(email);
                        currentUser.get().getFriends().remove(id);
                        requestedUser.get().getFriends().remove(email);
                        user.get().getFriends().remove(id);
                        reqUser.get().getFriends().remove(email);
                        userRepositoryService.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                        userFriendsRepoService.saveAll(Arrays.asList(user.get(), reqUser.get()));
                        return ResponseEntity.ok("Requested PUBLIC account removed from your following list.");
                    case "Private":
                        currentUser.get().getFollowing().remove(id);
                        requestedUser.get().getFollowers().remove(email);
                        currentUser.get().getFriends().remove(id);
                        requestedUser.get().getFriends().remove(email);
                        user.get().getFriends().remove(id);
                        reqUser.get().getFriends().remove(email);
                        userRepositoryService.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                        userFriendsRepoService.saveAll(Arrays.asList(user.get(), reqUser.get()));
                        return ResponseEntity.ok("PRIVATE account removed, You have to send request if you want to follow again.");
                }
            }return ResponseEntity.badRequest().body("Requested account not found in your following list.");
        }return ResponseEntity.badRequest().body("Requested account not found or not enabled.");
    }

    @Override
    public ResponseEntity<?> removeFollowers(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepositoryService.findByEmail(email);
        Optional<User> requestedUser = userRepositoryService.findByEmail(id);
        if (requestedUser.isPresent() && requestedUser.get().getEnabled()){
            if (currentUser.get().getFollowers().contains(id)){
                Optional<UserFriends> user = userFriendsRepoService.findByEmail(email);
                Optional<UserFriends> reqUser = userFriendsRepoService.findByEmail(id);
                currentUser.get().getFollowers().remove(id);
                requestedUser.get().getFollowing().remove(email);
                currentUser.get().getFriends().remove(id);
                requestedUser.get().getFriends().remove(email);
                user.get().getFriends().remove(id);
                reqUser.get().getFriends().remove(email);
                userRepositoryService.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                userFriendsRepoService.saveAll(Arrays.asList(user.get(), reqUser.get()));
                return ResponseEntity.ok("Requested account removed from your following list.");
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(id +" this account not in your followers list.");
        }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested user not found or not enabled.");
    }

    @Override
    public ResponseEntity<?> requestList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.findByEmail(email);
        if(!user.get().getFriendRequests().isEmpty()){
            return ResponseEntity.ok(user.get().getFriendRequests());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friends request list is empty.");
        }
    }

    @Override
    public ResponseEntity<?> followersList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.findByEmail(email);
        if (!user.get().getFollowers().isEmpty()){
            return ResponseEntity.ok(user.get().getFollowers());
        }
        else {
            return ResponseEntity.ok("You have no followers.");
        }
    }

    @Override
    public ResponseEntity<?> followingList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.findByEmail(email);
        if (!user.get().getFollowing().isEmpty()){
            return ResponseEntity.ok(user.get().getFollowing());
        }
        else {
            return ResponseEntity.ok("Your following list is empty.");
        }
    }

    @Override
    public ResponseEntity<?> reject(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.findByEmail(email);
        if (user.get().getFriendRequests().contains(id)){
            user.get().getFriendRequests().remove(id);
            userRepositoryService.save(user.get());
            return ResponseEntity.ok("Friend request is rejected.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request not found");
    }
}
