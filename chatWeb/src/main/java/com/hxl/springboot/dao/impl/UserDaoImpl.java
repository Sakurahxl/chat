package com.hxl.springboot.dao.impl;

import com.hxl.springboot.dao.UserDao;
import com.hxl.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //查询所有关系
    public List<Map<String,Object>> userList(String account) {
        String sql = "select * from relation where faccount=? or saccount=?";
        //封装
        Object[] objects = new Object[2];
        objects[0] = account;
        objects[1] = account;
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql,objects);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).get("saccount").equals(account)) {
                String account2 = (String) users.get(i).get("faccount");
                users.get(i).put("faccount",account);
                users.get(i).put("saccount",account2);
            }
        }
        return users;
    }

    //查询所有关系的值
    public ArrayList<User> userListValue(String account) {
        List<Map<String, Object>> users = userList(account);
        ArrayList<User> usersValue = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            usersValue.add(searchContact((String) users.get(i).get("saccount")));
        }
        return usersValue;
    }

    //注册
    public String addUser(String account,String password) {
        String sql = "insert into user(account,password,name) values (?,?,?)";
        //封装
        Object[] objects = new Object[3];
        objects[0] = account;
        objects[1] = password;
        objects[2] = "未命名";
        try {
            jdbcTemplate.update(sql,objects);
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

//    public String delUser (@RequestParam("account") String account) {
//        String sql = "delete from user where account=?";
//        jdbcTemplate.update(sql,account);
//        return "success";
//    }

    //登录
    public User searchUser(String account,String password) {
        String sql = "select * from user where account=? and password=?";
        //每行查询并将结果进行赋值
        RowMapper<User> userRowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setAccount(resultSet.getString("account"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setAvatar(resultSet.getString("avatar"));
                return user;
            }
        };
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper,account,password);
            return user;
        }catch (DataAccessException e) {
            System.out.println("账号或密码错误");
            return null;
        }
    }

    //修改名字
    public String updateName(String account,String name) {
        String sql = "update mychat.user set name=? where account=?";
        //封装
        Object[] objects = new Object[2];
        objects[0] = name;
        objects[1] = account;
        try {
            int i = jdbcTemplate.update(sql,objects);
            System.out.println("影响的行数: " + i);
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    //修改头像
    public String updateAvatar(String account,String avatar) {
        String sql = "update mychat.user set avatar=? where account=?";
        //封装
        Object[] objects = new Object[2];
        objects[0] = avatar;
        objects[1] = account;
        try {
            int i = jdbcTemplate.update(sql,objects);
            System.out.println("影响的行数: " + i);
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    //搜索联系人(包括自己)
    public User searchContact(String account) {
        String sql = "select * from user where account=?";
        //每行查询并将结果进行赋值
        RowMapper<User> userRowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setAccount(resultSet.getString("account"));
                user.setPassword(null);
                user.setName(resultSet.getString("name"));
                user.setAvatar(resultSet.getString("avatar"));
                return user;
            }
        };
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper,account);
            return user;
        }catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    //建立关系
    public String addContact(String account1,String account2) {
        List<Map<String, Object>> accounts = userList(account1);
        boolean judge = true;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).get("saccount").equals(account2)) {
                judge = false;
                break;
            }
        }
        //验证是否已存在
        if (judge) {
            String sql = "insert into relation(faccount,saccount) values (?,?)";
            //封装
            Object[] objects = new Object[2];
            objects[0] = account1;
            objects[1] = account2;
            try {
                jdbcTemplate.update(sql,objects);
            }catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            return "success";
        }else {
            return "exist";
        }
    }
}
