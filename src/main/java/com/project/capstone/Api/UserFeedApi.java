package com.project.capstone.Api;

import com.project.capstone.Models.UserFeed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/feed")
public interface UserFeedApi {

    @GetMapping("/myfeed")
    public ResponseEntity<?> myFeed();

    @PostMapping("/newpost")
    public ResponseEntity<?> newPost(@RequestBody UserFeed userFeed);

    @GetMapping("/myfeed/{id}")
    public ResponseEntity<?> myFeedById(@PathVariable String id);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id);

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<?> deleteArchive(@PathVariable String id);

    @GetMapping("/archive")
    public ResponseEntity<?> myArchive();

    @GetMapping("/archive/restore/{id}")
    public ResponseEntity<?> restoreFrmArchive(@PathVariable String id);

    @GetMapping("/view/friends")
    public List<List<UserFeed>> friendsFeed();

    @GetMapping("/view/public")
    public List<UserFeed> publicFeed();

    @GetMapping("/view/private")
    public List<UserFeed> privateFeed();
}
