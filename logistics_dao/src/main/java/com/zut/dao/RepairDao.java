package com.zut.dao;

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
public interface RepairDao extends JpaRepository<Repair,String>, JpaSpecificationExecutor<Repair> {

    @Query(value = "select * from repair WHERE username like %?1% ",nativeQuery = true)
    public Page<List<Map>> getRepairList(String username,Pageable pageable);

    public Repair findByUsername(String name);

    public Repair findByPhone(String phone);

    public Repair findByUsernameAndPassword(String username, String password);

    public Repair findByOpenID(String openid);

    @Query(value = "select * from repair where id=?1", nativeQuery = true)
    public Repair getById(String id);

    @Modifying
    @Query(value = "update Repair set username=?1, phone=?2 where id=?3")
    public int updateUsernameAndPhoneByid(String username, String phone, String id);

    @Modifying
    @Query(value = "update Repair set password=?1 where id=?2")
    public int updatePasswordByid(String password, String id);

//    @Modifying
//    @Query(value = "insert into repair va")
//    public Repair addRepair(Repair repair);
}

