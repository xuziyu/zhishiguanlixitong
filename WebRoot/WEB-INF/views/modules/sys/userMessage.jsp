<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var fflag=0;
		$(document).ready(function() {
			var $all=$("#all");
			$all.css("color","#000");
			$all.css("text-decoration","none");
			$("#selectAll").click(function(){
				var attr=$("#selectAll").attr("checked");
				if(attr=="checked"){
					$(".msgItemC").attr("checked","checked");
				}else{
					$(".msgItemC").removeAttr("checked");
				}
				changeFlag();
			});
			
			$(".msgItemC").click(function(){
				changeFlag();
			});
			
		});
		function del(){
			var text=$("#all").text();
			var t="";
			if(text=="删除"){
				t="确定删除以下的消息吗？";
				confirmx(t,function(){
				delMsg();
			});
			}
			
			else{
				//t="确定将以下消息设置为已读吗？";
				delMsg();
			}
			
			
		}
		
		function changeFlag(){
				var flag=$("input[type='checkbox'].msgItemC").is(':checked');
				var $all=$("#all");
				if(flag==true){
					if($("#readFlag2[checked='checked']").val() == '1'){
       					$all.text("删除");
       					
       				}else{
       					$all.text("已读");
       				}
       				$("#all").css("color","#4C88D3");
       				$all.css("text-decoration","underline");
					$all.attr("onClick","del()");
					$all.attr("href","#");
				}else{
					$("#selectAll").removeAttr("checked");
					$all.css("text-decoration","none");
					$all.text("全选");
					$("#all").css("color","#000");
					$all.removeAttr("onClick");
					$all.removeAttr("href");
				}
			}
		function delMsg(){
		var fflag=0;
			if($("#readFlag2[checked='checked']").val() == '1'){
       			fflag=1;
       		}
			var text=$("input[name='msgItem']:checked");
			var str="";
			var length=text.length;
				for(var i=0;i<length;i++){
					str=str+text[i].value+",";
				}
				
				$.post("${ctx}/oa/oaNotify/delFlags",{'ids':str,'flag':fflag},function(){
					var $all=$("#all");
					$("#selectAll").removeAttr("checked");
					$all.text("全选");
					$all.removeAttr("onClick");
					$all.removeAttr("href");
					$all.css("text-decoration","none");
					$all.css("color","#000");
					$("input[type='checkbox'].msgItemC:checked").each(function(){
							var tr_id="tr_"+this.value;
							$("#"+tr_id).remove();
  					});
			});
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
		
		<li class="active"><a>消息列表</a></li>
		<c:if test="${!oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><li><a href="${ctx}/oa/oaNotify/form">通知添加</a></li></shiro:hasPermission></c:if>
	</ul>
	<form:form id="searchForm" modelAttribute="oaNotify" action="${ctx}/sys/user/userMessage" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>知识标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<c:if test="${requestScope.oaNotify.self}"><li><label>状态：</label>
				<form:radiobuttons onclick="$('#searchForm').submit();" path="readFlag" items="${fns:getDictList('oa_notify_read')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li></c:if>
			<li class="clearfix"></li>
			
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>知识名称</th>
				<th>内容</th>
				<th>查阅状态</th>
				<th>更新时间</th>
				<c:if test="${oaNotify.self}"><th width="20px"><input type="checkbox" id="selectAll"/><a id="all">全选</a></th></c:if>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaNotify">
			<tr id="tr_${oaNotify.id}">
				
				<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${fnc:getArticlecid(oaNotify.remarks)}-${oaNotify.remarks}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(oaNotify.title,40)}</a></td>
				
				
				<td>
					${fns:abbr(oaNotify.content,50)}
				</td>
				<td>
					<c:if test="${requestScope.oaNotify.self}">
						${fns:getDictLabel(oaNotify.readFlag, 'oa_notify_read', '')}
					</c:if>
					<c:if test="${!requestScope.oaNotify.self}">
						${oaNotify.readNum} / ${oaNotify.readNum + oaNotify.unReadNum}
					</c:if>
				</td>
				<td>
					<c:choose>
					<c:when test="${oaNotify.readFlag==0}">
					<fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</c:when>
					<c:when test="${oaNotify.readFlag==1}">
					<fmt:formatDate value="${fnc:getReadDateByOaNotifyId(oaNotify.id)}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</c:when>
					</c:choose>
				</td>
				<td >
					<input class="msgItemC" type="checkbox" name="msgItem" value="${oaNotify.id}"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>