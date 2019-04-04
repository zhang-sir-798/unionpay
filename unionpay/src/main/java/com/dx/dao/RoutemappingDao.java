package com.dx.dao;

import org.apache.ibatis.annotations.Param;

import com.dx.model.Routemapping;

public interface RoutemappingDao {
    
    Routemapping selectByMerNoXType(@Param("merNo")String merNo,@Param("trxType")String trxType);
    
    int insert(Routemapping record);

    Routemapping selectByPrimaryKey(Long id);

  
    int updateByPrimaryKey(Routemapping record);

}