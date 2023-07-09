package com.hxl.springboot.service;

import com.hxl.springboot.pojo.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RelationService {
    ArrayList<Relation> searchHistory(String account1,String account2);
    String addHistory(String speak,String hear,String say);
    ArrayList<Relation> receiveInfo(String account);
}
