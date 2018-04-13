<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的推荐</title>
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
		<li class="active"><a>推荐列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="recommend" action="${ctx}/sys/user/userRecommend" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${recommend.category.id}" labelName="category.name" labelValue="${recommend.category.name}"
					title="知识分类" url="/cms/category/treeData" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>知识分类</th><th>知识标题</th><th>作者</th><th>更新时间</th><th>推荐时间</th><th>操作</th></tr>
		<c:forEach items="${page.list}" var="recommend">
			<tr>
				<td><a href="javascript:" onclick="$('#categoryId').val('${recommend.category.id}');$('#categoryName').val('${recommend.category.name}');$('#searchForm').submit();return false;" title="${fnc:getCategoryStringByIds(recommend.category.id)}${recommend.category.name}">${fns:abbr(recommend.category.name,40)}</a></td>
				<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${fnc:getArticlecid(recommend.titleId)}-${recommend.titleId}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(recommend.articletitle,40)}</a></td>
				<td>${fnc:getUserNameByArticleId(recommend.titleId)}</td>
				
				<td><fmt:formatDate value="${recommend.articleupdateDate}" type="both"/></td>
				<td><fmt:formatDate value="${recommend.updateDate}" type="both"/></td>
				<td>
					<a href="${ctx}/sys/user/recommenddelete?id=${recommend.id}&titleid=${recommend.titleId}" onclick="return confirmx('确认要取消推荐该知识吗？', this.href)">取消推荐</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>