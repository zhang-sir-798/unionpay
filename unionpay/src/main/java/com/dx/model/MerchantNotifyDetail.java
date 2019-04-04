package com.dx.model;


import java.io.Serializable;

/**
 * @author 
 */
public class MerchantNotifyDetail implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 上游单号
     */
    private String serial_no;

    /**
     * 下游单号
     */
    private String order_no;

    /**
     * 商户编号
     */
    private String mer_no;

    /**
     * 交易类型
     */
    private String trx_type;

    /**
     * 回调地址
     */
    private String notify_url;

    private String msg;

    /**
     * 回调次数
     */
    private Integer count;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getTrx_type() {
        return trx_type;
    }

    public void setTrx_type(String trx_type) {
        this.trx_type = trx_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}