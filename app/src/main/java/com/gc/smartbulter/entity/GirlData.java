package com.gc.smartbulter.entity;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.entity
 * 文件名  ：GirlData
 * 创建者  ：GC
 * 创建时间：2017/8/17 11:16
 * 描述    ：美女社区实体类
 */
public class GirlData {

    private String ctime;

    private String title;

    private String description;

    private String picUrl;

    private String url;

    public void setCtime(String ctime){
        this.ctime = ctime;
    }
    public String getCtime(){
        return this.ctime;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
    }
    public String getPicUrl(){
        return this.picUrl;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }

}
