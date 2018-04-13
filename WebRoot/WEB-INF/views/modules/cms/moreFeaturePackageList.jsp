<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>更多专题栏</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		#more_featurePackkage {
			width:1200px;
			margin:20px auto;
		}
	</style>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		
	</script>
</head>
<body>
	<div id="more_featurePackkage">
	
		<form:form id="searchForm" modelAttribute="featurePackage" action="${ctx}/cms/featurePackage/initcomment?flag=moreFeaturePackage" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="12"/>
			<label>专题搜索：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-bordered table-condensed">
			<tr><th>专题包名称</th><th>显示顺序</th><th>是否上传App</th><th>知识数</th><th>创建时间</th></tr>
			<c:forEach items="${page.list}" var="featurePackage">
				<tr>
					<td><a href="${ctx_f}/getArticleFromFeaturePackage?featurePackageId=${featurePackage.id}">${featurePackage.name }</a></td>
					<!--<td><a href="${ctx_f}/getPageList?featurePackageId=${featurePackage.id}">${featurePackage.name }</a></td>-->
					
					
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
						${featurePackage.count }
					</td>
					<td>
						<fmt:formatDate value="${featurePackage.updateDate}"  type="both" pattern="yyyy-MM-dd HH-mm-ss"/>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="pagination">${page}</div>
	</div>
</body>
</html>
