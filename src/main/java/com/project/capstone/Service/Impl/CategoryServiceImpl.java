package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    UserRepositoryService userRepositoryService;
    @Override
    public ResponseEntity<?> allCategories() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.findByEmail(email);
        if (user.isPresent() && user.get().getEnabled()) {
            return ResponseEntity.ok(user.get().getCategories());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or check your account if it is enabled.");
    }
}
