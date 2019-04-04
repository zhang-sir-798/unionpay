package com.dx.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.dx.dao.FixedBatchNoDao;
import com.dx.model.FixedBatchNo;


public class FixedBatchNoDaoImpl implements FixedBatchNoDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	
	
	@Override
	public int insert( FixedBatchNo record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedBatchNoDao.class).insert(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedBatchNoDao.class).deleteByPrimaryKey(id);
	}

	@Override
	public  FixedBatchNo selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedBatchNoDao.class).selectByPrimaryKey(id);
	}
	@Override
	public  FixedBatchNo selectByBacthNo(String no) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedBatchNoDao.class).selectByBacthNo(no);
	}

	@Override
	public int updateByPrimaryKey(FixedBatchNo record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedBatchNoDao.class).updateByPrimaryKey(record);
	}

	@Override
	public List< FixedBatchNo> selectAll() {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedBatchNoDao.class).selectAll();
	}


}
