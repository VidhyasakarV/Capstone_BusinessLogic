package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.SignUpVerifyService;
import com.project.capstone.Utils.EncryptionAndDecryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignUpVerifyServiceImpl implements SignUpVerifyService {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EncryptionAndDecryption encryptionAndDecryption;
    public String verifySignUp(String code){
        String[] paramList = code.split("-");
        String verifyCode = paramList[0];
        String email = paramList[1];
        Optional<User> user = userRepositoryService.findByEmail(email);
        if(user.get().getVerifyotp().equals(verifyCode) && user.get().getVerifyotp()!=null){
            user.get().setEnabled(true);
            user.get().setVerifyotp(null);
            userRepositoryService.save(user.get());
            return "Account Verified Successfully.";
        }
        return "Invalid Credentials.";
    }

    @Override
    public String passwordGenerate(String plainText) throws Exception {
        String encrypted = encryptionAndDecryption.encrypt(plainText);
        return (encrypted);
    }
}
