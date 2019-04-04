package com.dx.model;


import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Routemapping implements Serializable {
    private Long id;

    /**
     * 渠道产品表ID
     */
    private Long chnl_id;

    /**
     * 渠道商户ID
     */
    private String sub_mer_id;

    /**
     * 下游商户ID
     */
    private String mer_no;

    /**
     * 交易类型标识
     */
    private String tran_type;

    /**
     * 路由状态
     */
    private String status;

    private String creator;

    private Date create_time;

    private String update_name;

    private Date update_time;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChnl_id() {
        return chnl_id;
    }

    public void setChnl_id(Long chnl_id) {
        this.chnl_id = chnl_id;
    }

    public String getSub_mer_id() {
        return sub_mer_id;
    }

    public void setSub_mer_id(String sub_mer_id) {
        this.sub_mer_id = sub_mer_id;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getTran_type() {
        return tran_type;
    }

    public void setTran_type(String tran_type) {
        this.tran_type = tran_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_name() {
        return update_name;
    }

    public void setUpdate_name(String update_name) {
        this.update_name = update_name;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", chnl_id=").append(chnl_id);
        sb.append(", sub_mer_id=").append(sub_mer_id);
        sb.append(", mer_no=").append(mer_no);
        sb.append(", tran_type=").append(tran_type);
        sb.append(", status=").append(status);
        sb.append(", creator=").append(creator);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_name=").append(update_name);
        sb.append(", update_time=").append(update_time);
        sb.append("]");
        return sb.toString();
    }

}