<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			
			$.ajax({
		   		type:"GET",
		   		url:"${ctx}/sys/office/officeAssigned",
		   		data:{"officeId":"${office.id}"},
		   		success:function(data){
		   			appendOffceAudits(data);
		   		}
			});
			
			$("#inputForm").validate({
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
			
			//分配知识审核员
			$("#assignButton").click(function(){
								$.jBox.open("iframe:${ctx}/sys/office/user2office?id=${office.id}", "分配知识审核员",780,400,{iframeScrolling:'no',
					buttons:{"确定分配":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择部门，然后为列出的人员分配知识审核员权限。",submit:function(v, h, f){
						var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
						var ids = h.find("iframe")[0].contentWindow.ids;
						//nodes = selectedTree.getSelectedNodes();
						
						if (v=="ok"){
							// 删除''的元素
							if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
							}
							if(pre_ids.sort().toString() == ids.sort().toString()){
								$.jBox.tip("未给部门【${office.name}】分配新的知识审核员！", 'info');
								return false;
							};
					    	// 执行保存
					    	//loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	//数据提交   ids+office.id
					    	$.ajax({
					    		type:"POST",
					    		url:"${ctx}/sys/office/officeAssign",
					    		data:{"ids":idsArr,"officeId":"${office.id}"},
					    		success:function(data){
					    			appendOffceAudits(data);
					    		}
					    	});
					    	//异步加上到页面
					    	//$('#idsArr').val(idsArr);
					    	//$('#assignRoleForm').submit();
					    	return true;
						} else if (v=="clear"){
							h.find("iframe")[0].contentWindow.clearAssign();
							return false;
		                }
					}, loaded:function(h){
						
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
				$('.jbox-content').css('overflow-y','hidden');
				return false;
			});
			
			//加载审核管理员
			var appendOffceAudits=function(data){
				$("#audit").html("");
				for(var i=0;i<data.length;i++){
					$("#audit").append(data[i].name+",");
				}
			};
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/office/list?id=${office.parent.id}&parentIds=${office.parentIds}">机构列表</a></li>
		<li class="active"><a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">机构<shiro:hasPermission name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级机构:</label>
			<div class="controls">
				<c:if test=""></c:if>
				<c:choose>
					<c:when test="${fns:length(office.id)<=20||fns:length(office.id)>=40}">
						<sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
							title="机构" url="/sys/office/treeData" extId="${office.id}" cssClass="" allowClear="${office.currentUser.admin}"
							 disabled="disabled"/>
					</c:when>
					<c:otherwise>
						<sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
							title="机构" url="/sys/office/treeData" extId="${office.id}" cssClass="" allowClear="${office.currentUser.admin}"/>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
		<div class="control-group" style="display: none">
			<label class="control-label">归属区域:</label>
			<div class="controls">
                <sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">机构名称:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${fns:length(office.id)<=20}">
						<form:input path="name" htmlEscape="false" maxlength="50" class="required" disabled="true"/>
					</c:when>
					<c:otherwise>
						<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
					</c:otherwise>
				</c:choose>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">机构编码:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${fns:length(office.id)<=20}">
						<form:input path="code" htmlEscape="false" maxlength="50" disabled="true"/>
					</c:when>
					<c:otherwise>
						<form:input path="code" htmlEscape="false" maxlength="50" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">机构类型:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${fns:length(office.id)<=20}">
						<form:select path="type" class="input-medium" disabled="true">
							<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</c:when>
					<c:otherwise>
						<form:select path="type" class="input-medium">
							<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
		<div class="control-group" style="display: none">
			<label class="control-label">机构级别:</label>
			<div class="controls">
				<form:select path="grade" class="input-medium">
					<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${fns:length(office.id)<=20}">
						<form:select path="useable" disabled="true">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</c:when>
					<c:otherwise>
						<form:select path="useable">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</c:otherwise>
				</c:choose>

				<span class="help-inline">“是”代表此账号允许登陆，“否”则表示此账号不允许登陆</span>
			</div>
		</div>
		<!-- <div class="control-group">
			<label class="control-label">主负责人:</label>
			<div class="controls">
				 <sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">副负责人:</label>
			<div class="controls">
				 <sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div> 
		<div class="control-group">
			<label class="control-label">联系地址:</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮政编码:</label>
			<div class="controls">
				<form:input path="zipCode" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人:</label>
			<div class="controls">
				<form:input path="master" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">传真:</label>
			<div class="controls">
				<form:input path="fax" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="50"/>
			</div>
		</div>-->
		<!--
			<c:if test="${office.id!=null}">
				<div class="control-group">
						<label class="control-label">知识审核员:</label>
						<div class="controls">
							<span id="audit"></span>
							<button id="assignButton" class="btn btn-primary"/>分配知识审核员</button>
						</div>
				</div>
			</c:if>
		-->
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<!-- 
		<c:if test="${empty office.id}">
			<div class="control-group">
				<label class="control-label">快速添加下级部门:</label>
				<div class="controls">
					<form:checkboxes path="childDeptList" items="${fns:getDictList('sys_office_common')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</div>
			</div>
		</c:if> -->
		<div class="form-actions">
			<shiro:hasPermission name="sys:office:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>