package com.project.capstone.Service;

import com.project.capstone.UserDto.MsgDto;
import com.project.capstone.UserDto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    public ResponseEntity<MsgDto> profileUpdate(@Validated @RequestBody UserDto userDto);

    public  ResponseEntity<?> follow(@PathVariable String id);

    public ResponseEntity<?> accept(@PathVariable String id);

    public ResponseEntity<?> unfollow(@PathVariable String id);

    public ResponseEntity<?> removeFollowers(@PathVariable String id);

    public ResponseEntity<?> requestList();

    public ResponseEntity<?> followersList();

    public ResponseEntity<?> followingList();

    public ResponseEntity<?> reject(@PathVariable String id);

}
