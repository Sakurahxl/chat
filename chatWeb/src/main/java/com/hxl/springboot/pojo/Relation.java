package com.hxl.springboot.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//消息表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation {
    //speak,hear都存账号
    private String speak;
    private String hear;
    private String say;
    private String date;
    private int isRead;
}
