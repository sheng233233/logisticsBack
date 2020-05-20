package com.zut.service;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zut.dao.RepairDao;
import com.zut.po.Repair;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
@Transactional
public class RepairService {

    @Autowired
    private RepairDao repairDao;

    @Autowired
    private IdWorker idWorker;

    //报修人员列表
    public Result getRepairList(String query,int page, int rows){
        /*Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData;
        if (query==null){
            pageData = repairDao.getRepairList(null,pageble);
        }else {
            pageData = repairDao.getRepairList(query,pageble);
        }*/
        Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData= repairDao.getRepairList(query,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    public Result pageQuery(Repair repair, int pageNum, int pageSize) {

        Example<Repair> example = Example.of(repair);
        //按照id降序排列
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        Sort sort = Sort.by(order);
        //查询
        PageRequest pageRequest = PageRequest.of(pageNum - 1,pageSize,sort);
        Page<Repair> pageDate = repairDao.findAll(example,pageRequest);

        return new Result(StatusCode.OK,"查询成功",new PageResult<Repair>(pageDate.getTotalElements(),pageDate.getContent()));

    }

    //查询所有
    public Result findAll(){
        List<Repair> repairList = repairDao.findAll();
        return new Result(StatusCode.OK,"查询成功",repairList);
    }

    //查询
    public Result findById(String id){
        Repair repair = repairDao.findById(id).get();
        return new Result(StatusCode.OK,"查询成功",repair);
    }

    //新增
    public Result insert(Repair repair){
        repair.setId(idWorker.nextId()+"");
        repairDao.save(repair);
        return new Result(StatusCode.OK,"新增成功",null);
    }

    //修改
    public  Result update(Repair repair){

        repairDao.save(repair); //如果发现id是已存在数据，自动默认使用修改
        return new Result(StatusCode.OK,"修改成功",repairDao.findById(repair.getId()));
    }

    //删除
    public  Result deleteById(String id){

        repairDao.deleteById(id);
        return new Result(StatusCode.OK,"删除成功",null);
    }

    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Repair> findSearch(Map whereMap, int page, int size) {
        Specification<Repair> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return repairDao.findAll(specification, pageRequest);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<Repair> createSpecification(Map searchMap) {

        return new Specification<Repair>() {
            @Override
            public Predicate toPredicate(Root<Repair> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 账号
                if (searchMap.get("username")!=null && !"".equals(searchMap.get("username"))) {
                    predicateList.add(cb.like(root.get("username").as(String.class), "%"+(String)searchMap.get("username")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 电话
                if (searchMap.get("phone")!=null && !"".equals(searchMap.get("phone"))) {
                    predicateList.add(cb.like(root.get("phone").as(String.class), "%"+(String)searchMap.get("phone")+"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };

    }

    /**
     * @Description 校验注册数据
     * @param param 数据
     * @param type 校验类型
     * @return
     */
    public Result check(String param, String type) {
        Repair repair = null;
        switch (type){
            case "1":{  //校验用户名
                repair = repairDao.findByUsername(param);
            }break;

            case "2":{  //校验电话号码
                repair = repairDao.findByPhone(param);
            }break;
        }
        return new Result(StatusCode.OK,repair==null?"校验通过":"校验不通过",repair==null);
    }

    /**
     * 注册
     * @param repair
     * @return
     */
    public Result register(Repair repair) {
        return insert(repair);
    }

    /**
     * 登录
     * @param repair
     * @return
     */
    public Result login(Repair repair) {
        Repair result = repairDao.findByUsernameAndPassword(repair.getUsername(), repair.getPassword());
        return new Result(StatusCode.OK,"OK",result);
    }

    /**
     * 修改用户信息
     * @param repair
     * @param id
     * @return
     */
    public Result edit(Repair repair, String id) {
        int line = repairDao.updateUsernameAndPhoneByid(repair.getUsername(),repair.getPhone(),id);
        if (line == 1){
            return new Result(StatusCode.OK,"信息修改成功",repairDao.findById(id));
        }else {
            return new Result(StatusCode.ERROR,"修改失败,请确认id");
        }

    }

    /**
     * 修改用户密码
     * @param repair
     * @param id
     * @return
     */
    @Transactional
    public Result editPwd(Repair repair, String id, String newPwd) {
        //根据旧密码查询用户
        Repair byUsernameAndPassword = repairDao.findByUsernameAndPassword(repair.getUsername(), repair.getPassword());
        if (byUsernameAndPassword == null){ //旧密码验证不通过,防止绕过前端验证
            return new Result(StatusCode.ERROR,"密码验证不通过");
        }
        //修改密码
        int line = repairDao.updatePasswordByid(newPwd, id);
        if (line == 1){
            Repair byId = repairDao.getById(id);
            System.out.println(byId);
            return new Result(StatusCode.OK,"密码修改成功",byId);
        }else {
            return new Result(StatusCode.ERROR,"修改失败,请确认id");
        }


    }


    /**
     * 微信登录
     * @param code 用户授权码,访问微信接口获得openID,查询数据库确认openID
     * @param nickname 微信昵称,如果第一次授权,进行注册 用户名为昵称
     * @return
     */
    public Result loginByWX(String code, String nickname) { //043VAITM1VAkF91EQzQM1wlXTM1VAIT6
        //微信请求地址  https://api.weixin.qq.com/sns/jscode2session?appid=wx25fe2e9ec9c3a827&secret=4f0fc381419b5174da7df27ccfae32f9&js_code=043VAITM1VAkF91EQzQM1wlXTM1VAIT6&grant_type=authorization_code
        String APPID = "wx25fe2e9ec9c3a827";  //微信小程序id
        String SECRET = "4f0fc381419b5174da7df27ccfae32f9";  //微信小程序密钥
        String openid = getWxUserOpenid(code, APPID, SECRET);  // openID oA0gL48YBbGm3549jWrGqHxxGHTk
        if (openid != null){ //在登录5分钟之内,登录code有效
            Repair repair = repairDao.findByOpenID(openid);
            if (repair == null){//没有查询到相应用户,即为第一册登录,进行注册
                repair = new Repair();
                repair.setId(idWorker.nextId()+"");
                repair.setUsername(nickname);
                repair.setPassword("default");  //默认密码,微信登录后要求修改
                repair.setOpenID(openid);
                register(repair);
                return new Result(StatusCode.OK,"微信登录成功",repair);
            }else{// 查询到相应用户,登录成功,将用户返回
                return new Result(StatusCode.OK,"微信登录成功",repair);
            }
        }else {//登录code失效
            return new Result(StatusCode.ERROR,"登录信息过期,请重新登录");
        }

    }

    //获取openid
    public static String getWxUserOpenid(String code, String APPID, String APPSecret) {
        String openID = null;
        //拼接url
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session?");
        url.append("appid=");//appid设置
        url.append(APPID);
        url.append("&secret=");//secret设置
        url.append(APPSecret);
        url.append("&js_code=");//code设置
        url.append(code);
        url.append("&grant_type=authorization_code");
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(url.toString());    //构建一个GET请求
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
            System.out.println(content);//打印返回的信息
            JSONObject res = JSONObject.parseObject(content);//把信息封装为json

//            System.out.println(res.getString("openid"));
            openID = res.getString("openid");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return openID;
    }


    public Result sitePwd(String password, String id) {
        //修改密码
        int line = repairDao.updatePasswordByid(password, id);
        if (line == 1){
            return new Result(StatusCode.OK,"密码修改成功",repairDao.getById(id));
        }else {
            return new Result(StatusCode.ERROR,"修改失败,请确认id");
        }
    }
}
