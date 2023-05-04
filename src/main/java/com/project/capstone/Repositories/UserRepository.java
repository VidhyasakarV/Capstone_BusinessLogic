package com.project.capstone.Repositories;

import com.project.capstone.Models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Cacheable(key = "#s")
    public  default Optional<User> findOneById(String s){
        return findById(s);
    }

}
