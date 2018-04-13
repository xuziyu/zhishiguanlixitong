<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>知识统计</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		function page(n,s,u){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#uploaderForm").submit();
        		return false;
        }
		$(document).ready(function(){
	        //验证
	        $("#uploaderForm").validate({
	       	 	onfocusout : function(element) {
					
				},
				rules: {
					displaybtn:"required",
					beginDate:{
						valiDate:true
					}
				},
				messages: {
					displaybtn:"请选择审核人",
					beginDate:"请同时选择开始和结束时间"
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorPlacement: function(error, element) {
					$.jBox.tip($(error).html());
					
				}
	        
	        
	        	
	        });
	        
	        jQuery.validator.addMethod("valiDate",function(value,element,params){
	        	var anotherValue=$('#endDate').val();
	        	if(value =='' && anotherValue ==''){
	        		return true;
	        	}else if(value !='' && anotherValue!=''){
	        		return true;
	        	}else{
	        		$.jBox.tip("必须同时填写开始和结束时间");
	        	}
	        });
	        
	        
			$("#displaybtn,#selectbtn").click(function(){
				
				//var selectids=$('#selectids').val();
				$.jBox.open("iframe:${ctx}/cms/stats/uploaderTree?delflag=2", "选择用户",780,400,{iframeScrolling:'no',
					buttons:{"确定":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择部门，然后选择部门下的人员",submit:function(v, h, f){
						//nodes = selectedTree.getSelectedNodes();
						var selectNodes=h.find("iframe")[0].contentWindow.selectedTree;
						var nodes = selectNodes.getNodes();
						var ids="";
						var names="";
						if(v=="ok"){
							$.each(nodes,function(i,item){
								ids=ids+item.id+",";
								if(i<5){
									names=names+$(item.name).html()+",";
								}
							})
							
							//把信息储存在服务器上
							 $.ajax({
             					type: "POST",
             					url: "${ctx}/cms/stats/setSession",
             					data: {
             						selectids:ids
             					},
             					dataType: "text",
             					success: function(data){
                         			$('#displaybtn').val(names);
                      			},
                      			error:function(){
                      				$.jBox.tip("服务器错误,请稍后再试");
                      				
                      			}
         					});
							
					    	//$('#selectids').val(ids);
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/stats/article">知识统计</a></li>
		<li ><a href="${ctx}/cms/stats/uploader">上传者统计</a></li>
		<li class="active"><a href="${ctx}/cms/stats/examer">审核者统计</a></li>
	</ul>
	<form id="uploaderForm" action="${ctx}/cms/stats/uploaderSearcher?flag=1" method="post" class=" form-search">
		<div>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<label>审核者:</label>
			<div class="input-append">
			<input id="displaybtn" name="displaybtn" type="text" class="input-small"  readonly="true" value="${displaybtn}" /><a id="selectbtn" href="javascript:" class="btn" ><i class="icon-search"></i></a>
			</div>
			<label>开始日期：</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				value="${beginDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label>结束日期：</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				value="${endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>审核者</th><th>所在单位</th><th>所在部门</th><th>原创数量</th><th>转载数量</th><th>文章数量</th>
		<tbody>
		<c:forEach items="${page.list}" var="stats">
			<tr>
				<td>
				${stats.username}
				</td>
				
				<td>${stats.departname}</td>
				
				<td>${stats.officename}</td>
				<!--
					isExamer用来判断是否为审核者的列表
				-->
				<td><a href="${ctx}/cms/stats/Articlelist?id=${stats.userid}&isChecked=1&isExamer=0&beginDate=${stats.beginDate}&endDate=${stats.endDate}">${stats.oricount}</a></td>
				
				<td><a href="${ctx}/cms/stats/Articlelist?id=${stats.userid}&isChecked=2&isExamer=0&beginDate=${stats.beginDate}&endDate=${stats.endDate}">${stats.repcount}</a></td>
				
				<td><a href="${ctx}/cms/stats/Articlelist?id=${stats.userid}&isChecked=3&isExamer=0&beginDate=${stats.beginDate}&endDate=${stats.endDate}">${stats.arcount}</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>