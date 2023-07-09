package com.hxl.springboot.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用户表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String account;
    private String password;
    private String name;
    private String avatar;
}
