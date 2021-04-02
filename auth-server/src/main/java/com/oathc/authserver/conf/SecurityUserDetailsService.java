package com.oathc.authserver.conf;

import com.oathc.authserver.model.User;
import com.oathc.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个类SecurityUserDetailsService，实现UserDetailsService接口
 * 主要处理loadUserByUsername方法,根据用户名查找用户,若用户不存在,抛出UsernameNotFoundException即可
 *
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        return new org.springframework.security.core.userdetails.User(username,
                user.getPsd(),
                true,
                true,
                true,
                true,
                getGrantedAuthority(user));
    }

    public List<GrantedAuthority> getGrantedAuthority(User user){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return list;
    }

}
