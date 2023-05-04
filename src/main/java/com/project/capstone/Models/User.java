package com.project.capstone.Models;

import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Document
@Entity
@Table(name = "User")
@Builder
public class User implements Serializable {
//    private Long id;
    @Id
    private String email;
    private String fullname;
    private String password;
    private Boolean enabled = false;
    private String verifyotp;
    private String provider="LOCAL";
    private String role="USER";
    private String accVisibility="Public";
    @ElementCollection
    @CollectionTable(name = "user_categories", joinColumns = @JoinColumn(name = "user_email"))
    @Column(name = "category")
    public List<String> categories=new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "user_following",joinColumns = @JoinColumn(name ="user_email"))
    @Column(name = "following")
    private List<String> following= new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "user_followers", joinColumns = @JoinColumn(name = "user_email"))
    @Column(name = "followers")
    private List<String> followers=new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "user_friend_requests", joinColumns = @JoinColumn(name = "user_email"))
    @Column(name = "friend_requests")
    private List<String> friendRequests=new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "user_friends",joinColumns = @JoinColumn(name = "user_email"))
    @Column(name = "friends")
    private List<String> friends = new ArrayList<>();

    public User() {
        super();
    }

    public User(String email, String fullname, String password, Boolean enabled, String verifyotp, String provider, String role, String accVisibility, List<String> categories, List<String> following, List<String> followers, List<String> friendRequests, List<String> friends) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.enabled = enabled;
        this.verifyotp = verifyotp;
        this.provider = provider;
        this.role = role;
        this.accVisibility = accVisibility;
        this.categories = categories;
        this.following = following;
        this.followers = followers;
        this.friendRequests = friendRequests;
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerifyotp() {
        return verifyotp;
    }

    public void setVerifyotp(String verifyotp) {
        this.verifyotp = verifyotp;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccVisibility() {
        return accVisibility;
    }

    public void setAccVisibility(String accVisibility) {
        this.accVisibility = accVisibility;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}