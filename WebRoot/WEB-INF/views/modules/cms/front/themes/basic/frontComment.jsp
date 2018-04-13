<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<style type="text/css">
	.reply{border:1px solid #ddd;background:#fefefe;margin:10px;}
	#p-comment-list{border-left: 5px solid #1489c9;background: #dcedf7;}
	.commentForm .liuyan textarea{width:40%;height:120px;}
	.commentForm .liuyan .submit{ 
	  border: 2px solid #FFF;
	 width: 110px;
  height: 44px;
  color: #fff;
  line-height: 42px; 
  text-align: center;
  background: #f19715;
  font-size: 18px;
  cursor: pointer;
  display: block;
  margin-top: 4px;
  margin-bottom: -42px;
  margin-left: 9%;
  font-family: arial,'Hiragino Sans GB','Microsoft YaHei','微软雅黑','宋体',sans-serif;}
</style>
<script type="text/javascript">

	$(document).ready(function() {
		comment(0);
	});
	function commentForm(form){
	
	$('form textarea').keydown(function(e){
		if(e.which==13&&e.ctrlKey==true){
		$('form').submit();
		}
	});
	
		if(${user.id!=null}){
		
		$(form).validate({
			rules: {
			},
			messages: {
				content: {required: "请填写评论内容"},
			},
			submitHandler: function(form){
			    $.post($(form).attr("action"), $(form).serialize(), function(data){
			    	data = eval("("+data+")");
			    	$.jBox.tip(data.message);
			    	//alert(data.message);
			    	if (data.result==1){
			    		page(1);
			    	}
			    });
			},
			errorContainer: form + " .messageBox",
			errorPlacement: function(error, element) {
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
		}else{
			$(form).validate({
			rules: {
				validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
			},
			messages: {
				content: {required: "请填写评论内容"},
				validateCode: {remote: "验证码不正确", required: "请填写验证码"}
			},
			submitHandler: function(form){
			    $.post($(form).attr("action"), $(form).serialize(), function(data){
			    	data = eval("("+data+")");
			    	$.jBox.tip(data.message);
			    	//alert(data.message);
			    	if (data.result==1){
			    		page(1);
			    	}
			    });
			},
			errorContainer: form + " .messageBox",
			errorPlacement: function(error, element) {
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
		}
	}
	
	
	function comment(id){
		if ($("#commentForm"+id).html()==""){
			$(".validateCodeRefresh").click();
			$(".commentForm").hide(500,function(){$(this).html("");});
			$("#commentForm"+id).html($("#commentFormTpl").html()).show(500);
			$("#commentForm"+id+" input[name='replyId']").val(id);
			commentForm("#commentForm"+id + " form");
		}else{
			$("#commentForm"+id).hide(500,function(){$(this).html("");});
		}
	}
</script>
<div class="pre_list">
<p id="p-comment-list">评论列表</p>
	<ul>
		<c:forEach items="${page.list}" var="comment">
			<li>
				<h6>姓名: ${comment.name} &nbsp;时间：<fmt:formatDate value="${comment.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<a href="javascript:comment('${comment.id}')">回复</a></h6>
				<div style="padding-left: 28px;">${comment.content}</div>
				<div id="commentForm${comment.id}" class="commentForm"></div>
			</li>
		</c:forEach>
		<c:if test="${fn:length(page.list) eq 0}">
			<li>暂时还没有人评论！</li>
		</c:if>
	</ul>
</div>
<div class="pagination">${page}</div>
<div class="pre_list">
	<h3>我要评论</h3>
	<div id="commentForm0"></div>
 	<script id="commentFormTpl" type="text/javascript">
 
	<form:form action="${ctx}/comment" method="post" class="form-horizontal">
		<input type="hidden" name="category.id" value="${comment.category.id}"/>
		<input type="hidden" name="contentId" value="${comment.contentId}"/>
		<input type="hidden" name="title" value="${comment.title}"/>
		<input type="hidden" name="replyId"/>
		<div class="liuyan">
			<span>评论内容:</span>
			<!--<div class="controls">-->
				<textarea name="content" rows="15" ></textarea>
				
			<!--</div>-->
		
		
		  <c:if test="${user.id==null}">
		  <br><br>
			<!--
				<label class="control-label">姓名:</label>
				
					<lable style="width:50px;height:20px;border: 1px solid #ccc;background:#fff">匿名</label>
					-->
					<span>验证码:</span><sys:validateCode name="validateCode" />
					<input type="submit" class="submit" value="提交">&nbsp;
					${user.id}
			
		</c:if>
		 <c:if test="${user.id!=null}">
		 	<!--<div class="control-group" >-->
				<label class="control-label" style="display:none;" >姓名:</label>
				<!--<div class="controls">-->
					<input type="text"  style="visibility:hidden"  name="name" maxlength="11" class="txt required" style="width:100px;" value="${user.name}"/>
					<!--<label class="mid" style="visibility:hidden">验证码:</label>
					<label class="mid" style="visibility:hidden">验证码:</label>-->
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="submit" value="提交">&nbsp;
				<!--</div>
			</div>-->
		 
		 </c:if>
		 </div>
		<div class="alert alert-error messageBox" style="display:none">输入有误，请先更正。</div>
	</form:form>
</div>
 </script>