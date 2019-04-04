package com.dx.dao;

import com.dx.model.Merchant;
import java.util.List;

public interface MerchantDao {
	
    int deleteByPrimaryKey(Long id);

    int insert(Merchant record);

    Merchant selectByPrimaryKey(Long id);
    
    Merchant selectByMerNo(String merNo);

    List<Merchant> selectAll();

    int updateByPrimaryKey(Merchant record);
}