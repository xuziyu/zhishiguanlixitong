<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
  <head>
    <title>知识管理</title>
	<meta name="decorator" content="default"/>
<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value : 'default'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
  </head>
  <body>
 <body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">留言列表</a></li><%--
		<shiro:hasPermission name="cms:guestbook:edit"><li><a href="${ctx}/cms/guestbook/form?id=${guestbook.id}">留言添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="zhishiForm" method="post" class="breadcrumb form-search">

		<label>分类：</label><input type="text" >
		<label>内容 ：</label><input type="text" >
		 <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		 <input id="btndaochu" class="btn btn-primary" type="button" value="导入"/>&nbsp;&nbsp;
	</form:form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>知识标题</th><th>知识内容</th><th>发布人</th><th>发布时间</th><th>审批人</th><th>审批状态</th><th>审批时间</th><th>操作</th></tr></thead>
		<tbody>
		<!--<c:forEach items="${page.list}" var="guestbook">-->
			<tr>
				<td>123</td>
				<td>2</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><a href="#">修改</a>&nbsp;<a href="#">删除</a>&nbsp;<a href="#">推荐</a>&nbsp;<a href="#">下架</a>&nbsp;<a href="#">导出</a>&nbsp;</td>
			</tr>
		<!--</c:forEach>-->
		</tbody>
	</table>
	<div class="pagination"><form >
	 <input id="btnadd" class="btn btn-primary" type="button" value="添加"/>&nbsp;&nbsp;
	
	</form></div>
  </body>
</html>
