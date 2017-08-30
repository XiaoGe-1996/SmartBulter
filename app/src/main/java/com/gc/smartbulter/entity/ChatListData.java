package com.gc.smartbulter.entity;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.entity
 * 文件名  ：ChatListData
 * 创建者  ：GC
 * 创建时间：2017/8/15 8:38
 * 描述    ：对话列表实体类
 */
public class ChatListData  {

    //文本
    private String text;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
