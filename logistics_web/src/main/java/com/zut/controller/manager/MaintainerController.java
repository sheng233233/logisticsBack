package com.zut.controller.manager;

import com.zut.po.Maintainer;
import com.zut.po.Repair;
import com.zut.service.MaintainerService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/maintainer")
public class MaintainerController {

    @Autowired
    private MaintainerService maintainerService;

    /**
     * @Description 分页查询维修人员列表
     * @param query 查询条件
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
    /*@RequestMapping(value = "/showall/{page}/{size}",method = RequestMethod.GET)
    public Result getList(@PathVariable int page,@PathVariable int size){
        return maintainerService.getMaintainerList(page,size);
    }*/
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public Result getList( String query,int pagenum, int pagesize){
        return maintainerService.getMaintainerList(query,pagenum,pagesize);
    }

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.GET)
    public Result pageQuery(@PathVariable int page, @PathVariable int size){
        Maintainer maintainer = new Maintainer();
        Page<Maintainer> pageDate= maintainerService.pageQuery(maintainer,page,size);
        return new Result(StatusCode.OK,"查询成功",new PageResult<Maintainer>(pageDate.getTotalElements(),pageDate.getContent()));

    }

    @RequestMapping(method = RequestMethod.GET)
    public Result finAll(){
        return new Result(StatusCode.OK,"查询成功",maintainerService.findAll());
    }

    /**
     * @Description 根据id查询维修人员
     * @param mid 维修人员ID
     * @return Result
     */
    @RequestMapping(value = "/get/{mid}",method = RequestMethod.GET)
    public Result getById(@PathVariable("mid") String mid){
        return maintainerService.getById(mid);
    }

    /**
     * @Description 根据工单所属类别查询维修师傅名单
     * @param sid 分类ID
     * @return Result
     */
    @RequestMapping(value = "/getBySort/{sid}",method = RequestMethod.GET)
    public Result getBySort(@PathVariable("sid") String sid){
        return maintainerService.getBySort(sid);
    }

    /*@RequestMapping(value = "/{mid}",method = RequestMethod.GET)
    public Result findById(@PathVariable("mid") String mid){
        return new Result(StatusCode.OK,"查询成功",maintainerService.findById(mid));
    }*/

    /**
     * @Description 新增维修人员
     * @param maintainer
     * @return Result
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result insert( @RequestBody Maintainer maintainer){

        return maintainerService.insert(maintainer);
    }

    /**
     * @Description 修改维修人员信息
     * @param maintainer
     * @param mid 目标维修员的id
     * @return Result
     */
    @RequestMapping(value = "/update/{mid}",method = RequestMethod.PUT)
    public Result update( @RequestBody Maintainer maintainer,@PathVariable("mid") String mid){
        maintainer.setId(mid);
        return maintainerService.update(maintainer);
    }

    /**
     * @Description 删除维修人员
     * @param mid
     * @return Result
     */
    @RequestMapping(value = "/delete/{mid}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("mid") String mid){
        return maintainerService.deleteById(mid);
    }


    /**
     * 登录
     * @param maintainer
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody Maintainer maintainer){
        return maintainerService.login(maintainer);
    }

    /**
     * 修改用户信息
     * @param maintainer
     * @param id
     * @return
     */
    @RequestMapping(value = "/update/{id}")
    public Result edit(@RequestBody Maintainer maintainer, @PathVariable String id){
        return maintainerService.edit(maintainer, id);
    }

    /**
     * 修改密码
     * @param maintainer
     * @param id
     * @return
     */
    @RequestMapping(value = "/updatePwd/{id}")
    public Result editPwd(@RequestBody MaintainerPwd maintainer, @PathVariable String id){
        return maintainerService.editPwd(maintainer, id, maintainer.getNewPwd());
    }

    /**
     * 微信登录
     * @param wxlogin 接收微信登录所需要的字段
     * @return
     */
    @RequestMapping(value = "/loginByWX", method = RequestMethod.POST)
    public Result loginByWX(@RequestBody Map wxlogin){
        return maintainerService.loginByWX((String) wxlogin.get("code"), (String) wxlogin.get("nickname"));
    }

}

/**
 * 用于接收修改密码的用户
 */
class MaintainerPwd extends Maintainer{
    private String newPwd;

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}



