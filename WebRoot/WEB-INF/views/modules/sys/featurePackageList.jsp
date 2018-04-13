<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专题包管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function view(href){
			$.jBox.open('iframe:'+href,'查看文档',$(top.document).width()-220,$(top.document).height()-180,{
				buttons:{"关闭":true},
				loaded:function(h){
					//$(".jbox-content", top.document).css("overflow-y","hidden");
					//$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
				}
			});
			return false;
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">
			<a href="${ctx}/sys/user/featurePackageList">我的专题</a>
		</li>
	</ul>
	<form:form id="searchForm" modelAttribute="featurePackage" action="${ctx}/sys/user/featurePackageList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="12"/>
		<input id="flag" name="flag" type="hidden" value="userCenterFeaturePackage"/>
		<label>专题包：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<tr><th>专题包名称</th><th>显示顺序</th><th>是否上传App</th><th nowrap="nowrap">操作</th></tr>
		<c:forEach items="${page.list}" var="featurePackage">
			<tr>
				<td>${featurePackage.name }</td>
				
				
				<td>
					${featurePackage.sort }
				</td>
				<td>
					<c:if test="${featurePackage.canShare =='1'}">
						是
					</c:if>	
					<c:if test="${featurePackage.canShare =='0'}">
						否
					</c:if>	
					
				
				</td>
				<td>
 					<a href="${ctx}/cms/featurePackage/addArticle?id=${featurePackage.id}&flag=userCenterFeaturePackage">添加知识</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
