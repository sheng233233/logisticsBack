package com.zut.service;

import com.zut.dao.SortDao;
import com.zut.po.Sort;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.management.relation.Role;
import javax.sound.midi.Soundbank;
import java.sql.Array;
import java.util.*;

@Service
@Transactional
public class SortService {

    @Autowired
    private SortDao sortDao;

    @Autowired
    private IdWorker idWorker;

    public Result findByPid(String pid){
        List<Sort> sortList = sortDao.findByPid(pid);
        return new Result(StatusCode.OK,"查询成功",sortList);
    }

    //根据pid查询子分类
    public Result findSortByPid(String pid){
        List<Sort> sortList = sortDao.findSortByPid(pid);
        return new Result(StatusCode.OK,"查询成功",sortList);
    }

    //查看分类列表
    public Result pageQuery(Sort sort, int pageNum, int pageSize) {
        Example<Sort> example = Example.of(sort);
        //查询
        PageRequest pageRequest = PageRequest.of(pageNum - 1,pageSize);
        Page<Sort> pageDate = sortDao.findAll(example,pageRequest);
        return new Result(StatusCode.OK,"查询成功",new PageResult<Sort>(pageDate.getTotalElements(),pageDate.getContent()));
    }

    //分类列表
    public Result getSortList(int page, int rows){
        Pageable pageble=PageRequest.of(page-1,rows);
        Page<List<Map>> pageData= sortDao.getSortList(pageble);

        List<Map> content = (List)pageData.getContent();
        List<Map> sorts = new ArrayList<>();
        for (Map sort:content){
            Map<String, Object> newMap = new HashMap<>(sort);
            newMap.put("deleted",false);//界面展示有效
           /* sorts.add(newMap);*/

            /*String pid = (String)newMap.get("pid");
            System.out.println(pid);
            if(pid==null){
                newMap.put("children",null);
            }else {
                List<Map> sortByPid = sortDao.getSortByPid(pid);
                Map map = sortByPid.get(0);
                Map<String, Object> newMap2 = new HashMap<>(map);
                newMap2.put("deleted",false);
                List<Map> newList = new ArrayList<>();
                newList.add(newMap2);
                newMap.put("children",newList);
            }*/

            /*if(pid!=null){
                List<Map> sortByPid = sortDao.getSortByPid(pid);
                Map<String, Object> newMap2 = new HashMap<>(sortByPid.get(0));
                newMap2.put("children",sort);
                newMap2.put("deleted",false);
                sorts.add(newMap2);
            }*/
            sorts.add(newMap);
            }
        /*List<Map> content = (List)pageData.getContent();
        System.out.println(content.toString());
        List<Map> sorts = new ArrayList<>();
        for (Map sort:content){
            String pid = (String)sort.get("id");
            System.out.println(pid);
            if(pid==null){
                sort.put("children",null);
                sorts.add(sort);
            }else {
                List<Map> sortByPid = sortDao.getSortByPid(pid);
                Map map = sortByPid.get(0);
                map.put("children",sort);
                sorts.add(map);
            }
        }*/
        //return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
        return new Result(StatusCode.OK,"查询成功",new PageResult<Map>(pageData.getTotalElements(),sorts));
    }

    public Result findByLevel(int level){
        List<Sort> sortList = sortDao.findByLevel(level);
        return new Result(StatusCode.OK,"查询成功",sortList);
    }

    //查询所有
    public Result findAll(){
        List<Sort> sortList = sortDao.findAll();
        return new Result(StatusCode.OK,"查询成功",sortList);
    }

    //查询
    public Result findById(String id){
        Sort sort = sortDao.findById(id).get();
        return new Result(StatusCode.OK,"查询成功",sort);
    }

    //新增
    public Result insert(Sort sort){
        sort.setId(idWorker.nextId()+"");
        sortDao.save(sort);
        return new Result(StatusCode.OK,"新增成功",null);
    }

    //修改
    public  Result update(Sort sort){

        sortDao.save(sort);
        return new Result(StatusCode.OK,"修改成功",sortDao.findById(sort.getId()));
    }

    //删除
    public  Result deleteById(String id){

        sortDao.deleteById(id);
        return new Result(StatusCode.OK,"删除成功",null);
    }


    /**
     * 为小程序的滚动选择栏提供分类数据
     */
    public Result getSorts() {
        List<Object> sorts = new LinkedList<>();
        List<Sort> parent0 = sortDao.findByPid("0");
        sorts.add(parent0);
        for (Sort sort:parent0) {
            if (sort != null){
                sorts.add(sortDao.findByPid(sort.getId()));
            }
        }
        return new Result(StatusCode.OK, "查询成功", sorts);
    }
}
