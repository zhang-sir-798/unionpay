package com.dx.dao;

import com.dx.model.Channel;
import java.util.List;

public interface ChannelDao {
	
    int deleteByPrimaryKey(Long id);

    int insert(Channel record);

    Channel selectByChannelId(Long id);

    List<Channel> selectAll();

    int updateByPrimaryKey(Channel record);
}