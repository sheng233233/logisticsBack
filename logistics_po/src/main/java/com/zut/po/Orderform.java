package com.zut.po;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *工单实体类
 */
@Entity
public class Orderform {
    @Id
    private String id;//工单ID
    private String rid;//报修人员ID
    private String mid;//维修人员ID
    private String sid;//类别ID
    private String cid;//评价ID
    private String location;//地点
    private String description;//描述
    private String remarks;//备注
    private String status;//状态
    private String repair_date;//报修日期
    private String repair_image;//报修图片
    private String contact_person;//联系人姓名
    private String contact_number;//联系电话
    private String order_date;//接单日期
    private String maintain_image;//维修完成图片
    private String parts;//配件信息
    private Integer isdelete;//是否被删除 0:未删除  1:已删除

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setRepair_date(String repair_date) {
        this.repair_date = repair_date;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepair_date() {
        return repair_date;
    }

    public String getRepair_image() {
        return repair_image;
    }

    public void setRepair_image(String repair_image) {
        this.repair_image = repair_image;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }



    public String getMaintain_image() {
        return maintain_image;
    }

    public void setMaintain_image(String maintain_image) {
        this.maintain_image = maintain_image;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }
}
