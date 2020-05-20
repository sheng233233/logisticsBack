package com.zut.controller.manager;

import com.zut.po.Repair;
import com.zut.service.RepairService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    /*@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<Repair> pageList = repairService.findSearch(searchMap, page, size);
        return  new Result(StatusCode.OK,"查询成功",  new PageResult<Repair>(pageList.getTotalElements(), pageList.getContent()) );
    }*/

    /**
     * @Description 分页查看报修人员列表
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
   /* @RequestMapping(value = "/showall/{pagenum}/{pagesize}",method = RequestMethod.GET)
    public Result getList(@PathVariable int pagenum,@PathVariable int pagesize){
        return repairService.getRepairList(pagenum,pagesize);
    }*/
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public Result getList(String query,int pagenum,int pagesize){
        return repairService.getRepairList(query,pagenum,pagesize);
    }

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.GET)
    public Result pageQuery(@PathVariable int page, @PathVariable int size){
        return repairService.pageQuery(new Repair(),page,size);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result finAll(){
        return repairService.findAll();
    }

    /**
     * @Description 根据id查询报修人员
     * @param rid 报修人员id
     * @return Result
     */
    @RequestMapping(value = "/get/{rid}",method = RequestMethod.GET)
    public Result findById(@PathVariable("rid") String rid){
        return repairService.findById(rid);
    }

    /**
     * @Description 新增报修人员信息
     * @param repair
     * @return Result
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result insert( @RequestBody Repair repair){

        return repairService.insert(repair);
    }

    /**
     * @Description 修改报修人员信息
     * @param repair
     * @param rid 报修人员id
     * @return Result
     */
    @RequestMapping(value = "/update/{rid}",method = RequestMethod.PUT)
    public Result update( @RequestBody Repair repair,@PathVariable("rid") String rid){
        repair.setId(rid);
        return repairService.update(repair);
    }

    /**
     * @Description 删除报修人员
     * @param rid 报修人员id
     * @return Result
     */
    @RequestMapping(value = "/delete/{rid}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("rid") String rid){
        return repairService.deleteById(rid);
    }

    /**
     * @Description 校验注册数据
     * @param param 数据
     * @param type 校验类型 1用户名 2手机号
     * @return
     */
    @RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
    public Result check(@PathVariable("param") String param, @PathVariable("type") String type){
        return repairService.check(param, type);
    }

    /**
     * 注册
     * @param repair
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody Repair repair){
        return repairService.register(repair);
    }

    /**
     * 登录
     * @param repair
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody Repair repair){
        return repairService.login(repair);
    }

    /**
     * 修改用户信息
     * @param repair
     * @param id
     * @return
     */
    @RequestMapping(value = "/update/{id}")
    public Result edit(@RequestBody Repair repair, @PathVariable String id){
        return repairService.edit(repair, id);
    }

    /**
     * 修改密码
     * @param repair
     * @param id
     * @return
     */
    @RequestMapping(value = "/updatePwd/{id}")
    public Result editPwd(@RequestBody RepairPwd repair, @PathVariable String id){
        return repairService.editPwd(repair, id, repair.getNewPwd());
    }

//    /**
//     * 微信登录
//     * @param wxlogin 接收微信登录所需要的字段
//     * @return
//     */
//    @RequestMapping(value = "/loginByWX", method = RequestMethod.POST)
//    public Result loginByWX(@RequestBody Wxlogin wxlogin){
//        return repairService.loginByWX(wxlogin.getCode(), wxlogin.getNickname());
//    }

    /**
     * 微信登录
     * @param wxlogin 接收微信登录所需要的字段
     * @return
     */
    @RequestMapping(value = "/loginByWX", method = RequestMethod.POST)
    public Result loginByWX(@RequestBody Map wxlogin){
        return repairService.loginByWX((String) wxlogin.get("code"), (String) wxlogin.get("nickname"));
    }


    @RequestMapping(value = "/sitePwd/{id}",method = RequestMethod.PUT)
    public Result sitePwd(@RequestBody Map map, @PathVariable String id){
        return repairService.sitePwd((String) map.get("password"), id);
    }
}


/**
 * 用于接收修改密码的用户
 */
class RepairPwd extends Repair{
    private String newPwd;

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}

