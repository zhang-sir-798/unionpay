package com.dx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class FixedQrRepository implements Serializable {
    private Long id;

    /**
     * 二维码编号
     */
    private String qr_num;

    /**
     * 二维码状态  00:可用，01:已经绑定，02:已经销毁
     */
    private String qr_status;

    /**
     * 二维码url
     */
    private String qr_code;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 本批次对应银联批次号
     */
    private String yl_batch_no;

    /**
     * 批次号
     */
    private String batch_no;

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

    private String union_mer_id;

    private String mer_no;

    private String notify_url;

    private String open_type;

    private String bind_state;

    private Date bind_time;

    private String channel_id;
    private String mer_name;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMer_name() {
		return mer_name;
	}

	public void setMer_name(String mer_name) {
		this.mer_name = mer_name;
	}

	public String getQr_num() {
        return qr_num;
    }

    public void setQr_num(String qr_num) {
        this.qr_num = qr_num;
    }

    public String getQr_status() {
        return qr_status;
    }

    public void setQr_status(String qr_status) {
        this.qr_status = qr_status;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
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

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
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

    public String getUnion_mer_id() {
        return union_mer_id;
    }

    public void setUnion_mer_id(String union_mer_id) {
        this.union_mer_id = union_mer_id;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
    }

    public String getBind_state() {
        return bind_state;
    }

    public void setBind_state(String bind_state) {
        this.bind_state = bind_state;
    }

    public Date getBind_time() {
        return bind_time;
    }

    public void setBind_time(Date bind_time) {
        this.bind_time = bind_time;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
    
    
    
    
    
}