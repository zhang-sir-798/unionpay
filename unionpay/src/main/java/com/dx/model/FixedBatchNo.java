package com.dx.model;


import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class FixedBatchNo implements Serializable {
    private Long id;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 本批次对应银联批次号
     */
    private String yl_batch_no;

    /**
     * 本批次请求码数量
     */
    private String gen_count;

    private String agent_key;

    /**
     * 所属机构编号
     */
    private String agent_no;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getYl_batch_no() {
        return yl_batch_no;
    }

    public void setYl_batch_no(String yl_batch_no) {
        this.yl_batch_no = yl_batch_no;
    }

    public String getGen_count() {
        return gen_count;
    }

    public void setGen_count(String gen_count) {
        this.gen_count = gen_count;
    }

    public String getAgent_key() {
        return agent_key;
    }

    public void setAgent_key(String agent_key) {
        this.agent_key = agent_key;
    }

    public String getAgent_no() {
        return agent_no;
    }

    public void setAgent_no(String agent_no) {
        this.agent_no = agent_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
 
}