package com.gc.smartbulter.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.entity
 * 文件名  ：MyUser
 * 创建者  ：GC
 * 创建时间：2017/8/9 13:45
 * 描述    ：用户属性
 */
public class MyUser extends BmobUser{

    private int age;
    private boolean sex;
    private String desc;

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
