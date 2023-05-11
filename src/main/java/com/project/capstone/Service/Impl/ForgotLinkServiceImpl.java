package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.ForgotLinkService;
import com.project.capstone.UserDto.MsgDto;
import com.project.capstone.Utils.EncryptionAndDecryption;
import com.project.capstone.Utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Optional;

@Service
public class ForgotLinkServiceImpl implements ForgotLinkService {
    @Autowired
    UserRepositoryService userRepositoryService;
    @Autowired
    MailService mailService;
    @Autowired
    EncryptionAndDecryption encryptionAndDecryption;
    @Override
    public ResponseEntity<MsgDto> pwdForgotLink(String email) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userRepositoryService.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getEnabled()) {
                mailService.forgotPassword(user.get());
                return ResponseEntity.ok(new MsgDto("Forgot password link has been sent to " + email, HttpStatus.OK));
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("Not enabled",HttpStatus.BAD_REQUEST));
        }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("Email not found, Account not enabled.",HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<MsgDto> passwordUpdate(HashMap<String,String> forgotUser,String code) throws Exception {
        String[] list = code.split("-");
        String verifyCode = list[0];
        String userEmail = list[1];
        if (userRepositoryService.existsById(userEmail)) {
            Optional<User> user = userRepositoryService.findByEmail(userEmail);
            String previousPass = user.get().getPassword();
            if (user.get().getVerifyotp() != null && user.get().getVerifyotp().equals(verifyCode)) {
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                String newPassPlain = encryptionAndDecryption.decrypt(forgotUser.get("newPassword"));
                String newPassHash = bCrypt.encode(newPassPlain);
                if (!bCrypt.matches(newPassPlain, previousPass)){
                    if (forgotUser.get("newPassword").equals(forgotUser.get("reNewPassword"))) {
                        user.get().setPassword(newPassHash);
                        user.get().setVerifyotp(null);
                        userRepositoryService.save(user.get());
                        return ResponseEntity.ok(new MsgDto("Password changed",HttpStatus.OK));
                    }
                }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("New password is looks same like previous Password.",HttpStatus.BAD_REQUEST));
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("Verification failed.",HttpStatus.BAD_REQUEST));
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("User not found.",HttpStatus.BAD_REQUEST));
    }

}
