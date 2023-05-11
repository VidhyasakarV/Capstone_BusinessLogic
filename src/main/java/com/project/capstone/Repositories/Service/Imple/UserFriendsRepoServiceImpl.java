package com.project.capstone.Repositories.Service.Imple;

import com.project.capstone.Models.UserFriends;
import com.project.capstone.Repositories.Service.UserFriendsRepoService;
import com.project.capstone.Repositories.UserFriendsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFriendsRepoServiceImpl implements UserFriendsRepoService {

    @Autowired
    UserFriendsRepo userFriendsRepo;

    @Override
    public void save(UserFriends user) {
        userFriendsRepo.save(user);
    }

    @Override
    public Optional<UserFriends> findByEmail(String email) {
        return userFriendsRepo.findById(email);
    }

    @Override
    public void saveAll(List<UserFriends> userFriends) {
        userFriendsRepo.saveAll(userFriends);
    }


}
