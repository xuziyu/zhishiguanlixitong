<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<html>
<head>
	<title>管理标签</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
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
        	$("a[name=mergeTag]").click(function(){
        		var labelid1=$(this).attr("valueTag");
				mergeTagjBox(labelid1);
				$('.jbox-content').css('overflow-y','hidden');
        	});
        	function mergeTagjBox(labelid){
        		$.jBox("iframe:${ctx}/cms/mergeTaglist?id="+labelid,{ 
          		 	title: "选择合并目标",  
            		width: 300,  
            		height: 420,
            		top:'20px',  
            		buttons: { '确定':true,'关闭': false},
            		submit:function(v,h,f){
            			if(v==true){
            				confirmx("确认合并标签吗?(不可恢复)",function(){
            					var window=h.find("#jbox-iframe")[0].contentWindow.document;
            					var radiovalue=$(window).find("input[name=label]:checked").val();
            					var value=$(window).find("#mergeTag").val();
            					//alert(radiovalue+"..."+value);
            					//var alllabel=window.alllabel.value;
            					$.ajax({
									type 	: 	"POST",   
		        					url 	: 	'${ctx}/cms/mergeTag',
		        					data 	:	{
		        								firstlabelid:labelid,
		        								secondlabelid:radiovalue,
		        								newname:value	
		        								},
									success : 	function(data){
													if(data=='1')
														$.jBox.tip("合并名称重名,请重新填写!");
													if(data=='0'){
														location.reload();
													}
													if(data=='2'){
														$.jBox.tip("请选择第二个需要合并的标签");
													}
		              		  					},
		         					error 	: 	function(){     
		            							$.jBox.error("Error Transufal", "Error");	
		          								} 
									});
							
            					});
            					return false;
            				}else{
            					return true;
            				}
            		}  
        	});
        	}
        	
        });
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>管理标签</a></li>
		<li><a href="${ctx}/cms/addTag"><c:if test="${tagflag==1}">增加</c:if>标签</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="label" action="${ctx}/cms/tagControl" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>标签名称：</label><form:input path="labelvalue" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;		
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag_label')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	</form:form>
	<sys:message2 content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>标签名称</th><th>关联用户数</th><th>关联知识数</th><th>创建时间</th><th>创建人</th><th>操作</th></tr>
		<c:forEach items="${page.list}" var="label"  >
			<tr>
				<td>${label.labelvalue}</td>
				<td>${label.countuser}</td>
				<td>${label.countarticle}</td>
				<td><fmt:formatDate value="${label.createDate}" type="both"/></td>
				<td>${label.createBy.name}</td>
				<td>
					<c:choose>
						
						<c:when test="${label.delFlag==0}">
							<a href="${ctx}/cms/addTag?id=${label.id}&tagflag=${0}" >修改</a>
							<%-- <a href="${ctx}/cms/pass?id=${label.id}&delFlag=${1}">弃审</a>--%>
							<a name="mergeTag" href="#" valueTag="${label.id}">合并到</a>
							<c:if test="${label.countuser == 0 && label.countarticle==0}">
							<a href="${ctx}/cms/deleteTag?id=${label.id}&delFlag=${0}&flag=${1}" onclick="return confirmx('确认删除这标签吗？',this.href)">删除</a>
							</c:if>
						</c:when>
						
						<c:when test="${label.delFlag==1}">
							<a href="${ctx}/cms/pass?id=${label.id}&delFlag=${0}&ischecked=${1}" >通过</a>
							<a href="${ctx}/cms/pass?id=${label.id}&delFlag=${2}" >不通过</a>
						</c:when>
						
						<c:when test="${label.delFlag==2}">
							<a href="${ctx}/cms/addTag?id=${label.id}&tagflag=${0}" >修改</a>
							<a href="${ctx}/cms/pass?id=${label.id}&delFlag=${0}&ischecked=${2}" >通过</a>
							<a href="${ctx}/cms/deleteTag?id=${label.id}&delFlag=${2}&flag=${1}" onclick="return confirmx('确认删除这标签吗？',this.href)">删除</a>
						</c:when>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>