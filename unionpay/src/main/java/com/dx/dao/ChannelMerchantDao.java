package com.dx.dao;

import com.dx.model.ChannelMerchant;

import java.util.List;


public interface ChannelMerchantDao {
	
	int deleteByPrimaryKey(Long id);

    int insert(ChannelMerchant record);

    ChannelMerchant selectByPrimaryKey(Long id);

    List<ChannelMerchant> selectAll();

    int updateByPrimaryKey(ChannelMerchant record);
}