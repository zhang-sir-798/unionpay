package com.dx.model;



public class FormModel {
	
	private String masterOrderID;//订单ID
	
	private String transAmt;//交易金额
	
	private String mcht_code;//二维码编号
	
	
	private String mcht_name;//商户名称

	private String staff_phone;//发送短信手机号

	private String trxType;

	private String msg;
	
	private String url;
	
	private String isSuccess;
	
	private String returnValue;
	
	
	public String getMasterOrderID() {
		return masterOrderID;
	}

	public void setMasterOrderID(String masterOrderID) {
		this.masterOrderID = masterOrderID;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getMcht_code() {
		return mcht_code;
	}

	public void setMcht_code(String mcht_code) {
		this.mcht_code = mcht_code;
	}

	public String getMcht_name() {
		return mcht_name;
	}

	public void setMcht_name(String mcht_name) {
		this.mcht_name = mcht_name;
	}

	public String getStaff_phone() {
		return staff_phone;
	}

	public void setStaff_phone(String staff_phone) {
		this.staff_phone = staff_phone;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}
	
	
	
}
