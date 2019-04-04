package com.dx.model;


import java.io.Serializable;
import java.util.Date;


/**
 * @author 
 */

public class ChannelMerchant implements Serializable {
   
    private Long id;
    private Long channel_id;

    /**
     * 子商户号
     */
    private String sub_mch_id;

    /**
     * 商户名称 --ali --name
     */
    private String merchant_name;

    /**
     * 商户简称--ali -alias_name
     */
    private String merchant_shortname;

    /**
     * 客服电话 --ali service_phone 商户客服电话
     */
    private String service_phone;

    /**
     * 联系人 -- ali name
     */
    private String contact;

    /**
     * 联系电话 -- ali phone
     */
    private String contact_phone;

    /**
     * 联系邮箱 -- ali-email
     */
    private String contact_email;

    /**
     * 经营类目   行业类目，请填写对应的ID  --ali category_id
     */
    private String business;

    /**
     * 联系人微信账号类型
     */
    private String contact_weachatid_type;

    /**
     * 联系人微信帐号 
     */
    private String contact_wechatid;

    /**
     * 商户备注 -- ali memo
     */
    private String merchant_remark;

    /**
     * 状态
     */
    private String status;

    /**
     * 渠道商户类型（微信侧、支付宝侧、银联标、银联直）
     */
    private String type;

    private String creator;

    private Date create_time;

    private String update_name;

    private Date update_time;
    private String data_json;
    /**
     * 支付宝-商户编号
     */
    private String external_id;

    /**
     * 支付宝-联系人类型
     */
    private String ai_type;

    /**
     * 手机
     */
    private String ali_mobile;

    /**
     * 身份证号
     */
    private String id_card_no;

    /**
     * 省份编码，省份编码是与国家统计局一致
     */
    private String province_code;

    /**
     * 城市编码，城市编码是与国家统计局一致
     */
    private String city_code;

    /**
     * 区县编码，区县编码是与国家统计局一致，
     */
    private String district_code;

    /**
     * 地址。商户详细经营地址或人员所在地点
     */
    private String address;

    /**
     * 经度，浮点型, 小数点后最多保留6位。
     */
    private String longitude;

    /**
     * 纬度，浮点型,小数点后最多保留6位如需要录入经纬度，请以高德坐标系为准，
     */
    private String latitude;

    /**
     * 地址类型。取值范围：BUSINESS_ADDRESS：经营地址（默认）
     */
    private String addinfos_type;

    /**
     * 商户证件编号（企业或者个体工商户提供营业执照，事业单位提供事证号）
     */
    private String business_license;

    /**
     * 银行卡号
     */
    private String card_no;

    /**
     * 银行卡持卡人姓名
     */
    private String card_name;

    /**
     * 商户的支付二维码中信息，用于营销活动
     */
    private String pay_code_info;

    /**
     * 商户的支付宝账号
     */
    private String logon_id;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_shortname() {
        return merchant_shortname;
    }

    public void setMerchant_shortname(String merchant_shortname) {
        this.merchant_shortname = merchant_shortname;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getContact_weachatid_type() {
        return contact_weachatid_type;
    }

    public void setContact_weachatid_type(String contact_weachatid_type) {
        this.contact_weachatid_type = contact_weachatid_type;
    }

    public String getContact_wechatid() {
        return contact_wechatid;
    }

    public void setContact_wechatid(String contact_wechatid) {
        this.contact_wechatid = contact_wechatid;
    }

    public String getMerchant_remark() {
        return merchant_remark;
    }

    public void setMerchant_remark(String merchant_remark) {
        this.merchant_remark = merchant_remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getAi_type() {
        return ai_type;
    }

    public void setAi_type(String ai_type) {
        this.ai_type = ai_type;
    }

    public String getAli_mobile() {
        return ali_mobile;
    }

    public void setAli_mobile(String ali_mobile) {
        this.ali_mobile = ali_mobile;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String id_card_no) {
        this.id_card_no = id_card_no;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddinfos_type() {
        return addinfos_type;
    }

    public void setAddinfos_type(String addinfos_type) {
        this.addinfos_type = addinfos_type;
    }

    public String getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(String business_license) {
        this.business_license = business_license;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getPay_code_info() {
        return pay_code_info;
    }

    public void setPay_code_info(String pay_code_info) {
        this.pay_code_info = pay_code_info;
    }

    public String getLogon_id() {
        return logon_id;
    }

    public void setLogon_id(String logon_id) {
        this.logon_id = logon_id;
    }

	public Long getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(Long channel_id) {
		this.channel_id = channel_id;
	}

	public String getData_json() {
		return data_json;
	}

	public void setData_json(String data_json) {
		this.data_json = data_json;
	}
    
}