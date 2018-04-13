<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>评论管理</title>
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
		<li class="active"><a href="#">评论列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="comment" action="${ctx}/cms/comment/initcomment" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${comment.category.id}" labelName="category.name" labelValue="${comment.category.name}"
					title="知识分类" url="/cms/category/treeData1" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		  <label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag_comment')}" itemLabel="label" itemValue="value" htmlEscape="false" />
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<tr><th>知识分类</th><th>知识标题</th><th>作者</th><th>评论时间</th><th>评论人</th><th>评论内容</th><th>评论人IP</th><th nowrap="nowrap">操作</th></tr>
		<c:forEach items="${page.list}" var="comment">
			<tr>
				<td><a href="javascript:" onclick="$('#categoryId').val('${comment.category.id}');$('#categoryName').val('${comment.category.name}');$('#searchForm').submit();return false;" title="${fnc:getCategoryStringByIds(fnc:getArticlecid(comment.contentId))}${fnc:getArticleCategoryName(comment.contentId)}">${fnc:getArticleCategoryName(comment.contentId)}</a></td>
				<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${comment.category.id}-${comment.contentId}${fns:getUrlSuffix()}" title="${comment.title}" target="_blank">${fns:abbr(comment.title,40)}</a></td>
				<td>${comment.articleCreater }</td>
				<td><fmt:formatDate value="${comment.createDate}" type="both"/></td>
				<td>${comment.name}</td>
				<td><a href="javascript:" onclick="$('#c_${comment.id}').toggle()">${fns:abbr(fns:replaceHtml(comment.content),40)}</a></td>
				<td>${comment.ip}</td>
				<td><shiro:hasPermission name="cms:comment:edit">
					<c:choose>
	    						<c:when test="${comment.delFlag ne '2'}">
	    							<a href="${ctx}/cms/comment/change?id=${comment.id}${comment.delFlag ne 0?'&isRe=true':''}" 
											onclick="return confirmx('确认要${comment.delFlag ne 0?'恢复审核':'删除'}该评论吗？', this.href)">${comment.delFlag ne 0?'恢复审核':'删除'}</a>
	    						</c:when>
	    						
	    						<c:when test="${comment.delFlag eq '2'}">
	    							<a href="${ctx}/cms/comment/save?id=${comment.id}">恢复</a>
	    							<a href="${ctx}/cms/comment/delete?id=${comment.id}${comment.delFlag ne 0?'&isRe=true':''}"  onclick="return confirmx('确认要清除该评论吗？（永久删除）', this.href)">清除</a>
	    						</c:when>
	    			</c:choose>
				
					</shiro:hasPermission>
				</td>
			</tr>
			<tr id="c_${comment.id}" style="background:#fdfdfd;display:none;"><td colspan="6">${fns:replaceHtml(comment.content)}</td></tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
