package com.zut.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 管理员实体类
 */
@Entity
public class Administrator implements Serializable {
    @Id
    private String id;
    private String username;//账号
    private String password;//密码

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
}
