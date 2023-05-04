package com.project.capstone.Controllers;

import com.project.capstone.Config.SecurityConfig;
import com.project.capstone.Jwt.JwtToken;
import com.project.capstone.Models.LoginAuthRequest;
import com.project.capstone.Models.LoginAuthResponse;
import com.project.capstone.Models.User;
import com.project.capstone.Models.UserFriends;
import com.project.capstone.Repositories.UserFriendsRepo;
import com.project.capstone.Repositories.UserRepository;
import com.project.capstone.Services.EncryptionAndDecryption;
import com.project.capstone.Services.MailService;
import com.project.capstone.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
    SecurityConfig security;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EncryptionAndDecryption encryptionAndDecryption;
    @Autowired
    UserFriendsRepo userFriendsRepo;

    @PostMapping("/signup")
    public String signUp(@Validated @RequestBody User user) throws Exception {
        if (!userRepository.existsById(user.getEmail())) {
            String decrypt = encryptionAndDecryption.decrypt(user.getPassword());
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            String hashPass = bCrypt.encode(decrypt);
            user.setPassword(hashPass);
            user.setFollowing(new ArrayList<>());
            user.setFollowers(new ArrayList<>());
            user.setFriendRequests(new ArrayList<>());
            user.setFriends(new ArrayList<>());
            UserFriends userFriends = new UserFriends();
            userFriends.setEmail(user.getEmail());
            userFriends.setFriends(new ArrayList<>());
            userRepository.save(user);
            mailService.register(user);
            userFriendsRepo.save(userFriends);
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
    @GetMapping("/verifysignup")
    public ResponseEntity<?> verifyUser(@RequestParam String code){
        String[] paramList = code.split("-");
        String verifyCode = paramList[0];
        String email = paramList[1];
        Optional<User> user = userRepository.findById(email);
        if(user.get().getVerifyotp().equals(verifyCode) && user.get().getVerifyotp()!=null){
            user.get().setEnabled(true);
            user.get().setVerifyotp(null);
            userRepository.save(user.get());
            return ResponseEntity.ok("Account Verified Successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Credentials.");
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginAuthRequest loginAuthRequest) throws Exception {
        String email = loginAuthRequest.getEmail();
        String password = loginAuthRequest.getPassword();
        if (userRepository.existsById(email)) {
            Optional<User> user = userRepository.findOneById(email);
            if (user.get().getEnabled()) {
                String decryptPass = encryptionAndDecryption.decrypt(password);
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                if (bCrypt.matches(decryptPass,user.get().getPassword())){
                    final UserDetails userDetails = userService.loadUserByUsername(loginAuthRequest.getEmail());
                    final String jwt = jwtToken.generateToken(userDetails);
                    return ResponseEntity.ok(new LoginAuthResponse(jwt));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account is not enabled or invalid password.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }
    @GetMapping("/forgot/{email}")
    public ResponseEntity<?> forgotPwdLink(@PathVariable String email) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            if (user.get().getEnabled()) {
                mailService.forgotPassword(user.get());
                return ResponseEntity.ok("Forgot password link has been sent to " + email);
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enabled");
        }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found, Account not enabled.");
    }

    @PostMapping("/password/verify")
    private ResponseEntity<?> passwordUpdate(@RequestBody HashMap<String,String> forgotUser, @RequestParam String code) throws Exception {
        String[] list = code.split("-");
        String verifyCode = list[0];
        String userEmail = list[1];
        if (userRepository.existsById(userEmail)) {
            Optional<User> user = userRepository.findById(userEmail);
            String previousPass = user.get().getPassword();
            if (user.get().getVerifyotp() != null && user.get().getVerifyotp().equals(verifyCode)) {
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                String newPassPlain = encryptionAndDecryption.decrypt(forgotUser.get("newPassword"));
                String newPassHash = bCrypt.encode(newPassPlain);
                if (!bCrypt.matches(newPassPlain, previousPass)){
                    if (forgotUser.get("newPassword").equals(forgotUser.get("reNewPassword"))) {
                        user.get().setPassword(newPassHash);
                        user.get().setVerifyotp(null);
                        userRepository.save(user.get());
                        return ResponseEntity.ok("Password changed");
                    }
                }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password is looks same like previous Password.");
            }return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed.");
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
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

    @GetMapping("/password/{plainText}")
    private String passwordGenerate(@PathVariable String plainText) throws Exception {
        String encrypted = encryptionAndDecryption.encrypt(plainText);
        return (encrypted);
    }

}
