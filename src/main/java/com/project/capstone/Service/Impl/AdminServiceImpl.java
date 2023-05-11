package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Models.UserFriends;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.AdminService;
import com.project.capstone.UserDto.MsgDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Override
    public ResponseEntity<MsgDto> addCategories(String item) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepositoryService.findByEmail(email).get().getRole().equals("Admin")){
            Optional<User> admin = userRepositoryService.findByEmail(email);
            if (!admin.get().getCategories().contains(item)){
                admin.get().getCategories().add(item);
                userRepositoryService.save(admin.get());
                return ResponseEntity.ok(new MsgDto("Category added.", HttpStatus.OK));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("Category already exists.",HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("User not found or User is not a admin.",HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<MsgDto> deleteCategory(String item) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepositoryService.findByEmail(email).get().getRole().equals("Admin")){
            Optional<User> admin = userRepositoryService.findByEmail(email);
            if (admin.get().getCategories().contains(item)){
                admin.get().getCategories().remove(item);
                userRepositoryService.save(admin.get());
                return ResponseEntity.ok(new MsgDto("Category deleted.",HttpStatus.OK));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("Category not exist.",HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("User not found or User is not a admin.",HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<MsgDto> deleteAllCategories() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepositoryService.findByEmail(email).get().getRole().equals("Admin")){
            Optional<User> admin = userRepositoryService.findByEmail(email);
            admin.get().setCategories(new ArrayList<>());
            userRepositoryService.save(admin.get());
            return ResponseEntity.ok(new MsgDto("Category has been reset.",HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("User not found or User is not a admin.",HttpStatus.BAD_REQUEST));
    }

}
