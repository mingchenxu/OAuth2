package com.oathc.authresource.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 因为是资源服务器，假设所有的请求都需要登录
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 匿名用户访问无权限资源时的异常
                .accessDeniedHandler(new CustomAccessDeineHandler())    // 认证过的用户访问无权限资源时的异常
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable().httpBasic();
    }
}
