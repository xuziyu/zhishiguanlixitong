<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>知识库</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>知识标题</th></tr>
		<c:forEach items="${xxx}" var="article">
			<tr>
				<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article[0]}-${article[1]}${fns:getUrlSuffix()}" target="_blank">${article[2]}</a></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>