<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/modules/cms/front/include/head.jsp" %>
<head>
	<title>知识管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#mergeTag").click(function(){
        		var labelid="${label.id}";
				mergeTagjBox(labelid);
				$('.jbox-content').css('overflow-y','hidden');
        	});
        	function mergeTagjBox(labelid){
        		$.jBox("iframe:${ctx}/cms/tagtree2?id="+labelid,{ 
          		 	title: "选择合并目标",  
            		width: 300,  
            		height: 420,
            		top:'20px',  
            		buttons: { '确定':true,'关闭': false},
            		submit:function(v,h,f){
            			if(v==true){
            				confirmx("确认合并标签吗?(不可恢复)",function(){
            					var window=h.find("#jbox-iframe")[0].contentWindow.document;
            					var radiovalue=$(window).find("#label").val();
            					var value=$(window).find("#mergeTag").val();
            					//var alllabel=window.alllabel.value;
            					$.ajax({
									type 	: 	"POST",   
		        					url 	: 	'${ctx}/cms/mergeTag',
		        					data 	:	{
		        								firstlabelid:labelid,
		        								secondlabelid:radiovalue,
		        								newname:value	
		        								},
									success : 	function(data){
													if(data=='1')
														$.jBox.tip("合并名称重名,请重新填写!");
													if(data=='0'){
														$.jBox.tip("合并名称成功!");
														parent.location.reload();
													}
													if(data=='2'){
														$.jBox.tip("请选择第二个需要合并的标签");
													}
		              		  					},
		         					error 	: 	function(){     
		            							$.jBox.error("Error Transufal", "Error");	
		          								} 
									});
							
            					});
            					return false;
            				}else{
            					return true;
            				}
            		}  
        	});
        }
        
		$("#relaTag").click(function(){
					var issys="${label.issys}";
					var delflag="${label.delFlag}";
					if(issys=='2' || delflag=='1'){
						return;
					}
					var selectedTagString = "";
					$("input[name=labellist]").each(function(){
      						selectedTagString = selectedTagString+$(this).attr("value")+".";
      				});
					$.jBox.open(	
				 		"iframe:${ctx_a}/tag/treeselectlable?url="+encodeURIComponent("/cms/TagtreeData2?flag=2")+"&module=article&checked=${checked}&extId=${extId}&isAll=${isAll}&selectedTagString="+selectedTagString, 
				 		"标签列表",550,500,
				 		{id:'Tag',
					 		top:'15px',
		            		buttons: {'确定':true ,'关闭':false},
		            		submit:function(v,h,f){
				            	if(v==true){
				            		var lid="${label.id}";
				            		$("#taglist").empty();
						            var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						            obj = $(doc).find("input[name=selectTag_1]");
									for(k in obj){
								    	if(obj[k].checked){
											var v1 = obj[k];
											var v=$(v1);
											if(lid==obj[k].value){
												$.jBox.tip("不能选择自己!");
												continue;
											}
											var temp="<li><input name='labellist' type='hidden' value="+obj[k].value+" />"+$(v).attr('lablename')+"</li>";
											$('#taglist').append(temp);
										}
									}
					           	}
		            		} 	
	            		}
        			);
        			$('.jbox-content').css('overflow-y','hidden');
		 			$(".jbox-iframe").attr("scrolling","no");		
		});
		
	});
	</script>
	<style>
		.ul li{
			float:left;
			margin-left:25px;
		}
		.clear{ height:0px;font-size:0;clear:both;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs" >
		 <li class="active"><a href="${ctx}/cms/TagControl">标签详情</a></li>
		 <li><a href="${ctx_a}/cms/addTag">增加标签</a></li>
	</ul>
	<sys:message2 content="${message}"/>
		<form action="${ctx_a}/cms/save" method="post" class="breadcrumb form-search">
		
		<!-- 自主增加的标签 -->
		<c:if test="${label.issys =='1'}">
		<div class="form-actions">
		<label class="control-label">标签名称：
		</label>
		<input id="labelvalue" name="labelvalue" maxlength="10" style="width:182px;" value="${label.labelvalue}"/>&nbsp;
		<input type="hidden" name="id" value="${label.id}"/>
		<input type="hidden" name="issys" value="${label.issys}"/>
		<label>上级分类：
		</label><sys:treeselect id="pid" name="pid" value="${label.pid}" labelName="pname" labelValue="${label.pname}"
					title="标签列表" url="/cms/TagtreeData2?flag=${2}&pid=${label.pid}" module="label" notAllowSelectRoot="false" cssClass="input-small" extId="待审核"/>
		</div>
		<div class="control-group">&nbsp;&nbsp;&nbsp;&nbsp;
		<label class="control-label">关联标签：
		</label> 
		<div class="input-append" id="relaTag">
			<input class="input-small" type="text" />
			<a class="btn" href="#">
				<i class="icon-search"></i>
			</a>
		</div>	
			<p></p>
			<ul class="ul" id="taglist" name="taglist">
				<c:forEach items="${labellist}" var="label">
					<li><input name="labellist" type="hidden" value="${label.id}"/>${label.labelvalue}</li>
				</c:forEach>				
			</ul>
		</div>
		<div class="clear"></div>
		<div class="control-group">
			<p></p>&nbsp;&nbsp;&nbsp;&nbsp;
			<label class="control-label">创建人：</label><span>${label.createBy.name}</span>
			<label class="control-label">创建时间：</label><span><fmt:formatDate value="${label.createDate}" type="both"/></span>
			<p></p>&nbsp;&nbsp;&nbsp;&nbsp;
			<label class="control-label">关联用户数：</label><span>${label.countuser}</span>
			<label class="control-label">关联知识数：</label><span>${label.countarticle }</span>
		</div>
		</c:if>
		<!-- NC系统导入的标签 -->
		<c:if test="${label.issys =='0'}">
		<div class="form-actions">
		<label class="control-label">标签名称：
		</label>
		<input id="labelvalue" name="labelvalue" maxlength="10" style="width:182px;" value="${label.labelvalue}" readonly="readonly"/>&nbsp;
		<input type="hidden" name="id" value="${label.id}"/>
		<input type="hidden" name="issys" value="${label.issys}"/>
		<label>上级分类：
		</label><sys:treeselect id="pid" name="pid" value="${label.pid}" labelName="pname" labelValue="${label.pname}"
					title="标签列表" url="/cms/TagtreeData2?flag=${2}&pid=${label.pid}" module="label" notAllowSelectRoot="false" cssClass="input-small" disabled="disabled" extId="${label.pid}"/>
		</div>
		<div class="control-group">&nbsp;&nbsp;&nbsp;&nbsp;
		<label class="control-label">关联标签：
		</label>
		<div class="input-append" id="relaTag">
			<input class="input-small" type="text"/>
			<a class="btn" href="#" >
				<i class="icon-search"></i>
			</a>
		</div>	
			<p></p>
			<ul class="ul" id="taglist" name="taglist">
				<c:forEach items="${labellist}" var="label">
					<li>${label.labelvalue}</li>
				</c:forEach>				
			</ul>
		</div>
		</c:if>
		<!-- 本系统的标签 -->
		<c:if test="${label.issys =='2'}">
		<div class="form-actions">
		<label class="control-label">标签名称：
		</label>
		<input id="labelvalue" name="labelvalue" maxlength="10" style="width:182px;" value="${label.labelvalue}" disabled="disabled"/>&nbsp;
		<input type="hidden" name="id" value="${label.id}"/>
		<input type="hidden" name="issys" value="${label.issys}"/>
		<label>上级分类：
		</label><sys:treeselect id="pid" name="pid" value="${label.pid}" labelName="pname" labelValue="${label.pname}"
					title="标签列表" url="/cms/TagtreeData2?flag=${2}" module="label" notAllowSelectRoot="false" cssClass="input-small" disabled="disabled"/>
		</div>
		<div class="control-group">&nbsp;&nbsp;&nbsp;&nbsp;
		<label class="control-label">关联标签：
		</label>
		<div class="input-append" id="relaTag">
			<input class="input-small" type="text" />
			<a class="btn" href="#">
				<i class="icon-search"></i>
			</a>
		</div>	
			<p></p>
			<ul class="ul" id="taglist" name="taglist">
				<c:forEach items="${labellist}" var="label">
					<li>${label.labelvalue}</li>
				</c:forEach>				
			</ul>
		</div>
		</c:if>
		
		<div class="form-actions">
		<input id="examineTag"class="btn btn-info"  value="标签审核" type="button"/>&nbsp;&nbsp;
		<c:if test="${label.issys =='1' and label.delFlag=='0' and label.pid !='1'}">
		<input id="mergeTag" class="btn btn-info"  value="标签合并到" type="button"/>&nbsp;&nbsp;
		</c:if>
		<c:if test="${label.countarticle =='0' and label.countuser =='0' and label.issys !='0'}">
		<Button class="btn btn-info" id="deleteTag"><a href="#" style="text-decoration:none;color:white;">标签删除</a></Button>&nbsp;&nbsp;
		</c:if>
		<c:if test="${label.issys !='2'}">
		<input class="btn btn-primary" style="margin-left:175px;" value="保存" type="submit"/>
		</c:if>
		</div>
		</form>
</body>
</html>