package com.dx.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.dx.dao.OrderDetailDao;
import com.dx.model.OrderDetail;


public class OrderDetailDaoImpl implements OrderDetailDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	

	@Override
	public OrderDetail selectByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).selectByOrderNo(orderNo);
	}

	@Override
	public OrderDetail selectBySerialNo(String serialNo) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).selectBySerialNo(serialNo);
	}

	@Override
	public int insert(OrderDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).insert(record);
	}

	@Override
	public OrderDetail selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).updateByPrimaryKey(record);
	}
	
	@Override
	public OrderDetail selectByOrderAndTime(String orderNo ,String time) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).selectByOrderAndTime(orderNo , time);
	}
	
	@Override
	public OrderDetail selectHistoryByOrderAndTime(String orderNo , String time) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(OrderDetailDao.class).selectHistoryByOrderAndTime(orderNo , time);
	}

	

}
