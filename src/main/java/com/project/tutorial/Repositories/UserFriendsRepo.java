package com.project.tutorial.Repositories;

import com.project.tutorial.Models.UserFriends;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendsRepo extends MongoRepository<UserFriends,String> {

}
