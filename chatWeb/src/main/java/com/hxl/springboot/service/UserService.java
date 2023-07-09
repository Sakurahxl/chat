package com.hxl.springboot.service;

import com.hxl.springboot.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {
    User login(String account, String password);
    String logon(String account,String password);
    String updateName(String account,String name);
    String updateAvatar(String account,String avatar);
    User searchContact(String account);
    String addContact(String account1,String account2);
    ArrayList<User> userListValue(String account);
}
