<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<html>
<head>
	<title>增加关注标签</title>
	<meta name="decorator" content="default"/>
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
		
       function selectAll(){
        	var selectall=document.getElementById("selectall");
        	if(selectall.checked){
        		var select=document.getElementsByName("selectTag_1");
        		for(var i=0;i<select.length;i++){
        			select[i].checked = true;
        		}
        	var selectTag= $("input[name='selectTag_1']:checked").serialize();
			$("#alllabel").val(selectTag);   		
        	}else{
        		var select=document.getElementsByName("selectTag_1");
        		for(var i=0;i<select.length;i++){
        			select[i].checked = false;
        		}		
        	}	
       }
       
     $(document).ready(function() {
     	var st1=$("input[name='selectTag_1']:checked");
     	var st2=$("input[name='selectTag_1']");
     	if(st1.length==st2.length){
     		$('#selectall').attr("checked","checked");
     	}
		<%-- 
		$("#btnSubmit").click(function(){
			var attr=[];
			var selectTag = $("input[name='selectTag']:checked").serialize();
 			 $("input[type='checkbox']:checked").each(function(){ 
       			attr.push(this.value);
  				})
			$.ajax({
				type : "POST",   
		        url : '${ctx}/sys/user/savemyTag',
		        data :{
		        	addtag: selectTag
		        }
				,success : function(){
					parent.window.location.reload(); 
		            parent.$.XYTipsWindow.removeBox();
		            window.parent.location.refresh();
		              		  	
		         }
		         ,error : function(){     
		            alert("数据错误！请重新填写");	
		          } 
			});
		});
		--%>
		$("input[name='selectTag_1']").click(function(){
			var selectTag= $("input[name='selectTag_1']:checked").serialize();
			$("#alllabel").val(selectTag);
		});
		$("#btnSubmit").click(function(){
			var value=$('#select').val();
			if(value==''){
				$('#contentTable tr').show();
				$('#selectall').removeAttr("disabled");
				return;
			}
			$('#contentTable tr:gt(0)').hide();
			$('#contentTable tr:gt(0)').each(function(){
				var text=$(this).children().eq(0).text();
				if(text.indexOf(value)>-1){
					$(this).show();
					//$(this).siblings().hide();
					$('#selectall').attr("disabled","disabled");
				}           
			});
			
		});
		
		
	});
	
	
	
	
	</script>
	<style type="text/css">
		.blueTag{
			color:blue;
		}
	
	</style>
</head>
<body>	
		<div id="search" class="form-search" style="padding:10px 4px 8px 0px;">
		<label class="control-label" style="padding:5px 5px 3px 2px;">标签名称：</label>
		<input id="select" type="text"  class="empty" maxlength="50" style="width:110px;"/>	
		<input id="btnSubmit" class="btn" type="button" value="查询"/>
		</div>
	<form id="form1" action="${ctx}/sys/user/savemyTag" method="post" class="form-horizontal">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr><th>标签名称</th><th>全选<input id= "selectall" name="selectall" type="checkbox" onclick="selectAll()" /></th></tr>
			</thead>
			<tbody>
				<c:forEach items="${userlabellist}" var="userlabel">
					<tr>
					<c:if test="${userlabel.delFlag==0}">
					<td>${userlabel.labelvalue}</td>
					</c:if>
					<c:if test="${userlabel.delFlag==1}">
					<td class="blueTag" title="待审核">${userlabel.labelvalue}</td>
					</c:if>
					<td>
						<c:if test="${userlabel.ischecked==1}">   
							选择<input name="selectTag_1" type="checkbox" checked="checked" labelvalue1="${userlabel.labelvalue}" delFlag="${userlabel.delFlag}" value="${userlabel.labelid}"/>
						</c:if>
						<c:if test="${userlabel.ischecked==0}">
							选择<input name="selectTag_1" type="checkbox" labelvalue1="${userlabel.labelvalue}" delFlag="${userlabel.delFlag}" value="${userlabel.labelid}"/>
						</c:if>
					</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input id="alllabel" type="hidden" value=" "/>
		<p></p>
	</form>	
</body>
</html>