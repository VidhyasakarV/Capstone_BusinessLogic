package com.project.capstone.Repositories;

import com.project.capstone.Models.UserFriends;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendsRepo extends MongoRepository<UserFriends,String> {

}
