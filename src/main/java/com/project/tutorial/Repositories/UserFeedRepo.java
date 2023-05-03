package com.project.tutorial.Repositories;

import com.project.tutorial.Models.UserFeed;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserFeedRepo extends MongoRepository<UserFeed,String> {
    @Cacheable(key = "#email")
    List<UserFeed> findByEmail(String email);
    List<UserFeed> findAllByEmailAndAvailableAndVisibility(String emailIds, Boolean available, String visibility);
    List<UserFeed> findAllByVisibility(String visibility);
}
