<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

	<head>
		<title>首页轮播</title>
		<meta name="decorator" content="default" />
		<script type="text/javascript">
            </script>
	</head>

	<body>
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="#">首页轮播</a>
			</li>
		</ul>
		<form:form id="searchForm" modelAttribute="frontswitch"
			action="${ctx}/cms/frontswitch" method="post"
			class="breadcrumb form-search">
		</form:form>
		<sys:message2 content="${message}" />
		<table id="contentTable" class="table table-bordered table-condensed">
			<tr>
				<th>
					知识链接
				</th>
				<th>
					图片路径
				</th>
				<th>
					主题概要
				</th>
				<th>
					详细描述
				</th>
				<th>
					修改时间
				</th>
				<th>
					修改人
				</th>
				<th nowrap="nowrap">
					操作
				</th>
			</tr>
			<c:forEach items="${frontswitch}" var="list">
				<tr>
					<td>
						${list.articleUrl}
						</a>
					</td>
					<td>
						${list.imageUrl}
						</a>
					</td>
					<td>
						${list.topicWord}
						</a>
					</td>
					<td>
						${list.detailExplanation}
						</a>
					</td>
					<td>
						<fmt:formatDate type="both" value="${list.updateDate}"></fmt:formatDate>
					</td>
					<td>
						${list.updateBy.name}
					</td>
					<td>
						<a href="${ctx}/cms/frontswitchmodify?id=${list.id}">修改</a>
						<c:if test="${list.delFlag==0}">
							<a
								href="${ctx}/cms/frontswitchdisabled?id=${list.id}&delFlag=${list.delFlag}">禁用</a>
						</c:if>
						<c:if test="${list.delFlag==1}">
							<a
								href="${ctx}/cms/frontswitchdisabled?id=${list.id}&delFlag=${list.delFlag}">启用</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	</body>

</html>