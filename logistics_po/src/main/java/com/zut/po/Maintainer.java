package com.zut.po;

import javax.persistence.*;

/**
 * 维修人员实体类
 */
@Entity
public class Maintainer {
    @Id
    private String id;
    private String username;
    private String password;
    private String phone;//联系电话
    private String sid;//维修类型关联外键 sort.id
    private String openID;

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

//    private String grade;
//    public String getGrade() {
//        return grade;
//    }
//    public void setGrade(String grade) {
//        this.grade = grade;
//    }
    /*@ManyToOne
    private Sort sort;*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    /*public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }*/
}
