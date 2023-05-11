package com.project.capstone.Service;

import com.project.capstone.UserDto.MsgDto;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public interface ForgotLinkService {

    ResponseEntity<MsgDto> pwdForgotLink(String email) throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<MsgDto> passwordUpdate(HashMap<String,String> forgotUser,String code) throws Exception;
}
