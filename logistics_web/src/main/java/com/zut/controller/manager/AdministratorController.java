package com.zut.controller.manager;

import com.zut.po.Administrator;
import com.zut.service.AdministratorService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController  //@ResponseBody+@Controller
@CrossOrigin  //所有数据支持跨域了
@RequestMapping("/admin")
public class AdministratorController {
    @Autowired
    private AdministratorService adminiservice;

    /*@RequestMapping
    public Result login(String username,String password){
        return null;
    }*/

    /*@RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result getByUserNameAndAge(@RequestBody Administrator administrator){
        return administratorservice.login(administrator.getUsername(), administrator.getPassword());
    }*/

    /**
     * @Description: 管理员登录
     * @param username
     * @param password
     * @return Result
     */
    @RequestMapping(value = "/login")
    public Result getByUserNameAndAge(String username,String password){
        return adminiservice.login(username, password);
    }

    /**
     * @Description 分页查看管理员列表
     * @param query 查询条件
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public Result getList(String query,int pagenum,int pagesize){
        return adminiservice.getAdminList(query,pagenum,pagesize);
    }

    /*@RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result searchByxxx(@RequestBody Administrator administrator){
        Result result = administratorservice.searchLabel(administrator);
        return result;
    }*/

    @RequestMapping(method = RequestMethod.GET)
    public Result finAll(){
        //System.out.println(1/0);//故意造错
        return adminiservice.findAll();
    }

    //根据id查询管理员
    @RequestMapping(value = "/get/{adminId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("adminId") String adminId){
        return adminiservice.findById(adminId);
    }

    //新增
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result insert( @RequestBody Administrator administrator){

        return adminiservice.insert(administrator);
    }

    /**
     * @Description 修改当前管理员信息
     * @param admin
     * @param adminId
     * @return Result
     */
    @RequestMapping(value = "/update/{adminId}",method = RequestMethod.PUT)
    public Result update( @RequestBody Administrator admin,@PathVariable("adminId") String adminId){
        admin.setId(adminId);
        return adminiservice.update(admin);
    }

    //删除管理员信息
    @RequestMapping(value = "/delete/{adminId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("adminId") String adminId){
        return adminiservice.deleteById(adminId);
    }

}
