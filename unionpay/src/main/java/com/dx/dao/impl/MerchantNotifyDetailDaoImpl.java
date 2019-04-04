package com.dx.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.dx.dao.MerchantNotifyDetailDao;
import com.dx.model.MerchantNotifyDetail;


public class MerchantNotifyDetailDaoImpl implements MerchantNotifyDetailDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	
	
	@Override
	public int insert(MerchantNotifyDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(MerchantNotifyDetailDao.class).insert(record);
	}


	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(MerchantNotifyDetailDao.class).deleteByPrimaryKey(id);
	}


	@Override
	public MerchantNotifyDetail selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(MerchantNotifyDetailDao.class).selectByPrimaryKey(id);
	}


	@Override
	public List<MerchantNotifyDetail> selectAll() {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(MerchantNotifyDetailDao.class).selectAll();
	}


	@Override
	public int updateByPrimaryKey(MerchantNotifyDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(MerchantNotifyDetailDao.class).updateByPrimaryKey(record);
	}


	@Override
	public int updateBySerialNo(MerchantNotifyDetail record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(MerchantNotifyDetailDao.class).updateBySerialNo(record);
	}

	
}
