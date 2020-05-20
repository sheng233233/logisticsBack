package com.zut.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *维修类别实体类
 */
@Entity
public class Sort {
    @Id
    private String id;
    private String name;//类别名称
    private String pid;//上级类别

    /*private String deleted;*/

    /*public String getDeleted() {
        return deleted;
    }
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }*/
    /**
     * 父类
     *//*
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pid")
    private Sort parent;

    *//**
     * 子类
     *//*
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid")
    @OrderBy("id asc")
    private List<Sort> children = new ArrayList<>();

    public Sort getParent() {
        return parent;
    }

    public void setParent(Sort parent) {
        this.parent = parent;
    }

    public List<Sort> getChildren() {
        if (this.children.size() == 0) {
            return null;
        }
        return children;
    }

    public void setChildren(List<Sort> children) {
        this.children = children;
    }*/

    /*@OneToMany()
    private List<Maintainer> maintainers;*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    /*public List<Maintainer> getMaintainers() {
        return maintainers;
    }

    public void setMaintainers(List<Maintainer> maintainers) {
        this.maintainers = maintainers;
    }*/
}
