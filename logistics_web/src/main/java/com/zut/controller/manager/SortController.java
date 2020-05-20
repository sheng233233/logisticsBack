package com.zut.controller.manager;

import com.zut.po.Sort;
import com.zut.service.SortService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sort")
public class SortController {

    @Autowired
    private SortService sortService;

    @RequestMapping(method = RequestMethod.GET)
    public Result finAll(){

        return sortService.findAll();
    }

    @RequestMapping(value = "/getSortTree",method = RequestMethod.GET)
    @ResponseBody
    public Result getItemCatList(@RequestParam(value="id", defaultValue="0")String parentId) {
        return sortService.findByPid(parentId);
    }

    /**
     * @Description 根据id查询子分类
     * @param sortId 分类ID
     * @return Result
     */
    @RequestMapping(value = "/getSub/{sortId}",method = RequestMethod.GET)
    public Result findSortByPid(@PathVariable("sortId") String sortId){
        return sortService.findSortByPid(sortId);
    }

    /**
     * @Description 分页查看分类列表
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
    /*@RequestMapping(value = "/showall/{page}/{size}",method = RequestMethod.GET)
    public Result pageQuery(@PathVariable int page, @PathVariable int size){
        return sortService.pageQuery(new Sort(),page,size);
    }*/
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public Result getList(int pagenum,int pagesize){
        //return sortService.pageQuery(new Sort(),pagenum,pagesize);
        return sortService.getSortList(pagenum,pagesize);
    }

    @RequestMapping(value = "/showParentSort",method = RequestMethod.GET)
    public Result showParentSort(int level){
        return sortService.findByLevel(level);
    }

    /**
     * @Description 根据id查询分类
     * @param sortId 分类id
     * @return Result
     */
    @RequestMapping(value = "/get/{sortId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("sortId") String sortId){
        return sortService.findById(sortId);
    }

    /**
     * @Description 新增分类
     * @param sort
     * @return Result
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result insert( @RequestBody Sort sort){
        return sortService.insert(sort);
    }

    /**
     * @Description 修改分类
     * @param sort
     * @param sortId
     * @return Result
     */
    @RequestMapping(value = "/update/{sortId}",method = RequestMethod.PUT)
    public Result update( @RequestBody Sort sort,@PathVariable("sortId") String sortId){
        sort.setId(sortId);
        return sortService.update(sort);
    }

    /**
     * @Description 删除分类
     * @param sortId
     * @return
     */
    @RequestMapping(value = "/delete/{sortId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("sortId") String sortId){
        return sortService.deleteById(sortId);
    }


    /**
     * 为小程序的滚动选择栏提供分类数据
     */
    @RequestMapping(value = "/getAllsort", method = RequestMethod.GET)
    public Result getSorts(){
        return sortService.getSorts();
    }

}
