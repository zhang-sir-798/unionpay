package com.dx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Agent implements Serializable {
    private Long id;

    /**
     * 代理商编号
     */
    private String agent_no;

    /**
     * 代理商名称
     */
    private String short_name;

    /**
     * 代理商状态
     */
    private String state;

    private Date update_time;

    private Date create_time;

    private String creator;

    /**
     * 结算类型
     */
    private String settle_type;

    /**
     * 结算姓名
     */
    private String settle_name;

    /**
     * 结算卡号
     */
    private String settle_no;

    /**
     * 结算手机号
     */
    private String settle_phone;

    /**
     * 结算银行分行
     */
    private String settle_branch_bank;

    /**
     * 结算银行分行号
     */
    private String settle_branch_no;

    /**
     * 请求秘钥
     */
    private String req_key;

    /**
     * 卡秘钥
     */
    private String card_key;

    /**
     * 回调秘钥
     */
    private String notify_key;

    /**
     * 代付秘钥
     */
    private String payment_key;

    /**
     * 联系人
     */
    private String contact_person;

    /**
     * 联系人电话
     */
    private String phone_no;

    /**
     * 代理商省
     */
    private String prov_code;

    /**
     * 代理商市
     */
    private String city_code;

    /**
     * 代理商区县
     */
    private String area_code;

    /**
     * 结算卡省
     */
    private String settle_bank_prov;

    /**
     * 结算卡市
     */
    private String settle_bank_city;

    /**
     * 结算卡银行
     */
    private String settle_bank;

    /**
     * 法人名称
     */
    private String legal_name;

    /**
     * 法人手机号
     */
    private String legal_contact;

    /**
     * 法人身份证
     */
    private String legal_id;

    /**
     * 身份证生效日期
     */
    private String id_start_time;

    /**
     * 身份证失效日期
     */
    private String id_end_time;

    /**
     * 营业执照号
     */
    private String license_no;

    /**
     * 营业执照名
     */
    private String license_name;

    /**
     * 身份证正面
     */
    private String id_photo;

    /**
     * 身份证反面
     */
    private String id_back_photo;

    /**
     * 营业执照
     */
    private String license_photo;

    /**
     * 协议
     */
    private String agreement_photo;

    /**
     * 银行卡照片
     */
    private String settleCard_photo;
    /**
     * 代理商回调地址
     */
    private String agent_notify_url;

    /**
     * 备注
     */
    private String comments;

    /**
     * 代理商类别
     */
    private String agent_type;

    /**
     * 机构秘钥
     */
    private String agent_key;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgent_no() {
        return agent_no;
    }

    public void setAgent_no(String agent_no) {
        this.agent_no = agent_no;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSettle_type() {
        return settle_type;
    }

    public void setSettle_type(String settle_type) {
        this.settle_type = settle_type;
    }

    public String getSettle_name() {
        return settle_name;
    }

    public void setSettle_name(String settle_name) {
        this.settle_name = settle_name;
    }

    public String getSettle_no() {
        return settle_no;
    }

    public void setSettle_no(String settle_no) {
        this.settle_no = settle_no;
    }

    public String getSettle_phone() {
        return settle_phone;
    }

    public void setSettle_phone(String settle_phone) {
        this.settle_phone = settle_phone;
    }

    public String getSettle_branch_bank() {
        return settle_branch_bank;
    }

    public void setSettle_branch_bank(String settle_branch_bank) {
        this.settle_branch_bank = settle_branch_bank;
    }

    public String getSettle_branch_no() {
        return settle_branch_no;
    }

    public void setSettle_branch_no(String settle_branch_no) {
        this.settle_branch_no = settle_branch_no;
    }

    public String getReq_key() {
        return req_key;
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public String getCard_key() {
        return card_key;
    }

    public void setCard_key(String card_key) {
        this.card_key = card_key;
    }

    public String getNotify_key() {
        return notify_key;
    }

    public void setNotify_key(String notify_key) {
        this.notify_key = notify_key;
    }

    public String getPayment_key() {
        return payment_key;
    }

    public void setPayment_key(String payment_key) {
        this.payment_key = payment_key;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getProv_code() {
        return prov_code;
    }

    public void setProv_code(String prov_code) {
        this.prov_code = prov_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getSettle_bank_prov() {
        return settle_bank_prov;
    }

    public void setSettle_bank_prov(String settle_bank_prov) {
        this.settle_bank_prov = settle_bank_prov;
    }

    public String getSettle_bank_city() {
        return settle_bank_city;
    }

    public void setSettle_bank_city(String settle_bank_city) {
        this.settle_bank_city = settle_bank_city;
    }

    public String getSettle_bank() {
        return settle_bank;
    }

    public void setSettle_bank(String settle_bank) {
        this.settle_bank = settle_bank;
    }

    public String getLegal_name() {
        return legal_name;
    }

    public void setLegal_name(String legal_name) {
        this.legal_name = legal_name;
    }

    public String getLegal_contact() {
        return legal_contact;
    }

    public void setLegal_contact(String legal_contact) {
        this.legal_contact = legal_contact;
    }

    public String getLegal_id() {
        return legal_id;
    }

    public void setLegal_id(String legal_id) {
        this.legal_id = legal_id;
    }

    public String getId_start_time() {
        return id_start_time;
    }

    public void setId_start_time(String id_start_time) {
        this.id_start_time = id_start_time;
    }

    public String getId_end_time() {
        return id_end_time;
    }

    public void setId_end_time(String id_end_time) {
        this.id_end_time = id_end_time;
    }

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public String getLicense_name() {
        return license_name;
    }

    public void setLicense_name(String license_name) {
        this.license_name = license_name;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public String getId_back_photo() {
        return id_back_photo;
    }

    public void setId_back_photo(String id_back_photo) {
        this.id_back_photo = id_back_photo;
    }

    public String getLicense_photo() {
        return license_photo;
    }

    public void setLicense_photo(String license_photo) {
        this.license_photo = license_photo;
    }

    public String getAgreement_photo() {
        return agreement_photo;
    }

    public void setAgreement_photo(String agreement_photo) {
        this.agreement_photo = agreement_photo;
    }

    public String getSettleCard_photo() {
        return settleCard_photo;
    }

    public void setSettleCard_photo(String settleCard_photo) {
        this.settleCard_photo = settleCard_photo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAgent_type() {
        return agent_type;
    }

    public void setAgent_type(String agent_type) {
        this.agent_type = agent_type;
    }

    public String getAgent_key() {
        return agent_key;
    }

    public void setAgent_key(String agent_key) {
        this.agent_key = agent_key;
    }

	public String getAgent_notify_url() {
		return agent_notify_url;
	}

	public void setAgent_notify_url(String agent_notify_url) {
		this.agent_notify_url = agent_notify_url;
	} 
    

}