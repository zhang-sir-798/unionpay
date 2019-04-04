package com.dx.dao;

import com.dx.model.FixedQrRepository;
import java.util.List;

public interface FixedQrRepositoryDao {
	
    int deleteByPrimaryKey(Long id);

    int insert(FixedQrRepository record);

    FixedQrRepository selectByPrimaryKey(Long id);
    
    FixedQrRepository selectByQrCode(String qrCode);

    List<FixedQrRepository> selectAll();

    int updateByPrimaryKey(FixedQrRepository record);
    
    int insertListQrRepository(List<FixedQrRepository> qrRepository);
}