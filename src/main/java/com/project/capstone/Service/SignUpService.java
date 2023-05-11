package com.project.capstone.Service;

import com.project.capstone.Models.User;

import java.util.Optional;


public interface SignUpService {
    String SignUp(User user) throws Exception;

    Optional<User> findById(String email);

    public void req();
}
