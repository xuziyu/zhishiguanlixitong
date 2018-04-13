<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<html>
<head>
	<title>知识库管理员</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" >
		function selectAll(){
        	var selectall=document.getElementById("selectall");
        	if(selectall.checked){
        		var select=document.getElementsByName("selectManger");
        		for(var i=0;i<select.length;i++){
        			select[i].checked = true;
        		}   		
        	}else{
        		var select=document.getElementsByName("selectManger");
        		for(var i=0;i<select.length;i++){
        			select[i].checked = false;
        		}		
        	}	
       }
	
		
		$(document).ready(function() {
			var st1=$("input[name='selectManger']:checked");
     		var st2=$("input[name='selectManger']");
     		if(st1.length==st2.length){
     			$('#selectall').attr("checked","checked");
     		}
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
				//if(value==text)
				if(text.indexOf(value)>-1)
				{
					$(this).show();
					//$(this).siblings().hide();
					$('#selectall').attr("disabled","disabled");
				}           
			});
			
		});
		
		
		
		});
	
	
	
	</script>
</head>
<body>	
		<div id="search" class="form-search" style="padding:10px 4px 8px 0px;">
		<label class="control-label" style="padding:5px 5px 3px 2px;">名称：</label>
		<input id="select" type="text"  class="empty" maxlength="50" style="width:110px;"/>	
		<input id="btnSubmit" class="btn" type="button" value="查询"/>
		</div>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr><th>名称</th><th>全选<input id= "selectall" name="selectall" type="checkbox" onclick="selectAll()" /></th></tr>
			</thead>
			<tbody>
				<c:forEach items="${managers}" var="managers">
					<tr>
						<td>${managers.name}</td>
						<td>
							<c:if test="${managers.isChecked=='0'}">
							选择<input name="selectManger" type="checkbox"  value="${managers.id}" manager="${managers.name}"/>
							</c:if>
							<c:if test="${managers.isChecked=='1'}">
							选择<input name="selectManger" type="checkbox" checked="checked" value="${managers.id}" manager="${managers.name}"/>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		<p></p>
	</form>	
</body>
</html>