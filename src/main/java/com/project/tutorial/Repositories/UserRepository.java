package com.project.tutorial.Repositories;

import com.project.tutorial.Models.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@CacheConfig(cacheNames = {"user"})
public interface UserRepository extends MongoRepository<User,String> {
    @Cacheable(key = "#s")
    public  default Optional<User> findOneById(String s){
        return findById(s);
    }

}
