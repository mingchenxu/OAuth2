package com.oathc.authclient.conf.oauth;

import com.oathc.authclient.model.User;
import com.oathc.authclient.service.UserService;
import com.oathc.authclient.system.ApplicationContextRegister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        // 认证成功后，获取用户信息
        String thirdUserId = (String) authentication.getPrincipal();
        // 从数据库中获取该用户信息
        UserService userService = (UserService) ApplicationContextRegister.getBean("userService", UserService.class);
        User user = userService.findByThirdUserId(thirdUserId);
        login(request, user.getName(), "user");

        // 将用户的信息保存在服务中
        request.getSession().setAttribute("user", user);

        // 存入登录user信息后交给认证成功后的处理类SavedRequestAwareAuthenticationSuccessHandler
        // 该类集成了SavedRequestAwareAuthenticationSuccessHandler，他会从缓存中提取请求，从而可以恢复之前请求的数据
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * 模拟登录
     * @param request
     * @param username
     * @param password
     */
    private void login (HttpServletRequest request, String username, String password) {
        AuthenticationManager authenticationManager = (AuthenticationManager)
                ApplicationContextRegister.getBean("authenticationManagerBean", AuthenticationManager.class);
        // 将用户名，密码生成认证可用的AuthenticationToken, loginName 用户名，loginName 密码
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        // 设置authenticationToken的Details，主要获取当前请求的一些信息
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        // 使用authenticationManager 接口中的 authenticate 进行 SpringSecurity 认证
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        // 设置当前认证对象
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        // 如果没有session，生成一个session并设置当前的SecurityContext
        request.getSession().setAttribute(HttpSessionSecurityContextRepository
                .SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

}
