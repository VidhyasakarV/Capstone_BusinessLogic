package com.project.tutorial.Repositories;

import com.project.tutorial.Models.UserFeed;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"feed"})
public interface UserFeedRepo extends MongoRepository<UserFeed,String> {

    @Cacheable(key = "#email")
    default Optional<UserFeed> findOneById(String email){return findOneById(email);}

    @Cacheable(key = "#email")
    List<UserFeed> findByEmail(String email);

    List<UserFeed> findAllByEmailAndAvailableAndVisibility(String emailIds, Boolean available, String visibility);

    List<UserFeed> findAllByVisibility(String visibility);

    List<UserFeed> findAllByEmail(String email);
}
