package com.project.capstone.Repositories;

import com.project.capstone.Models.UserFeed;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserFeedRepo extends MongoRepository<UserFeed,String> {
    @Cacheable(key = "#email")
    List<UserFeed> findByEmail(String email);

    List<UserFeed> findByEmailAndAvailableAndVisibility(String emailIds, Boolean available, String visibility);

    List<UserFeed> findAllByVisibility(String visibility);

}
