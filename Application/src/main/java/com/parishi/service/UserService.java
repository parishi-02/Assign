package com.parishi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
    void registerUser(String username, String role,String password);

    void changePassword(String username, String newPassword);
}
