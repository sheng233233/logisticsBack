package com.zut.dao;

import com.zut.po.Administrator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdministratorDao extends JpaRepository<Administrator,String>, JpaSpecificationExecutor<Administrator> {
//JpaRepository 包含基本crud
//    JpaSpecificationExecutor包含复杂查询

    public List<Administrator> findAdministratorByUsernameAndPassword(String username, String password);

    @Query(value = "select * from administrator WHERE username like %?1% ",nativeQuery = true)
    public Page<List<Map>> getAdminList(String username, Pageable pageable);

}
