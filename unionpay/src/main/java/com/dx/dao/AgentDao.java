package com.dx.dao;

import com.dx.model.Agent;
import java.util.List;

public interface AgentDao {
    int deleteByPrimaryKey(Long id);

    int insert(Agent record);

    Agent selectByPrimaryKey(Long id);
    Agent selectByAgengNo(String  org_id);

    List<Agent> selectAll();

    int updateByPrimaryKey(Agent record);
}