package com.project.tutorial.Controllers;

import com.project.tutorial.Models.User;
import com.project.tutorial.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/viewCategories")
    public ResponseEntity<?> allCategories(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent() && user.get().getEnabled()) {
            return ResponseEntity.ok(user.get().getCategories());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or check your account if it is enabled.");
    }
}
