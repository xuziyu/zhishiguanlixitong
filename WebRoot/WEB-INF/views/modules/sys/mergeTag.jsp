<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<html>
<head>
	<title>合并标签</title>
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
       
     $(document).ready(function() {
		$("#btnSubmit").click(function(){
			var value=$('#select').val();
			if(value==''){
				$('#contentTable tr').show();
				return;
			}
			$('#contentTable tr:gt(0)').hide();
			$('#contentTable tr:gt(0)').each(function(){
				var text=$(this).children().eq(0).text();
				if(text.indexOf(value)>-1){
					$(this).show();
					//$(this).siblings().hide();
				}           
			});
		});
		<%-- 
		$('input[name=label]').click(function(){
			var value=$(this).attr("tagname");
			$('#mergeTag').val(value);		
		});
		--%>
		
		
	});
	
	
	
	
	</script>
</head>
<body>	
		<div id="search" class="form-search" style="padding:10px 4px 8px 0px;">
		<label class="control-label" style="padding:5px 5px 3px 2px;" >合并名称：</label>
		<input id="mergeTag" type="text"  class="empty" 	 style="width:110px;" maxlength="4"/>	
		<%-- 
		<label class="control-label" style="padding:5px 5px 3px 2px;">搜索标签：</label>
		<input id="select" type="text"  class="empty" maxlength="50" style="width:110px;"/>	
		<input id="btnSubmit" class="btn" type="button" value="查询"/>
		</div>
		--%>
		<div style="height: 530px;width: 300px;">
		<iframe id="cmsMainFrame" name="cmsMainFrame" src="${ctx}/tag/treeselect?url=/cms/TagtreeData2?flag=2" style="overflow:auto;width:100%;height:100%"
				 frameborder="no" ></iframe>
		
		</div>
		
		<%-- 
	<form id="form1" action="#" method="post" class="form-horizontal">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr><th>标签名称</th><th>选择</th></tr>
			</thead>
			<tbody>
				<c:forEach items="${labellist}" var="label">
					<tr>
						<td>${label.labelvalue}</td>
						<td><input name="label" type="radio" value="${label.id}" tagname="${label.labelvalue}"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<p></p>
	</form>	
	--%>
</body>
</html>