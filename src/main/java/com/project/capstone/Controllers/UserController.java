package com.project.capstone.Controllers;

import com.project.capstone.Api.UserApi;
import com.project.capstone.Service.UserService;
import com.project.capstone.UserDto.MsgDto;
import com.project.capstone.UserDto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController implements UserApi {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<MsgDto> profileUpdate(UserDto userDto) {
        return userService.profileUpdate(userDto);
    }

    @Override
    public ResponseEntity<?> follow(String id) {
        return userService.follow(id);
    }

    @Override
    public ResponseEntity<?> accept(String id) {
        return userService.accept(id);
    }

    @Override
    public ResponseEntity<?> unfollow(String id) {
        return userService.unfollow(id);
    }

    @Override
    public ResponseEntity<?> removeFollowers(String id) {
        return userService.removeFollowers(id);
    }

    @Override
    public ResponseEntity<?> requestList() {
        return userService.requestList();
    }

    @Override
    public ResponseEntity<?> followersList() {
        return userService.followersList();
    }

    @Override
    public ResponseEntity<?> followingList() {
        return userService.followingList();
    }

    @Override
    public ResponseEntity<?> reject(String id) {
        return userService.reject(id);
    }

}
