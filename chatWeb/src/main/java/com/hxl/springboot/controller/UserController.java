package com.hxl.springboot.controller;


import com.hxl.springboot.pojo.FileResult;
import com.hxl.springboot.pojo.User;
import com.hxl.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600,methods = {RequestMethod.POST,RequestMethod.GET})
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public User login(String account,String password) {
        User user = userService.login(account,password);
        if (user != null) {
            return user;
        }
        return null;
    }

    @RequestMapping("/logon")
    @ResponseBody
    public String logon(String account,String password) {
        String result = userService.logon(account,password);
        return result;
    }

    @RequestMapping("/login/updateName")
    @ResponseBody
    public String updateName(String account,String name) {
        String result = userService.updateName(account,name);
        return result;
    }

    @RequestMapping("/login/updateAvatar")
   public FileResult updateAvatar(@RequestParam("avatar") MultipartFile avatar, HttpServletRequest request,@RequestParam String account) {
        //获取文件在服务器的存储位置
        String path1 = "E://develop//hello//src//chat//my-chat//src//";
        String path2 = "assets//uploadImg";
        String path = path1 + path2;
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }
        //获取原始文件名称(包含格式)
        String originalFileName = avatar.getOriginalFilename();
        System.out.println("原始文件名称：" + originalFileName);

        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);

        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String count = "";
        //排查特殊符号
        for (int i = 0; i < name.length(); i++) {
            if ((name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z')
                    || (name.charAt(i) >= '0' && name.charAt(i) <= '9')) {
                count = count + name.charAt(i);
            }
        }
        name = count;
        String fileName = name + "." + type;
        System.out.println("新文件名称：" + fileName);

        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);
        System.out.println("图片地址："+path+"/"+fileName);
        //将文件保存到服务器指定位置
        try {
            avatar.transferTo(targetFile);
            System.out.println("上传成功");
            System.out.println("account" + account);
            userService.updateAvatar(account,fileName);
            //将文件在服务器的存储路径返回
            return new FileResult(true,fileName,path+"/"+fileName);
        } catch (IOException e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return new FileResult(false, "上传失败","");
        }
    }

    @RequestMapping("/login/searchContact")
    @ResponseBody
    public User searchContact(String account) {
        if (!account.equals("")) {
            User contact = userService.searchContact(account);
            return contact;
        }
        return null;
    }

    @RequestMapping("/login/addContact")
    @ResponseBody
    public String addContact(String account1,String account2) {
        System.out.println("account1" + account1);
        System.out.println("account2" + account2);
        String result = userService.addContact(account1,account2);
        return result;
    }

    @RequestMapping("/login/userList")
    @ResponseBody
    public ArrayList<User> userListValue(String account) {
        System.out.println("account" + account);
        ArrayList<User> users = userService.userListValue(account);
        return users;
    }
}
