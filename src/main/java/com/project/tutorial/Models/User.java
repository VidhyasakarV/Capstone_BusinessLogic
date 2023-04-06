package com.project.tutorial.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class User {
    @Id
    private String email;
    private String fullname;
    private String password;
    private Boolean enabled = false;
    private String verifyotp;
    private String provider="LOCAL";
    private String role="USER";
    public ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> friendRequests = new ArrayList<>();

    public User(String email, String fullname, String password,String role, ArrayList<String> categories, ArrayList<String> friends, ArrayList<String> friendRequests) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.role = role;
        this.categories = categories;
        this.friends = friends;
        this.friendRequests = friendRequests;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getFriendRequests() {
        return friendRequests;
    }
    public void setFriendRequests(ArrayList<String> friendRequests) {
        this.friendRequests = friendRequests;
    }
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getVerifyotp() {
        return verifyotp;
    }

    public void setVerifyotp(String verifyotp) {
        this.verifyotp = verifyotp;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
