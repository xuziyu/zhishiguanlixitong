<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>知识库管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
		<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
		<script src="${ctxStatic}/jquery/jquery.XYTipsWindow.min.2.8.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {			
			$("#name").focus();
			$("#inputForm").validate({ 	
				submitHandler: function(form){
					var length=$('input[name=managers]:checked').length;
					var module="${hierNum}";
					if(module==3){
						if(length==0){
						$.jBox.tip("必须选择一个分类管理员!");
						return false;
						}	
					}
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			//add by yangshw6
			//添加知识分类管理员
			$("#addCategoryManage").click(function(){
				var managers=$('input[name=managers]:checked');
				var string="";
				managers.each(function(){
					if($(this).attr("checked")){
						var value=$(this).val()+".";
						string=string+value;
					}
				});
				$.jBox.open(
            			"iframe:${ctx_a}/cms/category/addCategoryManage?managers="+string,
				 		"选择知识库管理员",300,420, 
				 		{
					 		top:'20px',
		            		buttons: {'确定':true ,'关闭':false},
		            		submit:function(v,h,f){
				            	if(v==true){
				            		$("#CategoryManagers").empty();
				            		var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						            obj = $(doc).find("input[name=selectManger]");
				            		for(k in obj){
				            			if(k==8){
											$("#CategoryManagers").append("<br />");
										}
				            			if(obj[k].checked){
											var v1 = obj[k];
											var v=$(v1);
											$("#CategoryManagers").append("<input name='managers' type='checkbox' value="+obj[k].value +" checked='checked' />"+$(v).attr("manager"));
										}
				            		
				            		}
				            	}
		            		} 	
	            		} 
        		);
        		$('.jbox-content').css('overflow-y','hidden');
			});
			
			//添加角色
			$("#addRole").click(function(){
				var roles=$('input[name=selectroles]:checked');
				var string="";
				roles.each(function(){
					if($(this).attr("checked")){
						var value=$(this).val()+".";
						string=string+value;
					}
				});
				$.jBox.open(
            			"iframe:${ctx_a}/cms/category/addRole?roles="+string,//string 是已选人员的id集合,需要默认选中的人员
				 		"选择角色",300,420, 
				 		{
					 		top:'20px',
		            		buttons: {'确定':true ,'关闭':false},
		            		submit:function(v,h,f){
				            	if(v==true){
				            		$("#roles").empty();
				            		var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						            obj = $(doc).find("input[name=selectrole]");
				            		for(k in obj){
				            			if(k==8){
											$("#roles").append("<br />");
										}
				            			if(obj[k].checked){
											var v1 = obj[k];
											var v=$(v1);
											$("#roles").append("<input name='selectroles' type='checkbox' value="+obj[k].value +" checked='checked' />"+$(v).attr("role"));
										}
				            		
				            		}
				            	}
		            		} 	
	            		} 
        		);
        		$('.jbox-content').css('overflow-y','hidden');
			});
			
			
			$('#seleTag').click(function (){
				var selectedTagString = "";
				$(".selectedTag").each(function(){
					if($(this).attr("checked")){
      						selectedTagString = selectedTagString+$(this).attr("value")+".";
      				}
    			});
    			$(".selectedTags").empty();
			 	$.jBox.open(
				 	"iframe:${ctx_a}/tag/treeselectlable?url="+encodeURIComponent("/cms/TagtreeData2?remarks=${0}&iscategory=${1}")+"&module=article&checked=${checked}&extId=${extId}&isAll=${isAll}&selectedTagString="+selectedTagString,
				 	"标签列表",580,380,
				 	{id:'Tag',	  
					 //ajaxData:{
					 //showScrolling:true,
					top:'20px',
		            buttons: {'确定':true ,'关闭':false},
		            submit:function(v,h,f){
				        $(".selectedTags").empty();
						var doc = h.find("#jbox-iframe")[0].contentWindow.document;
						obj = $(doc).find("input[name=selectTag_1]:checked");
						var i=0;
						var length=obj.length;
						for(k in obj){
							if(i%4==0 && i<length+1 && i!=0){
								$(".selectedTags").append("</br>");
							}
							if(obj[k].checked){
								var v1 = obj[k];
								var v=$(v1);
								$(".selectedTags").append("<input name='selectTag' delFlag='0' class='selectedTag'   type='checkbox' value=" +obj[k].value + " lablename="+$(v).attr('lablename')+" checked='checked'  />" +$(v).attr('lablename'));
								i++;
							}
						}
		           	}	 	
	           }
        	);
        	$('.jbox-content').css('overflow-y','hidden');
		});
			
			
		});
		
	</script>
	<style type="text/css">
	.fl{float:left;vertical-align: middle;width: 30px;}
.cl{clear:both;}
.content{margin:0 3px;}
.aaa{display:inline-block;
    height: 36px;
    text-align: left;
    line-height: 25px;}
    .position1{
    position:relative;}
    .position2{
    position:absolute;
    top: 62px;
    left: 473px;
    }
    .position3{
    position:absolute;
    top: 5px;
    }
	</style>
</head>

<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/category/">知识分类列表</a></li>
		<li class="active"><a href="${ctx}/cms/category/form?id=${category.id}&parent.id=${category.parent.id}"><c:if test="${hierNum==1}">知识库</c:if><c:if test="${hierNum !=1}">知识分类</c:if><shiro:hasPermission name="cms:category:edit">${not empty category.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:category:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="category" action="${ctx}/cms/category/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">归属机构:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${isNew==true}">
					<c:if test="${Topoffice != null}">
						<sys:treeselect id="office" name="office.id" value="${Topoffice.id}" labelName="office.name" labelValue="${Topoffice.name}"
						title="机构" url="/sys/office/treeData" cssClass="required"/>
						&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${Topoffice == null}">
						<sys:treeselect id="office" name="office.id" value="${parentCategory.office.id}" labelName="office.name" labelValue="${parentCategory.office.name}"
						title="机构" url="/sys/office/treeData" cssClass="required"/>
						&nbsp;&nbsp;&nbsp;
					</c:if>
					</c:when>
					<c:otherwise>
					<sys:treeselect id="office" name="office.id" value="${category.office.id}" labelName="office.name" labelValue="${category.office.name}"
					title="机构" url="/sys/office/treeData" cssClass="required"/>
					&nbsp;&nbsp;&nbsp;
					</c:otherwise>
				</c:choose>
                
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上级知识分类:</label>
			<div class="controls">
                <sys:treeselect id="category" name="parent.id" value="${category.parent.id}" labelName="parent.name" labelValue="${category.parent.name}"
					title="知识库" url="" extId="${category.id}" cssClass="required" hideBtn="true" disabled="disabled"/>
			</div>
		</div>
		<div class="control-group" style="display:none">
			<label class="control-label">模型:</label>
			<div class="controls">
				<form:select path="module">
				<c:choose>
						<c:when test="${hierNum==3}">
							<form:option value="article" label="知识分类"/>
						</c:when>
						<c:otherwise>
							<form:option value="" label="知识库"/>
						</c:otherwise>
					</c:choose>
				</form:select>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">知识库名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		
		
		<c:if test="${hierNum==1}">
		<div class="control-group position1">
		<legend><small>知识库图标设置(非必选项):</small></legend>
		<label class="control-label">图标:</label>
			<div class="controls">
                            <div id="fileQueue"></div>
                            <div id="fileInfo"></div>
                             <img id="uploadify_hidden" style="width:176px;height:27px;display:none" >
                             <input id="icon1" name="icon1" type="hidden" value="" >
                            <input type="file" name="uploadify" id="uploadify" />
                           
			</div>
			<label class="control-label">悬浮图标:</label>
			<div class="controls">
							 <div id="fileQueue2"></div>
                             <div id="fileInfo2"></div>
                             <img id="uploadify_hidden2" style="width:176px;height:27px;display:none" >
                              <input id="icon2" name="icon2" type="hidden" value="" >
                            <input type="file" name="uploadify2" id="uploadify2" />
                           
			</div>
			<div class="position2">
			<label class="control-label">(图标规格示例:</label>
			<div class="controls">
                           	<a class="aaa" target="blank" href="${ctxStatic}/source-index/images/icon3444.png">默认图标</a>&nbsp;&nbsp;
                           	<a class="aaa" target="blank" href="${ctxStatic}/source-index/images/icon34.png">鼠标悬浮时</a>)
			</div>
			</div>
			<label class="control-label">背景色:</label>
			<div class="controls">
			<div class="content">
    	<div class="fl position1"><input type="radio" name="color" value="blue"/><span class="position3" style="width:10px;height:10px;background-color:#1489c9;display:inline-block;"></div>
        <div class="fl position1"><input type="radio" name="color" value="green"/><span class="position3" style="width:10px;height:10px;background-color:#23ad8b;display:inline-block;"></div>
        <div class="fl position1"><input type="radio" name="color" value="orange"/><span class="position3" style="width:10px;height:10px;background-color:#e6870a;display:inline-block;"></div>
        <div class="cl"></div>
        </div>
   		</div>
		</div>
	
		</c:if>
		
		<c:choose>
			<c:when test="${hierNum==3}">
				<div class="control-group">
					<label class="control-label">是否为作物:</label>
					<form:radiobuttons  path="remarks" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</div>
				<div class="control-group">
					<label class="control-label">选择绑定标签:</label>
					<div class="controls" id="taginfo">
						<input type="button" class="btn btn-primary" id="seleTag" value="选择"/>
					<p></p>
					<div id="taglist" class="selectedTags">
						<c:forEach items="${listlabel}" var="listlabel" varStatus="status">
							<input class="selectedTag" name='selectTag' type="checkbox" value="${listlabel.id}" checked />
							${listlabel.labelvalue}
							<c:if test="${status.index==3}">
								</br>
							</c:if>
						</c:forEach>
					</div>
					</div>
				</div>
				<div class="control-group" >
				<label class="control-label">分类管理员:</label>
				<div class="controls">
					<input class="btn btn-primary" id="addCategoryManage" value="添加" type="button"/>
					<div id="CategoryManagers">
						<c:forEach items="${categorymanagers}" var="managers">
							${managers.name}<input name='managers' type="checkbox" value="${managers.id}" checked="checked" />
						</c:forEach>
					</div>
				</div>
				</div>
				<div class="control-group" >
				<label class="control-label">分类角色:</label>
				<div class="controls">
					<input class="btn btn-primary" id="addRole" value="添加" type="button"/>
					<div id="roles">
						<c:forEach items="${roles}" var="roles">
							${roles.name}<input name='selectroles' type="checkbox" value="${roles.id}" checked="checked" />
						</c:forEach>
					</div>
				</div>
				</div>
			</c:when>
			<c:when test="${hierNum==3}">
			
			
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		
		
		
		<%-- <div class="control-group">
			<label class="control-label">缩略图:</label>
			<div class="controls">
				<form:hidden path="image" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="image" type="thumb" uploadPath="/cms/category"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接:</label>
			<div class="controls">
				<form:input path="href" htmlEscape="false" maxlength="200"/>
				<span class="help-inline">知识库超链接地址，优先级“高”</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">目标:</label>
			<div class="controls">
				<form:input path="target" htmlEscape="false" maxlength="200"/>
				<span class="help-inline">知识库超链接打开的目标窗口，新窗口打开，请填写：“_blank”</span>
			</div>
		</div>--%>
		<div class="control-group">
			<label class="control-label">描述:</label>
			<div class="controls">
				<form:textarea path="description" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关键字:</label>
			<div class="controls">
				<form:input path="keywords" htmlEscape="false" maxlength="200"/>
				<span class="help-inline">填写描述及关键字</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits"/>
				<span class="help-inline">排列次序(注:新增各级知识分类默认取当前层级最大数+1)</span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">在导航中显示:</label>
			<div class="controls">
				<form:radiobuttons path="inMenu" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline">是否在导航中显示该栏目</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">在分类页中显示列表:</label>
			<div class="controls">
				<form:radiobuttons path="inList" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline">是否在分类页中显示该知识库的知识列表</span>
			</div>
		</div>--%>
		<!-- <div class="control-group">
			<label class="control-label" title="默认展现方式：有子知识显示知识库列表，无子知识显示内容列表。">展现方式:</label>
			<div class="controls">
				<form:radiobuttons path="showModes" items="${fns:getDictList('cms_show_modes')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/><%--
				<form:select path="showModes" class="input-medium">
					<form:option value="" label="默认"/>
					<form:options items="${fns:getDictList('cms_show_modes')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select><span class="help-inline"></span> --%>
			</div>
		</div> -->
		<div class="control-group">
			<label class="control-label">是否允许评论:</label>
			<div class="controls">
				<form:radiobuttons path="allowComment" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<!-- 
		<div class="control-group">
			<label class="control-label">是否需要审核:</label>
			<div class="controls">
				<form:radiobuttons path="isAudit" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		 -->
		<%-- 
		<div class="control-group">
			<label class="control-label">自定义列表视图:</label>
			<div class="controls">
                <form:select path="customListView">
                    <form:option value="" label="默认视图"/>
                    <form:options items="${listViewList}" htmlEscape="false"/>
                </form:select>
                <span class="help-inline">自定义列表视图名称必须以"${category_DEFAULT_TEMPLATE}"开始</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自定义内容视图:</label>
			<div class="controls">
                <form:select path="customContentView">
                    <form:option value="" label="默认视图"/>
                    <form:options items="${contentViewList}" htmlEscape="false"/>
                </form:select>
                <span class="help-inline">自定义内容视图名称必须以"${article_DEFAULT_TEMPLATE}"开始</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自定义视图参数:</label>
			<div class="controls">
                <form:input path="viewConfig" htmlEscape="true"/>
                <span class="help-inline">视图参数例如: {count:2, title_show:"yes"}</span>
			</div>
		</div>
		--%>
		<div class="form-actions">
			<shiro:hasPermission name="cms:category:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<!-- <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/> -->
		</div>
	</form:form>
	 <%
					String path = request.getContextPath()+"/";
					//String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
				%>
                            <c:set var="ctx1" value="<%=path%>" />

                            <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/uploadify.css"></link>
                          <!--  
                           <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery1.8.2.js"></script>
                           -->
                            <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.uploadify.min.js"></script>
                            <script type="text/javascript">
                                function setCookie(c_name, value, expiredays) {
                                    var exdate = new Date()
                                    exdate.setDate(exdate.getDate() + expiredays)
                                    document.cookie = c_name + "=" + escape(value) +
                                        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
                                }

                                function getCookie(name) {
                                    alert(name);
                                    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
                                    if (arr = document.cookie.match(reg))
                                        return unescape(arr[2]);
                                    else
                                        return null;
                                }

                                function newGuid() {
                                    var guid = "";
                                    for (var i = 1; i <= 32; i++) {
                                        var n = Math.floor(Math.random() * 16.0).toString(16);
                                        guid += n;
                                        if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
                                            guid += "";
                                    }
                                    return guid;
                                }

                                function cutstr(str, len) {
                                    var str_length = 0;
                                    var str_len = 0;
                                    str_cut = new String();
                                    str_len = str.length;
                                    for (var i = 0; i < str_len; i++) {
                                        a = str.charAt(i);
                                        str_length++;
                                        if (escape(a).length > 4) {
                                            //中文字符的长度经编码之后大于4
                                            str_length++;
                                        }
                                        str_cut = str_cut.concat(a);
                                        if (str_length >= len) {
                                            str_cut = str_cut.concat("...");
                                            return str_cut;
                                        }
                                    }
                                    //如果给定字符串小于指定长度，则返回源字符串；
                                    if (str_length < len) {
                                        return str;
                                    }
                                }



                                $(document).ready(function() {


                                    //附件标记->对应知识保存状态
                                    var attfile_temp_guid = newGuid()
                                        //$("#attfile_temp_guid").val(attfile_temp_guid);

                                    var cookie_guid = newGuid();

                                    //$("#cookie_guid").val(cookie_guid);
                                    //var category_id =$("#current_category_id").val();

                                    //var current_article_id = $("#current_article_id").val();


                                    var uploadify_onSelectError = function(file, errorCode, errorMsg) {
                                        var msgText = "上传失败\n";
                                        switch (errorCode) {
                                            /*
	            case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:  
	                //this.queueData.errorMsg = "每次最多上传 " + this.settings.queueSizeLimit + "个文件";  
	                msgText += "每次最多上传 " + this.settings.queueSizeLimit + "个文件";  
	                break;
	             */
                                            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                                                msgText += "文件大小超过限制( " + this.settings.fileSizeLimit + " )";
                                                break;
                                            default:
                                                msgText += "错误代码：" + errorCode + "\n" + errorMsg;
                                        }
                                        alert(msgText);
                                    };


                                    $("#uploadify").uploadify({
                                        'swf': '${ctx1}resources/js/uploadify.swf',
                                        'cancelImg': '${ctx1}resources/img/uploadify-cancel.png',
                                        'uploader': '${ctx1}a/cms/category/uploadpicture',
                                        'onSelect': function(file) {
                                            category_id = $("#categoryId").val();
                                        },
                                        'queueID': 'fileQueue', //文件在页面的显示队列的id
                                        'queueSizeLimit': 1, //可上传文件的个数
                                        'auto': true,
                                        'fileTypeExts': '*.jpg;*.png;*.gif',
                                        'method': 'post',
                                        'width': '72',
                                        'height': '23',
                                        'progressData': 'percentage', //显示进度条
                                        'removeTimeout'	:0.5, //如果设置了任务完成后自动从队列中移除，则可以规定从完成到被移除的时间间隔。*/
                                        'removeCompleted':true,
                                        'multi': true,
                                        'successTimeout': 50,
                                        'fileSizeLimit': '10MB', //限制上传文件大小
                                        'buttonText': '选择文件',
                                        'buttonClass': '',
                                        /*'overrideEvents'  : 	[ 'onDialogClose', 'onUploadSuccess', 'onUploadError', 'onSelectError' ], */
                                        /*'onSelect'        : 	uploadify_onSelect, */
                                        'overrideEvents': ['onUploadSuccess', 'onUploadStart', 'onSelectError'],
                                        "formData": {
                                           
                                        },
                                        // 'onUploadStart' 	: 	function(file) {$("#uploadify").uploadify("settings", "formData", {'userName':'huangmj');}, 
                                        'onCancel': function(file) {
                                            alert('文件：' + file.name + '取消！')
                                        },
                                        'onSelectError': uploadify_onSelectError,
                                        'onUploadSuccess': function(file, data, response) { //成功提醒 
                                        //成功时把网络地址给界面的一个隐藏域
                                       			$("#icon1").val(data);
												$("#uploadify_hidden").attr("src",data);
												$("#uploadify_hidden").show();
												//
												//$("#uploadify").hide();
                                        }
                                    });
                                    
                                     $("#uploadify2").uploadify({
                                        'swf': '${ctx1}resources/js/uploadify.swf',
                                        'cancelImg': '${ctx1}resources/img/uploadify-cancel.png',
                                        'uploader': '${ctx1}a/cms/category/uploadpicture',
                                        'onSelect': function(file) {
                                            category_id = $("#categoryId").val();
                                        },
                                        'queueID': 'fileQueue2', //文件在页面的显示队列的id
                                        'queueSizeLimit': 1, //可上传文件的个数
                                        'auto': true,
                                        'fileTypeExts': '*.jpg;*.png;*.gif',
                                        'method': 'post',
                                        'width': '72',
                                        'height': '23',
                                        'progressData': 'percentage', //显示进度条
                                        'removeTimeout':0.5, //如果设置了任务完成后自动从队列中移除，则可以规定从完成到被移除的时间间隔。*/
                                        'removeCompleted': true,
                                        'multi': true,
                                        'successTimeout': 50,
                                        'fileSizeLimit': '10MB', //限制上传文件大小
                                        'buttonText': '选择文件',
                                        'buttonClass': '',
                                        /*'overrideEvents'  : 	[ 'onDialogClose', 'onUploadSuccess', 'onUploadError', 'onSelectError' ], */
                                        /*'onSelect'        : 	uploadify_onSelect, */
                                        'overrideEvents': ['onUploadSuccess', 'onUploadStart', 'onSelectError'],
                                        "formData": {
                                            
                                        },
                                        // 'onUploadStart' 	: 	function(file) {$("#uploadify").uploadify("settings", "formData", {'userName':'huangmj');}, 
                                        'onCancel': function(file) {
                                            alert('文件：' + file.name + '取消！')
                                        },
                                        'onSelectError': uploadify_onSelectError,
                                        'onUploadSuccess': function(file, data, response) { //成功提醒 
                                        //成功时把网络地址给界面的一个隐藏域
                                        		$("#icon2").val(data);
												$("#uploadify_hidden2").attr("src",data);
												$("#uploadify_hidden2").show();
												//这个还有点冲突
												//$("#uploadify2").hide();
                                        }
                                    });
                                });
                            </script>
                            </body>
	
</body>
</html>