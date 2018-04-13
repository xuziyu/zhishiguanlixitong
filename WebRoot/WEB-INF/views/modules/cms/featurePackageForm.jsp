
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#name").focus();
			$("#inputForm").validate({
				onfocusout : function(element) {
					$(element).valid();
				},
				rules: {
					name: {remote: "${ctx}/cms/featurePackage/checkName?oldName=" + encodeURIComponent("${featurePackage.name}")},
					
				},
				messages: {
					name: {remote: "专题包名称已存在"},
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li>
			<a href="${ctx}/cms/featurePackage/">专题包列表</a>
		</li>
		<li class="active">
			<%-- <a href="${ctx}/cms/featurePackage/form?id=${featurePackage.id}">专题包添加</a> --%>
			<a href="${ctx}/cms/featurePackage/form?id=${featurePackage.id}">专题包${not empty featurePackage.id?'修改':'添加'}</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="featurePackage" action="${ctx}/cms/featurePackage/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">专题包名称:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${featurePackage.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">是否同步到App</label>
			<div class="controls">
				<form:select path="canShare">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				<span class="help-inline">“是”代表此数据可用，“否”则表示此数据不可用</span>
				
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">专题包类型:</label>
			<div class="controls">
				<form:input path="type" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font><font color="#999">(注：请填写数字，1 知识圈 2 产品大全 3 业务知识 4 其他 ...)</font><!--  工作流用户组标识 --></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">显示顺序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font><font color="#999">(注：请填写数字)</font><!--  工作流用户组标识 --></span>
			</div>
		</div>
		 
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
				
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>

</html>