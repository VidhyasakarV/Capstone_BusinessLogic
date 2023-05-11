package com.project.capstone.Service;

import com.project.capstone.Models.UserFeed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserFeedService {

    public ResponseEntity<?> myFeed();

    public ResponseEntity<?> newPost(@RequestBody UserFeed userFeed);

    public ResponseEntity<?> myFeedById(@PathVariable String id);

    public ResponseEntity<?> deletePost(@PathVariable String id);

    public ResponseEntity<?> deleteArchive(@PathVariable String id);

    public ResponseEntity<?> myArchive();

    public ResponseEntity<?> restoreFrmArchive(@PathVariable String id);

    public List<List<UserFeed>> friendsFeed();

    public List<UserFeed> publicFeed();

    public List<UserFeed> privateFeed();
}
