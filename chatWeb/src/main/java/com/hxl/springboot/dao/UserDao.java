package com.hxl.springboot.dao;

import com.hxl.springboot.pojo.User;

import java.util.ArrayList;

public interface UserDao {
    User searchUser(String account,String password);
    String addUser(String account,String password);
    String updateName(String account,String name);
    String updateAvatar(String account,String avatar);
    User searchContact(String account);
    String addContact(String account1,String account2);
    ArrayList<User> userListValue(String account);
}
