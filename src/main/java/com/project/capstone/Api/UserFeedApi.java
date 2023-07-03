package com.project.capstone.Api;

import com.project.capstone.Models.UserFeed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/feed")
public interface UserFeedApi {

    @ApiOperation(value = "User's All Feed",notes = "User's All Feed",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "OK, User's All Feed.",response = UserFeed.class),
            @ApiResponse(code = 400,message = "Bad Request",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error",response = String.class)})
    @GetMapping(value = "/myfeed",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> myFeed();

    @ApiOperation(value = "Put a new post.",notes = "Put a new post.",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Ok, User successfully uploaded a post.",response = UserFeed.class),
            @ApiResponse(code = 400,message = "Bad Request.",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error.",response = String.class)
    })
    @PostMapping(value = "/newpost",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newPost(@RequestBody UserFeed userFeed);

    @ApiOperation(value = "User's Feed By Id.",notes = "User's Feed By Id.",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "OK, User's Feed By Id.",response = UserFeed.class),
            @ApiResponse(code = 400,message = "Bad Request",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error",response = String.class)})
    @GetMapping(value = "/myfeed/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> myFeedById(@PathVariable String id);


    @ApiOperation(value = "Delete a post by user.",notes = "Delete a post by user.",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Ok, User post successfully deleted.",response = String.class),
            @ApiResponse(code = 400,message = "Bad Request.",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error.",response = String.class)
    })
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id);

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<?> deleteArchive(@PathVariable String id);

    @GetMapping("/archive")
    public ResponseEntity<?> myArchive();

    @GetMapping("/archive/restore/{id}")
    public ResponseEntity<?> restoreFrmArchive(@PathVariable String id);


    @ApiOperation(value = "User's Friends Feeds.",notes = "User's friends Feeds.",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Ok, User's Friends Feeds.",response = UserFeed.class),
            @ApiResponse(code = 400,message = "Bad Request.",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error.",response = String.class)
    })
    @GetMapping("/view/friends")
    public List<List<UserFeed>> friendsFeed();


    @ApiOperation(value = "All Public Feeds",notes = "All Public Feeds",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "OK. Returns all public feeds.",response = UserFeed.class),
            @ApiResponse(code = 400,message = "Bad Request",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error",response = String.class)})
    @GetMapping(value = "/view/public",produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserFeed> publicFeed();


    @ApiOperation(value = "User's Private Feeds.",notes = "User's Private Feeds.",response = UserFeed.class,responseContainer = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Ok, User's Private Feeds.",response = UserFeed.class),
            @ApiResponse(code = 400,message = "Bad Request.",response = String.class),
            @ApiResponse(code = 500,message = "Internal Server Error.",response = String.class)
    })
    @GetMapping(value = "/view/private",produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserFeed> privateFeed();
}
