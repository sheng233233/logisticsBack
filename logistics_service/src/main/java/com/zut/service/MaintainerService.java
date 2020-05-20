package com.zut.service;

import com.alibaba.fastjson.JSONObject;
import com.zut.dao.MaintainerDao;
import com.zut.po.Maintainer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MaintainerService {

    @Autowired
    private MaintainerDao maintainerDao;

    @Autowired
    private IdWorker idWorker;

    //维修人员列表
    public Result getMaintainerList(String query,int page, int rows){
        Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = maintainerDao.getMaintainerList(query,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //根据id查询维修人员
    public Result getById(String mid){

        List<Map> maintainerList = maintainerDao.getById(mid);
        return new Result(StatusCode.OK,"查询成功",maintainerList);
    }

    //根据工单所属类别查询维修师傅名单
    public Result getBySort(String sid){
        List<Maintainer> maintainerList = maintainerDao.findMaintainerBySid(sid);
        return new Result(StatusCode.OK,"查询成功",maintainerList);
    }

    //查询所有
    public List<Maintainer> findAll(){
        return maintainerDao.findAll();
    }

    //查询
    public Maintainer findById(String id){
        return maintainerDao.findById(id).get();
    }

    //新增
    public Result insert(Maintainer maintainer){
        maintainer.setId(idWorker.nextId()+"");
        maintainerDao.save(maintainer);
        return new Result(StatusCode.OK,"新增成功",null);
    }

    //修改
    public  Result update(Maintainer maintainer){

        maintainerDao.save(maintainer); //如果发现id是已存在数据，自动默认使用修改
        return new Result(StatusCode.OK,"修改成功",maintainerDao.findById(maintainer.getId()).get());
    }

    //删除
    public  Result deleteById(String id){

        maintainerDao.deleteById(id);
        return new Result(StatusCode.OK,"删除成功",null);
    }

    public Page<Maintainer> pageQuery(Maintainer maintainer, int pageNum, int pageSize) {
       /* Pageable pageable= PageRequest.of(page-1,size);
        return maintainerDao.findAll(new Specification<Maintainer>() ,pageable);*/

        Example<Maintainer> example = Example.of(maintainer);
        //按照id降序排列
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        Sort sort = Sort.by(order);
        //查询
        PageRequest pageRequest = PageRequest.of(pageNum - 1,pageSize,sort);
        Page<Maintainer> page = maintainerDao.findAll(example,pageRequest);

        return page;
        /*Role role = new Role();
        role.setId(1);
        Example<Role> example = Example.of(role);

//按照id降序排列
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        Sort sort = Sort.by(order);
//查询
        PageRequest pageRequest = PageRequest.of(pageNum - 1,pageSize,sort);
        Page<Role> page = roleRepository.findAll(example,pageRequest);*/

    }


    public Result login(Maintainer maintainer) {
        Maintainer result = maintainerDao.findByUsernameAndPassword(maintainer.getUsername(), maintainer.getPassword());
        return new Result(StatusCode.OK,"OK",result);
    }

    @Transactional
    public Result edit(Maintainer maintainer, String id) {
        int line = maintainerDao.updateUsernameAndPhoneByid(maintainer.getUsername(),maintainer.getPhone(),id);
        if (line == 1){
            return new Result(StatusCode.OK,"修改成功", findById(id));
        }else {
            return new Result(StatusCode.ERROR,"修改失败,请确认id");
        }
    }

    @Transactional
    public Result editPwd(Maintainer maintainer, String id, String newPwd) {
        Maintainer byUsernameAndPassword = maintainerDao.findByUsernameAndPassword(maintainer.getUsername(), maintainer.getPassword());
        if (byUsernameAndPassword == null){ //旧密码验证不通过,防止绕过前端验证
            return new Result(StatusCode.ERROR,"密码验证不通过");
        }
        //修改密码
        int line = maintainerDao.updatePasswordByid(newPwd, id);
        if (line == 1){
            return new Result(StatusCode.OK,"修改成功", findById(id));
        }else {
            return new Result(StatusCode.ERROR,"修改失败,请确认id");
        }
    }

    public Result loginByWX(String code, String nickname) {
        String APPID = "wx25fe2e9ec9c3a827";  //微信小程序id
        String SECRET = "4f0fc381419b5174da7df27ccfae32f9";  //微信小程序密钥
        String openid = getWxUserOpenid(code, APPID, SECRET);  // openID oA0gL48YBbGm3549jWrGqHxxGHTk
        if (openid != null){ //在登录5分钟之内,登录code有效
            Maintainer maintainer = maintainerDao.findByOpenID(openid);
            if (maintainer == null){//没有查询到相应用户,即为第一册登录,进行注册
                maintainer = new Maintainer();
                maintainer.setId(idWorker.nextId()+"");
                maintainer.setUsername(nickname);
                maintainer.setPassword("123456");  //默认密码,微信登录后要求修改
                maintainer.setOpenID(openid);
                insert(maintainer);
                return new Result(StatusCode.OK,"微信登录成功",maintainer);
            }else{// 查询到相应用户,登录成功,将用户返回
                return new Result(StatusCode.OK,"微信登录成功",maintainer);
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
}
