package com.zut.dao;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Value;
import com.zut.po.Orderform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderformDao extends JpaRepository<Orderform,String>, JpaSpecificationExecutor<Orderform> {


    @Query(value = "SELECT a.id,sid,b.name maintain_type,description,repair_date,status,parts FROM orderform a,sort b WHERE a.sid=b.id",nativeQuery = true)
    public Page<List<Map>> getOrderformList(Pageable pageable);

    @Query(value = "SELECT a.id,a.sid,d.name maintain_type,rid,b.username repair_name,mid,c.username maintainer_name,c.phone maintainer_phone,cid,e.level comment_level," +
            "e.details comment_details,location, description,remarks,status,repair_date,repair_image,contact_person,contact_number,order_date,maintain_image,parts " +
            "FROM orderform a,repair b,maintainer c,sort d,comment e WHERE a.rid=b.id AND a.mid=c.id AND a.sid=d.id AND a.cid=e.id and a.id=?",nativeQuery = true)
    public List<Map> getById(String orderId);



    //查询不需要，增删改都需要  state=1 自己约定1是审核通过，0是未通过
    @Modifying
    @Query(value="UPDATE orderform set status=? WHERE id=?",nativeQuery = true)
    public void review(String status,String orderId);

    @Modifying
    @Query(value="UPDATE orderform set mid=?,status='审核通过,未接单' WHERE id=?",nativeQuery = true)
    public void appoint(String mid,String orderId);

    //根据状态查询工单
    @Query(value = "SELECT a.id,sid,b.name maintain_type,description,repair_date,status,parts FROM orderform a,sort b WHERE a.sid=b.id and status=?",nativeQuery = true)
    public Page<List<Map>> findByStatus(String status,Pageable pageable);

    //根据分类查询工单
    @Query(value = "SELECT a.id,sid,b.name maintain_type,description,repair_date,status,parts FROM orderform a,sort b WHERE a.sid=b.id and b.name like %?%",nativeQuery = true)
    public Page<List<Map>> findBySort(String sort,Pageable pageable);


    /**
     * 更新工单状态
     * @param status
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update Orderform set status=?1 where id=?2")
    public Integer updateStatusByid(String status, String id);

    /**
     * 更新维修完成的图片路径
     * @param mainImg
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update Orderform  set maintain_image=?1 where id=?2")
    public Integer updataMainImgByid(String mainImg, String id);

    /**
     * 更新配件信息
     * @param parts
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update Orderform  set parts=?1 where id=?2")
    public Integer updatePartsByid(String parts, String id);



    @Modifying
    @Query(value = "update Orderform set repair_image=?1, description=?2, remarks=?3, contact_person=?4, contact_number=?5, repair_date=?6, status='未审核' where id=?7")
    public Integer update(String repair_image,String description,String remarks,String contact_person,String contact_number,String repair_date,String id);

    /**
     * 报修人员删除工单
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update Orderform  set isdelete=1 where id=?1")
    public Integer deleteOrderform(String id);


    /**
     * 新增评价
     * @param cid
     * @param oid
     * @return
     */
    @Modifying
    @Query(value = "update  Orderform set cid=?1 where id=?2")
    public Integer updateOrderformComment(String cid, String oid);

    /**
     * 更新维修人员接单日期
     * @param orderdate
     * @param oid
     * @return
     */
    @Modifying
    @Query(value = "update  Orderform set order_date=?1 where id=?2")
    public Integer updateOrderdateById(String orderdate, String oid);

    @Query(value = "select orderform.ID, orderform.`status`, orderform.description, orderform.repair_date, sort.`name` sortName " +
            "FROM orderform,sort WHERE orderform.sid=sort.ID AND isdelete=0 AND rid=?1", nativeQuery = true)
    public List<Map> showByRid(String rid);

    @Query(value = "select orderform.ID, orderform.`status`, orderform.description, orderform.repair_date, sort.`name` sortName " +
            "FROM orderform,sort WHERE orderform.sid=sort.ID AND isdelete=0 AND mid=?1", nativeQuery = true)
    public List<Map> showByMid(String mid);

    /**
     * 微信小程序工单详情
     * @param id
     * @return
     */
    @Query(value = "SELECT orderform.ID,orderform.location,orderform.rid,sort.`name` sort_name,orderform.description,orderform.repair_image,orderform.remarks,contact_person,contact_number,status,orderform.mid,maintainer.username maintainer_name,orderform.maintain_image,`comment`.level comment_level,`comment`.details comment_details\n" +
            "FROM orderform LEFT JOIN maintainer ON orderform.mid=maintainer.ID LEFT JOIN sort ON orderform.sid=sort.ID LEFT JOIN `comment` ON orderform.cid=comment.ID\n" +
            "WHERE orderform.isdelete=0 AND orderform.ID=?1", nativeQuery = true)
    public Map findOrderformById(String id);

    /**
     * 微信小程序报修工单列表
     * @param
     * @return
     */
    @Query(value = "SELECT orderform.ID,status,sort.name sid,description,repair_date \n" +
            "FROM orderform LEFT JOIN maintainer ON orderform.mid=maintainer.ID LEFT JOIN sort ON orderform.sid=sort.ID LEFT JOIN `comment` ON orderform.cid=comment.ID\n" +
            "WHERE orderform.isdelete=0 and orderform.rid=?1", nativeQuery = true)
    public List<Map> getRepairList(String rid);

    /**
     * 微信小程序维修工单列表
     * @param
     * @return
     */
    @Query(value = "SELECT orderform.ID,status,sort.name sid,description,repair_date \n" +
            "FROM orderform LEFT JOIN maintainer ON orderform.mid=maintainer.ID LEFT JOIN sort ON orderform.sid=sort.ID LEFT JOIN `comment` ON orderform.cid=comment.ID\n" +
            "WHERE orderform.isdelete=0 and orderform.mid=?1", nativeQuery = true)
    public List<Map> getMainList(String mid);

    /**
     * 微信小程序通过状态维修工单列表
     * @param
     * @return
     */
    @Query(value = "SELECT orderform.ID,status,sort.name sid,description,repair_date \n" +
            "FROM orderform LEFT JOIN maintainer ON orderform.mid=maintainer.ID LEFT JOIN sort ON orderform.sid=sort.ID LEFT JOIN `comment` ON orderform.cid=comment.ID\n" +
            "WHERE orderform.isdelete=0 and orderform.mid=?1 AND status=?2", nativeQuery = true)
    public List<Map> getMainListByStatus(String mid, String status);

}


