package com.project.capstone.Repositories.Service.Imple;

import com.project.capstone.Models.UserFeed;
import com.project.capstone.Repositories.Service.UserFeedRepoService;
import com.project.capstone.Repositories.UserFeedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserFeedRepoServiceImpl implements UserFeedRepoService {

    @Autowired
    UserFeedRepo userFeedRepo;

    @Override
    public void save(UserFeed userFeed) {
        userFeedRepo.save(userFeed);
    }

    @Override
    public void saveAll(List<UserFeed> userFeeds) {
        userFeedRepo.saveAll(userFeeds);
    }

    @Override
    public Boolean existsById(String email) {
        return userFeedRepo.existsById(email);
    }

    @Override
    public void deleteById(String id) {
        userFeedRepo.deleteById(id);
    }

    @Override
    public List<UserFeed> findByEmail(String email) {
        return userFeedRepo.findByEmail(email);
    }

    @Override
    public List<UserFeed> findAll() {
        return userFeedRepo.findAll();
    }

    @Override
    public List<UserFeed> findByEmailAndAvailableAndVisibility(String emailIds, Boolean available, String visibility) {
        return userFeedRepo.findByEmailAndAvailableAndVisibility(emailIds, available, visibility);
    }

    @Override
    public List<UserFeed> findAllByVisibility(String visibility) {
        return userFeedRepo.findAllByVisibility(visibility);
    }

    @Override
    public Optional<UserFeed> findById(String id) {
        return userFeedRepo.findById(id);
    }

}
