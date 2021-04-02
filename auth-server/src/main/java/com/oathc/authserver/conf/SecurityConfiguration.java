package com.oathc.authserver.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security 配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置认证信息
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定认证service和加密方式
        auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 配置核心过滤器
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/asserts/**");
    }

    /**
     * 配置Security的认证策略
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置登录页并允许访问
        http.formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            // 配置登出页面
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
            .authorizeRequests()
                .antMatchers("/oauth/**", "/login/**", "/logout/**", "/error/**")
                .permitAll()
            // 其余所有请求全部需要鉴权认证
            .anyRequest()
                .authenticated()
                .and()
            // 关闭跨域保护,Basic登录
            .csrf().disable()
            .httpBasic().disable();
    }
}
