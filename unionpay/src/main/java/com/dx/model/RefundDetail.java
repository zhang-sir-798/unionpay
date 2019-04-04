package com.dx.model;


import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class RefundDetail implements Serializable {
    private Long id;

    /**
     * 退款单号
     */
    private String refund_no;
    /**
     * 渠道号
     */
    private String channel_id;
    /**
     * 通道子商户号（通道商编）
     */
    private String sub_mer_id;

    /**
     * 回调地址
     */
    private String notify_url;

    /**
     * 商户编号
     */
    private String mer_no;

    /**
     * 退款原因
     */
    private String refund_reason;

    /**
     * 订单总额
     */
    private String total_order_amount;

    /**
     * 申请退款金额（单位：分）
     */
    private String refund_amount;

    /**
     * 退款全量报文
     */
    private String refund_detail_item_list;

    /**
     * 商户订单号
     */
    private String order_no;

    /**
     * 银联流水号
     */
    private String trade_no;
    /**
     * 交易类型
     */
    private String trx_type;

    /**
     * 平台订单号
     */
    private String serial_no;

    /**
     * 订单状态、初始化、一扫描、成功、失败、超时
     */
    private String refund_state;

    /**
     * 商户秘钥
     */
    private String req_key;

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

    public String getRefund_no() {
        return refund_no;
    }

    public void setRefund_no(String refund_no) {
        this.refund_no = refund_no;
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

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getTotal_order_amount() {
        return total_order_amount;
    }

    public void setTotal_order_amount(String total_order_amount) {
        this.total_order_amount = total_order_amount;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_detail_item_list() {
        return refund_detail_item_list;
    }

    public void setRefund_detail_item_list(String refund_detail_item_list) {
        this.refund_detail_item_list = refund_detail_item_list;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

 

    public String getRefund_state() {
		return refund_state;
	}

	public void setRefund_state(String refund_state) {
		this.refund_state = refund_state;
	}

	public String getReq_key() {
        return req_key;
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public Date getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(Date notify_time) {
        this.notify_time = notify_time;
    }

	public String getTrx_type() {
		return trx_type;
	}

	public void setTrx_type(String trx_type) {
		this.trx_type = trx_type;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getSub_mer_id() {
		return sub_mer_id;
	}

	public void setSub_mer_id(String sub_mer_id) {
		this.sub_mer_id = sub_mer_id;
	}
    
    


}