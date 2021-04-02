package com.oathc.authclient.service.impl;

import com.oathc.authclient.model.User;
import com.oathc.authclient.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {


    @Override
    public User findByUsername(String username) {
        // 为了简单起见，省略数据库获取用户步骤
        if (username.equals("user")) {
            User user = new User();
            user.setUserId(username);
            user.setName(username);
            user.setPsd("$2a$10$RZZl184eOUaE.bfgVVjg7.haBzBoHvQC.zWq/UtfcRtcTIFFln5r6"); // 明文 user
            return user;
        }
        return null;
    }
}
