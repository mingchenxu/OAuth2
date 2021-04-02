package com.oathc.authserver.service;


import com.oathc.authserver.model.User;

public interface UserService {

    User findByUsername(String username);

}
