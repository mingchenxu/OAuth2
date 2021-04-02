package com.oathc.authclient.conf.securiity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter;

    /**
     * 放开静态资源拦截
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略URL
        web.ignoring().antMatchers("/**/*.js",
                "/lang/*.json",
                "/**/*.css",
                "/**/*.js",
                "/**/*.map",
                "/**/*.html",
                "/**/*.png",
                "/static/asserts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    // user请求都需要授权
                .anyRequest().permitAll()   // 其他请求放开
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")) // 匿名用户访问无权限资源时的异常
                .accessDeniedHandler(new CustomAccessDeniedHandler())    // 认证过的用户访问无权限资源时的异常
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and()
                // 在网站基本认证之前添加OAuth2ClientAuthenticationProcessingFilter
                .addFilterBefore(oAuth2ClientAuthenticationProcessingFilter, BasicAuthenticationFilter.class)
                .csrf().disable().httpBasic();
    }
}
