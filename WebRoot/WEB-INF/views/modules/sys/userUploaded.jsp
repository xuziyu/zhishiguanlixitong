<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的上传</title>
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
        $(document).ready(function(){
       		if($("#delFlag1[checked='checked']").val() == '0'){
       			$("#control").hide();
       			$(".control_td").hide();
       		}
        });
        
        
        //add by wuwq
        $(function(){
        	$(".show_reason").css("cursor","pointer");
        	$("#contentTable tr td .show_reason").click(function(){
        		var id=$(this).attr("value");
        		$.jBox.open(
        			"iframe:${ctx_a}/cms/article/showReason?id="+id,
        			"审核不通过原因",200,250,
        			{
        			}
        		);
        	});
        });
        //end
        
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/user/uploaded">我的知识</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/sys/user/uploaded" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
					title="知识分类" url="/cms/category/treeData" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<label>状态：</label><form:radiobuttons name="flag" onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag_user')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>知识分类</th><th>知识标题</th><th>属性</th><th>知识审核人</th><th>更新时间</th><th>点击数</th><th>退回原因</th><th id="control">操作</th></tr>
		<c:forEach items="${page.list}" var="article">
			<tr>
				<td><a href="javascript:" onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;" title="${fnc:getCategoryStringByIds(article.category.id)}${article.category.name}">${fns:abbr(article.category.name,40)}</a></td>
				<c:choose>
					<c:when test="${article.delFlag==0}">
					<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(article.title,40)}</a></td>
					</c:when>
					<c:when test="${article.delFlag!=0}">
					<td>${fns:abbr(article.title,40)}</td>
					</c:when>
				</c:choose>
				
				<td>
					<c:if test="${article.isOriginal=='1'}">
						原创
					</c:if>
					<c:if test="${article.isOriginal=='0'}">
						转载
					</c:if>
				</td>
				<td>
					<c:if test="${article.examiner==null}">
						
					</c:if>
					<c:if test="${article.examiner !=null}">
						${article.examiner.name}
					</c:if>
				
				
				</td>
				<td><fmt:formatDate value="${article.updateDate}" type="both"/></td>
				<td>${article.hits}</td>
				<td class="control_td">
			<!-- 	<a href="${ctx}/cms/article/addReason?id=${article.reason}"></a>  -->	
					<a class="show_reason" value="${article.id}">查看</a>
				</td>
				<td class="control_td">
	    					<c:choose>
	    						
	    						<c:when test="${article.delFlag!=0}">
	    						<a href="${ctx}/cms/article/add_person_article?id=${article.id}">修改</a>
	    						<a href="${ctx}/cms/article/deleteUserArticle?id=${article.id}&categoryId=${article.category.id}" onclick="return confirmx('确认要删除该知识吗？', this.href)" >删除</a>
	    						</c:when>
	    					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>