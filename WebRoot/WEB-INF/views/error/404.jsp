<%
response.setStatus(404);

// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print("页面不存在.");
}

//输出异常信息页面
else {
%>
<%@page import="com.yonyou.kms.common.web.Servlets"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<title>404 - 页面不存在</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
<c:if test="${article.delFlag==1}">
<h1>本知识已下架!</h1>
	<div class="container-fluid">
		<div class="page-header">
			<h1>页面不存在.</h1>
		</div>
		<div>
			<a href="${ctx_f}" onclick="history.go(-1);" class="btn">返回知识库主页</a>
		</div>
		<script>try{$.jBox.closeTip();}catch(e){}</script>
	</div>
</c:if>
<c:if test="${article.delFlag==2}">
<h1>本知识未审核,不能分享</h1>
	<div class="container-fluid">
		<div class="page-header">
			<h1>页面不存在.</h1>
		</div>
		<div>
			<a href="${ctx_f}" onclick="history.go(-1);" class="btn">返回知识库主页</a>
		</div>
		<script>try{$.jBox.closeTip();}catch(e){}</script>
	</div>
</c:if>
<c:if test="${Message==1}">
<h1>该用户所在公司或部门暂无知识库！</h1>
	<div class="container-fluid">
		<div class="page-header">
			<h1>页面不存在.</h1>
		</div>
		<!--
		<div>
			<a href="${ctx_f}" onclick="history.go(-1);" class="btn">返回知识库主页</a>
		</div>
		-->
		<script>try{$.jBox.closeTip();}catch(e){}</script>
	</div>
</c:if>

</body>
</html>

<%
out.print("<!--"+request.getAttribute("javax.servlet.forward.request_uri")+"-->");
} out = pageContext.pushBody();
%>