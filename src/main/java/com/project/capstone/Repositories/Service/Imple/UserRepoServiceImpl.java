package com.project.capstone.Repositories.Service.Imple;

import com.project.capstone.Models.User;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRepoServiceImpl implements UserRepositoryService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findById(email);
    }

    @Override
    public Boolean existsById(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveAll(List<User> userList) {
        userRepository.saveAll(userList);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteAll(List<User> users) {
        userRepository.deleteAll();
    }

}
