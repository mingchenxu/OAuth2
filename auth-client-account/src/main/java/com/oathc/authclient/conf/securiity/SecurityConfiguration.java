package com.oathc.authclient.conf.securiity;

import com.oathc.authclient.conf.securiity.service.SecurityUserDetailsService;
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
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 无需登录的白名单
    private static String[] WHITE_URL_LIST = {"/login", "/thirdLogin", "/error**"};

    @Autowired
    private OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter;

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

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

    /**
     * Spring Security 认证接口
     * 通过 AuthenticationManager 实现类 ProviderManager 等等实现类中的 authenticate 函数完成 Spring Security 的认证。
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 密码加密方式
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(WHITE_URL_LIST).permitAll()  // 添加白名单
                .anyRequest().authenticated()   // 其他请求全部认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")) // 匿名用户访问无权限资源时的异常
                .accessDeniedHandler(new CustomAccessDeniedHandler())    // 认证过的用户访问无权限资源时的异常
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(oAuth2ClientAuthenticationProcessingFilter, BasicAuthenticationFilter.class)
                .csrf().disable().httpBasic();
    }
}
