package com.gc.smartbulter.entity;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.entity
 * 文件名  ：WeChatData
 * 创建者  ：GC
 * 创建时间：2017/8/15 14:09
 * 描述    ：TODO
 */
public class WeChatData {

    private String id;

    private String title;

    private String source;

    private String firstImg;

    private String mark;

    private String url;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }
    public void setFirstImg(String firstImg){
        this.firstImg = firstImg;
    }
    public String getFirstImg(){
        return this.firstImg;
    }
    public void setMark(String mark){
        this.mark = mark;
    }
    public String getMark(){
        return this.mark;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }

}
