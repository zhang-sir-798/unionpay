package com.dx.dao;

import com.dx.model.MerchantNotifyDetail;
import java.util.List;

public interface MerchantNotifyDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(MerchantNotifyDetail record);

    MerchantNotifyDetail selectByPrimaryKey(Long id);

    List<MerchantNotifyDetail> selectAll();

    int updateByPrimaryKey(MerchantNotifyDetail record);
   
    int updateBySerialNo(MerchantNotifyDetail record);
}