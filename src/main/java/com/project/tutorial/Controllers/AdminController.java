package com.project.tutorial.Controllers;

import com.project.tutorial.Models.User;
import com.project.tutorial.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/categories")
    public ResponseEntity<?> allCategories(){
        Optional<User> admin = userRepository.findOneById("vidhyasakar@divum.in");
        return ResponseEntity.ok(admin.get().getCategories());
    }
    @PutMapping("/addCategories/{item}")
    public ResponseEntity<?> addCategories(@PathVariable String item){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.findOneById(email).get().getRole().equals("ADMIN")){
            Optional<User> admin = userRepository.findOneById(email);
            if (!admin.get().getCategories().contains(item)){
                admin.get().getCategories().add(item);
                userRepository.save(admin.get());
                return ResponseEntity.ok("Category added.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category already exists.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }
    @DeleteMapping("/categories/deleteCategory/{item}")
    public ResponseEntity<?> deleteCategory(@PathVariable String item){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.findOneById(email).get().getRole().equals("ADMIN")){
            Optional<User> admin = userRepository.findOneById(email);
            if (admin.get().getCategories().contains(item)){
                admin.get().getCategories().remove(item);
                userRepository.save(admin.get());
                return ResponseEntity.ok("Category deleted.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category not exist.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    @DeleteMapping("/categories/deleteall")
    public ResponseEntity<?> deleteAllCategories(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.findOneById(email).get().getRole().equals("ADMIN")){
            Optional<User> admin = userRepository.findOneById(email);
            System.out.println(admin);
            admin.get().setCategories(new ArrayList<>());
            userRepository.save(admin.get());
            return ResponseEntity.ok("Category has been reset.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

}
