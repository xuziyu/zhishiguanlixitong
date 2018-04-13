<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>知识库管理</title>
		<meta name="decorator" content="default" />
		<%@include file="/WEB-INF/views/include/treetable.jsp"%>
	</head>
	<body>
		<ul class="nav nav-tabs">
			<li class="active">
				<a href="${ctx}/cms/category/">知识分类列表</a>
			</li>
			<shiro:hasPermission name="cms:category:edit">
				<li>
					<a href="${ctx}/cms/category/form?remarks=${2}">知识库添加</a>
				</li>
			</shiro:hasPermission>
		</ul>
		<sys:message2 content="${message1}" />
		<sys:message2 content="${message}" />
		<!-- <form id="listForm" method="post"> -->
			<div class="control-group" style="position: relative;">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<label class="control-label">
					知识分类：
				</label>
				<!-- <div class="input-append"> -->
					<input type="hidden"/>
					<input class="input-small" type="text" id="categoryInput">
					<!-- <a id="categorySearch" class="btn" href="javascript:"> <i class="icon-search"></i> </a> -->
					
					<div style="display:inline;" id="categorySearch"><input type="button"  class="btn  btn-primary" value="查询" style="margin-bottom: 10px;"/></div> 
				<!-- </div> -->
				<shiro:hasPermission name="sys:role:view">
					<%-- <a href="${ctxStatic}/excel/知识分类导入模板.xls">模板下载</a> --%>
					<div id="downTemplate"  style="position: absolute; top:50%;right:150px;margin-top: -15px;"><a class="btn btn-primary" href="${ctxStatic}/excel/知识分类导入模板.xls" id="adowntemplete">模板下载</a></div>
					<div id="containeruploadfile" style="position: absolute; top:50%;right:10px;margin-top: -15px;">
						<!-- <button class="btn btn-primary" id="pickfiles">导入知识分类</button> -->
						<input type="button" class="btn btn-primary" id="pickfiles" value="导入知识分类"/>
					</div>
				</shiro:hasPermission>
			</div>
			<table id="treeTable" class="table table-striped table-bordered table-condensed">
				<tr>
					<th>知识分类名称</th>
					<th>序号</th>
					<th>归属机构</th>
					<%--<th>知识库模型</th><th style="text-align:center;">排序</th><th title="是否在导航中显示该知识库">导航菜单</th><th title="是否在分类页中显示该栏目的知识列表">知识库列表</th><th>展现方式</th>--%>
					<th>操作</th>
				</tr>
				<c:forEach items="${list}" var="tpl">
					<tr id="${tpl.id}" pId="${tpl.parent.id ne '1'?tpl.parent.id:'0'}">
						<!-- <td><a href="${ctx}/cms/category/form?id=${tpl.id}">${tpl.name}</a></td> -->
						<td>${tpl.name}</td>
						<td>${tpl.sort }</td>
						<td>${tpl.office.name}</td>
						<%--<td>${fns:getDictLabel(tpl.module, 'cms_module', '公共模型')}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="cms:category:edit">
							<input type="hidden" name="ids" value="${tpl.id}"/>
							<input name="sorts" type="text" value="${tpl.sort}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission>
						<shiro:lacksPermission name="cms:category:edit">
							${tpl.sort}
						</shiro:lacksPermission>
					</td>
					<td>${fns:getDictLabel(tpl.inMenu, 'show_hide', '隐藏')}</td>
					<td>${fns:getDictLabel(tpl.inList, 'show_hide', '隐藏')}</td>
					<td>${fns:getDictLabel(tpl.showModes, 'cms_show_modes', '默认展现方式')}</td>--%>
						<td>
							<%-- <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${tpl.id}${fns:getUrlSuffix()}" target="_blank">访问</a>&nbsp;&nbsp;&nbsp;--%>
							<shiro:hasPermission name="cms:category:edit">
								<c:if test="${tpl.isAdmin=='1'}">
									<a href="${ctx}/cms/category/form?id=${tpl.id}">修改</a>&nbsp;&nbsp;&nbsp;
									<a href="${ctx}/cms/category/delete?id=${tpl.id}" onclick="return confirmx('要删除该知识库及所有子知识项吗？', this.href)">删除</a>&nbsp;&nbsp;&nbsp;
									<shiro:hasPermission name="sys:category:merge">
										<c:if test="${tpl.module=='article'}">
											<sys:categorytreeselect id="category_${tpl.id}" name="category.id" value="${tpl.id}" labelName="category.name" labelValue="${tpl.name}" title="合并到另一个知识分类" url="/cms/category/treeData" module="article" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="input-small" />
										</c:if>
									</shiro:hasPermission>
								</c:if>
								<c:if test="${tpl.module!='article'}">
									&nbsp;
									<a href="${ctx}/cms/category/form?parent.id=${tpl.id}&remarks=${tpl.parent.id}">添加下级</a>&nbsp;&nbsp;&nbsp;
								</c:if>
							</shiro:hasPermission>
						</td>
					</tr>
				</c:forEach>
			</table>
			<!--<shiro:hasPermission name="cms:category:edit"><div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div></shiro:hasPermission>-->
		<!-- </form> -->
		<!--模态框 -->
		<div class="modal fade" id="mymodal" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="myModalLabel">导入提示</h4>
					</div>
					<div class="modal-body" style="overflow-x: hidden;">
							 <div style="margin-top: 5px;"> 
							    <div style="float: left;width: 25%;text-align: center;">导入状态:</div>
							    <div style="float: left;width: 75%;" id="tdflag"></div>
							    <div style="clear: both;"></div>
							 </div>
							 <div style="margin-top: 5px;" > 
							    <div style="float: left;width: 25%;text-align: center;">导入详情:</div>
							    <div id="tdmsg" style="float: left;width: 75%;"></div>
							    <div style="clear: both;"></div>
							 </div>
							 <div style="margin-top: 5px;" > 
							    <div style="float: left;width: 25%;text-align: center;">冲突原因:</div>
							    <div id="tdwhy" style="float: left;width: 75%;"></div>
							    <div style="clear: both;"></div>
							 </div>
							 <div style="margin-top: 5px;" > 
							    <div style="float: left;width: 25%;text-align: center;">错误行号:</div>
							    <div id="tdlist" style="float: left;width: 75%;word-break: break-all;"></div>
							    <div style="clear: both;"></div>
							 </div>
							 <div style="margin-top: 5px;" > 
							    <div style="float: left;width: 25%;text-align: center;">重复行号:</div>
							    <div id="tdlike" style="float: left;width: 75%;word-break: break-all;"></div>
							    <div style="clear: both;"></div>
							 </div>
					</div>
					<div class=" modal-footer boxFooter ">
						<button type="button " id="sub " class="btn boxButton " onclick="assign() ">刷新页面</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
		<script type="text/javascript">
			$(document).ready(function() {
				console.time("test");
				$("#mymodal").modal('show');
				$(".modal-backdrop").remove();
				$("#mymodal").modal('hide');
				$("#treeTable").treeTable({
					expandLevel: 3
				});
				console.timeEnd("test");
			});

			function updateSort() {
				loading('正在提交，请稍等...');
				$("#listForm").attr("action", "${ctx}/cms/category/updateSort");
				$("#listForm").submit();
			}
			$("#aaaa").click(function() {
				return true;
			});
		</script>
		<script type="text/javascript" src="${ctxStatic}/plupload-2.1.8/js/plupload.full.min.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${ctxStatic}/plupload-2.1.8/js/i18n/zh_CN.js"></script>
		<script type="text/javascript" src="${ctxStatic}/plupload-2.1.8/js/jquery.ui.plupload/jquery.ui.plupload.min.js" charset="UTF-8"></script>
		<script type="text/javascript">
			$(function() {
				//进来全部收起
				shutAll();
				function shutAll() {
					var ids = [];
					$("#treeTable tr").each(function() {
						if($(this).attr("pid") && $(this).attr("pid") == 0 || $(this).attr("haschild")) {
							if($(this).attr("haschild")) {
								$(this).find("span.prev_sp").next().removeClass().addClass("default_active_node default_shut");
							}
							ids.push($(this).attr("id"));
						}
					});
					//因为这里是数组,所以的这么传.
					$("#treeTable").trigger("shutAll", [ids]);
				}
				$("#categorySearch").on("click", function() {
					shutAll();
					var search = $("#categoryInput").val().trim();
					if(search != "") {
						$("#treeTable tr td:nth-child(1)").each(function() {
							//td中的字体恢复默认的颜色
							$(this).css("color", "#000");
							//如果包含search
							if($(this).text().trim().indexOf(search) > -1) {
								//拿到当前tr的id
								$(this).css("color", "red");
								var id = $(this).parent().attr("id");
								var $this = $("#" + id);
								if($this.attr("pid") == 0) {
									//如果pid=0，证明是最父级id，根节点。
									$this.find("span.prev_sp").next().trigger("onmyevent");
								} else {
									//拿到当前tr的父级id
									var parentid = $this.attr("pid");
									//拿到当前tr的父级对象。
									var $parent = $("#" + parentid);
									//在判断一次
									if($parent.attr("pid") == 0) {
										if($parent.find("span.prev_sp").next().hasClass("default_active_node default_open")) {
											$this.find("span.prev_sp").next().trigger("onmyevent");
										} else {
											$parent.find("span.prev_sp").next().trigger("onmyevent");
											$this.find("span.prev_sp").next().trigger("onmyevent");
										}
									} else {
										//拿到当前tr的父级id的父级di
										var grandparentid = $parent.attr("pid");
										//拿到当前tr的父级对象的父级对象。
										var $grandparent = $("#" + grandparentid);
										if($grandparent.attr("pid") == 0) {
											if($grandparent.find("span.prev_sp").next().hasClass("default_active_node default_open")) {
												if(!$parent.find("span.prev_sp").next().hasClass("default_active_node default_open")) {
													$parent.find("span.prev_sp").next().trigger("onmyevent");
												}
											} else {
												$grandparent.find("span.prev_sp").next().trigger("onmyevent");
												$parent.find("span.prev_sp").next().trigger("onmyevent");
											}
										}
									}
								}
							}

						});
					}
				});
				$("#categoryInput").keydown(function(e) {
					if(e.which == 13) {
						$("#categorySearch").trigger("click");
						e.stopPropagation();
						e.cancleBubble = true;
					}
				})
			})
		</script>
		<script type="text/javascript">
			//附件标记->对应知识保存状态

			$(document).ready(function() {
				//模态框确认
				assign = function() {
					$("#mymodal").modal('hide');
					window.location.reload();
				}
				var uploader = new plupload.Uploader({
					runtimes: 'html5,flash,silverlight,html4',
					browse_button: 'pickfiles', // you can pass an id...
					container: document.getElementById('containeruploadfile'), // ... or DOM Element itself
					url: '${ctx_a}/cms/article/excel',
					flash_swf_url: '${ctxStatic}/plupload-2.1.8/js/Moxie.swf',
					silverlight_xap_url: '${ctxStatic}/plupload-2.1.8/js/Moxie.xap',

					filters: {
						max_file_size: '30mb',
						mime_types: [{
							title: "files",
							extensions: "xlxs,xls"
						}]
					},
					multipart_params: {
					},
					init: {
						FilesAdded: function(up, files) {
							uploader.start();
							$("#pickfiles").text("导入中......");
						},
						Error: function(up, err) {
							$.jBox.tip("文件上传失败！请重新上传文件");
						},
						FileUploaded: function(uploader, file, result) {
							$("#pickfiles").text("导入知识分类");
							var data = JSON.parse(result.response);
							$("#tdflag").text(data.flag);
							$("#tdmsg").text(data.msg);
							$("#tdwhy").text(data.why);
							$("#tdlist").text(data.list);
							$("#tdlike").text(data.likename);
							$("#mymodal").modal('show');
						}

					}
				});
				uploader.init();
			});
		</script>
	</body>
</html>