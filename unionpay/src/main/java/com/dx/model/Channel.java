package com.dx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Channel implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 渠道费率
     */
    private String fee;

    /**
     * 渠道号
     */
    private String channel_id;

    /**
     * app_id
     */
    private String app_id;

    /**
     * 商户号
     */
    private String mer_id;

    /**
     * 公钥串
     */
    private String public_key;

    /**
     * 私钥串
     */
    private String private_key;

    /**
     * 创建人
     */
    private String create_name;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 修改时间
     */
    private Date update_time;

    /**
     * 修改人
     */
    private String update_name;

    private String type;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMer_id() {
        return mer_id;
    }

    public void setMer_id(String mer_id) {
        this.mer_id = mer_id;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_name() {
        return update_name;
    }

    public void setUpdate_name(String update_name) {
        this.update_name = update_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}