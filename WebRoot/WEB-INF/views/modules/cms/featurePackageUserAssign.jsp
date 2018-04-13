<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/featurePackage/">专题包列表</a></li>
		<li class="active"><a href="${ctx}/cms/featurePackage/assignuser?id=${featurePackage.id}"><shiro:hasPermission name="sys:role:edit">授权用户</shiro:hasPermission></a></li>
	</ul>
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">专题包名称: <b>${featurePackage.name}</b></span>
			<span class="span4">显示顺序: ${featurePackage.sort}</span>
			<span class="span4">是否同步到App: <c:if test="${featurePackage.canShare==1}">是</c:if><c:if test="${featurePackage.canShare==0}">否</c:if></span>
		</div>
		<div class="row-fluid span12">
			<span class="span4">备注: ${featurePackage.remarks}</span>
		</div>
	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignRoleForm" action="${ctx}/cms/featurePackage/assignrole" method="post" class="hide">
			<input type="hidden" name="id" value="${featurePackage.id}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<input id="assignButton" class="btn btn-primary" type="submit" value="分配用户"/>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				$.jBox.open("iframe:${ctx}/cms/featurePackage/usertorole?id=${featurePackage.id}", "分配用户",780,400,{iframeScrolling:'no',
					buttons:{"确定分配":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择部门，然后为列出的人员分配角色。",submit:function(v, h, f){
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
								$.jBox.tip("未给角色【${role.name}】分配新成员！", 'info');
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#idsArr').val(idsArr);
					    	$('#assignRoleForm').submit();
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
			});
		</script>
	</div>
<!-- 左边加上知识库和知识分类的树列表 -->
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>归属公司</th><th>归属部门</th><th>登录名</th><th>姓名</th><th>电话</th><th>手机</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td>${user.office.name}</td>
				<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
				<td>${user.name}</td>
				<td>${user.phone}</td>
				<td>${user.mobile}</td>
				<shiro:hasPermission name="sys:role:edit"><td>
					<a href="${ctx}/cms/featurePackage/unassign?userId=${user.id}&featurePackageId=${featurePackage.id}" 
						onclick="return confirmx('确认要将用户<b>[${user.name}]</b>从<b>[${featurePackage.name}]</b>专题包中移除吗？', this.href)">移除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
