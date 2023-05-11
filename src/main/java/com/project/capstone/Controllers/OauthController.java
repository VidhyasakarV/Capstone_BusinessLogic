package com.project.capstone.Controllers;

import com.project.capstone.Api.OauthApi;
import com.project.capstone.Service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class OauthController implements OauthApi {
    
    @Autowired
    OauthService oauthService;
    
    @Override
    public String OauthSuccess(String jwt) {
        return oauthService.OauthSuccess(jwt);
    }

    @Override
    public String OauthUnsuccess() {
        return oauthService.OauthUnsuccess();
    }

    @Override
    public String login() {
        return oauthService.login();
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return oauthService.logout(request,response);
    }
}
