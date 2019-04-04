<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<!-- optional javascript: effects.js,jquery.js -->
<link href="<c:url value="/styles/global.css"/>" type="text/css"
	rel="stylesheet">
<!--basic styles-->
<link href="${ctx}/css/bootstrap.min.css" rel="stylesheet" />
<link href="${ctx}/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css" />
<!--[if IE 7]>
		  <link rel="stylesheet" href="css/font-awesome-ie7.min.css" />
		<![endif]-->
<!--page specific plugin styles-->
<link rel="stylesheet" href="${ctx}/css/colorbox.css" type="text/css" />
<link href="${ctx}/css/font-awesome.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="${ctx}/css/jquery-ui-1.10.3.custom.min.css" />
<link rel="stylesheet" href="${ctx}/css/chosen.css" />
<link rel="stylesheet" href="${ctx}/css/datepicker.css" />
<link rel="stylesheet" href="${ctx}/css/bootstrap-timepicker.css" />
<link rel="stylesheet" href="${ctx}/css/daterangepicker.css" />
<link rel="stylesheet" href="${ctx}/css/colorpicker.css" />
<!--fonts-->
<link rel="stylesheet"
	href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />
<!--ace styles-->
<link rel="stylesheet" href="${ctx}/css/ace.min.css" />
<link rel="stylesheet" href="${ctx}/css/ace-responsive.min.css" />
<link rel="stylesheet" href="${ctx}/css/ace-skins.min.css" />
<script type="text/javascript" src="${ctx }/js/Calendar.js"></script>
<!-- rapid-validation BEGIN-->
<link href="<c:url value="/scripts/rapid-validation/validation.css"/>"
	type="text/css" rel="stylesheet">
<script
	src="<c:url value="/scripts/rapid-validation/prototype_for_validation.js"/>"
	type="text/javascript"></script>

<!-- 
	<script src="<c:url value="/scripts/rapid-validation/effects.js"/>" type="text/javascript"></script>
	-->

<!-- show validation error as tooptips -->
<script src="<c:url value="/scripts/rapid-validation/tooltips.js"/>"
	type="text/javascript"></script>
<link href="<c:url value="/scripts/rapid-validation/tooltips.css"/>"
	type="text/css" rel="stylesheet">



<!--basic scripts-->
<!-- 
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
 -->
<script type="text/javascript">
	window.jQuery
			|| document.write("<script src='${ctx}/js/jquery-1.9.1.min.js'>"
					+ "<"+"/script>");
</script>
<script
	src="<c:url value="/scripts/rapid-validation/validation_cn.js"/>"
	type="text/javascript"></script>
<!-- rapid-validation END-->

<script src="<c:url value="/scripts/application.js"/>"
	type="text/javascript"></script>

<script src="<c:url value="/scripts/My97DatePicker/WdatePicker.js"/>"
	type="text/javascript"></script>
<script src="${ctx}/js/bootstrap.min.js"></script>

<!--page specific plugin scripts-->
<!-- InstanceBeginEditable name="EditRegion5" -->
<script src="${ctx}/js/jquery.colorbox.js"></script>
<script src="${ctx}/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/js/jquery.dataTables.bootstrap.js"></script>
<!-- InstanceEndEditable -->

<!--ace scripts-->

<script src="${ctx}/js/ace-elements.min.js"></script>
<script src="${ctx}/js/ace.min.js"></script>

<!--inline scripts related to this page-->
<script type="text/javascript"
	src="${ctx}/js/jquery.form.js"></script>