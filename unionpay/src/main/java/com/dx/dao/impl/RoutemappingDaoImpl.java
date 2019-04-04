package com.dx.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.dx.dao.RoutemappingDao;
import com.dx.model.Routemapping;


public class RoutemappingDaoImpl implements RoutemappingDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	

	@Override
	public int insert(Routemapping record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RoutemappingDao.class).insert(record);
	}

	@Override
	public Routemapping selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RoutemappingDao.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(Routemapping record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RoutemappingDao.class).updateByPrimaryKey(record);
	}

	@Override
	public Routemapping selectByMerNoXType(String merNo, String trxType) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(RoutemappingDao.class).selectByMerNoXType( merNo,  trxType);
	}

}
