package com.zut.dao;

import com.zut.po.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentDao extends JpaRepository<Comment,String>, JpaSpecificationExecutor<Comment> {

    /*@Query(value = "SELECT orderform.ID orderId,sid,level,details from orderform,comment WHERE orderform.CID=comment.ID",nativeQuery = true)*/
    @Query(value = "SELECT orderform.ID orderId,orderform.mid,maintainer.username maintainer_name,orderform.sid," +
            "sort.name maintain_type,comment.level,details from orderform,comment,maintainer,sort WHERE " +
            "orderform.CID=comment.ID and orderform.MID=maintainer.ID and orderform.SID=sort.ID",nativeQuery = true)
    public Page<List<Map>> getCommentList(Pageable pageable);

    @Query(value = "SELECT orderform.ID orderId,mid, cid,level,details from orderform,comment,maintainer WHERE " +
            "orderform.CID=comment.ID and orderform.MID=maintainer.ID and mid=?",nativeQuery = true)
    public Page<List<Map>> findByMid(String mid,Pageable pageable);

    @Query(value = "SELECT orderform.ID orderId,orderform.mid,maintainer.username maintainer_name,orderform.sid," +
            "sort.name maintain_type,comment.level,details from orderform,comment,maintainer,sort WHERE orderform.CID=comment.ID " +
            "and orderform.MID=maintainer.ID and orderform.SID=sort.ID and maintainer.username like %?% ",nativeQuery = true)
    public Page<List<Map>> findByMname(String username,Pageable pageable);

}
