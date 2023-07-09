package com.hxl.springboot.service.impl;

import com.hxl.springboot.dao.UserDao;
import com.hxl.springboot.pojo.User;
import com.hxl.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User login(String account, String password) {
        return userDao.searchUser(account,password);
    }

    public String logon(String account, String password) {
        return userDao.addUser(account,password);
    }

    public String updateName(String account,String name) {
        return userDao.updateName(account,name);
    }

    public String updateAvatar(String account,String avatar) {
        return userDao.updateAvatar(account,avatar);
    }

    public User searchContact(String account){
        return userDao.searchContact(account);
    }

    public String addContact(String account1,String account2){
        return userDao.addContact(account1,account2);
    }

    public ArrayList<User> userListValue(String account) {
        return userDao.userListValue(account);
    }
}
