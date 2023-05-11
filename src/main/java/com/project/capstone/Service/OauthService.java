package com.project.capstone.Service;

import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OauthService {
    public String OauthSuccess(@PathVariable String jwt);

    public String OauthUnsuccess();

    public String login();

    public String logout(HttpServletRequest request, HttpServletResponse response);
}
