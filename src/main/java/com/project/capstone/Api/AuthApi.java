package com.project.capstone.Api;

import com.project.capstone.Dao.LoginAuthRequest;
import com.project.capstone.Models.User;
import com.project.capstone.UserDto.MsgDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@RequestMapping("/restapi")
public interface AuthApi {

    @GetMapping("/password/{plainText}")
    public String passwordGenerate(@PathVariable String plainText) throws Exception;

    @RequestMapping("/signup")
    String signUp(@RequestBody User user) throws Exception;

    @RequestMapping("/verifysignup")
    String verifyUser(@RequestParam String code);


    @PostMapping("/login")
    ResponseEntity<MsgDto> login(@RequestBody LoginAuthRequest loginAuthRequest) throws Exception;

    @GetMapping("/forgot/{email}")
    ResponseEntity<MsgDto> pwdForgotLink(@PathVariable String email) throws MessagingException, UnsupportedEncodingException;
    
    @PostMapping("/password/verify")
    ResponseEntity<MsgDto> passwordUpdate(@RequestBody HashMap<String,String> forgotUser,@RequestParam String code) throws Exception;

    @GetMapping("/deletereq")
    public void req();

}
