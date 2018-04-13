]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的分享</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function viewComment(href){
			$.jBox.open('iframe:'+href,'查看评论',$(top.document).width()-220,$(top.document).height()-120,{
				buttons:{"关闭":true},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
					$("body", h.find("iframe").contents()).css("margin","10px");
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
		<li class="active"><a>我的分享</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="share" action="${ctx}/sys/user/userShare" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${share.category.id}" labelName="category.name" labelValue="${share.category.name}"
					title="知识分类" url="/cms/category/treeData" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>知识分类</th><th>知识标题</th><th>作者</th><th>更新时间</th><th>分享时间</th></tr>
		<c:forEach items="${page.list}" var="share">
			<tr>
				<td><a href="javascript:" onclick="$('#categoryId').val('${share.category.id}');$('#categoryName').val('${share.category.name}');$('#searchForm').submit();return false;" title="${fnc:getCategoryStringByIds(share.category.id)}${share.category.name}">${fns:abbr(share.category.name,40)}</a></td>
				<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${fnc:getArticlecid(share.titleId)}-${share.titleId}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(share.articletitle,40)}</a></td>
				<td>${fnc:getUserNameByArticleId(share.titleId)}</td>
				
				<td><fmt:formatDate value="${share.articleupdateDate}" type="both"/></td>
				<td><fmt:formatDate value="${share.shareDate}" type="both"/></td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>