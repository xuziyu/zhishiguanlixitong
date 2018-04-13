<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="isAll" type="java.lang.Boolean" required="false" description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<div class="input-append">
	<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}"/>
	<input id="${id}Name" name="${labelName}" ${allowInput?'':'readonly="readonly"'} type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" title="${labelValue}" style="${cssStyle}"/><a id="${id}Button" href="javascript:" class="btn ${disabled} ${hideBtn ? 'hide' : ''}" style="${smallBtn?'padding:4px 2px;':''}">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
</div>
<script type="text/javascript">
	$("#${id}Button, #${id}Name").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		//$.jBox.defaults = {  
    	//	top: '20px' /* 窗口离顶部的距离,可以是百分比或像素(如 '100px') */ 
    	//	};
		// 正常打开
		$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("${url}")+"&module=${module}&checked=${checked}&extId=${extId}&isAll=${isAll}", "<span style='font-size: 16px;'>选择${title}</span>", 300, 420, {
			
			ajaxData:{selectIds: $("#${id}Id").val()},top:'20px',buttons:{"确定":"ok", ${allowClear?"\"清除\":\"clear\", ":""}"关闭":true}, submit:function(v, h, f){
				if (v=="ok"){
					var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
					var ids = [], names = [], nodes = [];
					if ("${checked}" == "true"){
						nodes = tree.getCheckedNodes(true);
					}else{
						nodes = tree.getSelectedNodes();
					}
					for(var i=0; i<nodes.length; i++) {//<c:if test="${checked && notAllowSelectParent}">
						if (nodes[i].isParent){
							continue; // 如果为复选框选择，则过滤掉父节点
						}//</c:if><c:if test="${notAllowSelectRoot}">
						if (nodes[i].level == 0){
							$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
							return false;
						}//</c:if><c:if test="${notAllowSelectParent}">
						if (nodes[i].isParent){
							$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
							return false;
						}//</c:if><c:if test="${not empty module && selectScopeModule}">
						if (nodes[i].module == ""){
							$.jBox.tip("不能选择知识库（"+nodes[i].name+"）请重新选择。");
							return false;
						}else if (nodes[i].module != "${module}"){
							$.jBox.tip("不能选择当前栏目以外的知识分类，请重新选择。");
							return false;
						}//</c:if>
						ids.push(nodes[i].id);
						names.push(nodes[i].name);//<c:if test="${!checked}">
						break; // 如果为非复选框选择，则返回第一个选择  </c:if>
					}
					$("#${id}Id").val(ids.join(",").replace(/u_/ig,""));
					$("#${id}Name").val(names.join(","));
					var categoryid=$('#categoryId').val();
					$(".selectedTags").empty();
					
					 /**   
    					  @author luqibao 
    				      @date 2016/8/31 
    				      @description  三期需求  选择分类的时候  不能将所有的标签都去掉
    				      				只能去掉原有的分类关联  但是用户自己选择的标签不需要
    				      				去掉
    				**/
					var articleId=$("#current_article_id").val();
					var tempCategoryId=$("#tempCategoryId").val();
					var articleLabels=new Array();					
					if(articleId){
							//获取知识的所有标签
	    				$.ajax({
							type : "POST",   
			        		url : '${ctx_a}/cms/article/articleLabels',
			        		data :{
			        			articleId: articleId
			        		},
			        		async:false,
			        		success : function(data){
			        			for(var i=0;i<data.length;i++){
			        				articleLabels.push(data[i]);
			        			}
			              	},
			         		error : function(){     
			            		console.log("获取分类关联标签失败");
			         		} 
						});
					}
    				
					//获取之前的分类标签
    				if(tempCategoryId){
    					$.ajax({
							type : "POST",   
		        			url : '${ctx_a}/cms/category/categoryLabel',
		        			data :{
		        				categoryid: tempCategoryId
		        			},
		        			async:false,
		        			success : function(data){
		        				for(var i=0;i<data.length;i++){
		        					//将那些属于分类的标签都去掉  留下那些用户手动添加的
		        					for(var j=0;j<articleLabels.length;j++){
		        						if(data[i].id==articleLabels[j].id){
		        							articleLabels.splice(j,1);
		        						}
		        					}
		        				}
		              		},
		         			error : function(){     
		            			console.log("获取分类关联标签失败");
		         			} 
						});
    				}
    				
    				for(var i=0;i<articleLabels.length;i++){
    					$(".selectedTags").append("<span class='delTags'><input name='selectTag' delFlag='0' class='selectedTag'   type='checkbox' value=" +articleLabels[i].id + " lablename="+articleLabels[i].labelvalue+" checked='checked'  />" +articleLabels[i].labelvalue+"</span>");
    				}
    				
    				/**end*/
    				
					$.ajax({
						type : "POST",   
		        		url : '${ctx_a}/cms/category/categoryLabel',
		        		data :{
		        			categoryid: categoryid
		        		},
		        		success : function(data){
							$.each(data,function(i,item){
								$(".selectedTags").append("<input name='selectTag' delFlag='0' class='selectedTag categoryTag' disabled='true'  type='checkbox' value=" +item.id + " lablename="+item.labelvalue+" checked='checked'  />" +item.labelvalue);
							});
		              	},
		         		error : function(){     
		            		
		         		} 
					});
					
					$("#tempCategoryId").val(categoryid);
					
				}//<c:if test="${allowClear}">
				else if (v=="clear"){
					$("#${id}Id").val("");
					$("#${id}Name").val("");
                }//</c:if>
				if(typeof ${id}TreeselectCallBack == 'function'){
					${id}TreeselectCallBack(v, h, f);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
		
		$(".jbox-content").css("overflow-y","hidden");
	});
</script>