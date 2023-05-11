package com.project.capstone.Service.Impl;

import com.project.capstone.Service.OauthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class OauthServiceImpl implements OauthService {
    @Override
    public String OauthSuccess(String jwt) {
        return jwt;
    }

    @Override
    public String OauthUnsuccess() {
        return "<h1> Oauth login faild. </h1>";
    }

    @Override
    public String login() {
        return "login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

}
