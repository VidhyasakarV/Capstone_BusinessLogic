package com.project.capstone.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/Oauth")
public interface OauthApi {

    @GetMapping("/oauthsucess/{jwt}")
    public String OauthSuccess(@PathVariable String jwt);

    @GetMapping("/oauthUnsuces")
    public String OauthUnsuccess();

    @GetMapping("/sociallogin")
    public String login();

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response);

}
