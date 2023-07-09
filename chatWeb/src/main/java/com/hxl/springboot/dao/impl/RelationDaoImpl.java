package com.hxl.springboot.dao.impl;

import com.hxl.springboot.dao.RelationDao;
import com.hxl.springboot.pojo.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class RelationDaoImpl implements RelationDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //查询以往的消息
    public ArrayList<Relation> searchHistory(String account1,String account2) {
        String sql = "select * from relation_main where (faccount=? and saccount=?) or (saccount=? and faccount=?)";
        //封装
        Object[] objects = new Object[4];
        objects[0] = account1;
        objects[1] = account2;
        objects[2] = account1;
        objects[3] = account2;
        ArrayList<Relation> history = new ArrayList<>();
        List<Map<String, Object>> h = jdbcTemplate.queryForList(sql,objects);
        for (int i = 0; i < h.size(); i++) {
            Relation relation = new Relation();
            relation.setSpeak((String) h.get(i).get("faccount"));
            relation.setHear((String) h.get(i).get("saccount"));
            relation.setSay((String) h.get(i).get("say"));
            relation.setDate(String.valueOf(h.get(i).get("date")));
            relation.setIsRead((Integer) h.get(i).get("isread"));
            history.add(relation);
        }
        setRead(account1,account2);
        return history;
    }

    //增加消息记录（默认是speak说）
    public String addHistory(String speak,String hear,String say) {
        String sql = "insert into relation_main(faccount,saccount,date,say,isread) values (?,?,?,?,?)";
        //封装
        Object[] objects = new Object[5];
        objects[0] = speak;
        objects[1] = hear;
        Date day=new Date();
        //管道格式化
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objects[2] = df.format(day);
        objects[3] = say;
        //0未读1已读
        objects[4] = 0;
        try {
            jdbcTemplate.update(sql,objects);
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public ArrayList<Relation> receiveInfo(String account) {
        String sql = "select * from relation_main where saccount=? and isread=?";
        //封装
        Object[] objects = new Object[2];
        objects[0] = account;
        objects[1] = 0;
        ArrayList<Relation> receive = new ArrayList<>();
        List<Map<String, Object>> r = jdbcTemplate.queryForList(sql,objects);
        for (int i = 0; i < r.size(); i++) {
            Relation relation = new Relation();
            relation.setSpeak((String) r.get(i).get("faccount"));
            relation.setHear((String) r.get(i).get("saccount"));
            relation.setSay((String) r.get(i).get("say"));
            relation.setDate(String.valueOf(r.get(i).get("date")));
            receive.add(relation);
        }
        return receive;
    }

    //设为已读
    public void setRead(String account1,String account2) {
        String sql = "update relation_main set isread=? where faccount=? and saccount=?";
        //封装
        Object[] objects = new Object[3];
        objects[0] = 1;
        objects[1] = account2;
        objects[2] = account1;
        try {
            int i = jdbcTemplate.update(sql,objects);
            System.out.println("影响的行数: " + i);
        }catch (Exception e) {
            System.out.println("未修改成功！");
        }
    }
}
