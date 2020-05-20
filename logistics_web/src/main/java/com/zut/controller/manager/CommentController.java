package com.zut.controller.manager;

import com.zut.po.Comment;
import com.zut.service.CommentService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * @Description 获得所有评论
     * @param pagenum 请求的页码
     * @param pagesize 每页显示的记录数
     * @return Result
     */
    /*@RequestMapping(value = "/showall/{page}/{size}",method = RequestMethod.GET)
    public Result getList(@PathVariable int page,@PathVariable int size){
        return commentService.getCommentList(page,size);
    }*/
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public Result getList(int pagenum, int pagesize){
        return commentService.getCommentList(pagenum,pagesize);
    }

    /**
     * @Description 维修员考核：根据维修人员id查询所有评价
     * @param mid 所属维修员ID
     * @param page 请求的页码
     * @param size 每页显示的记录数
     * @return Result
     */
    @RequestMapping(value = "/findByMid/{mid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByMid(@PathVariable String mid,@PathVariable int page,@PathVariable int size){
        return commentService.findByMid(mid,page,size);
    }
    //根据维修人员账号查询评价
    @RequestMapping(value = "/findByMname",method = RequestMethod.GET)
    public Result findByMname(String username,int pagenum,int pagesize){
        return commentService.findByMname(username,pagenum,pagesize);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result finAll(){
        return commentService.findAll();
    }

    @RequestMapping(value = "/get/{cid}",method = RequestMethod.GET)
    public Result findById(@PathVariable("cid") String cid){
        return commentService.findById(cid);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result insert( @RequestBody Comment comment){
        return commentService.insert(comment);
    }

    @RequestMapping(value = "/update/{cid}",method = RequestMethod.PUT)
    public Result update(@RequestBody Comment comment, @PathVariable("cid") String cid){
        comment.setId(cid);
        return commentService.update(comment);
    }

    @RequestMapping(value = "/delete/{cid}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("cid") String cid){
        return commentService.deleteById(cid);
    }


    /**
     * 报修人员评论工单
     * @param comment 评论详情与星级
     * @param oid 所属工单
     * @return
     */
    @RequestMapping(value = "/create/{oid}", method = RequestMethod.POST)
    public Result create(@RequestBody Comment comment, @PathVariable String oid){
        return commentService.create(comment, oid);
    }

}
