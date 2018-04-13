<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>知识统计</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s,u){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#uploaderForm").submit();
        		return false;
        }
	        
	       
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/stats/article">知识统计</a></li>
		<li ><a href="${ctx}/cms/stats/uploader">上传者统计</a></li>
		<li ><a href="${ctx}/cms/stats/examer">审核者统计</a></li>
		<li class="active"><a href="">知识列表</a></li>
	</ul>
	<form id="uploaderForm" action="${ctx}/cms/stats/Articlelist" method="post" class=" form-search">
		<div>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="id" name="id" type="hidden" value="${param.id}"/>
			<input id="isChecked" name="isChecked" type="hidden" value="${param.isChecked}"/>
		</div>
	</form>
	<input id="history" class="btn btn-primary" type="button" onclick="history.go(-1)" value="返回"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>上传者</th><th>所在单位</th><th>所在部门</th><th>文章标题</th><th>点击数</th><th>推荐数</th><th>原创</th><th>发布时间</th>
		<tbody>
			<c:forEach items="${page.list}" var="stats">
			<tr>
				<td>${stats.createBy.name}</td>
				<td>${stats.createBy.company.name}</td>
				<td>${stats.createBy.office.name}</td>
				<td><a title="${stats.title}" href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${stats.category.id}-${stats.id}${fns:getUrlSuffix()}" target="_blank">
					  ${fns:abbr(stats.title,34)}
					</a>
				</td>
				<td>${stats.hits}</td>
				<td>${stats.posid}</td>
				<td>
					<c:if test="${stats.isOriginal==1}">
						原创
					</c:if>
					<c:if test="${stats.isOriginal==0}">
						转载
					</c:if>
				</td>
				<td><fmt:formatDate value="${stats.createDate}" type="both"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>