package com.dx.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.dx.dao.RefundDetailDao;
import com.dx.model.RefundDetail;


public class RefundDetailDaoImpl implements RefundDetailDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	
	
	@Override
	public int insert(RefundDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).insert(record);
	}

	@Override
	public RefundDetail selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).selectByPrimaryKey(id);
	}

	@Override
	public RefundDetail selectByParams(String orderNo , String refundNo) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).selectByParams(orderNo,refundNo);
	}

	@Override
	public int updateByPrimaryKey(RefundDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).updateByPrimaryKey(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).deleteByPrimaryKey(id);
	}

	@Override
	public List<RefundDetail> selectAll() {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).selectAll();
	}

	@Override
	public RefundDetail selectByRefundNo(String refundNo) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RefundDetailDao.class).selectByRefundNo(refundNo);
	}

}
