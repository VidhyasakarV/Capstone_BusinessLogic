package com.project.capstone.Controllers;

import com.project.capstone.Models.User;
import com.project.capstone.Models.UserFriends;
import com.project.capstone.Repositories.UserFriendsRepo;
import com.project.capstone.Repositories.UserRepository;
import com.project.capstone.UserDto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFriendsRepo userFriendsRepo;

    @PutMapping("/update")
    public ResponseEntity<?> profileUpdate(@Validated @RequestBody UserDto userDto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.existsById(email)){
            Optional<User> user = userRepository.findById(email);
            if (user.get().getEnabled()){
                user.get().setFullname(userDto.getFullname());
                user.get().setAccVisibility(userDto.getAccVisibility());
                userRepository.save(user.get());
                return ResponseEntity.ok("User has been updated.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not enabled.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    @GetMapping("/follow/{id}")
    public  ResponseEntity<?> follow(@PathVariable String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findById(email);
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isPresent() && requestedUser.get().getEnabled()) {
            switch (requestedUser.get().getAccVisibility()) {
                case "Public":
                    if (!currentUser.get().getFollowing().contains(id)) {
                        if (requestedUser.get().getFollowing().contains(email)) {
                            currentUser.get().getFollowing().add(id);
                            currentUser.get().getFriends().add(id);
                            requestedUser.get().getFollowers().add(email);
                            requestedUser.get().getFriends().add(email);
                            userRepository.saveAll(Arrays.asList(currentUser.get(), requestedUser.get()));
                            Optional<UserFriends> user = userFriendsRepo.findById(email);
                            Optional<UserFriends> reqUser = userFriendsRepo.findById(id);
                            if ((!user.get().getFriends().contains(id) || user.get().getFriends().isEmpty()) && (!reqUser.get().getFriends().contains(email) || reqUser.get().getFriends().isEmpty())) {
                                user.get().getFriends().add(id);
                                reqUser.get().getFriends().add(email);
                                userFriendsRepo.saveAll(Arrays.asList(user.get(), reqUser.get()));
                                return ResponseEntity.ok("You and " + requestedUser.get().getFullname() + " are now friends.");
                            }
                            return ResponseEntity.badRequest().body("Friends list error in mongodb friends list.");
                        }
                        currentUser.get().getFollowing().add(id);
                        requestedUser.get().getFollowers().add(email);
                        userRepository.saveAll(Arrays.asList(currentUser.get(), requestedUser.get()));
                        return ResponseEntity.ok("You're following " + requestedUser.get().getFullname());
                    }return ResponseEntity.badRequest().body("You're already follow this account.");
                case "Private":
                    if (!currentUser.get().getFollowing().contains(id)){
                        if (!requestedUser.get().getFriendRequests().contains(email)){
                            requestedUser.get().getFriendRequests().add(email);
                            userRepository.save(requestedUser.get());
                            return ResponseEntity.ok("Friend request sent.");
                        }return ResponseEntity.badRequest().body("Already friend request sent");
                    }return ResponseEntity.badRequest().body("You already follow this PRIVATE account");
            }
        } return ResponseEntity.badRequest().body("Requested account is not found or requested account might be inactive.");
    }

    @GetMapping("/accept/{id}")
    public ResponseEntity<?> accept(@PathVariable String id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findById(email);
        Optional<User> requestedUser = userRepository.findOneById(id);
        if (requestedUser.isPresent() && requestedUser.get().getEnabled()) {
            if (currentUser.get().getFriendRequests().contains(id)) {
                if (currentUser.get().getFollowing().contains(id) && requestedUser.get().getFollowers().contains(email)){
                    currentUser.get().getFriendRequests().remove(id);
                    requestedUser.get().getFollowing().add(email);
                    currentUser.get().getFollowers().add(id);
                    currentUser.get().getFriends().add(id);
                    requestedUser.get().getFriends().add(email);
                    userRepository.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                    Optional<UserFriends> curUser = userFriendsRepo.findById(email);
                    Optional<UserFriends> reqUser = userFriendsRepo.findById(id);
                    if ((!curUser.get().getFriends().contains(id) || curUser.get().getFriends().isEmpty())&& (!reqUser.get().getFriends().contains(email)||reqUser.get().getFriends().isEmpty())) {
                        curUser.get().getFriends().add(id);
                        reqUser.get().getFriends().add(email);
                        userFriendsRepo.saveAll(Arrays.asList(curUser.get(),reqUser.get()));
                        return ResponseEntity.ok("You and "+requestedUser.get().getFullname()+" are now friends.");
                    }return ResponseEntity.badRequest().body("Friends list error in mongodb");
                }
                currentUser.get().getFriendRequests().remove(id);
                currentUser.get().getFollowers().add(id);
                requestedUser.get().getFollowing().add(email);
                userRepository.saveAll(Arrays.asList(currentUser.get(), requestedUser.get()));
                return ResponseEntity.ok("Requested " + requestedUser.get().getFullname()+" account accepted.");
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested account not found in current user's request list.");
        }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested account not enabled.");
    }

    @GetMapping("/unfollow/following/{id}")
    public ResponseEntity<?> unfollow(@PathVariable String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findById(email);
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isPresent()&&requestedUser.get().getEnabled()) {
            if (currentUser.get().getFollowing().contains(id)){
                Optional<UserFriends> user = userFriendsRepo.findById(email);
                Optional<UserFriends> reqUser = userFriendsRepo.findById(id);
                switch (requestedUser.get().getAccVisibility()) {
                    case "Public":
                        currentUser.get().getFollowing().remove(id);
                        requestedUser.get().getFollowers().remove(email);
                        currentUser.get().getFriends().remove(id);
                        requestedUser.get().getFriends().remove(email);
                        user.get().getFriends().remove(id);
                        reqUser.get().getFriends().remove(email);
                        userRepository.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                        userFriendsRepo.saveAll(Arrays.asList(user.get(), reqUser.get()));
                        return ResponseEntity.ok("Requested PUBLIC account removed from your following list.");
                    case "Private":
                        currentUser.get().getFollowing().remove(id);
                        requestedUser.get().getFollowers().remove(email);
                        currentUser.get().getFriends().remove(id);
                        requestedUser.get().getFriends().remove(email);
                        user.get().getFriends().remove(id);
                        reqUser.get().getFriends().remove(email);
                        userRepository.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                        userFriendsRepo.saveAll(Arrays.asList(user.get(), reqUser.get()));
                        return ResponseEntity.ok("PRIVATE account removed, You have to send request if you want to follow again.");
                }
            }return ResponseEntity.badRequest().body("Requested account not found in your following list.");
        }return ResponseEntity.badRequest().body("Requested account not found or not enabled.");
    }

    @GetMapping("/remove/followers/{id}")
    private ResponseEntity<?> removeFollowers(@PathVariable String id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findById(email);
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isPresent() && requestedUser.get().getEnabled()){
            if (currentUser.get().getFollowers().contains(id)){
                Optional<UserFriends> user = userFriendsRepo.findById(email);
                Optional<UserFriends> reqUser = userFriendsRepo.findById(id);
                currentUser.get().getFollowers().remove(id);
                requestedUser.get().getFollowing().remove(email);
                currentUser.get().getFriends().remove(id);
                requestedUser.get().getFriends().remove(email);
                user.get().getFriends().remove(id);
                reqUser.get().getFriends().remove(email);
                userRepository.saveAll(Arrays.asList(currentUser.get(),requestedUser.get()));
                userFriendsRepo.saveAll(Arrays.asList(user.get(), reqUser.get()));
                return ResponseEntity.ok("Requested account removed from your following list.");
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(id +" this account not in your followers list.");
        }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested user not found or not enabled.");
    }

    @GetMapping("/requestlist")
    public ResponseEntity<?> requestList(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findOneById(email);
        if(!user.get().getFriendRequests().isEmpty()){
            return ResponseEntity.ok(user.get().getFriendRequests());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friends request list is empty.");
        }
    }

    @GetMapping("/myfollowers")
    public ResponseEntity<?> followersList(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        if (!user.get().getFollowers().isEmpty()){
            return ResponseEntity.ok(user.get().getFollowers());
        }
        else {
            return ResponseEntity.ok("You have no followers.");
        }
    }
    @GetMapping("/following")
    public ResponseEntity<?> followingList(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        if (!user.get().getFollowing().isEmpty()){
            return ResponseEntity.ok(user.get().getFollowing());
        }
        else {
            return ResponseEntity.ok("Your following list is empty.");
        }
    }

    @GetMapping("/rejectReq/{id}")
    public ResponseEntity<?> reject(@PathVariable String id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findOneById(email);
        if (user.get().getFriendRequests().contains(id)){
            user.get().getFriendRequests().remove(id);
            userRepository.save(user.get());
            return ResponseEntity.ok("Friend request is rejected.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request not found");
    }

    @GetMapping("/deletereq/{id}/{id2}")
    public void req(@PathVariable String id,@PathVariable String id2){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        Optional<User> reqUser = userRepository.findById(id);
        Optional<User> thUser = userRepository.findById(id2);
        user.get().getFollowing().removeAll(user.get().getFollowing());
        user.get().getFollowers().removeAll(user.get().getFollowers());
        user.get().getFriendRequests().removeAll(user.get().getFriendRequests());
        reqUser.get().getFollowing().removeAll(reqUser.get().getFollowing());
        reqUser.get().getFollowers().removeAll(reqUser.get().getFollowers());
        reqUser.get().getFriendRequests().removeAll(reqUser.get().getFriendRequests());
        user.get().getFriends().removeAll(user.get().getFriends());
        reqUser.get().getFriends().removeAll(reqUser.get().getFriends());
        thUser.get().getFollowing().removeAll(thUser.get().getFollowing());
        thUser.get().getFollowers().removeAll(thUser.get().getFollowers());
        thUser.get().getFriends().removeAll(thUser.get().getFriends());
        thUser.get().getFriendRequests().removeAll(thUser.get().getFriendRequests());
        userRepository.saveAll(Arrays.asList(user.get(),reqUser.get(),thUser.get()));
    }
}
