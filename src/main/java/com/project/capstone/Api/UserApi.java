package com.project.capstone.Api;

import com.project.capstone.UserDto.MsgDto;
import com.project.capstone.UserDto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
public interface UserApi {

    @PutMapping("/update")
    public ResponseEntity<MsgDto> profileUpdate(@Validated @RequestBody UserDto userDto);

    @GetMapping("/follow/{id}")
    public  ResponseEntity<?> follow(@PathVariable String id);

    @GetMapping("/accept/{id}")
    public ResponseEntity<?> accept(@PathVariable String id);

    @GetMapping("/unfollow/following/{id}")
    public ResponseEntity<?> unfollow(@PathVariable String id);

    @GetMapping("/remove/followers/{id}")
    public ResponseEntity<?> removeFollowers(@PathVariable String id);

    @GetMapping("/requestlist")
    public ResponseEntity<?> requestList();

    @GetMapping("/myfollowers")
    public ResponseEntity<?> followersList();

    @GetMapping("/following")
    public ResponseEntity<?> followingList();

    @GetMapping("/rejectReq/{id}")
    public ResponseEntity<?> reject(@PathVariable String id);

}
