package com.gc.smartbulter.entity;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.entity
 * 文件名  ：CourierData
 * 创建者  ：GC
 * 创建时间：2017/8/12 15:37
 * 描述    ：TODO
 */
public class CourierData  {
    //时间，状态，城市
    private String datetime;
    private String remark;
    private String zone;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "dateTime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
