<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>知识库列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}<%--
		.ztree li span.button.level0, .ztree li a.level0 {display:none;height:0;}
		.ztree li ul.level0 {padding:0;background:none;}--%>
		.accordion-inner{padding:2px;}
	</style>
	<!--huangmj 2015 11 10点击后台管理，默认显示顶级知识列表 替换[${not empty tpl.module?tpl.module:'none'}]为article-->
	<script type="text/javascript">
		$(document).ready(function(){
			var setting = {view:{selectedMulti:false},data:{simpleData:{enable:true}}};
			var zNodes=[
		            <c:forEach items="${categoryList}" var="tpl">
		            	{id:'${tpl.id}', pId:'${not empty tpl.parent?tpl.parent.id:0}', name:"${tpl.name}", url:"${ctx}/cms/article/?category.id=${tpl.id}", target:"cmsMainFrame"},
		            </c:forEach>];
			for(var i=0; i<zNodes.length; i++) {
				// 移除父节点
				if (zNodes[i] && zNodes[i].id == 1){
					zNodes.splice(i, 1);
				}<%--
				// 并将没有关联关系的父节点，改为父节点
				var isExistParent = false;
				for(var j=0; j<zNodes.length; j++) {
					if (zNodes[i].pId == zNodes[j].id){
						isExistParent = true;
						break;
					}
				}
				if (!isExistParent){
					zNodes[i].pId = 1;
				}--%>
			}
			// 初始化树结构
			var tree = $.fn.zTree.init($("#tree"), setting, zNodes);
			<%--
			// 展开第一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, true, false);
				//更改图标样式 add hefeng
				nodes[i].iconSkin="diy";
				tree.updateNode(nodes[i]);
			}
			// 展开第二级节点
			nodes = tree.getNodesByParam("level", 1);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, true, false);
				//更改图标样式 add hefeng
				nodes[i].iconSkin="diy";
				tree.updateNode(nodes[i]);
			}
			--%>
			wSize();
		});
		
		$(window).resize(function(){
			wSize();
		});
		function wSize(){
			$(".ztree").width($(window).width()-16).height($(window).height()-72);
			$(".ztree").css({"overflow":"auto","overflow-x":"auto","overflow-y":"auto"});
			$("html,body").css({"overflow":"hidden","overflow-x":"hidden","overflow-y":"hidden"});
		}
		
	</script>
</head>
<body>
	<div class="accordion-group">
	    <div class="accordion-heading">
	    	<a class="accordion-toggle">请选择知识分类</a>
	    </div>
	    
	    <a id="delFlag_2"  style="visibility:hidden" class="level0 curSelectedNode" treenode_a="" onclick="" href="${ctx}/cms/article/?article.delFlag=2" target="cmsMainFrame" style="" title="知识库">
	    	<span id="tree_6_ico" title="" treenode_ico="" class="button ico_open" style=""></span>
	    	<span id="tree_6_span">知识库待审核</span>
	    </a>
	    
	    <div class="accordion-body">
			<div class="accordion-inner">
				<div id="tree" class="ztree"></div>
			</div>
	    </div>
	</div>
	<!-- 知识管理界面，默认显示根结点下的知识，不显示空 hungmj 2015.10.29 -->
	<script type="text/javascript">
		$(document).ready(function(){
			document.getElementById("delFlag_2").click();
		});
	</script>
</body>
</html>