package com.hxl.springboot.service.impl;

import com.hxl.springboot.dao.RelationDao;
import com.hxl.springboot.pojo.Relation;
import com.hxl.springboot.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationDao relationDao;

    public ArrayList<Relation> searchHistory(String account1,String account2) {
        return relationDao.searchHistory(account1,account2);
    }

    public String addHistory(String speak,String hear,String say) {
        return relationDao.addHistory(speak,hear,say);
    }

    public ArrayList<Relation> receiveInfo(String account) {
        return relationDao.receiveInfo(account);
    }
}
