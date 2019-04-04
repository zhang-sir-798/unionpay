package com.dx.dao;

import com.dx.model.OrderDetail;

public interface OrderDetailDao {
	
	OrderDetail selectByOrderNo(String orderNo);
	
	OrderDetail selectByOrderAndTime(String orderNo , String time);
	OrderDetail selectHistoryByOrderAndTime(String serialNo , String time);
	
	OrderDetail selectBySerialNo(String serialNo);
	
	
	int insert(OrderDetail record);

	OrderDetail selectByPrimaryKey(Long id);

	int updateByPrimaryKey(OrderDetail record);
	
}
