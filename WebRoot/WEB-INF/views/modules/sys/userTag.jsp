<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<html>
<head>
	<title>我增加的标签</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.XYTipsWindow.min.2.8.js" type="text/javascript"></script>
	<script type="text/javascript" >
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
        
	$(document).ready(function() {
		$("#addTag").click(function(){
			 $.jBox("iframe:${ctx}/sys/user/addmyTag",{  
			 //$.jBox("${ctx}/sys/user/addmyTag",{  
          		 	title: "标签列表",  
            		width: 300,  
            		height: 420,  
            		buttons: { '确定':true ,'关闭': false},
            		submit:function(v,h,f){
            			if(v==true){
            				//var checked=f.find("input[name='selectTag']:checked").val();
            				//var checked=h.find("#jbox-iframe")[0].contentWindow;
            				var window=h.find("#jbox-iframe")[0].contentWindow;
            				var alllabel=window.alllabel.value;
            				$.ajax({
								type 	: 	"POST",   
		        				url 	: 	'${ctx}/sys/user/savemyTag',
		        				data 	:	{
		        								addtag: alllabel
		        							},
								success : 	function(){
		            						location.reload();
		              		  				},
		         				error 	: 	function(){     
		            						$.jBox.error("Error Transufal", "Error");	
		          							} 
							});
            			}
            		}  
        	}); 
		});
	})
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/myTag">我关注的标签</a></li>
		<li class="active"><a>我增加的标签</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="label" action="${ctx}/sys/user/userTag" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>标签名称：</label><form:input path="labelvalue" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;		
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag_usertag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	</form:form>
	<sys:message2 content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>标签名称</th><th>关联用户数</th><th>关联知识数</th><th>操作</th></tr>
		<c:forEach items="${page.list}" var="label">
			<tr>
				<td> <a>${label.labelvalue}</a></td>
				<td>${label.countuser}</td>
				<td>${label.countarticle}</td>
				<td>
					<c:if test="${label.delFlag==1}">
					<a href="${ctx}/cms/deleteTag?id=${label.id}&delFlag=${1}&flag=${0}" onclick="return confirmx('确认删除这标签吗？',this.href)">删除</a>
					</c:if>
					<c:if test="${label.delFlag==0 && label.countuser == 0 && label.countarticle==0}">
					<a href="${ctx}/cms/deleteTag?id=${label.id}&delFlag=${0}&flag=${0}" onclick="return confirmx('确认删除这标签吗？',this.href)">删除</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>