package com.oathc.authclient.conf.securiity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录后操作
 * 登录成功后的类配置，存入登录user信息后交给认证成功后的处理类MyAuthenticationSuccessHandler
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        // 认证成功后，获取用户信息并添加到session中
        UserDetails user = (UserDetails) authentication.getPrincipal();
        // 可以从数据库中获取该用户信息，比如将用户的信息保存在服务中
        request.getSession().setAttribute("user", user);

        // 存入登录user信息后交给认证成功后的处理类SavedRequestAwareAuthenticationSuccessHandler
        // 该类集成了SavedRequestAwareAuthenticationSuccessHandler，他会从缓存中提取请求，从而可以恢复之前请求的数据
        super.onAuthenticationSuccess(request, response, authentication);
    }


}
