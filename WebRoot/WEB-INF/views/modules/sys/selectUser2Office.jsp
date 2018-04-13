<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="blank"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$.ajaxSetup({ cache: false });
	
	
		var officeTree;
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			var key=$("#key");
			//key.on("change cut input propertychange", searchNode);
			key.on('keydown', function (e){if(e.which == 13){searchNode();}});
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("officeTree"==treeId){
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id, function(userNodes){
					var userTree_setting={
							view: {selectedMulti:false,nameIsHTML:true,showTitle:true,dblClickExpand:false},
							data: {simpleData: {enable: true},key:{title:"officename"}},
							callback: {onClick: treeOnClick},
							check:{chkStyle:"checkbox",enable:true}};
					$.fn.zTree.init($("#userTree"), userTree_setting, userNodes);
				});
			}
			if("userTree"==treeId){
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				var tree=$.fn.zTree.getZTreeObj("userTree");
				//勾选当前点击的节点。
				tree.checkNode(treeNode,true,false);
				/*此时知识勾选，
				if($.inArray(String(treeNode.id), ids)<0){
					//selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}*/
				
			};
 			if("selectedTree"==treeId){
				//if($.inArray(String(treeNode.id), pre_ids)<0){
				selectedTree.removeNode(treeNode);
				ids.splice($.inArray(String(treeNode.id), ids), 1);
				//}else{
				//	$.jBox.tip("部门原有成员不能清除！", 'info');
				//}
			}
		};
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "未给部门【${office.name}】分配新成员！";
					}else{
						tips = "已选人员清除成功！";
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
			tips="确定清除部门【${role.name}】下的已选人员？";
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
			//var url="${ctx}/sys/role/findcategory?username="+filterString;
			var url="${ctx}/sys/role/findcategory?username="+encodeURI(filterString);
			
			var values=[];
			$.ajax({
				type:"GET",
				dataType:"json",
				url:url,
				async:false,
				success:function(data){
				if(data){
				//通过输入的员工名字，得到可能的部门id集合。
					for(var i=0;i<data.length;i++){
						values.push(data[i]);
					}
				}
				}
			});
			var username={name:filterString};
			$.ajax({
				type:"GET",
				dataType:"json",
				url:"${ctx}/sys/role/getSearchNodes",
				data:jQuery.param(username),
				success:function(data){
				//data就是新的treenode
					if(data){
					//先销毁已存在的待选人员列表。
						$.fn.zTree.destroy("userTree");
							var userTree_setting={
							view: {selectedMulti:false,nameIsHTML:true,showTitle:true,dblClickExpand:false},
							data: {simpleData: {enable: true},key:{title:"officename"}},
							callback: {onClick: treeOnClick},
							check:{chkStyle:"checkbox",enable:true}};
						$.fn.zTree.init($("#userTree"), userTree_setting, data);
					}
				}
			});
			var nodes = officeTree.getNodes();
			if (filterString == "") {
				showAllNode(nodes);
				return;
			}
			if(values!=""){
				hideAllNode(nodes);
				for(var i in values){
				//更新左边的部门树。根据筛选出来的id集合。
					updateNodes(officeTree.getNodesByParam("id",values[i], null));
				}
				//更新userTree的状态
				
				
			}else{
				//$.jBox.tip("没有该人员");
			}
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){			
			nodes = officeTree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				officeTree.hideNode(nodes[i]);
			}
		}
		
		//显示所有节点
		function showAllNode(nodes){			
			nodes = officeTree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes[i].getParentNode()!=null){
						officeTree.expandNode(nodes[i],false,false,false,false);
					}else{
						officeTree.expandNode(nodes[i],true,true,false,false);
					}
					officeTree.showNode(nodes[i]);
					showAllNode(nodes[i].children);
				/* } */
			}
		}
		
		//更新节点状态
		function updateNodes(nodeList) {
			officeTree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				
				//展开当前节点的父节点
				officeTree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					officeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					officeTree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				officeTree.showNode(nodeList[i].getParentNode());
				//展开根节点
				officeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
		function sure(){
			var tree=$.fn.zTree.getZTreeObj("userTree");
			//拿到所有选中的节点。
			var nodes=tree.getCheckedNodes();
			var lastNodes=[];
			for(var i in nodes){
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
			var tree=$.fn.zTree.getZTreeObj("userTree");
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
.rb{
	position: fixed;
    right: 200px;
    bottom: 50px;
}
</style>
	<div id="assignRole" class="row-fluid span12">
		<div class="span6" style="border-right: 1px solid #A8A8A8;width:290px; height:270px;overflow:auto">
			
			<p>所在部门：</p>
			<div id="officeTree" class="ztree" style=""></div>
		</div>
		<div class="span2" style=" border-right: 1px solid #A8A8A8;width:290px; height:270px;overflow:auto;position:relative"> 
			<div id="search" class="form-search hide" style="padding: 10px 0px 0px 13px; display: block;">
		<label for="key" class="control-label" style="padding:5px 5px 3px 0;">关键字：</label>
		<input type="text" class="" id="key" name="key" maxlength="50" style="width:110px;">
		<button class="btn" id="btn" onclick="searchNode()">搜索</button>
		</div>
			<p>待选人员：</p>
			<div id="userTree" class="ztree" ></div>
			<div class="rb">
			<label><input type="checkbox" id="checkbox">全选<label>&nbsp;&nbsp;
			<a href="javascript:" onclick="sure()">确定</a>
			</div>
		</div>
		<div class="span3" style="width:120px; height:270px;overflow:auto " >
			<p>已选人员：</p>
			<div id="selectedTree" class="ztree" ></div>
		</div>
	</div>
</body>
</html>
