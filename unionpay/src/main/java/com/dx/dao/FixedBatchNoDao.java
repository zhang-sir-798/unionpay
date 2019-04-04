package com.dx.dao;

import com.dx.model.FixedBatchNo;
import java.util.List;

public interface FixedBatchNoDao {
    int deleteByPrimaryKey(Long id);

    int insert(FixedBatchNo record);

    FixedBatchNo selectByPrimaryKey(Long id);
    FixedBatchNo selectByBacthNo(String no);

    List<FixedBatchNo> selectAll();

    int updateByPrimaryKey(FixedBatchNo record);
}