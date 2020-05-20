package com.zut.controller.manager;

import com.zut.po.Orderform;
import com.zut.service.OrderformService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/orderform")
public class OrderformController {

    @Autowired
    private OrderformService orderformService;

    /**
     * @Description 分页查看工单列表
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
   /* @RequestMapping(value = "/showall/{page}/{size}",method = RequestMethod.GET)
    public Result getList(@PathVariable int page,@PathVariable int size){
        return orderformService.getOrderformList(page,size);
    }*/
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public Result getList(int pagenum, int pagesize){
        return orderformService.getOrderformList(pagenum,pagesize);
    }

    /**
     * @Description 根据id查询工单
     * @param orderId 工单ID
     * @return Result
     */
    @RequestMapping(value = "/info/{orderId}",method = RequestMethod.GET)
    public Result getById(@PathVariable("orderId") String orderId){
        return orderformService.getById(orderId);
    }

    /**
     * @Description 工单审核：修改工单状态至审核结果
     * @param resultMap 审核结果 字符串 值为(审核通过 或者 审核未通过)
     * @param orderId
     * @return Result
     */
    @RequestMapping(value = "/review/{orderId}",method= RequestMethod.PUT)
    public Result review(@RequestBody Map resultMap, @PathVariable String orderId){
        String result = (String)resultMap.get("result");
        return orderformService.review(result,orderId);
    }

    /**
     * @Description 派单 根据ID更新工单表的MID字段，修改工单状态至 审核通过,未接单
     * @param orderId 工单ID
     * @param mid 选择指派的维修人员ID
     * @return
     */
    @RequestMapping(value = "/appoint/{orderId}/{mid}",method= RequestMethod.PUT)
    public Result appoint(@PathVariable String orderId,@PathVariable String mid){
        return orderformService.appoint(mid,orderId);
    }

    /**
     * @Description 根据状态查询工单
     * @param status 工单状态
     * @param page 请求的页码
     * @param size 每页显示的记录数
     * @return Result
     */
    @RequestMapping(value = "/findByStatus/{page}/{size}",method = RequestMethod.GET)
    public Result findByStatus(String status,@PathVariable int page,@PathVariable int size){
        return orderformService.findByStatus(status,page,size);
    }

    /**
     * @Description 根据分类查询工单
     * @param sort 分类id
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
    /*@RequestMapping(value = "/findBySort/{sid}/{page}/{size}",method = RequestMethod.GET)
    public Result findBySort(@PathVariable String sid,@PathVariable int page,@PathVariable int size){
        return orderformService.findBySort(sid,page,size);
    }*/
    @RequestMapping(value = "/findBySort",method = RequestMethod.GET)
    public Result findBySort( String sort, int pagenum, int pagesize){
        return orderformService.findBySort(sort,pagenum,pagesize);
    }


    @RequestMapping(method = RequestMethod.GET)
    public Result finAll(){
        return orderformService.findAll();
    }

    @RequestMapping(value = "/get/{orderId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("orderId") String orderId){
        return orderformService.findById(orderId);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result insert( @RequestBody Orderform orderform){

        return orderformService.insert(orderform);
    }


    @RequestMapping(value = "/update/{orderId}",method = RequestMethod.PUT)
    public Result update(@RequestBody Orderform orderform, @PathVariable("orderId") String orderId){
        orderform.setId(orderId);
        return orderformService.update(orderform);
    }

    /**
     * @Description 删除工单
     * @param orderId 工单id
     * @return Result
     */
    @RequestMapping(value = "/delete/{orderId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("orderId") String orderId){
        return orderformService.deleteById(orderId);
    }


    /**
     * 报修人员新增订单
     * @param orderform
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result creat(@RequestBody Orderform orderform){
        return orderformService.creat(orderform);
    }


    /**
     * 维修人员接受订单
     * @param ID
     * @return
     */
    @RequestMapping(value = "/receipt/{ID}", method = RequestMethod.PUT)
    public Result receipt(@PathVariable String ID){
        return orderformService.receipt(ID);
    }

    /**
     * 维修完成,更新维修完成图片路径
     * @param orderform 接收maintain_image 数据
     * @param ID 工单ID
     * @return
     */
    @RequestMapping(value = "/after/{ID}", method = RequestMethod.PUT)
    public Result after(@RequestBody Orderform orderform, @PathVariable String ID){
        return orderformService.after(orderform,ID);
    }

    /**
     * 报送配件
     * @param orderform 接收parts 数据
     * @param ID 工单ID
     * @return
     */
    @RequestMapping(value = "/parts/{ID}", method = RequestMethod.PUT)
    public Result parts(@RequestBody Orderform orderform, @PathVariable String ID){
        return orderformService.parts(orderform,ID);
    }


    /**
     * 报修人员删除工单,即把isDelete置为1
     * @param ID
     * @return
     */
    @RequestMapping(value = "/{ID}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String ID){
        return orderformService.delete(ID);
    }


    /**
     * 根据不同用户,显示其所属工单
     * @param userType 维修人员或报修人员 maintainer or repair
     * @param userID 维修人员或报修人员的用户id
     * @return 工单列表
     */
    @RequestMapping(value = "/show/{userType}/{userID}", method = RequestMethod.GET)
    public Result show(@PathVariable String userType,@PathVariable String userID){
        return orderformService.show(userType,userID);
    }



    /**
     * 报修人员修改工单
     * @param orderform
     * @param oid
     * @return
     */
    @RequestMapping(value = "/edit/{oid}", method = RequestMethod.PUT)
    public Result edit(@RequestBody Orderform orderform,@PathVariable String oid){
        return orderformService.edit(orderform,oid);
    }


    /**
     * 微信小程序获得工单详情
     * @param oid
     * @return
     */
    @RequestMapping(value = "/details/{oid}", method = RequestMethod.GET)
    public Result findbyoid(@PathVariable String oid){
        return orderformService.findbyoid(oid);
    }

    /**
     * 微信小程序获得工单列表
     * @param userType
     * @param userID
     * @return
     */
    @RequestMapping(value = "/findlist/{userType}/{userID}",method = RequestMethod.GET)
    public Result findList(@PathVariable String userType, @PathVariable String userID){
        return orderformService.findList(userType,userID);
    }

    /**
     * 微信小程序根据状态获得工单列表
     * @param userID
     * @param status
     * @return
     */
    @RequestMapping(value = "/getByStatus/{userID}/{status}", method = RequestMethod.GET)
    public Result getBystatus(@PathVariable String userID,@PathVariable String status){
        return orderformService.findListByStatus(userID,status);
    }


}
