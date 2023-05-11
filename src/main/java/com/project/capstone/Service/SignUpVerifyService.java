package com.project.capstone.Service;


import org.springframework.web.bind.annotation.PathVariable;

public interface SignUpVerifyService {
    String verifySignUp(String code);

    public String passwordGenerate(@PathVariable String plainText) throws Exception;
}