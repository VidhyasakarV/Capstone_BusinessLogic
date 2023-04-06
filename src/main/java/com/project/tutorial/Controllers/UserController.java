package com.project.tutorial.Controllers;

import com.project.tutorial.Models.User;
import com.project.tutorial.Repositories.UserRepository;
import com.project.tutorial.UserDto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @PutMapping("/update")
    public ResponseEntity<?> profileUpdate(@Validated @RequestBody UserDto userDto, BindingResult bindingResult){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.existsById(email)){
            Optional<User> user = userRepository.findById(email);
            if (bindingResult.getAllErrors().isEmpty()){
                user.get().setFullname(userDto.getFullname());
                userRepository.save(user.get());
                return ResponseEntity.ok("User has been updated.");
            }
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> listError = new ArrayList<>();
            for (ObjectError i:errors){
                listError.add(i.getDefaultMessage());
            }
            HashMap<String,List> errorMessage = new HashMap<>();
            errorMessage.put("Validation error.",listError);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    @GetMapping("/requestlist")
    public ResponseEntity<?> requestList(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findOneById(email);
        System.out.println(user);
        if(!user.get().getFriendRequests().isEmpty()){
            return ResponseEntity.ok(user.get().getFriendRequests());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friends request list is empty.");
        }
    }
    @GetMapping("/myfriends")
    public ResponseEntity<?> friendList(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        if (!user.get().getFriends().isEmpty()){
            return ResponseEntity.ok(user.get().getFriends());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friend list is empty.");
        }
    }

    @GetMapping("/accept/{id}")
    public ResponseEntity<?> acceptReq(@PathVariable String id){
        String email =SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.existsById(email) && userRepository.existsById(id)){
            Optional<User> myUser = userRepository.findOneById(email);
            Optional<User> thatUser = userRepository.findOneById(id);
            if (myUser.get().getFriendRequests().contains(id)){
                myUser.get().getFriends().add(id);
                ArrayList<String> myUserReq = myUser.get().getFriendRequests();
                myUserReq.remove(id);
                thatUser.get().getFriends().add(email);
                userRepository.save(thatUser.get());
                userRepository.save(myUser.get());
                return ResponseEntity.ok("Friend request accepted.");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friend request not found.");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
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

    @GetMapping("/follow/{id}")
    public ResponseEntity<?> follow(@PathVariable String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        Optional<User> thatUser = userRepository.findById(id);
        if (userRepository.existsById(id)){
            if (user.get().getFriends().contains(id)){
                if (thatUser.get().getFriendRequests().contains(email)){
                    return ResponseEntity.ok("Already friend request sent.");
                }
                return ResponseEntity.ok("You are already friend with each other.");
            }
            user.get().getFriends().add(id);
            thatUser.get().getFriendRequests().add(email);
            userRepository.save(user.get());
            userRepository.save(thatUser.get());
            return ResponseEntity.ok("Friend request sent to "+ id);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    @GetMapping("/unfollow/{id}")
    public ResponseEntity<?> unfollow(@PathVariable String id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.existsById(email) && userRepository.existsById(id)){
            Optional<User> myUser = userRepository.findOneById(email);
            Optional<User> thatUser = userRepository.findOneById(id);
            if (myUser.get().getFriends().contains(id) && thatUser.get().getFriends().contains(email)) {
                myUser.get().getFriends().remove(id);
                thatUser.get().getFriends().remove(email);
                userRepository.save(myUser.get());
                userRepository.save(thatUser.get());
                return ResponseEntity.ok("Friend removed, Next time you want to send request to be friends each other.");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friend not in the list.");
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }
    }
}
