<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head lang="zh-CN">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
<title>${form.mcht_name}</title>
<link rel="stylesheet" href="${ctx}/css/normalize.css">
<link rel="stylesheet" href="${ctx}/css/main.css">
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/setup.js"></script>
<script type="text/javascript" src="${ctx}/js/main.js"></script>
<script type="text/javascript">
    var flag="false";
    function pay(){      	
    	if(flag == "false"){
    		flag = "true";
    		$("#paybut").html("处理中...");
        	if ($("#transAmt").val().trim() == "") {
        		$("#transAmtSpan").html("交易金额不能为空！");
        		flag="false";
        		$("#paybut").html("确认支付");
        		return false;
        	}else {
        		var reg = new RegExp("^\\d+(\\.\\d{1,2})?$");
        		if(!reg.test($("#transAmt").val().trim())){
        			$("#transAmtSpan").html("交易金额格式不正确！");
        			flag="false";
        			$("#paybut").html("确认支付");
        			return false;
        	    }else{
        	    	$("#transAmtSpan").html("");
        	    }  		
        	}
        	var url = "${ctx}/gateway/unifiedorder?transAmt="+$("#transAmt").val()+"&qr="+$("#mcht_code").val()+"&trxType="+$("#trx_type").val();
    	    window.location.href = url;
    	}  	
    }
	</script>
</head>

<body>
	<div class="container">
		<div class="top">在线支付</div>
		<div class="main">
			<div class="user">
				<div class="user-message">
					金额: <input id="transAmt" type="text" placeholder="请输入金额">
				</div>
				<span id="transAmtSpan"></span>
			</div>
			<a id="paybut" href="javascript: pay()" class="btn">确认支付</a>
		</div>
	</div>
	<input id="mcht_code" value="${form.mcht_code}" type="hidden" />
	<input id="trx_type" value="${form.trxType}" type="hidden" />
</body>
</html>