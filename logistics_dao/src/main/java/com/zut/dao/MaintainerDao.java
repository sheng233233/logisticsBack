package com.zut.dao;

import com.zut.po.Maintainer;
import com.zut.po.Repair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MaintainerDao extends JpaRepository<Maintainer,String>, JpaSpecificationExecutor<Maintainer> {

    //select  * from maintainer,sort where sort.id=sid
    //SELECT maintainer.ID,username,password,phone,sort.id sortID,name sortName,pid sortPid FROM maintainer INNER JOIN  sort ON maintainer.sid=sort.id
    @Query(value = "select maintainer.id mid,username,password,phone,sid,name maintainType,pid sortPid from maintainer,sort where sort.id=sid and username like %?1%",nativeQuery = true)
    public Page<List<Map>> getMaintainerList(String username,Pageable pageable);

    @Query(value = "select maintainer.id mid,username,password,phone,sid,name maintainType,pid sortPid from maintainer,sort where sort.id=sid and maintainer.id=?",nativeQuery = true)
    public List<Map> getById(String mid);

    public List<Maintainer> findMaintainerBySid(String sid);

    public Maintainer findByUsernameAndPassword(String username, String password);

    public Maintainer findByOpenID(String openid);

    @Modifying
    @Query(value = "update Maintainer set username=?1, phone=?2 where id=?3")
    public int updateUsernameAndPhoneByid(String username, String phone, String id);

    @Modifying
    @Query(value = "update Maintainer set password=?1 where id=?2")
    public int updatePasswordByid(String password, String id);



}

