package com.dx.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.dx.dao.AgentDao;
import com.dx.model.Agent;


public class AgentDaoImpl implements AgentDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	
	
	@Override
	public int insert(Agent record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(AgentDao.class).insert(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(AgentDao.class).deleteByPrimaryKey(id);
	}

	@Override
	public Agent selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(AgentDao.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(Agent record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(AgentDao.class).updateByPrimaryKey(record);
	}

	@Override
	public List<Agent> selectAll() {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(AgentDao.class).selectAll();
	}

	@Override
	public Agent selectByAgengNo(String org_id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(AgentDao.class).selectByAgengNo(org_id);
	}

}
