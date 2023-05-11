package com.project.capstone.Controllers;

import com.project.capstone.Api.AuthApi;
import com.project.capstone.Dao.LoginAuthRequest;
import com.project.capstone.Models.User;
import com.project.capstone.Service.*;
import com.project.capstone.UserDto.MsgDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@RestController
public class UserAuthController implements AuthApi {
    @Autowired
    SignUpService signUpService;
    @Autowired
    SignUpVerifyService signUpVerifyService;
    @Autowired
    LoginService loginService;
    @Autowired
    ForgotLinkService forgotLinkService;
    @Autowired
    OauthService oauthService;


    @Override
    public String passwordGenerate(String plainText) throws Exception {
        return signUpVerifyService.passwordGenerate(plainText);
    }

    @Override
    public String signUp(@Validated @RequestBody User user) throws Exception {
        return signUpService.SignUp(user);
    }

    @Override
    public String verifyUser(String code) {
        return signUpVerifyService.verifySignUp(code);
    }

    @Override
    public ResponseEntity<MsgDto> login(@RequestBody LoginAuthRequest loginAuthRequest) throws Exception {
        return loginService.login(loginAuthRequest);
    }

    @Override
    public ResponseEntity<MsgDto> pwdForgotLink(@PathVariable String email) throws MessagingException, UnsupportedEncodingException {
        return forgotLinkService.pwdForgotLink(email);
    }

    @Override
    public ResponseEntity<MsgDto> passwordUpdate(@RequestBody HashMap<String, String> forgotUser,@RequestParam String code) throws Exception {
        return forgotLinkService.passwordUpdate(forgotUser,code);
    }

    @Override
    public void req() {
        signUpService.req();
    }
}
