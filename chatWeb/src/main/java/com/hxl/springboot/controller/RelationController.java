package com.hxl.springboot.controller;

import com.hxl.springboot.pojo.Relation;
import com.hxl.springboot.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600,methods = {RequestMethod.POST,RequestMethod.GET})
public class RelationController {

    @Autowired
    private RelationService relationService;

    @RequestMapping("/addHistory")
    @ResponseBody
    public String addHistory(String speak,String hear,String say) {
        return relationService.addHistory(speak, hear, say);
    }

    @RequestMapping("/searchHistory")
    @ResponseBody
    public ArrayList<Relation> searchHistory(String account1,String account2) {
        return relationService.searchHistory(account1,account2);
    }

    @RequestMapping("/login/isRead")
    @ResponseBody
    public ArrayList<Relation> receiveInfo(String account) {
        return relationService.receiveInfo(account);
    }
}
