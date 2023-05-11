package com.project.capstone.Controllers;

import com.project.capstone.Api.AdminApi;
import com.project.capstone.Repositories.UserRepository;
import com.project.capstone.Service.AdminService;
import com.project.capstone.UserDto.MsgDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController implements AdminApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminService adminService;

    @Override
    public ResponseEntity<MsgDto> addCategories(String item) {
        return adminService.addCategories(item);
    }

    @Override
    public ResponseEntity<MsgDto> deleteCategory(String item) {
        return adminService.deleteCategory(item);
    }

    @Override
    public ResponseEntity<MsgDto> deleteAllCategories() {
        return (ResponseEntity<MsgDto>) adminService.deleteAllCategories();
    }
}
