package com.project.capstone.Controllers;

import com.project.capstone.Api.UserFeedApi;
import com.project.capstone.Models.UserFeed;
import com.project.capstone.Service.UserFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserFeedController implements UserFeedApi {

    @Autowired
    UserFeedService userFeedService;
    @Override
    public ResponseEntity<?> myFeed() {
        return userFeedService.myFeed();
    }

    @Override
    public ResponseEntity<?> newPost(UserFeed userFeed) {
        return userFeedService.newPost(userFeed);
    }

    @Override
    public ResponseEntity<?> myFeedById(String id) {
        return userFeedService.myFeedById(id);
    }

    @Override
    public ResponseEntity<?> deletePost(String id) {
        return userFeedService.deletePost(id);
    }

    @Override
    public ResponseEntity<?> deleteArchive(String id) {
        return userFeedService.deleteArchive(id);
    }

    @Override
    public ResponseEntity<?> myArchive() {
        return userFeedService.myArchive();
    }

    @Override
    public ResponseEntity<?> restoreFrmArchive(String id) {
        return userFeedService.restoreFrmArchive(id);
    }

    @Override
    public List<List<UserFeed>> friendsFeed() {
        return userFeedService.friendsFeed();
    }

    @Override
    public List<UserFeed> publicFeed() {
        return userFeedService.publicFeed();
    }

    @Override
    public List<UserFeed> privateFeed() {
        return userFeedService.privateFeed();
    }
}