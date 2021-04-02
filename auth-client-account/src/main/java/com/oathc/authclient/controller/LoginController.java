package com.oathc.authclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @RequestMapping("/login")
    public String login() {
        return "go_login";
    }


//    @RequestMapping("/thirdLogin")
//    public void thirdLogin(String authType, String code) {
//        oAuth2RestTemplate.getAccessToken();
//    }

}
