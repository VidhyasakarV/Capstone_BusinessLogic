package com.project.tutorial.Controllers;

import com.project.tutorial.Jwt.JwtToken;
import com.project.tutorial.Models.LoginAuthRequest;
import com.project.tutorial.Models.LoginAuthResponse;
import com.project.tutorial.Models.User;
import com.project.tutorial.Repositories.UserRepository;
import com.project.tutorial.Services.MailService;
import com.project.tutorial.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/restapi")
public class UserAuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailService mailService;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtToken jwtToken;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public String signIn(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {
        if (!userRepository.existsById(user.getEmail())) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String encryptPwd = bcrypt.encode(user.getPassword());
            user.setPassword(encryptPwd);
            user.setFriends(new ArrayList<>());
            user.setFriendRequests(new ArrayList<>());
            userRepository.save(user);
            mailService.register(user);
            return "Verification mail sent successfully.";
        } else {
            Optional<User> userDetails = userRepository.findById(user.getEmail());
            if (userDetails.get().getEnabled()) {
                return "Already exists, try different email.";
            } else {
                return "Waiting for verification";
            }
        }
    }
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String code){
        String[] paramList = code.split("-");
        String verifyCode = paramList[0];
        String email = paramList[1];
        Optional<User> user = userRepository.findById(email);
        if(user.get().getVerifyotp().equals(verifyCode) && user.get().getVerifyotp()!=null){
            user.get().setEnabled(true);
            user.get().setVerifyotp(null);
            userRepository.save(user.get());
            return ResponseEntity.ok("Account has been Verified Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Credentials.");
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginAuthRequest loginAuthRequest){
        String email = loginAuthRequest.getEmail();
        String encPassword = loginAuthRequest.getPassword();
        if (userRepository.existsById(email)){
            Optional<User> user = userRepository.findOneById(email);
            if (user.get().getEnabled()){
                try{
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,encPassword));
                    }catch (Exception e){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials.");
                    }
                final UserDetails userDetails = userService.loadUserByUsername(loginAuthRequest.getEmail());
                final String jwt = jwtToken.generateToken(userDetails);
                return ResponseEntity.ok(new LoginAuthResponse(jwt));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account is not enabled.");
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    @GetMapping("/login/forgot/{email}")
    public ResponseEntity<?> forgotPwdLink(@PathVariable String email) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.existsById(email)) {
            Optional<User> user = userRepository.findById(email);
            mailService.forgotPassword(user.get());
            return ResponseEntity.ok("Forgot password link has been sent to " + email);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
    }

    @PostMapping("/forgot/verify")
    public ResponseEntity<?> forgotPassword(@RequestBody HashMap<String, String> forgotUser, @RequestParam String code) {
        String[] paramList = code.split("-");
        String verifyCode = paramList[0];
        String emailId = paramList[1];
        try {
            Optional<User> user = userRepository.findById(emailId);
            if (user.get().getVerifyotp() != null && user.get().getVerifyotp().equals(verifyCode)) {
                if (forgotUser.get("newPassword").equals(forgotUser.get("reNewPassword"))) {
                    user.get().setPassword(passwordEncoder.encode(forgotUser.get("newPassword")));
                    user.get().setVerifyotp(null);
                    userRepository.save(user.get());
                    return ResponseEntity.ok("Password has been changed successfully");
                }else {
                    return ResponseEntity.status(HttpStatus.OK).body("Confirm Password Did not Match.");
                }
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification is not Valid.");
            }
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification.");
        }
    }
    @GetMapping("/oauthsucess/{jwt}")
    public String OauthSuccess(@PathVariable String jwt){
        System.out.println(jwt);
        return jwt;
    }
    @GetMapping("/oauthUnsuces")
    public String OauthUnsuccess(){
        return "<h1> Oauth login faild. </h1>";
    }

    @GetMapping("/sociallogin")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
