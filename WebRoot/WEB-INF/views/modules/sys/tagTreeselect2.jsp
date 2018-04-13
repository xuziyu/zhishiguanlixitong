<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据选择</title>
	<meta name="decorator" content="blank"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
	#search{
		  border-bottom: 1px solid #ccc;
	}
	#tree{
		  padding: 15px 20px;
		  border-right: 1px solid #ccc;
		  width: 170px;
		  height: 296px;
		  overflow-y: auto;
	}
	#selectedLable{
		padding: 5px 0 7px 13px;
		border-top: 1px solid #ccc;
		/*border-bottom: 1px solid #ccc;*/
	}
	#relatedLable{
		  width: 339px;
		  height: 326px;
		 /*  border: 1px solid green;
		  float: right; */
		  position: absolute;
		  top: 50px;
		  left: 210px;
		  overflow-y: auto;
	}
	#relatetitle{
		  padding-top: 10px;
		  padding-left: 8px;
		  padding-bottom: 8px;
		  border-bottom: 1px solid #ccc;
		  font-size: 18px;
	}
	#relate{
	  	margin-left: 12px;
  		margin-right: 12px;
  		
	}
	.lables{
		/*padding-bottom: 5px;*/
		border-bottom: 1px solid #ccc;
	}
	
	.lables .selectedTag{
		position: absolute;
  		right: 69px;
	}
	#selectedLableList{
	  width: 485px;
	  height: 40px;
	  /*padding-top: 5px;*/
	  float: right;
	  overflow-y: auto;
	}
