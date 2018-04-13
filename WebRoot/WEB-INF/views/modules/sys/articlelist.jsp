<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>关注标签</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.XYTipsWindow.min.2.8.js" type="text/javascript"></script>
	<script type="text/javascript" >
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
		<li><a href="${ctx}/sys/user/myTag">我关注的标签</a></li>
		<li  class="active"><a>知识列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/sys/user/articlelist" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="labelid" name="labelid" type="hidden" value="${labelid}" />
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
					title="知识库" url="/cms/category/treeData" module="article" notAllowSelectRoot="false" cssClass="input-small"/>  
		
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;		
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
	</form:form>
	<sys:message2 content="${message}"/>
	<!-- <input name="labelid" type="hidden" value="${labelid}" /> -->
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>知识分类</th><th>知识标题</th><th>更新时间</th><th>点击数</th></tr>
		<c:forEach items="${page.list}" var="article">       
			<tr>  
			
				<td><a href="javascript:" onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;"   title="${fnc:getCategoryStringByIds(article.category.id)}${article.category.name}" title="${article.category.name}">${fns:abbr(article.category.name,40)}</a></td>
				<td>
					<a href="${ctx_f}/view-${fnc:getArticlecid(article.articleid)}-${article.articleid}${fns:getUrlSuffix()}" target="_blank" title="${article.title}">${fns:abbr(article.title,40)}</a>
				</td>
				<td><fmt:formatDate value="${article.createDate}" type="both"/></td>
				<td>${article.hits}</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>