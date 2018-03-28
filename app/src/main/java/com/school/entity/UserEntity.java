package com.school.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by XIAOHAO-PC on 2018-02-19.
 */

public class UserEntity extends DataSupport{
    private String name;
    private String school;
    private String tel;
    private String password;
    private int id;
    private String header;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", school='" + school + '\'' +
                ", tel='" + tel + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", header='" + header + '\'' +
                '}';
    }
}
