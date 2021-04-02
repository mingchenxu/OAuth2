package com.oathc.authclient.service;

import com.oathc.authclient.model.User;

public interface UserService {

    User findByUsername(String username);


    User findByThirdUserId(String userId);

}
