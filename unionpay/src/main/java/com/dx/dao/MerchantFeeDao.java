package com.dx.dao;

import com.dx.model.MerchantFee;
import java.util.List;

public interface MerchantFeeDao {
	
    int deleteByPrimaryKey(Long id);

    int insert(MerchantFee record);

    MerchantFee selectByPrimaryKey(Long id);
    
    MerchantFee selectByMerNo(String merNo);

    List<MerchantFee> selectAll();

    int updateByPrimaryKey(MerchantFee record);
}