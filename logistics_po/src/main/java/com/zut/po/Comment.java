package com.zut.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 评价实体类
 */
@Entity
public class Comment implements Serializable {
    @Id
    private String id;
    private Long level;//星级
    private String details;//详细评价

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
