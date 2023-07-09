package com.hxl.springboot;

import com.hxl.springboot.controller.RelationController;
import com.hxl.springboot.controller.UserController;
import com.hxl.springboot.pojo.Relation;
import com.hxl.springboot.pojo.User;
import com.hxl.springboot.service.RelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@SpringBootTest
public class Springboot04DataApplicationTests {

    @Autowired
    private RelationController relationController;

    @Test
    public void test() {
//        User result = userController.login("123456","123456");
//        System.out.println("result  " + result);
        ArrayList<Relation> relations = relationController.receiveInfo("123456");
        System.out.println(relations);
    }
}
