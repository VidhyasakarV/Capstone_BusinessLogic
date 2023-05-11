package com.project.capstone.Repositories.Service;

import com.project.capstone.Models.UserFeed;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserFeedRepoService {

    void save(UserFeed userFeed);

    void saveAll(List<UserFeed> userFeeds);

    Boolean existsById(String email);

    void deleteById(String id);

    List<UserFeed> findByEmail(String email);

    List<UserFeed> findAll();

    List<UserFeed> findByEmailAndAvailableAndVisibility(String emailIds, Boolean available, String visibility);

    List<UserFeed> findAllByVisibility(String visibility);

    Optional<UserFeed> findById(String id);
}
