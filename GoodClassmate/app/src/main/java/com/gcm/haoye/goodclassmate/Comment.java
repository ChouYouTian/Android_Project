package com.gcm.haoye.goodclassmate;

/**
 * Created by kancheng on 2017/12/6.
 */

public class Comment {
    private long list_id;
    private int money;
    private String name;
    private String dataend;
    private String datestart;
    private String wedt;

    @Override
    public String toString() {
        return "Comment{" +
                "list_id=" + list_id +
                ", money=" + money +
                ", name='" + name + '\'' +
                ", dataend='" + dataend + '\'' +
                ", datestart='" + datestart + '\'' +
                ", wedt='" + wedt + '\'' +
                '}';
    }

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataend() {
        return dataend;
    }

    public void setDataend(String dataend) {
        this.dataend = dataend;
    }

    public String getDatestart() {
        return datestart;
    }

    public void setDatestart(String datestart) {
        this.datestart = datestart;
    }

    public String getWedt() {
        return wedt;
    }

    public void setWedt(String wedt) {
        this.wedt = wedt;
    }
}
