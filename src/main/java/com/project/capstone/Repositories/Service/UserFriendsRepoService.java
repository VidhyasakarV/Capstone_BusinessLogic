package com.project.capstone.Repositories.Service;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.project.capstone.Models.UserFriends;

import java.util.List;
import java.util.Optional;

public interface UserFriendsRepoService {
    void save(UserFriends user);

    Optional<UserFriends> findByEmail(String email);

    void saveAll(List<UserFriends> userFriends);
}
