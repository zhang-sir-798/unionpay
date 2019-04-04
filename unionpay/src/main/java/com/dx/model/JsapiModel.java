package com.dx.model;

import java.io.Serializable;

public class JsapiModel implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String IsSuccess;
	
	private String appId;
	
	private String timeStamp;
	
	private String nonceStr;
	
	private String packages;
	
	private String paySign;
	
	private String signType;
	
	private String errMsg;
	
	
	public String getIsSuccess() {
		return IsSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		IsSuccess = isSuccess;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	@Override
	public String toString() {
		return "JsapiModel [IsSuccess=" + IsSuccess + ", appId=" + appId + ", timeStamp=" + timeStamp + ", nonceStr="
				+ nonceStr + ", packages=" + packages + ", paySign=" + paySign + ", signType=" + signType + ", errMsg="
				+ errMsg + "]";
	}
	
	

}
