<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="category" type="com.yonyou.kms.modules.cms.entity.Category" required="true" description="栏目对象"%>
	
	<li>
		<a href="${ctx_f}/list-vip${urlSuffix}">知识库</a>
		<span class="divider">></span> 
	</li>
	<c:forEach items="${fnc:getCategoryListByIds(category.parentIds)}" var="tpl">
		<c:if test="${tpl.id ne '1'}">
			<li>
				<!--  huangmj-->
				<a href="${ctx_f}/list-${tpl.id}${urlSuffix}">${fns:abbr(tpl.name,105)}</a>
				<span class="divider">></span> 
			</li>
		</c:if>
	</c:forEach>
	 <li>
		<!-- huangmj  -->
		<a href="${ctx_f}/list-${category.id}${urlSuffix}">${fns:abbr(category.name,100)}</a>
	</li>