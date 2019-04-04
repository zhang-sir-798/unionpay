package com.dx.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Merchant implements Serializable {
    /**
     * 主键
     */
    private Long id;

    private Date update_time;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新人
     */
    private String updator;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 商户编号
     */
    private String mer_no;

    /**
     * 商户状态
     */
    private String state;

    /**
     * 商户简称
     */
    private String short_name;

    /**
     * 商户名称
     */
    private String mer_name;

    /**
     * 商户类型
     */
    private String mer_type;

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
    private String settle_mobile;

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
     * 机构32域
     */
    private String mcc32;

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
     * MCC码
     */
    private String mcc;

    /**
     * 法人姓名
     */
    private String legal_name;

    /**
     * 注册地址
     */
    private String address;

    /**
     * 法人手机号
     */
    private String legal_contact;

    /**
     * 法人身份证号
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
     * 营业执照生效日期
     */
    private String license_start_time;

    /**
     * 营业执照失效日期
     */
    private String license_end_time;

    /**
     * 税务登记
     */
    private String tax_id;

    /**
     * 组织机构代码
     */
    private String org_code;

    /**
     * 经营范围
     */
    private String biz_domain;

    private String comments;

    private String line;

    /**
     * 主营业务
     */
    private String main_business;

    /**
     * 身份证照片
     */
    private String id_photo;

    /**
     * 营业执照
     */
    private String license_photo;

    /**
     * 协议
     */
    private String agreement_photo;

    /**
     * 门头照
     */
    private String banner_photo;

    /**
     * 收银台照
     */
    private String counter_photo;

    /**
     * 经营场所照片
     */
    private String spot_photo;

    /**
     * 法人手持照片
     */
    private String owner_photo;

    /**
     * 开户许可证
     */
    private String opening_photo;

    /**
     * 税务登记证
     */
    private String tax_photo;

    /**
     * 组织机构代码证
     */
    private String organization_photo;

    /**
     * 结算卡照片
     */
    private String settle_card_photo;

    private String type;

    private String pay_type;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getMer_name() {
        return mer_name;
    }

    public void setMer_name(String mer_name) {
        this.mer_name = mer_name;
    }

    public String getMer_type() {
        return mer_type;
    }

    public void setMer_type(String mer_type) {
        this.mer_type = mer_type;
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

    public String getSettle_mobile() {
        return settle_mobile;
    }

    public void setSettle_mobile(String settle_mobile) {
        this.settle_mobile = settle_mobile;
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

    public String getMcc32() {
        return mcc32;
    }

    public void setMcc32(String mcc32) {
        this.mcc32 = mcc32;
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

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getLegal_name() {
        return legal_name;
    }

    public void setLegal_name(String legal_name) {
        this.legal_name = legal_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLicense_start_time() {
        return license_start_time;
    }

    public void setLicense_start_time(String license_start_time) {
        this.license_start_time = license_start_time;
    }

    public String getLicense_end_time() {
        return license_end_time;
    }

    public void setLicense_end_time(String license_end_time) {
        this.license_end_time = license_end_time;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getBiz_domain() {
        return biz_domain;
    }

    public void setBiz_domain(String biz_domain) {
        this.biz_domain = biz_domain;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getMain_business() {
        return main_business;
    }

    public void setMain_business(String main_business) {
        this.main_business = main_business;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
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

    public String getBanner_photo() {
        return banner_photo;
    }

    public void setBanner_photo(String banner_photo) {
        this.banner_photo = banner_photo;
    }

    public String getCounter_photo() {
        return counter_photo;
    }

    public void setCounter_photo(String counter_photo) {
        this.counter_photo = counter_photo;
    }

    public String getSpot_photo() {
        return spot_photo;
    }

    public void setSpot_photo(String spot_photo) {
        this.spot_photo = spot_photo;
    }

    public String getOwner_photo() {
        return owner_photo;
    }

    public void setOwner_photo(String owner_photo) {
        this.owner_photo = owner_photo;
    }

    public String getOpening_photo() {
        return opening_photo;
    }

    public void setOpening_photo(String opening_photo) {
        this.opening_photo = opening_photo;
    }

    public String getTax_photo() {
        return tax_photo;
    }

    public void setTax_photo(String tax_photo) {
        this.tax_photo = tax_photo;
    }

    public String getOrganization_photo() {
        return organization_photo;
    }

    public void setOrganization_photo(String organization_photo) {
        this.organization_photo = organization_photo;
    }

    public String getSettle_card_photo() {
        return settle_card_photo;
    }

    public void setSettle_card_photo(String settle_card_photo) {
        this.settle_card_photo = settle_card_photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

}