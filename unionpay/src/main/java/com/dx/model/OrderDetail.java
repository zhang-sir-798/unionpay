package com.dx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class OrderDetail implements Serializable {
    private Long id;

    private String chan_mer_id;

    private String app_id;

    /**
     * 通道id
     */
    private String channel_id;

    /**
     * 上游扣费
     */
    private String cost;

    private String front_url;

    private String client_ip;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 回调地址
     */
    private String notify_url;

    /**
     * 商户编号
     */
    private String mer_no;

    /**
     * 交易金额
     */
    private String order_amount;

    /**
     * 平台订单号
     */
    private String serial_no;

    /**
     * 商户订单号
     */
    private String order_no;

    /**
     * 交易类型
     */
    private String trx_type;

    /**
     * 订单状态、初始化、一扫描、成功、失败、超时
     */
    private String state;

    /**
     * 通道编号（子商户号）
     */
    private String sub_mch_id;

    /**
     * 响应码
     */
    private String resp_code;

    /**
     * 响应信息
     */
    private String resp_msg;

    /**
     * 商户结算金额
     */
    private String mer_settle_amt;

    /**
     * 商户手续费
     */
    private String mer_fee;

    /**
     * 一级代理商结算金额
     */
    private String agent_lv1_settle_amt;

    /**
     * 二级代理商结算金额
     */
    private String agent_lv2_settle_amt;

    /**
     * 平台结算金额
     */
    private String plat_settle_amt;

    /**
     * 一级代理商分润
     */
    private String agent_lv1_profit;

    /**
     * 二级代理商分润
     */
    private String agent_lv2_profit;

    /**
     * 平台分润
     */
    private String platform_profit;

    /**
     * 渠道返回订单号
     */
    private String channel_trans_no;

    /**
     * 渠道私钥
     */
    private String channel_pri_key;

    /**
     * 渠道公钥
     */
    private String channel_pub_key;

    /**
     * 卡号
     */
    private String card_no;

    /**
     * 身份证号
     */
    private String id_card_no;

    /**
     * 订单创建时间格式为yyyyMMddHHmmss
     */
    private String start_time;

    /**
     * 订单失效时间格式为yyyyMMddHHmmss
     */
    private String expire_time;

    /**
     * 商户秘钥
     */
    private String req_key;

    /**
     * 卡类型
     */
    private String card_type;

    /**
     * 回调时间
     */
    private Date notify_time;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChan_mer_id() {
        return chan_mer_id;
    }

    public void setChan_mer_id(String chan_mer_id) {
        this.chan_mer_id = chan_mer_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFront_url() {
        return front_url;
    }

    public void setFront_url(String front_url) {
        this.front_url = front_url;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
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

    public String getTrx_type() {
        return trx_type;
    }

    public void setTrx_type(String trx_type) {
        this.trx_type = trx_type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getResp_code() {
        return resp_code;
    }

    public void setResp_code(String resp_code) {
        this.resp_code = resp_code;
    }

    public String getResp_msg() {
        return resp_msg;
    }

    public void setResp_msg(String resp_msg) {
        this.resp_msg = resp_msg;
    }

    public String getMer_settle_amt() {
        return mer_settle_amt;
    }

    public void setMer_settle_amt(String mer_settle_amt) {
        this.mer_settle_amt = mer_settle_amt;
    }

    public String getMer_fee() {
        return mer_fee;
    }

    public void setMer_fee(String mer_fee) {
        this.mer_fee = mer_fee;
    }

    public String getAgent_lv1_settle_amt() {
        return agent_lv1_settle_amt;
    }

    public void setAgent_lv1_settle_amt(String agent_lv1_settle_amt) {
        this.agent_lv1_settle_amt = agent_lv1_settle_amt;
    }

    public String getAgent_lv2_settle_amt() {
        return agent_lv2_settle_amt;
    }

    public void setAgent_lv2_settle_amt(String agent_lv2_settle_amt) {
        this.agent_lv2_settle_amt = agent_lv2_settle_amt;
    }

    public String getPlat_settle_amt() {
        return plat_settle_amt;
    }

    public void setPlat_settle_amt(String plat_settle_amt) {
        this.plat_settle_amt = plat_settle_amt;
    }

    public String getAgent_lv1_profit() {
        return agent_lv1_profit;
    }

    public void setAgent_lv1_profit(String agent_lv1_profit) {
        this.agent_lv1_profit = agent_lv1_profit;
    }

    public String getAgent_lv2_profit() {
        return agent_lv2_profit;
    }

    public void setAgent_lv2_profit(String agent_lv2_profit) {
        this.agent_lv2_profit = agent_lv2_profit;
    }

    public String getPlatform_profit() {
        return platform_profit;
    }

    public void setPlatform_profit(String platform_profit) {
        this.platform_profit = platform_profit;
    }

    public String getChannel_trans_no() {
        return channel_trans_no;
    }

    public void setChannel_trans_no(String channel_trans_no) {
        this.channel_trans_no = channel_trans_no;
    }

    public String getChannel_pri_key() {
        return channel_pri_key;
    }

    public void setChannel_pri_key(String channel_pri_key) {
        this.channel_pri_key = channel_pri_key;
    }

    public String getChannel_pub_key() {
        return channel_pub_key;
    }

    public void setChannel_pub_key(String channel_pub_key) {
        this.channel_pub_key = channel_pub_key;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String id_card_no) {
        this.id_card_no = id_card_no;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getReq_key() {
        return req_key;
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public Date getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(Date notify_time) {
        this.notify_time = notify_time;
    }

}