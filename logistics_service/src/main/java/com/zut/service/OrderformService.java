package com.zut.service;

import com.zut.dao.OrderformDao;
import com.zut.po.Orderform;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderformService {

    @Autowired
    private OrderformDao orderformDao;

    @Autowired
    private IdWorker idWorker;

    //工单列表
    public Result getOrderformList(int page, int rows){
        Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = orderformDao.getOrderformList(pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //根据id查询工单
    public Result getById(String orderId){

        List<Map> orderformList = orderformDao.getById(orderId);
        //JSONArray ja = JSONArray.parseArray(JSON.toJSONString(orderformList));
        return new Result(StatusCode.OK,"查询成功",orderformList);
    }

    //工单审核
    public Result review(String status,String orderId){
        orderformDao.review(status,orderId);
        return new Result(StatusCode.OK,"审核成功",orderformDao.findById(orderId).get().getStatus());
    }

    //选择维修人员进行派单
    public Result appoint(String mid,String orderId){
        orderformDao.appoint(mid,orderId);
        return new Result(StatusCode.OK,"派单成功",orderformDao.findById(orderId).get().getStatus());
    }

    //根据状态查询工单
    public Result findByStatus(String status,int page, int rows){
        Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = orderformDao.findByStatus(status,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //根据分类查询工单
    public Result findBySort(String sort,int page, int rows){
        Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = orderformDao.findBySort(sort,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //查询所有
    public Result findAll(){
        List<Orderform> orderformList = orderformDao.findAll();
        return new Result(StatusCode.OK,"查询成功", orderformList);
    }

    //查询
    public Result findById(String id){
        Orderform orderform = orderformDao.findById(id).get();
        return new Result(StatusCode.OK,"查询成功", orderform);
    }

    //新增
    public Result insert(Orderform orderform){
        orderform.setId(idWorker.nextId()+"");
        orderformDao.save(orderform);
        return new Result(StatusCode.OK,"新增成功",null);
    }

    //修改
    public  Result update(Orderform orderform){
        orderformDao.save(orderform);
        return new Result(StatusCode.OK,"修改成功", orderformDao.findById(orderform.getId()));
    }

    //删除
    public  Result deleteById(String id){
        orderformDao.deleteById(id);
        return new Result(StatusCode.OK,"删除成功",null);
    }

    /**
     * 报修人员提交订单,补全信息,插入
     * @param orderform
     * @return
     */
    public Result creat(Orderform orderform) {
        orderform.setStatus("未审核");
        orderform.setId(idWorker.nextId()+"");
        orderform.setRepair_date(
                new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                        .format(new Date())
        );
        orderform.setIsdelete(0);
        return insert(orderform);
    }



    /**
     * 维修人员接受工单
     * @param id 工单ID
     * @return
     */
    public Result receipt(String id) {
        //将工单状态设置为 已接单,维修中
        int line = orderformDao.updateStatusByid("已接单,维修中",id);
        String order_date = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());
        orderformDao.updateOrderdateById(order_date,id);
        if(line != 1){
            return new Result(StatusCode.ERROR,"请提供正确的ID");
        }
        return new Result(StatusCode.OK,"接单成功",orderformDao.getById(id));
    }


    /**
     * 维修完成,更新维修完成图片路径
     * @param orderform 接收maintain_image 数据
     * @param ID 工单ID
     * @return
     */
    public Result after(Orderform orderform, String ID) {
        //将工单状态设置为 已接单,维修中
        orderformDao.updataMainImgByid(orderform.getMaintain_image(), ID);
        int line = orderformDao.updateStatusByid("已完成维修,报送配件中",ID);
        if(line != 1){
            return new Result(StatusCode.ERROR,"请提供正确的ID");
        }
        return new Result(StatusCode.OK,"维修成功",orderformDao.getById(ID));
    }

    /**
     * 报送配件
     * @param orderform 接收parts 数据
     * @param ID 工单ID
     * @return
     */
    public Result parts(Orderform orderform, String ID) {
        orderformDao.updatePartsByid(orderform.getParts(), ID);
        int line = orderformDao.updateStatusByid("维修完成待评价",ID);
        if(line != 1){
            return new Result(StatusCode.ERROR,"请提供正确的ID");
        }
        return new Result(StatusCode.OK,"报送配件成功",orderformDao.getById(ID));
    }

    /**
     * 报修人员删除工单,即把isDelete置为1
     * @param id
     * @return
     */
    public Result delete(String id) {
        Integer line = orderformDao.deleteOrderform(id);
        if(line != 1){
            return new Result(StatusCode.ERROR,"请提供正确的ID");
        }
        return new Result(StatusCode.OK,"删除成功",null);
    }

    /**
     * 根据不同用户,显示其所属工单
     * @param userType 维修人员或报修人员 maintainer or repair
     * @param userID 维修人员或报修人员的用户id
     * @return 工单列表
     */
    public Result show(String userType, String userID) {
        List<Map> data = null;
        switch (userType){
            case "repair":{
                List<Map> showByRid = orderformDao.showByRid(userID);
                System.out.println(showByRid);
                data = showByRid;
            }break;
            case "maintainer":{
                List<Map> showByMid = orderformDao.showByMid(userID);
                System.out.println(showByMid);
                data = showByMid;
            }break;
            default:{
                data = new ArrayList<>();
            }break;
        }



        return new Result(StatusCode.OK,"查询成功",data);
    }

    /**
     * 微信小程序工单详情
     * @param oid
     * @return
     */
    public Result findbyoid(String oid) {
        return new Result(StatusCode.OK,"查询成功",orderformDao.findOrderformById(oid));

    }

    /**
     * 微信小程序获得工单列表
     * @param userType
     * @param userID
     * @return
     */
    public Result findList(String userType, String userID){
        switch (userType){
            case "repair":{
                return new Result(StatusCode.OK,"查询成功",orderformDao.getRepairList(userID));
            }
            case "maintainer":{
                return new Result(StatusCode.OK,"查询成功",orderformDao.getMainList(userID));
            }
            default:{
                return new Result(StatusCode.OK,"失败");
            }
        }

    }

    /**
     * 微信小程序根据状态获得工单列表
     * @param userID
     * @return
     */
    public Result findListByStatus(String userID, String status){
        Object data;
        switch(status){
            case "allocated":{
                data = orderformDao.getMainListByStatus(userID,"审核通过,未接单");
            }break;
            case "accept":{
                data = orderformDao.getMainListByStatus(userID,"已接单,维修中");
            }break;
            default:{
                data = orderformDao.getMainList(userID);
            }break;
        }


        return new Result(StatusCode.OK,"查询成功",data);
    }


    /**
     * 报修人员修改工单信息
     * @param orderform
     * @param oid
     * @return
     */
    public Result edit(Orderform orderform, String oid) {
        orderform.setRepair_date(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        Integer line = orderformDao.update(
                orderform.getRepair_image(),
                orderform.getDescription(),
                orderform.getRemarks(),
                orderform.getContact_person(),
                orderform.getContact_number(),
                orderform.getRepair_date(),
                oid
        );
        if (line==1){
            return new Result(StatusCode.OK,"修改成功");
        }
        return new Result(StatusCode.ERROR,"修改失败,请检查oid");
    }
}
