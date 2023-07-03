package com.project.capstone.Repositories.Service;

import com.project.capstone.Models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryService {
    Optional<User> findByEmail(String email);

    Boolean existsById(String email);

    void save(User user);

    void saveAll(List<User> userList);

    List<User> findAll();

    void deleteAll(List<User> users);

}
