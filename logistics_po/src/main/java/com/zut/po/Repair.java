package com.zut.po;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 报修人员实体类
 */
@Entity
public class Repair {
    @Id
    private String id;
    private String username;
    private String password;
    private String phone;
    private String openID;

    @Override
    public String toString() {
        return "Repair{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", openID='" + openID + '\'' +
                '}';
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

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

}
