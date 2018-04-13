<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>评论记录</title>
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
		<li class="active"><a href="${ctx}/sys/user/userComment">评论记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="comment" action="${ctx}/sys/user/userComment" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${comment.category.id}" labelName="category.name" labelValue="${comment.category.name}"
					title="知识分类" url="/cms/category/treeData" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<%-- <label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag_user')}" itemLabel="label" itemValue="value" htmlEscape="false" />--%>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>知识分类</th><th>知识标题</th><th>作者</th><th>评论时间</th><th>评论人</th><th>评论内容</th><th>评论人IP</th><th nowrap="nowrap">操作</th></tr>
		<c:forEach items="${page.list}" var="comment">
			<tr>
				<td><a href="javascript:" onclick="$('#categoryId').val('${comment.category.id}');$('#categoryName').val('${comment.category.name}');$('#searchForm').submit();return false;" title="${fnc:getCategoryStringByIds(comment.category.id)}${comment.category.name}">${fns:abbr(comment.category.name,40)}</a></td>
				<c:choose>
					<c:when test="${comment.delFlag==0}">
					<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${comment.category.id}-${comment.contentId}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(comment.articletitle,40)}</a></td>
					</c:when>
					<c:when test="${comment.delFlag==2}">
					<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${comment.category.id}-${comment.contentId}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(comment.articletitle,40)}</a></td>
					</c:when>
				</c:choose>
				
				<td>${comment.articleCreater }</td>
				<td><fmt:formatDate value="${comment.createDate}" type="both"/></td>
				<td>${comment.name}</td>
				<td><a href="javascript:" onclick="$('#c_${comment.id}').toggle()">${fns:abbr(fns:replaceHtml(comment.content),40)}</a></td>
				
				<td>${comment.ip}</td>
				
				<td>
					
					<a href="${ctx}/cms/comment/deleteUserComment?id=${comment.id}${comment.delFlag ne 0?'&isRe=true':''}" 
						onclick="return confirmx('确认要删除该评论吗？', this.href)">删除</a>
				</td>
			</tr>
			<tr id="c_${comment.id}" style="background:#fdfdfd;display:none;"><td colspan="6">${fns:replaceHtml(comment.content)}</td></tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>