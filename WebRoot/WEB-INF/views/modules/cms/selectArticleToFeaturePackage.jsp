<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加知识</title>
	<meta name="decorator" content="blank"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		var selectedTree;//zTree已选择对象
	
		
		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		
		// 初始化
		$(document).ready(function(){
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			var key=$("#key");
			//key.on("change cut input propertychange", searchNode);
			key.on('keydown', function (e){if(e.which == 13){searchNode();}});
			
		});

		var pre_selectedNodes =[
   		        <c:forEach items="${articleList}" var="article">
   		        {id:"${article.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${article.title}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${articleList}" var="article">
		        {id:"${article.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${article.title}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "未给专题【${featurePackage.name}】分配新成员！";
					}else{
						tips = "已选知识清除成功！";
					}
					ids=pre_ids.slice(0);
					selectedNodes=pre_selectedNodes;
					$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			    	$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	$.jBox.tip("取消清除操作！", 'info');
			    }
			    return true;
			};
			tips="确定清除专题包【${featurePackage.name}】下的已选知识？";
			$.jBox.confirm(tips, "清除确认", submit);
		};
		//模糊查询
		var lastValue;
		function searchNode(){
			var filterString=$.trim($("#key").val());
			// 如果和上次一次，就退出不查了。
			if (lastValue === filterString) {
				return;
			}
			// 保存最后一次
			lastValue = filterString;
			var searchParam={searchParam:filterString};
			$.ajax({
				type:"GET",
				dataType:"json",
				url:"${ctx}/cms/featurePackage/getSearchNodes2",
				data:jQuery.param(searchParam),
				success:function(data){
				//data就是新的treenode
					if(data){
						//先销毁已存在的待选人员列表。
						console.log(data);
						//先销毁已存在的待选人员列表。
						$.fn.zTree.destroy("articleTree");
							var articleTree_setting={
							view: {selectedMulti:false,nameIsHTML:true,showTitle:true,dblClickExpand:false},
							data: {simpleData: {enable: true},key:{title:"title"}},
							callback: {onClick: treeOnClick,onDblClick:treeOnDblClick},
							check:{chkStyle:"checkbox",enable:true}};
						$.fn.zTree.init($("#articleTree"), articleTree_setting, data);
						
					}
				}
			});
		}
		
		//双击事件
		function treeOnDblClick(event, treeId, treeNode){
			var id=treeNode.id;
			var categroryId=treeNode.categoryId;
			window.open("${ctx_f}/view-"+categroryId+"-"+id+".html");
		}	


		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("articleTree"==treeId){
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				var tree=$.fn.zTree.getZTreeObj("articleTree");
				//勾选当前点击的节点。
				tree.checkNode(treeNode,true,false);
				/*此时知识勾选，
				if($.inArray(String(treeNode.id), ids)<0){
					//selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}*/
			};
			
			if("selectedTree"==treeId){
				if($.inArray(String(treeNode.id), pre_ids)<0){
					selectedTree.removeNode(treeNode);
					ids.splice($.inArray(String(treeNode.id), ids), 1);
				}else{
					$.jBox.tip("整体原有知识不能清除！", 'info');
				}
			}
		}
		
		
		function sure(){
			var tree=$.fn.zTree.getZTreeObj("articleTree");
			//拿到所有选中的节点。
			var nodes=tree.getCheckedNodes();
			var lastNodes=[];
			for(var i in nodes){
				/* lastNodes.push(nodes[i]); */
			//如果没有在ids中,新选中的额外节点再存放到ids中。
				if($.inArray(String(nodes[i].id),ids)<0){
					ids.push(String(nodes[i].id));
					lastNodes.push(nodes[i]);
				}
			}
			//添加过滤后的所有节点。
			selectedTree.addNodes(null,lastNodes);
		}
		$(function(){
		//点击全选，勾选或者取消全部节点
			$("#checkbox").on("click",function(){
				var tree=$.fn.zTree.getZTreeObj("articleTree");
				if($(this).attr("checked")){
					tree.checkAllNodes(true);
				}else{
					tree.checkAllNodes(false);
				}
			});
		})
		
	</script>
</head>
<body>
<style>
.rb_addAtricle{
	position: fixed;
    bottom: 25px;
    left:300px;
}
</style>
	<div id="assignRole" class="row-fluid span12">
		<div class="span2" style=" border-right: 1px solid #A8A8A8;width:400px; height:270px;overflow:auto;position:relative"> 
			<div id="search" class="form-search hide" style="padding: 10px 0px 0px 13px; display: block;">
		<label for="key" class="control-label" style="padding:5px 5px 3px 0;">关键字：</label>
		<input type="text" class="" id="key" name="key" maxlength="50" style="width:110px;">
		<button class="btn" id="btn" onclick="searchNode()">搜索</button>
		</div>
			<p>待选知识：</p>
			<div id="articleTree" class="ztree" ></div>   
    
			<div class="rb_addAtricle">
				<label><input type="checkbox" id="checkbox">全选<label>&nbsp;&nbsp;
				<a href="javascript:" onclick="sure()">确定</a>
			</div>
		</div>
		<div class="span3" style="width:380px; height:270px;overflow:auto " >
			<p>已选知识：</p>
			<div id="selectedTree" class="ztree" ></div>
		</div>
	</div>
</body>
</html>
