<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>内容管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div id="content" class="row-fluid">
		<div id="left">
			<iframe id="cmsMenuFrame" name="cmsMenuFrame" src="${ctx}/cms/tree" style="overflow-y:visible;"
				scrolling="yes" frameborder="no" width="100%"></iframe>
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right" style="height:530px;"><!--  -->
			<iframe id="cmsMainFrame" name="cmsMainFrame" src="${ctx}/cms/none" style="overflow:auto;width:100%;height:100%"
				scrolling="no" frameborder="no" ></iframe>
		</div>
	</div>
	<script type="text/javascript">
		var leftWidth = "190"; // 左侧窗口大小
		function wSize(){
			var strs=getWindowSize().toString().split(",");
			//alert(strs+":"+strs[0]);
			$("#cmsMenuFrame").height(strs[0]-10);
			$("#openClose").height(strs[0]-5);
			//$("#cmsMainFrame").height(1200);
			$("#right").width($("body").width()-$("#left").width()-$("#openClose").width()-20);
		}
		// 鼠标移动到边界自动弹出左侧菜单
		$("#openClose").mouseover(function(){
			if($(this).hasClass("open")){
				$(this).click();
			}
		});
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#cmsMenuFrame").load(function(){
				$("#cmsMenuFrame").contents().find(".ztree a").on("click", function (){
					var url = $(this).attr('href');
					var delFlag = $("#cmsMainFrame").contents().find('#searchForm').find('input[name=delFlag]:checked').val();
					var w=url.indexOf("&");
					if(w>0){
						var olddelFlag = url.substring(w+9);//'delFlag=2'=>9
						if(olddelFlag!=delFlag){
							var newUrl = url.substring(0,w);
							url = newUrl+'&delFlag='+delFlag;
						}
					}else{
						url = url+'&delFlag='+delFlag;
					}
					$(this).attr('href',url);
					return true;	
				});
			});
		});
	</script>
</body>
</html>