<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
	<title>标签管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div id="content" class="row-fluid">
		<div id="left" style="width:190px;">
			<iframe id="cmsMenuFrame" name="cmsMenuFrame" src="${ctx}/cms/tagtree" style="overflow-y:visible;"
				scrolling="yes" frameborder="no" width="100%"></iframe>
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right" style="height: 530px;width: 935px;"><!--  -->
			<iframe id="cmsMainFrame" name="cmsMainFrame" src="${ctx}/cms/tagControl" style="overflow:auto;width:100%;height:100%"
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
			$("#cmsMainFrame").load(function(){
				//批量删除标签
				$("#cmsMainFrame").contents().find("#deleteTag").on("click", function (){
					var isparent=$("#cmsMenuFrame").contents().find('#isparent').val();
					var lid=$("#cmsMainFrame").contents().find("input[name='id']").val();
					if(isparent=='true'){
						$.jBox.tip("该标签下级有分类不能删除!");
						return;
					}
					$.jBox.confirm("确认删除标签吗?(不可恢复)","删除标签",function(v,h,f){
						if(v='ok'){
							$.ajax({
								type 	: 	"POST",   
		        				url 	: 	'${ctx}/cms/batchdeleteTag',
		        				data 	:	{
		        								id:lid
		        							},
								success : 	function(data){
												if(data =='1'){
													location.reload();
													$.jBox.tip("删除成功");
												}
		              		  				},
		         				error 	: 	function(){     
		            							$.jBox.error("Error Transufal", "Error");	
		          							} 
							});
						
						}
					});
				});
				
				//批量审核标签
				$("#cmsMainFrame").contents().find("#examineTag").on("click", function (){
					var list=$("#cmsMenuFrame").contents().find('#taglist').val();
					if(list==""){
						$.jBox.tip("请从左边标签树勾选您要审批的标签!");
						return;
					}
					$.jBox("iframe:${ctx}/tag/treeselect?url=/cms/TagtreeData2?flag=${2}",{ 
          		 	title: "标签列表",  
            		width: 300,  
            		height: 420,
            		top:'20px',  
            		buttons: { '确定':true,'关闭': false},
            		submit:function(v,h,f){
            			if(v==true){
            				//var checked=f.find("input[name='selectTag']:checked").val();
            				//var checked=h.find("#jbox-iframe")[0].contentWindow;
            				//var window=h.find("#jbox-iframe")[0].contentWindow.document;
            				//var obj=$(window).find("input[name=selectTag_1]:checked").serialize();
            				//var alllabel=window.alllabel.value;
            				var tree = h.find("iframe")[0].contentWindow.tree;
            				var nodes = [];
            				nodes = tree.getSelectedNodes();//选择的节点集合
            				$.ajax({
								type 	: 	"POST",   
		        				url 	: 	'${ctx}/cms/pass',
		        				data 	:	{
		        								pid:nodes[0].id,
		        								labellist:list
		        							},
								success : 	function(data){
											if(data=='1'){
												$.jBox.tip("审批成功");
												location.reload();
											}
		              		  				},
		         				error 	: 	function(){     
		            						$.jBox.error("Error Transufal", "Error");	
		          							} 
							});
            			}
            		}  
        	});
        	$('.jbox-content').css('overflow-y','hidden');
				
			});
				
				
			});
		});
	
	</script>
</body>
</html>