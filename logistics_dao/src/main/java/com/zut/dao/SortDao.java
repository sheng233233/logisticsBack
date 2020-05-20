package com.zut.dao;

import com.zut.po.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SortDao extends JpaRepository<Sort,String>, JpaSpecificationExecutor<Sort> {

    public List<Sort> findSortByPid(String pid);

    /**
     * 根据层级查找
     *
     * @param pid 层级
     * @return java.util.List<com.dcssn.weian.cms.entity.Category>
     * @author lihy
     */
    List<Sort> findByPid(String pid);

    @Query(value = "select * from sort ",nativeQuery = true)
    public Page<List<Map>> getSortList(Pageable pageable);

    @Query(value = "select * from sort where id=?",nativeQuery = true)
    public List<Map> getSortByPid(String pid);

    @Query(value = "select * from sort where level=?",nativeQuery = true)
    List<Sort> findByLevel(int level);
}

