package com.project.capstone.Service;

import com.project.capstone.Dao.LoginAuthRequest;
import com.project.capstone.UserDto.MsgDto;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<MsgDto> login(LoginAuthRequest loginAuthRequest) throws Exception;
}
