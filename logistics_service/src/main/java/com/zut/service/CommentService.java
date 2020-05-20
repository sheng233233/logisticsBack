package com.zut.service;

import com.zut.dao.CommentDao;
import com.zut.dao.OrderformDao;
import com.zut.po.Comment;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderformDao orderformDao;

    //评价列表
    public Result getCommentList(int page, int rows){
        Pageable pageble= PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = commentDao.getCommentList(pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //根据维修人员id查询所有评价
    public Result findByMid(String mid,int page, int rows){
        Pageable pageble= PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = commentDao.findByMid(mid,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }
    //根据维修人员账号查询评价
    public Result findByMname(String username,int page, int rows){
        Pageable pageble= PageRequest.of(page-1,rows);
        Page<List<Map>> pageData = commentDao.findByMname(username,pageble);
        return new Result(StatusCode.OK,"查询成功",new PageResult<List<Map>>(pageData.getTotalElements(),pageData.getContent()));
    }

    //查询所有
    public Result findAll(){
        List<Comment> commentList = commentDao.findAll();
        return new Result(StatusCode.OK,"查询成功", commentList);
    }

    //查询
    public Result findById(String id){
        Comment comment = commentDao.findById(id).get();
        return new Result(StatusCode.OK,"查询成功", comment);
    }

    //新增
    public Result insert(Comment comment){
        comment.setId(idWorker.nextId()+"");
        commentDao.save(comment);
        return new Result(StatusCode.OK,"新增成功",null);
    }

    //修改
    public Result update(Comment comment){
        commentDao.save(comment);
        return new Result(StatusCode.OK,"修改成功", commentDao.findById(comment.getId()));
    }

    //删除
    public Result deleteById(String id){
        commentDao.deleteById(id);
        return new Result(StatusCode.OK,"删除成功",null);
    }

    /**
     * 报修人员评论工单
     * @param comment 评论详情与星级
     * @param oid 所属工单
     * @return
     */
    @Transactional
    public Result create(Comment comment, String oid) {
        //为评论设置id
        comment.setId(idWorker.nextId()+"");
        //向数据库插入comment
        insert(comment);
        //补充工单的cid字段
        Integer line = orderformDao.updateOrderformComment(comment.getId(), oid);
        //修改订单状态
        orderformDao.updateStatusByid("已评价",oid);
        if (line != 1){
            return new Result(StatusCode.ERROR,"请给定正确的工单id");
        }
        return new Result(StatusCode.OK,"评价成功!");
    }
}
