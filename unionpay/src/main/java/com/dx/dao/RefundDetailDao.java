package com.dx.dao;

import com.dx.model.RefundDetail;
import java.util.List;

public interface RefundDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(RefundDetail record);

    RefundDetail selectByPrimaryKey(Long id);
    RefundDetail selectByRefundNo(String refundNo);

    List<RefundDetail> selectAll();
    
    RefundDetail selectByParams(String orderNo , String refundNo);

    int updateByPrimaryKey(RefundDetail record);
}