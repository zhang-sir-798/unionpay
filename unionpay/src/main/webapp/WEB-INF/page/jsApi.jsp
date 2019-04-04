<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/page/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head lang="zh-CN">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
<title></title>
<script type="text/javascript"
	src="${base}/jquery-easyui-1.4.4/jquery.min.js"></script>
<script type="text/javascript">
    var appId = "";
	var timeStamp = "";
	var nonceStr = "";
	var packages = "";
	var paySign = "";
	var signType = "";
    $(document).ready(function(){
    	var isSuccess = "${form.isSuccess}"; 	
    	var msg = "${form.errMsg}";
    	if (isSuccess == "true") {
    		appId = "${form.appId}";
            timeStamp = "${form.timeStamp}";
            nonceStr = "${form.nonceStr}";
            packages = "${form.packages}";
        	signType = "${form.signType}";
            paySign = "${form.paySign}";	
            window.setTimeout(callpay,500); 
		} else {
			$("#payMsg").html(msg);
		}
    });
    function pay(){  
    	callpay();
    }
    
    
    function onBridgeReady() {
    	$("#payMsg").html("");
   	    WeixinJSBridge.invoke('getBrandWCPayRequest', {  
   	        "appId" : appId,  
   	        "timeStamp" : timeStamp,   
   	        "nonceStr" : nonceStr,   
   	        "package" : packages,  
   	        "signType" : signType,   
   	        "paySign" : paySign
   	    }, function(res) {  
   	        WeixinJSBridge.log(res.err_msg);  
   	        if (res.err_msg == "get_brand_wcpay_request:ok") {
   				$("#payMsg").html("支付成功");		
   				WeixinJSBridge.call('closeWindow');
   	        } else if (res.err_msg == "get_brand_wcpay_request:cancel") {    
   				$("#payMsg").html("交易取消");
   				WeixinJSBridge.call('closeWindow');
   	        } else {   
   	          	$("#payMsg").html("支付失败,请联系商家");
   	        }  
   	    }) ;

   	}  

   	function callpay(appId,timeStamp,nonceStr,packages,paySign){ 
   	      if (typeof WeixinJSBridge == "undefined"){ 
   	        if( document.addEventListener ){ 
   	          document.addEventListener('WeixinJSBridgeReady',onBridgeReady, false); 
   	        }else if (document.attachEvent){ 
   	          document.attachEvent('WeixinJSBridgeReady', onBridgeReady);  
   	          document.attachEvent('onWeixinJSBridgeReady', onBridgeReady); 
   	        } 
   	      }else{  
   	        onBridgeReady(); 
   	      } 	        
   	 }  
	</script>
</head>
<body>
	<div id="payMsg"
		style="text-align: center; margin-top: 130px; font-size: 100%">页面加载中，请稍后......</div>
</body>
</html>