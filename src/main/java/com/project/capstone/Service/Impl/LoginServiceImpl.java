package com.project.capstone.Service.Impl;

import com.project.capstone.Utils.JwtToken;
import com.project.capstone.Dao.LoginAuthRequest;
import com.project.capstone.Models.User;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.LoginService;
import com.project.capstone.UserDto.MsgDto;
import com.project.capstone.Utils.EncryptionAndDecryption;
import com.project.capstone.Utils.UserServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserRepositoryService userRepositoryService;
    @Autowired
    EncryptionAndDecryption encryptionAndDecryption;
    @Autowired
    UserServiceUtils userServiceUtils;
    @Autowired
    JwtToken jwtToken;
    @Override
    public ResponseEntity<MsgDto> login(LoginAuthRequest loginAuthRequest) throws Exception {
        String email = loginAuthRequest.getEmail();
        String password = loginAuthRequest.getPassword();
        if (userRepositoryService.existsById(email)) {
            Optional<User> user = userRepositoryService.findByEmail(email);
            if (user.get().getEnabled()) {
                String decryptPass = encryptionAndDecryption.decrypt(password);
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                if (bCrypt.matches(decryptPass,user.get().getPassword())){
                    final UserDetails userDetails = userServiceUtils.loadUserByUsername(loginAuthRequest.getEmail());
                    final String jwt = jwtToken.generateToken(userDetails);
                    return ResponseEntity.status(HttpStatus.OK).body(new MsgDto(jwt,HttpStatus.OK));
                }
                return ResponseEntity.badRequest().body(new MsgDto("Incorrect password.",HttpStatus.BAD_REQUEST));
            }
            return ResponseEntity.badRequest().body(new MsgDto("Account is not enabled or invalid password.",HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.badRequest().body(new MsgDto("User not found.",HttpStatus.BAD_REQUEST));
    }
}