</style>
	<script type="text/javascript">
			/**
		 	* jQuery 扩展方法
			*
		 	* $.Object.count( p )
		 	* 获取一个对象的长度，需要指定上下文，通过 call/apply 调用
		 	* 示例: $.Object.count.call( obj, true );
		 	* @param {p} 是否跳过 null / undefined / 空值
			*
			*/
			$.extend({
			 	// 获取对象的长度，需要指定上下文 this
			 	Object: {
			 		count: function( p ) {
				 		p = p || false;
				 		
				 		return $.map( this, function(o) {
				 		if( !p ) return o;
				 			return true;
				 		} ).length;
					}
				}
			});
		
		// 过滤空值
		//console.log( $.Object.count.call( obj, true ) );
	
	
		var key, lastValue = "", nodeList = [], type = getQueryString("type", "${url}");
		var tree, setting = {view:{selectedMulti:false,dblClickExpand:false},check:{enable:"${checked}",nocheckInherit:true},
				async:{enable:(type==3),url:"${ctx}/sys/user/treeData",autoParam:["id=officeId"]},
				data:{simpleData:{enable:true}},callback:{<%--
					beforeClick: function(treeId, treeNode){
						if("${checked}" == "true"){
							//tree.checkNode(treeNode, !node.checked, true, true);
							tree.expandNode(treeNode, true, false, false);
						}
					}, --%>
					//Ztree节点点击事件监听，异步加载数据
					onClick:function(event, treeId, treeNode){
						tree.expandNode(treeNode);
						//alert(treeNode.deflg);
						var selectall=document.getElementById("selectall");
						selectall.checked = false;
						$("#relatelables").empty();
						$.ajax({ 
			          			     type : "POST",   
			          			     url : '${ctx_a}/cms/relatedlable',
			          			     data : {
			            				   id : treeNode.id
			          			},
			          			 dataType : "json",
			          			 success : function(data){
			          			 	 var flg1=0;
				          			 if(treeNode.deflg==0){
				          			 	var size = $.Object.count.call( $(".lablelist"), true );
				         				//正常标签 //已选标签队列不为空
				        				if(size!=0){
					          			 	$(".lablelist").each(function(){
					          			 		//遍历匹配已选标签队列，匹配成功，标记置为1,跳出循环
					          			 		if(treeNode.id==$(this).val()){
					          			 			flg1=1;
					          			 			$("#relatelables").append("<p class='lables'><span class='lablename'>"+treeNode.name+"</span><input checked='checked' name='selectTag' class='selectedTag' type='checkbox'  value="+treeNode.id +"  labelname="+treeNode.name+" delFlag="+treeNode.deflg+" /></p>");
					          			 			return false; //跳出循环
					          			 		}
					          			 	});
					          			 	//遍历匹配已选标签队列，匹配不成功，判断标记,执行添加元素
					          			 	if(flg1==0){
					          			 		$("#relatelables").append("<p class='lables'><span class='lablename'>"+treeNode.name+"</span><input  name='selectTag' class='selectedTag' type='checkbox'  value="+treeNode.id +"  labelname="+treeNode.name+" delFlag="+treeNode.deflg+" /></p>");
					          			 		
					          			 	}
					          			//已选标签队列为空
				          			 	}else{
				          			 		$("#relatelables").append("<p class='lables'><span class='lablename'>"+treeNode.name+"</span><input  name='selectTag' class='selectedTag' type='checkbox'  value="+treeNode.id +"  labelname="+treeNode.name+" delFlag="+treeNode.deflg+" /></p>");
				          			 		 
				          			 	}
				          			 //待审核	标签
				          			 }else if(treeNode.deflg==1){
				          			 	var size = $.Object.count.call( $(".lablelist"), true );
				         				//已选标签队列不为空
				        				if(size!=0){
					          			 	$(".lablelist").each(function(){
					         					//遍历匹配已选标签队列，匹配成功，标记置为1,跳出循环
					          			 		if(treeNode.id==$(this).val()){
					          			 			flg1=1;
				          			 				$("#relatelables").append("<p class='lables'><span class='lablename' style='color:blue;' title='待审核' >"+treeNode.name+"</span><input checked='checked' name='selectTag' class='selectedTag' type='checkbox'  value="+treeNode.id +"  labelname="+treeNode.name+" delFlag="+treeNode.deflg+" /></p>");
					          			 			return false; //跳出循环
					          			 		}
					          			 	});
					          			 	//遍历匹配已选标签队列，匹配不成功，判断标记,执行添加元素
					          			 	if(flg1==0){
				          			 			$("#relatelables").append("<p class='lables'><span class='lablename' style='color:blue;' title='待审核' >"+treeNode.name+"</span><input name='selectTag' class='selectedTag' type='checkbox'  value="+treeNode.id +"  labelname="+treeNode.name+" delFlag="+treeNode.deflg+" /></p>");
					          			 	}
					          			//已选标签队列为空
				          			 	}else{
				          			 		$("#relatelables").append("<p class='lables'><span class='lablename' style='color:blue;' title='待审核' >"+treeNode.name+"</span><input name='selectTag' class='selectedTag' type='checkbox'  value="+treeNode.id +"  labelname="+treeNode.name+" delFlag="+treeNode.deflg+" /></p>"); 
				          			 	}
				          			 }
				          			 
				          			//遍历ajx获取到的后台数据
			          			  	$.each(data,function(idx,item){
			          			  		//审核通过 
			          			  		if(data[idx].deflg==0){
			          			  			var falg2 = 0;
			          			  			$(".lablelist").each(function(){
			          			  				//遍历匹配已选标签队列，匹配成功，标记置为1,跳出循环
			          			  				if(data[idx].labelid==$(this).val()){
			          			  					//已选
			          			  					falg2=1;
			          			  					$("#relatelables").append("<p class='lables'><span class='lablename'>"+data[idx].labelname+"</span><input name='selectTag' checked='checked' class='selectedTag' type='checkbox'  value="+data[idx].labelid +"  labelname="+data[idx].labelname+" delFlag="+data[idx].deflg+" /></p>");
			          			  				}
			          			  			});
			          			  			
			          			  			if(falg2==0){
			          			  				$("#relatelables").append("<p class='lables'><span class='lablename'>"+data[idx].labelname+"</span><input name='selectTag'  class='selectedTag' type='checkbox'  value="+data[idx].labelid +"  labelname="+data[idx].labelname+" delFlag="+data[idx].deflg+" /></p>");
			          			  			}
			          			  		}else{
			          			  		//待审核
			          			  			var falg3 = 0;
			          			  			$(".lablelist").each(function(){
			          			  				if(data[idx].labelid==$(this).val()){
			          			  					//已选
			          			  					falg3=1;
			          			  					$("#relatelables").append("<p class='lables'><span class='lablename' style='color:blue;' title='待审核' >"+data[idx].labelname+"</span><input checked='checked' name='selectTag' class='selectedTag' type='checkbox'  value="+data[idx].labelid +"  labelname="+data[idx].labelname+" delFlag="+data[idx].deflg+" /></p>");
			          			  				}
			          			  			});
			          			  			if(falg3==0){
			          			  					$("#relatelables").append("<p class='lables'><span class='lablename' style='color:blue;' title='待审核' >"+data[idx].labelname+"</span><input  name='selectTag' class='selectedTag' type='checkbox'  value="+data[idx].labelid +"  labelname="+data[idx].labelname+" delFlag="+data[idx].deflg+" /></p>");
			          			  			}
			          			  		}
			          			  	});
			          			  	
			          			  	
			          			},
			          			 error : function(jqXHR){     
			            				alert("发生错误：" + jqXHR.status); 
			          			}    
			        	});
					},onCheck: function(e, treeId, treeNode){
						var nodes = tree.getCheckedNodes(true);
						for (var i=0, l=nodes.length; i<l; i++) {
							tree.expandNode(nodes[i], true, false, false);
						}
						return false;
					},onAsyncSuccess: function(event, treeId, treeNode, msg){
						var nodes = tree.getNodesByParam("pId", treeNode.id, null);
						for (var i=0, l=nodes.length; i<l; i++) {
							try{tree.checkNode(nodes[i], treeNode.checked, true);}catch(e){}
							//tree.selectNode(nodes[i], false);
						}
						selectCheckNode();
					},onDblClick: function(){//<c:if test="${!checked}">
						$.jBox.getBox().find("button[value='ok']").trigger("click");
						//$("input[type='text']", top.mainFrame.document).focus();//</c:if>
					}
				}
			};
		function expandNodes(nodes) {
			if (!nodes) return;
			for (var i=0, l=nodes.length; i<l; i++) {
				tree.expandNode(nodes[i], true, false, false);
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				}
			}
		}
		$(document).ready(function(){
			$.get("${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}extId=${extId}&isAll=${isAll}&module=${module}&t="
					+ new Date().getTime(), function(zNodes){
				// 初始化树结构
				tree = $.fn.zTree.init($("#tree"), setting, zNodes);
				
				// 默认展开一级节点
				var nodes = tree.getNodesByParam("level", 0);
				for(var i=0; i<nodes.length; i++) {
					tree.expandNode(nodes[i], true, false, false);
					//更改图标样式 add hefeng
					nodes[i].iconSkin="diy";
					tree.updateNode(nodes[i]);
				}
				//获取二级节点，更改图标样式 add hefeng
				var nodeslevel1=tree.getNodesByParam("level", 1);
				for(var i=0; i<nodeslevel1.length; i++) {
					nodeslevel1[i].iconSkin="diy";
					tree.updateNode(nodeslevel1[i]);
				}
				//异步加载子节点（加载用户）
				var nodesOne = tree.getNodesByParam("isParent", true);
				for(var j=0; j<nodesOne.length; j++) {
					tree.reAsyncChildNodes(nodesOne[j],"!refresh",true);
				}
				
				selectCheckNode();
			});
			key = $("#key");
			key.bind("focus", focusKey).bind("blur", blurKey).bind("change cut input propertychange", searchNode);
			key.bind('keydown', function (e){if(e.which == 13){searchNode();}});
			setTimeout("search();", "300");
		});
		
		
		// 默认选择节点
		function selectCheckNode(){
			var ids = "${selectIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", (type==3?"u_":"")+ids[i]);
				if("${checked}" == "true"){
					try{tree.checkNode(node, true, true);}catch(e){}
					tree.selectNode(node, false);
				}else{
					tree.selectNode(node, true);
				}
			}
		}
	  	function focusKey(e) {
			if (key.hasClass("empty")) {
				key.removeClass("empty");
			}
		}
		function blurKey(e) {
			if (key.get(0).value === "") {
				key.addClass("empty");
			}
			searchNode(e);
		}
		
		//搜索节点
		function searchNode() {
			// 取得输入的关键字的值
			var value = $.trim(key.get(0).value);
			
			// 按名字查询
			var keyType = "name";<%--
			if (key.hasClass("empty")) {
				value = "";
			}--%>
			
			// 如果和上次一次，就退出不查了。
			if (lastValue === value) {
				return;
			}
			
			// 保存最后一次
			lastValue = value;
			
			var nodes = tree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				return;
			}
			hideAllNode(nodes);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			updateNodes(nodeList);
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				tree.hideNode(nodes[i]);
			}
		}
		
		//显示所有节点
		function showAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes[i].getParentNode()!=null){
						tree.expandNode(nodes[i],false,false,false,false);
					}else{
						tree.expandNode(nodes[i],true,true,false,false);
					}
					tree.showNode(nodes[i]);
					showAllNode(nodes[i].children);
				/* } */
			}
		}
		
		//更新节点状态
		function updateNodes(nodeList) {
			tree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				
				//展开当前节点的父节点
				tree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					tree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					tree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				tree.showNode(nodeList[i].getParentNode());
				//展开根节点
				tree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
		
		// 开始搜索
		function search() {
			$("#search").slideToggle(200);
			$("#txt").toggle();
			$("#key").focus();
		}
		
		
	</script>
</head>
<body>
	<%-- 
	<div style="position:absolute;right:8px;top:5px;cursor:pointer;" onclick="search();">
		<i class="icon-search"></i><label id="txt">搜索</label>
	</div>
	--%>
	<div id="search" class="form-search hide" style="padding:10px 0 0 13px;">
		<label for="key" class="control-label" style="padding:5px 5px 3px 0;color:#1489c9;font-size:20px;">请选择标签</label>
		<input type="text" class="empty" id="key" name="key" maxlength="50" style="width:110px;  margin-bottom: 10px;">
		<button class="btn" id="btn" onclick="searchNode()" style=" margin-bottom: 10px;">搜索</button>
	</div>
	
	<div id="tree" class="ztree"></div>
	<div id="relatedLable">
		<p id="relatetitle">关联标签 </p>
		<div id="relate">
			<p class="lables">
				<span class="lablename">标签</span>
				<input id="selectall" name='selectTag' type='checkbox' value="" onclick="selectAll()"  style="  position: relative;margin-left: 212px;"/>
				<span >全选</span>
			</p>
			<span id="relatelables">
			<!--  	<p class="lables">
					<span class="lablename">标签BB</span>
					<input name='selectTag' class='selectedTag' type='checkbox' value="1"  labelname="标签BB" delFlag="0"/>
				</p>
				<p class="lables">
					<span class="lablename">标签C</span>
					<input name='selectTag' class='selectedTag' type='checkbox' value="2" labelname="标签C" delFlag="0"/>
				</p>
			-->
			</span>
		</div>
	</div>
	<div id="selectedLable">
		<label class="control-label" style="padding:5px 5px 3px 0;color:#1489c9;font-size:20px;">已选</label>
		<p id="selectedLableList">
			<c:forEach items="${userlabellist}" var="userlabel">
				<c:if test="${userlabel.ischecked==1}">
				<c:choose>
					<c:when test="${userlabel.delFlag==0}">
						<span  style='margin-right:8px;' ><input name='selectTag_1' class='lablelist' delFlag='${userlabel.delFlag}' type='checkbox' value="${userlabel.labelid}" lablename="${userlabel.labelvalue}" checked='checked' />${userlabel.labelvalue}</span>  
					</c:when>
					<c:when test="${userlabel.delFlag==1}">
						<span  style='margin-right:8px;' ><input name='selectTag_1' class='lablelist' delFlag='${userlabel.delFlag}' type='checkbox' value="${userlabel.labelid}" lablename="${userlabel.labelvalue}" checked='checked' /><span style="color:blue;" title='待审核'>${userlabel.labelvalue}</span></span>  
					</c:when>
				</c:choose>
					<!--  
					选择<input name="selectTag_1" type="checkbox" checked="checked" labelvalue1="${userlabel.labelvalue}" delFlag="${userlabel.delFlag}" value="${userlabel.labelid}"/>
					-->
				</c:if>
			</c:forEach>
		</p>
	</div>
<script>
		//checkbox点击事件监听
		$('body').on('click' , '.selectedTag' , function(){ 
			if($(this).is(':checked')){
		        	//选中标签，添加标签到已选队列
		        	var labelname = $(this).attr("labelname");
		        	var labelid = $(this).val();
		        	if($(this).attr("delFlag")==0){
		        		$("#selectedLableList").append("<span  style='margin-right:8px;' ><input name='selectTag_1' class='lablelist' delFlag='0' type='checkbox' value="+$(this).val() +" lablename="+$(this).attr('labelname')+" checked='checked' />"+$(this).attr('labelname')+"</span>");	
		        	}
		        	if($(this).attr("delFlag")==1){
		        		$("#selectedLableList").append("<span  style='margin-right:8px;' ><input name='selectTag_1' class='lablelist' delFlag='1' type='checkbox' value="+$(this).val() +" lablename="+$(this).attr('labelname')+" checked='checked' /><span style='color:blue;' title='待审核'>"+$(this).attr('labelname')+"</span></span>");	
		        	}
		        }else{
		        	//取消选中标签，遍历已选标签队列，移除对应的标签
		        	var id = $(this).val();
		        	$(".lablelist").each(function(){
		        		if(id==$(this).val()){
		        			$(this).parent().remove();
		        		}
		        	});
		        }    
		});	
		
		//无效
		$(document).ready(function(){
				var obj = $("#mainFrame").contents().find("input[name=selectTag]");
				for(k in obj){
					//alert(obj[k].val()+":"+obj[k].checked);
					if(obj[k].checked){
						var v1 = obj[k];
						var v = $(v1);
						if($(v).attr('delFlag')==0){
							
							$("#selectedLableList").append("<input name='selectTag' class='selectedTag'   type='checkbox' value=" +obj[k].value + " checked='checked' />" +$(v).attr('lablename'));								
						}else{
							$("#selectedLableList").append("<input name='selectTag' class='selectedTag' type='checkbox' value="+obj[k].value +" checked='checked' /><span class='blueTag' title='待审核'>"+$(v).attr("labelvalue1")+"</span>");
						}
					}
				}
		});
		
		//全选关联标签
		function selectAll(){
			
        	var selectall=document.getElementById("selectall");
        	if(selectall.checked){
        		var select=document.getElementsByName("selectTag");
        		for(var i=0;i<select.length;i++){
        			select[i].checked = true;
        		}
        		//全选后清空已选标签队列
        		//$("#selectedLableList").empty();
        		//遍历把所有的标签元素加入标签队列
        	  	$(".selectedTag").each(function(){
        	  		var selectTagid = $(this).val();
        	  		var deflag4=0;
        	  		$(".lablelist").each(function(){
        	  			if(selectTagid!=$(this).val()){
      						deflag4=1;
        	  			}else{
        	  				deflag4=2;
        	  				return false; //跳出循环
        	  			}
        	  		});
        	  		//遍历已选标签队列，不在已选队列，或者已选队列为零，未遍历，追加
        	  		if(deflag4==1||deflag4==0){
        	  			//追加正常标签
        	  			if($(this).attr("delFlag")==0){
        	  				$("#selectedLableList").append("<span  style='margin-right:8px;' ><input name='selectTag_1' class='lablelist' delFlag='0' type='checkbox' value="+$(this).val() +" lablename="+$(this).attr('labelname')+" checked='checked' />"+$(this).attr('labelname')+"</span>");	
        	  			}
        	  			//追加待审核标签
        	  			if($(this).attr("delFlag")==1){
        	  				$("#selectedLableList").append("<span  style='margin-right:8px;' ><input name='selectTag_1' class='lablelist' delFlag='0' type='checkbox' value="+$(this).val() +" lablename="+$(this).attr('labelname')+" checked='checked' /> <span style='color:blue;' title='待审核'>"+$(this).attr('labelname')+"</span></span>");	
        	  			}
        	  		}
        	  	});	
        	}else{
        		var select=document.getElementsByName("selectTag");
        		for(var i=0;i<select.length;i++){
        			select[i].checked = false;
        		}
        		//反全选后清空已选标签队列
        		//$("#selectedLableList").empty();	
        		$(".selectedTag").each(function(){
	        	  	var selectTagid = $(this).val();
	        	  	var deflag4=0;
	        	  	$(".lablelist").each(function(){
	        	  		if(selectTagid!=$(this).val()){
	      					deflag4=1;
	        	  		}else{
	        	  			//遍历已选标签队列，在已选队列，删除
	        	  			$(this).parent().remove();
	        	  			deflag4=2;
	        	  			return false; //跳出循环
	        	  		}
	        	  	});
        	  	});
        	}	
       }
</script>

</body>