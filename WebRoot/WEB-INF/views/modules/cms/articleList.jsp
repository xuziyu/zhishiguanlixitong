<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>知识管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">10
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
	</script>
</head>
<body>
 

	<ul class="nav nav-tabs" >
		 <li class="active"><!-- <a href="${ctx}/cms/article/?category.id=${article.category.id}">知识列表</a> --><a href="#">知识列表</a></li> 
	</ul>
	<%--bengin zhengyu --%>
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/article/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>知识分类：</label><sys:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
					title="知识分类" url="/cms/category/treeData1" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>知识标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<form:radiobutton path="isOriginal" value="3"/>原创
		<form:radiobutton path="isOriginal" value="4"/>转载
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		 <!-- 
			<span>
				<input id="delFlag1" name="delFlag" onclick="$('#searchForm').submit();" type="radio" value="0">
				<label for="delFlag1">已发布</label>
			</span>
			<span>
				<input id="delFlag2" name="delFlag" onclick="$('#searchForm').submit();" type="radio" value="2">
				<label for="delFlag2">待审核</label>
			</span>
			<span>
				<input id="delFlag3" name="delFlag" onclick="$('#searchForm').submit();" type="radio" value="1">
				<label for="delFlag3">已下架</label>
			</span>${fns:abbr(article.title,40)}
		 -->
			<label>状态：</label>
		  	<form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	</form:form>
	<%--end zhengyu --%>
	<sys:message2 content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>分类</th><th>标题</th><th>作者</th><th>作者单位</th><th>部门</th><th style="text-align:center">属性</th><th>分享</th><th>审批人</th><th>未审核人</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="article">
			<tr>
				<td ><a href="javascript:" title="${article.category.name}" onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;">
				<!--${article.category.name}-->
				${fns:abbr(article.category.name,20)}
				</a></td>
				<c:choose>
					<c:when test="${article.delFlag==0}">
					<td ><a title="${article.title}" href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank">
					<!--	${article.title}-->
					  ${fns:abbr(article.title,18)}
					</a></td>
					</c:when>
					<c:when test="${article.delFlag!=0}">
					<td><a title="${article.title}" href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(article.title,18)}</a></td>
					</c:when>
				</c:choose>
				<td>${article.user.name}</td>
				<td title="${article.createBy.company.name}">${fns:abbr(article.createBy.company.name,20)}</td>
				<td title="${article.createBy.office.name}">${fns:abbr(article.createBy.office.name,11)}</td>
				<td style="text-align:center">
					<c:if test="${article.isOriginal=='1'}">
						原创
					</c:if>
					<c:if test="${article.isOriginal=='0'}">
						转载
					</c:if>
				</td>
				<td><!-- ${fnc:getArticleRecommendCount(article.id)} -->
				${article.articleData.id}
					<c:choose>
						<c:when test="${article.articleData.id}">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</td>
				<td>${article.examiner.name}</td>
				<td class="pre_audit" id="${article.id}"></td>
				<td class="line_p">
					<shiro:hasPermission name="cms:article:edit">
						<c:if test="${article.category.allowComment eq '1'}"><shiro:hasPermission name="cms:comment:view">
							<a href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2" onclick="return viewComment(this.href);">评论</a>
						</shiro:hasPermission></c:if>
						<!-- 判断是否具有审核权限 -->
						<c:choose>
							<c:when test="${article.delFlag!=-1}">
								<shiro:hasPermission name="cms:article:audit">
	    					<c:choose>
	    						<c:when test="${article.delFlag==0}">
	    						<a class="line_down" article_id=${article.id} delFlag="1" href="#" >下架</a>
	    						<a class="line_giveup" article_id=${article.id} delFlag="2" href="#">弃审</a>
	    						</c:when>
	    						<c:when test="${article.delFlag==1}">
	    							<a class="line_up" article_id=${article.id} delFlag="0" href="#">上架</a>
	    							<a href="${ctx}/cms/article/form?id=${article.id}" target="mainFrame" >修改</a><!-- onclick="return confirmx('确认要修改该知识吗？', this.href)" -->
	    						</c:when>
	    						<c:when test="${article.delFlag==2}">
	    							<a href="${ctx}/cms/article/form?id=${article.id}" target="mainFrame" >修改</a>
	    						
	    							<a class="line_pass" article_id=${article.id} delFlag="0" href="#">通过</a>
	    							<%-- 
	    							 <c:if test="${article.category.id eq ''}">
	    								<a href="${ctx}/cms/category/toExamine?id=${article.id}&delFlag=0" onclick="return confirmx('确认要审核通过该知识吗？', this.href)">通过</a>
	    							</c:if>
	    							
	    							<c:if test="${article.category.id ne ''}">
	    								<a href="${ctx}/cms/category/toExamine?id=${article.id}&delFlag=0&category.id=${article.article.category.id}" onclick="return confirmx('确认要审核通过该知识吗？', this.href)">通过</a>
	    							</c:if>
	    							--%>
	    							<a class="line_no_pass" article_id=${article.id} delFlag="1" href="#">不通过</a>
	    							<a class="line_professor" article_id=${article.id} delFlag="3" href="#" href="#">提交专家</a>
	    						</c:when>
	    					</c:choose>
						</shiro:hasPermission>
							</c:when>
							<c:when test="${article.delFlag!=-1}">
								<c:if test="${article.delFlag!=0}">
									<a href="${ctx}/cms/article/form?id=${article.id}" target="mainFrame" >修改</a>
	    						</c:if>
							</c:when>
						</c:choose>
					</shiro:hasPermission>
					<c:if test="${isProfessor==true}">
						<a href="${ctx}/cms/article/form?id=${article.id}&delFlag=3" target="mainFrame" >修改</a>
						<a class="line_pass" article_id=${article.id} delFlag="0" href="#">通过</a>
						<a class="line_no_pass" article_id=${article.id} delFlag="1" href="#">不通过</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<script type="text/javascript" src="<%=request.getContextPath()%>/static/common/kms.js"></script>
	<script type="text/javascript">
	
		$(document).ready(function(){
		
				$(".pre_audit").each(function(index,item){
				/* 	${fns:abbr(article.createBy.company.name,24)} */
					var id=$(this).attr("id");
						  $.ajax({ 
			          			     type : "POST",   
			          			     url : '${ctx}/cms/article/preexamers',
			          			     data : {
			            					id : id,
			          					},
			          			 success : function(data){
			          				 
			          			 if(data.length>3){
				          				$(item).html(data.substr(0,3)+"...");
				          				$(item).attr("title",data);
			          			 }
			          			 else{
			          				$(item).html(data.substr(0,3));
			          			 }
			          			 },
			          			 error : function(jqXHR){     
			          			   $(item).html("获取失败");
			          			 }    
			        		});
				});
					
				$(".line_p > .line_professor").on("click", function (){
	            	var article_id = $(this).attr("article_id");
	            	var flag = $(this).attr("delFlag");
	            	var currenTr = $(this).parent('.line_p');
	             			 $.ajax({ 
			          			     type : "POST",   
			          			     url : '${ctx}/cms/category/toExamine',
			          			     data : {
			            					id : article_id,
			            			  		delFlag : flag
			          				},
			          			 dataType : "text",
			          			 success : function(data){
			          			 	$.jBox.tip('已提交专家');
			            		  	currenTr.empty();
			            		  	currenTr.html("<span style='color:green'>提交专家成功</span>"); 
			          			},
			          			   error : function(jqXHR){     
			            				alert("发生错误：" + jqXHR.status); 	
			          				}    
			        		});
			        	//}
		        	//});
         		});
         		
				//下架
			    $(".line_p > .line_down").on("click", function (){
	            	var article_id = $(this).attr("article_id");
	            	var flag = $(this).attr("delFlag");
	            	var currenTr = $(this).parent('.line_p');
	              	//var r = confirm("确认要下架该知识吗？");
	              	//$.jBox.confirm('确认要下架该知识吗？','系统提示',function(v,h,f){
	  					//if (r==true){
	  					//if(v=='ok'){
	  					$.jBox.open(
			          			 	"iframe:${ctx_a}/cms/article/addReason",
				 					"下架",300,370, 
				 					{
					 					top:'20px',
		            					buttons: {'确定':true ,'关闭':false},
		            					submit:function(v,h,f){
				            				if(v==true){
				            					var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						            			var obj = $(doc).find("textarea[name=reason]").val();
				            					$.ajax({ 
			          			     				type : "POST",   
			          			     				url : '${ctx}/cms/category/toExamine',
			          			     				data : {
			            				   				id : article_id,
			            			 					delFlag : flag,
			            			 					reason : obj
			          								},
			          			 					dataType : "text",
			          			 					success : function(data){
			          								// 	$.jBox.tip('下架成功');
			            		  					currenTr.empty();
			            		  					currenTr.html("<span style='color:green'>知识下架成功</span>"); 
			          								},
			          			   					error : function(jqXHR){     
			            								alert("发生错误：" + jqXHR.status); 	
			          								}    
			        							});
				            				} 	
				            			}
				            		}
        					);
        					$('.jbox-content').css('overflow-y','hidden');
			        	//}
		        	//});
         		});
         		
         		//弃审
			    $(".line_p > .line_giveup").on("click", function (){
	            	var article_id = $(this).attr("article_id");
	            	var flag = $(this).attr("delFlag");
	            	var currenTr = $(this).parent('.line_p');
	              	//var r = confirm("确认要弃审该知识吗？");
  					//if (r==true){
  					//$.jBox.confirm('确认要弃审该知识吗？','系统提示',function(v,h,f){
	  					//if(v=='ok'){
	  						$.jBox.open(
			          			 	"iframe:${ctx_a}/cms/article/addReason",
				 					"弃审",300,370, 
				 					{
					 					top:'20px',
		            					buttons: {'确定':true ,'关闭':false},
		            					submit:function(v,h,f){
				            				if(v==true){
				            					var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						            			var obj = $(doc).find("textarea[name=reason]").val();
				            					$.ajax({ 
			          			     				type : "POST",   
			          			     				url  : '${ctx}/cms/category/toExamine',
			          			    	 			data : {
			            				   				id : article_id,
			            			  					delFlag : flag,
			            			  					reason : obj
			          								},
			          			 					dataType : "text",
			          			 					success  : function(data){
			          									// 	$.jBox.tip('弃审成功');
			            		  						currenTr.empty();
			            		  						currenTr.html("<span style='color:green'>知识弃审成功</span>"); 
			          								},
			          			 					error : function(jqXHR){     
			            								alert("发生错误：" + jqXHR.status); 	
			          								}    
			        							});
				            				} 	
				            			}
				            		}
        					);
        					$('.jbox-content').css('overflow-y','hidden');
			           		
		        		//}
		        	//});
         		});
         		
         		//上架
			    $(".line_p > .line_up").on("click", function (){
	            	var article_id = $(this).attr("article_id");
	            	var flag = $(this).attr("delFlag");
	            	var currenTr = $(this).parent('.line_p');
	              	//var r = confirm("确认要上架该知识吗？");
  					//if (r==true){
  					//$.jBox.confirm('确认要上架该知识吗？','系统提示',function(v,h,f){
	  					//if(v=='ok'){
			           		$.ajax({ 
			          			     type : "POST",   
			          			     url : '${ctx}/cms/category/toExamine',
			          			     data : {
			            				   id : article_id,
			            			  delFlag : flag
			          				},
			          			 dataType : "text",
			          			 success : function(data){
			          			 	$.jBox.tip('上架成功');
			            		  	currenTr.empty();
			            		  	currenTr.html("<span style='color:green'>知识上架成功</span>"); 
			          			},
			          			   error : function(jqXHR){     
			            				alert("发生错误：" + jqXHR.status); 	
			          				}    
			        		});
			        	//}
		        	//});
         		});
         		
         		//通过
			    $(".line_p > .line_pass").on("click", function (){
	            	var article_id = $(this).attr("article_id");
	            	var flag = $(this).attr("delFlag");
	            	var currenTr = $(this).parent('.line_p');
	              	//var r = confirm("确认要审核通过该知识吗？");
  					//if (r==true){
  					//$.jBox.confirm('确认要审核通过该知识吗？','系统提示',function(v,h,f){
	  					//if(v=='ok'){
			           		$.ajax({ 
			          			     type : "POST",   
			          			     url : '${ctx}/cms/category/toExamine',
			          			     data : {
			            				   id : article_id,
			            			  delFlag : flag
			          				},
			          			 dataType : "text",
			          			 success : function(data){
			          			 	$.jBox.tip('审核通过成功');
			            		  	currenTr.empty();
			            		  	currenTr.html("<span style='color:green'>知识审核通过</span>"); 
			          			},
			          			   error : function(jqXHR){     
			            				alert("发生错误：" + jqXHR.status); 	
			          			}    
			        		});
			        	//}
		        	//});
         		});
         		
         		
       
         		
         		//不通过
         		 $(".line_p > .line_no_pass").on("click", function (){
         		 	var article_id = $(this).attr("article_id");
	            	var flag = $(this).attr("delFlag");
	            	var currenTr = $(this).parent('.line_p');
  								
  								$.jBox.open(
			          			 	"iframe:${ctx_a}/cms/article/addReason",
				 					"审核不通过",300,370, 
				 					{
					 					top:'20px',
		            					buttons: {'确定':true ,'关闭':false},
		            					submit:function(v,h,f){
				            				if(v==true){
				            					var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						            			var obj = $(doc).find("textarea[name=reason]").val();
				            					$.ajax({ 
			          			     				type : "POST",   
			          			     				url : "${ctx}/cms/category/toExamine",
			          			     				data : {
			            				   				id : article_id,
			            			  					delFlag : flag,
			            			  					reason : obj
			          								},
			          			 					dataType : "text",
			          			 					success : function(data){
			          			 						currenTr.empty();
			            		  						currenTr.html("<span style='color:red'>知识审核不通过</span>"); 
			          			 					},
			          			 					error : function(jqXHR){     
			            								alert("发生错误：" + jqXHR.status); 	
			          								}    
		            							});
				            				} 	
				            			}
				            		}
        						);
        					$('.jbox-content').css('overflow-y','hidden');
        					
         		 });
         });
	</script>
	
</body>
</html>