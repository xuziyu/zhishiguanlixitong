<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加知识</title>
	<meta name="decorator" content="default"/>
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/featurePackage/">专题包列表</a></li>
		<li class="active"><a href="${ctx}/cms/featurePackage/addArticle?id=${featurePackage.id}">添加知识</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="featurePackage" action="${ctx}/cms/featurePackage/addArticle" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="12"/>
		<form:hidden path="id"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" style="display: none;"/>&nbsp;&nbsp;
	</form:form>
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">专题包名称: <b>${featurePackage.name}</b></span>
			<span class="span4">显示顺序: ${featurePackage.sort}</span>
			<span class="span4">是否同步到App: 
				<c:if test="${featurePackage.canShare =='1'}">
					是
				</c:if>	
				<c:if test="${featurePackage.canShare =='0'}">
					否
				</c:if>	
			</span>
		</div>
		
		<div class="row-fluid span12">
			<span class="span4">备注: ${featurePackage.remarks}</span>
		</div>
		
	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="addArticleForm" action="${ctx}/cms/featurePackage/addArticleSave" method="post" class="hide">
			<input type="hidden" name="id" value="${featurePackage.id}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<input id="addArticleButton" class="btn btn-primary" type="submit" value="添加知识"/>
		<input id="delBatchButton" class="btn btn-primary" type="submit" value="批量移除"/>
		<script type="text/javascript">
			function page(n,s){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").submit();
	        	return false;
	        }
			
			$("#addArticleButton").click(function(){
				$.jBox.open("iframe:${ctx}/cms/featurePackage/articletofeaturepackage?id=${featurePackage.id}", "添加知识",800,400,{iframeScrolling:'no',
					buttons:{"确定添加":"ok", "清除已选":"clear", "关闭":true}, bottomText:"输入关键字，进行相关知识搜索，双击知识名称<span style='font-weight:bold;'>可查看知识内容</span>",submit:function(v, h, f){
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
								$.jBox.tip("未给专题包【${featurePackage.name}】分配新知识！", 'info');
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#idsArr').val(idsArr);
					    	$('#addArticleForm').submit();
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
			
			function getmarked(){
				var str = "";
				var chks = document.getElementsByName("marked");
					for(var i=0; i<chks.length; i++){
					if(chks[i].checked){
						str += chks[i].value+",";
					}
				}
				return str.substring(0, str.length-1);
			}
			
			function haschecked(){
				var nCnt = 0;
				var chks = document.getElementsByName("marked");
				if(chks != null){
					for(var i=0; i<chks.length; i++){
						if(chks[i].checked){
							nCnt++;
						}
					}			
				}
				if(nCnt == 0){
					$.jBox.tip("您没有选中任何信息!");
					return false;
				}
				return true;
			}
			
			//批量删除
			$("#delBatchButton").click(function(){
				if( !haschecked() ){
					return ;
				}
				
				var marked = getmarked() ;
				
				$.jBox.confirm('你确定删除被选中的知识吗?','提示',function(v,h,f){
					if(v=='ok'){
						$.ajax({
							type : 'GET',
							url : "${ctx}/cms/featurePackage/delBatch?featuerPackageId=${featurePackage.id}" + "&ids=" + marked,
							async: false,
							dataType: 'text',
							success : function(data) {
								if(data == "success"){
									$.jBox.tip("删除成功");
									
									window.location.reload();
									/* $.ajax({
										type : 'GET',
										url : "${ctx}/cms/featurePackage/deleteArtileFeaturePackage?featuerPackageId=${featurePackage.id}",
										async: false
									}); */
									
								}
							},
							error : function(XMLHttpRequest, textStatus, errorThrown) {
									$.jBox.tip("删除失败"); 
							},
						});
					}
				});
			   
			    
				
				
			});
			
			
			/**
			 * 选择选中全部或取消选中全部
			 */ 
			function selectAll() {
				var current_checkbox = document.getElementById("markedAll");
			    var chks = document.getElementsByName("marked");
			    if (chks != null){
				    for (var i=0; i<chks.length; i++) {
						chks[i].checked  =  current_checkbox.checked;
				    }		    
			    }
			}
		</script>
	</div>
	
	<!--专题包下已添加的知识 -->
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th style="display:none">id</th><th>全选<input type="checkbox" id="markedAll"  name="markedAll" onclick="selectAll()"  /></th><th>已添加知识</th><th>作者</th><th>更新时间</th><th>点击数</th><th>推荐数</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${articleList}" var="article">
			<tr>
				<td><input type="checkbox" name="marked" value="${article.id}"/></td>
				<c:choose>
					<c:when test="${article.delFlag==0}">
					<td >${article.title}</td>
					</c:when>
					<c:when test="${article.delFlag!=0}">
					<td><a title="${article.title}" href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank">${fns:abbr(article.title,20)}</a></td>
					</c:when>
				</c:choose>
				
				<td>${article.user.name}</td>
				<td><fmt:formatDate value="${article.updateDate}" type="both"/></td>
				<td>${article.hits}</td>
				<td>${fnc:getArticleRecommendCount(article.id)}</td>
				<td class="line_p">
					<a title="${article.title}" href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank">
					  查看
					</a>
					<a href="${ctx}/cms/featurePackage/deleteArtileFeaturePackage?featuerPackageId=${featurePackage.id}&&aritcleId=${article.id}" target="mainFrame" >移除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
</body>
</html>
