package com.dx.dao.impl;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.dx.dao.ChannelDao;
import com.dx.model.Channel;


public class ChannelDaoImpl implements ChannelDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	
	
	@Override
	public int insert(Channel record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(ChannelDao.class).insert(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(ChannelDao.class).deleteByPrimaryKey(id);
	}

	@Override
	public Channel selectByChannelId(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(ChannelDao.class).selectByChannelId(id);
	}

	@Override
	public List<Channel> selectAll() {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(ChannelDao.class).selectAll();
	}

	@Override
	public int updateByPrimaryKey(Channel record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(ChannelDao.class).updateByPrimaryKey(record);
	}

}
