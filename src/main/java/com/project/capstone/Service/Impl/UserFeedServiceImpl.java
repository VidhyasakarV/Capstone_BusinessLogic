package com.project.capstone.Service.Impl;

import com.project.capstone.Models.User;
import com.project.capstone.Models.UserFeed;
import com.project.capstone.Repositories.Service.UserFeedRepoService;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.UserFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFeedServiceImpl implements UserFeedService {

    @Autowired
    UserFeedRepoService userFeedRepoService;

    @Autowired
    UserRepositoryService userRepositoryService;

    @Override
    public ResponseEntity<?> myFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userFeedRepoService.findByEmail(email).isEmpty()) {
            return ResponseEntity.ok(userFeedRepoService.findByEmail(email));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Feed is empty.");
    }

    @Override
    public ResponseEntity<?> newPost(UserFeed userFeed) {
        if (userRepositoryService.existsById(userFeed.getEmail())
                && userFeed.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            userFeedRepoService.save(userFeed);
            return ResponseEntity.ok("Feed uploaded.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not valid");
    }

    @Override
    public ResponseEntity<?> myFeedById(String id) {
        if (userFeedRepoService.existsById(id)) {
            Optional<UserFeed> userOptional=userFeedRepoService.findById(id);
            if(!userOptional.isPresent()){
                return ResponseEntity.ok(userFeedRepoService.findById(id).get());
            }
            return null;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id not found.");
    }

    @Override
    public ResponseEntity<?> deletePost(String id) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userFeedRepoService.existsById(id)
                && userFeedRepoService.findById(id).get().getEmail().equals(myEmail)) {
            Optional<UserFeed> userFeed = userFeedRepoService.findById(id);
            userFeed.get().setAvailable(false);
            userFeedRepoService.save(userFeed.get());
            return ResponseEntity.ok("Post has been Deleted , You can see it in Archive");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id not found.");
    }

    @Override
    public ResponseEntity<?> deleteArchive(String id) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userFeedRepoService.existsById(id) && userFeedRepoService.findById(id).get().getEmail().equals(myEmail)){
            Optional<UserFeed> userFeed = userFeedRepoService.findById(id);
            if (!userFeed.get().isAvailable()){
                userFeedRepoService.deleteById(id);
                userFeedRepoService.save(userFeed.get());
                return ResponseEntity.ok("Content deleted in archive.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content not in false.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Id not valid.");
    }

    @Override
    public ResponseEntity<?> myArchive() {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserFeed> myFeed = new ArrayList<>();
        List<UserFeed> allFeed = userFeedRepoService.findAll();
        for (UserFeed i : allFeed) {
            if (i.getEmail().equals(myEmail) && !i.isAvailable()) {
                myFeed.add(i);
            }
        }
        if (myFeed.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your archive is empty.");
        } else {
            return  ResponseEntity.ok(myFeed);
        }
    }

    @Override
    public ResponseEntity<?> restoreFrmArchive(String id) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userFeedRepoService.existsById(id) && userFeedRepoService.findById(id).get().getEmail().equals(myEmail)){
            Optional<UserFeed> userFeed = userFeedRepoService.findById(id);
            userFeed.get().setAvailable(true);
            userFeedRepoService.save(userFeed.get());
            return ResponseEntity.ok("Restored content from archive.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content not found.");
    }

    @Override
    public List<List<UserFeed>> friendsFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.findByEmail(email);
        List<String> myFrdlist = user.get().getFollowing();
        return myFrdlist.stream().map((frd) -> userFeedRepoService.findByEmailAndAvailableAndVisibility(frd,true,"friends")).collect(Collectors.toList());
    }

    @Override
    public List<UserFeed> publicFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userFeedRepoService.findAllByVisibility("public");
    }

    @Override
    public List<UserFeed> privateFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userFeedRepoService.findByEmailAndAvailableAndVisibility(email,true,"private");
    }


}
