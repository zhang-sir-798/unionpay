package com.dx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class MerchantFee implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商户编号
     */
    private String mer_no;

    /**
     * 产品code
     */
    private String prod_node;

    private String prod_name;

    /**
     * 产品id
     */
    private Long prod_id;

    /**
     * 费率类型
     */
    private String fee_type;

    /**
     * 比率值
     */
    private String ratio;

    /**
     * 固定值
     */
    private String fixed;

    /**
     * 最低值
     */
    private String down_limit;

    /**
     * 最高值
     */
    private String up_limit;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 修改时间
     */
    private Date update_time;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 代付类型(0不代付1代付2充值)
     */
    private String payment_type;

    /**
     * 信用卡类型
     */
    private String c_fee_type;

    /**
     * 信用卡比例
     */
    private String c_ratio;

    /**
     * 信用卡固定值
     */
    private String c_fixed;

    /**
     * 信用卡最小值
     */
    private String c_down_limit;

    /**
     * 信用卡最大值
     */
    private String c_up_limit;

    private String day_init_limit;

    /**
     * 借记卡单日限额
     */
    private String day_limit;

    private String each_up_limit;

    private String each_down_limit;

    private String c_day_init_limit;

    private String c_day_limit;

    private String c_each_up_limit;

    private String c_each_down_limit;

    private String layered_type;

    /**
     * 是否区分借贷记
     */
    private String card_type;

    /**
     * 充值类型
     */
    private String recharge_type;

    /**
     * 代付机构
     */
    private String payment_org;

    /**
     * 代付账号
     */
    private String payment_account;

    private String scale_type;

    /**
     * 代付密钥
     */
    private String payment_key;

    private String state;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getProd_node() {
        return prod_node;
    }

    public void setProd_node(String prod_node) {
        this.prod_node = prod_node;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public Long getProd_id() {
        return prod_id;
    }

    public void setProd_id(Long prod_id) {
        this.prod_id = prod_id;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getDown_limit() {
        return down_limit;
    }

    public void setDown_limit(String down_limit) {
        this.down_limit = down_limit;
    }

    public String getUp_limit() {
        return up_limit;
    }

    public void setUp_limit(String up_limit) {
        this.up_limit = up_limit;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getC_fee_type() {
        return c_fee_type;
    }

    public void setC_fee_type(String c_fee_type) {
        this.c_fee_type = c_fee_type;
    }

    public String getC_ratio() {
        return c_ratio;
    }

    public void setC_ratio(String c_ratio) {
        this.c_ratio = c_ratio;
    }

    public String getC_fixed() {
        return c_fixed;
    }

    public void setC_fixed(String c_fixed) {
        this.c_fixed = c_fixed;
    }

    public String getC_down_limit() {
        return c_down_limit;
    }

    public void setC_down_limit(String c_down_limit) {
        this.c_down_limit = c_down_limit;
    }

    public String getC_up_limit() {
        return c_up_limit;
    }

    public void setC_up_limit(String c_up_limit) {
        this.c_up_limit = c_up_limit;
    }

    public String getDay_init_limit() {
        return day_init_limit;
    }

    public void setDay_init_limit(String day_init_limit) {
        this.day_init_limit = day_init_limit;
    }

    public String getDay_limit() {
        return day_limit;
    }

    public void setDay_limit(String day_limit) {
        this.day_limit = day_limit;
    }

    public String getEach_up_limit() {
        return each_up_limit;
    }

    public void setEach_up_limit(String each_up_limit) {
        this.each_up_limit = each_up_limit;
    }

    public String getEach_down_limit() {
        return each_down_limit;
    }

    public void setEach_down_limit(String each_down_limit) {
        this.each_down_limit = each_down_limit;
    }

    public String getC_day_init_limit() {
        return c_day_init_limit;
    }

    public void setC_day_init_limit(String c_day_init_limit) {
        this.c_day_init_limit = c_day_init_limit;
    }

    public String getC_day_limit() {
        return c_day_limit;
    }

    public void setC_day_limit(String c_day_limit) {
        this.c_day_limit = c_day_limit;
    }

    public String getC_each_up_limit() {
        return c_each_up_limit;
    }

    public void setC_each_up_limit(String c_each_up_limit) {
        this.c_each_up_limit = c_each_up_limit;
    }

    public String getC_each_down_limit() {
        return c_each_down_limit;
    }

    public void setC_each_down_limit(String c_each_down_limit) {
        this.c_each_down_limit = c_each_down_limit;
    }

    public String getLayered_type() {
        return layered_type;
    }

    public void setLayered_type(String layered_type) {
        this.layered_type = layered_type;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(String recharge_type) {
        this.recharge_type = recharge_type;
    }

    public String getPayment_org() {
        return payment_org;
    }

    public void setPayment_org(String payment_org) {
        this.payment_org = payment_org;
    }

    public String getPayment_account() {
        return payment_account;
    }

    public void setPayment_account(String payment_account) {
        this.payment_account = payment_account;
    }

    public String getScale_type() {
        return scale_type;
    }

    public void setScale_type(String scale_type) {
        this.scale_type = scale_type;
    }

    public String getPayment_key() {
        return payment_key;
    }

    public void setPayment_key(String payment_key) {
        this.payment_key = payment_key;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}