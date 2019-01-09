package com.ejar.dao;

import com.ejar.model.User;

public interface UserDetailsDao {

    User findUserByUsername(String username);
}
