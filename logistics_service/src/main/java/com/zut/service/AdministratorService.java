package com.zut.service;

import com.zut.dao.AdministratorDao;
import com.zut.po.Administrator;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@Service
@Transactional
public class AdministratorService {

    @Autowired
    private AdministratorDao adminDao;

    @Autowired
    private IdWorker idWorker;

    //查询所有
    public Result findAll(){
        List<Administrator> list = adminDao.findAll();
        return new Result(StatusCode.OK,"查询成功",list);
    }

    //查询
    public Result findById(String id){
        Administrator admin = adminDao.findById(id).get();
        return new Result(StatusCode.OK,"查询成功",admin);
    }

    //新增
    public  Result insert(Administrator administrator){
        administrator.setId(idWorker.nextId()+"");
        adminDao.save(administrator);
        return new Result(StatusCode.OK,"新增成功",null);
    }

    //修改
    public  Result update(Administrator administrator){

        adminDao.save(administrator); //如果发现id是已存在数据，自动默认使用修改
        return new Result(StatusCode.OK,"修改成功",adminDao.findById(administrator.getId()).get());
    }

    //删除
    public  Result deleteById(String id){

        adminDao.deleteById(id);
        return new Result(StatusCode.OK,"删除成功",null);
    }

    //管理员登录
    public Result login(String username,String password){
        List<Administrator> list = adminDao.findAdministratorByUsernameAndPassword(username, password);
        if (list.size()==0){
            return  new Result(StatusCode.LOGINERROR,"用户名或密码错误",list);
        }
        return  new Result(StatusCode.OK,"查询成功",list);
    }

    //管理员列表
    public Result getAdminList(String query,int page, int rows){
        Pageable pageble= PageRequest.of(page-1,rows);
        Page<List<Map>> pageData= adminDao.getAdminList(query,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //带条件的查询（label对象，可能根据label属性中的n个值作为条件）
    //先以一个条件为例 labelname
    public Result searchLabel(Administrator admin){
        List<Administrator> list = adminDao.findAll(new Specification<Administrator>() {
            @Override
            public Predicate toPredicate(Root<Administrator> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //Predicate labelname1=null;
                List<Predicate> list = new ArrayList<>();
                if (admin.getUsername()!=null&&admin.getPassword()!=null){
                    //作为条件模糊查询
                    Predicate username = criteriaBuilder.like(root.get("username"),admin.getUsername());
                    // where labelname like "%某某某%"
                    //return labelname;
                    list.add(username);
                    Predicate password = criteriaBuilder.equal(root.get("password"),admin.getPassword());
                    list.add(password);
                }

                /*//Predicate labelname2=null;
                if (!StringUtils.isNullOrEmpty(admin.getPassword())) {
                    //作为条件模糊查询
                    Predicate password = criteriaBuilder.equal(root.get("password"),admin.getPassword());
                    // where state = "0"
                    //return labelname;
                    list.add(password);
                }*/

                //将两个条件联合
                Predicate[] pd = new Predicate[list.size()];
                list.toArray(pd);
                //return criteriaBuilder.and(labelname1,labelname2);
                return criteriaBuilder.and(pd);//and方法中没法直接填入集合对象，所以转成数组再放进参数中
            }
        });

        return  new Result(StatusCode.OK,"查询成功",list);
    }

}
