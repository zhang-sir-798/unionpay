package com.dx.dao.impl;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.dx.dao.FixedQrRepositoryDao;
import com.dx.model.FixedQrRepository;


public class FixedQrRepositoryDaoImpl implements FixedQrRepositoryDao{

	@Autowired
	SqlSessionTemplate sqlSessionFactoryBeanName;
	
	
	@Override
	public int insert(FixedQrRepository record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).insert(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).deleteByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(FixedQrRepository record) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).updateByPrimaryKey(record);
	}

	@Override
	public FixedQrRepository selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).selectByPrimaryKey(id);
	}
	@Override
	public FixedQrRepository selectByQrCode(String qrCode) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).selectByQrCode(qrCode);
	}

	@Override
	public List<FixedQrRepository> selectAll() {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).selectAll();
	}

	@Override
	public int insertListQrRepository(List<FixedQrRepository> qrRepository) {
		// TODO Auto-generated method stub
		return sqlSessionFactoryBeanName.getMapper(FixedQrRepositoryDao.class).insertListQrRepository(qrRepository);
	}

	

}
