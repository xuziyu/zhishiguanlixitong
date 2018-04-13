<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${category.name}</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no">
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${category.description}" />
	<meta name="keywords" content="${category.keywords}" />
	<style type="text/css">
	
		*{margin:0;padding:0;}
		ul,li,ol{list-style:none; }
		img,a img,input,p{border:none;margin:0;padding:0;}
		table{border-collapse:collapse;border-spacing:0;}
		em,i,u{font-style:normal;}
		a{text-decoration:none; color:#000;}
		/*body{ font-size:12px;}*/
		body{ font-family:arial,'Hiragino Sans GB','Microsoft YaHei','微软雅黑','宋体',sans-serif; color:#676767;/*line-height:22px; */width:100%; }
		.pre_location{ width:100%; line-height:35px; border-bottom:2px solid #1489c9;}
		.pre_location .pre_loc{ width:1000px;  margin:0 auto; font-size:14px; color:#ccc;}
		.pre_location .pre_loc a:link,.pre_location .pre_loc a:visited{ color:#666;}
		.pre_location .pre_loc a:hover{ color:#1489c9;}
		.pre_center{ width:1000px; height:auto; overflow:hidden; margin:1px auto;}
	
	#span22{
 		width:170px;
	}
	.span10{
		width:988px;
	}
	
	
	
		.pre_list{ float:left; margin-top: 27px;}
		.pre_list h3{ height:27px; line-height:27px; border-left:5px solid #1489c9;  font-size:18px; color:#000; background:#dcedf7; width:984px; font-weight:normal; padding-left: 11px;}
		.pre_list ul{ width:1000px;}
		.pre_list ul li{ width:946px; height:42px; line-height:42px; font-size:14px; border-bottom:2px dotted #ccc; padding-left: 30px; background:url(images/dot.png) no-repeat 12px center;}
	
	
	footer{ 
		background-color: #1489c9;
		text-align:center;
		border-top:1px solid #000;
		padding:10px 0 10px 0;
		
		margin-top: 10px;
		margin-bottom: 1px;
	    width:100%;
	    font-size: 14px;
	    
		} 
		.color-F00{ color:#FFFFFF;}  
		.span11{
		width:98%;
		}
		.list{
			height:566px;
			/*overflow:hidden;*/
		}
@media (max-width:1000px) {
	.pre_center{ width:100%;}
	.pre_save .save{ width:100%;}
	.liuyan span{ width:100%;}
	.liuyan textarea{ width:92%;}
	.pre_center .pr_detail dl{ padding-left: 2%; width:98%;}
	.pre_center .pr_detail dl dd{ margin-right: 14px;}
	.pre_center .pr_detail dl .fb{ width:100%;}
	.pre_location .pre_loc{ width:100%;}
	.pre_center .pre_vedio{ padding-left: 2%;}
	.pre_location{ padding-left: 2%; width:98%;}
	.pre_center .fenye{ width:98%; padding-left: 2%;}
}
</style>
</head>
<body>
	 <!-- huangmj 2015 11 10 -->
	 <div class="pre_location">
		   <div class="pre_loc" >
			 <ul class="breadcrumb">
			    <cms:frontCurrentPosition category="${category}"/>
			 </ul>
		   </div>
	</div>
	<div class="pre_center">   	
		<div class="list">
	   		<div class="pre_list">
	   			<h3>${vip}${category.name}</h3>
	   			<ul>
		 			<cms:frontCategoryList categoryList="${categoryList}"/>
		 		</ul>
	      <c:set var="index" value="1"/>
	      <!--
		  <c:forEach items="${categoryList}" var="tpl">
			<c:if test="${tpl.inList eq '1' && tpl.module ne ''}">
				<c:set var="index" value="${index+1}"/>
				${index % 2 eq 0 ? '<div class="row">':''}
		    	<div class="span5">
		    		<h4><small><a href="${ctx_f}/list-${tpl.id}${urlSuffix}" class="pull-right">更多&gt;&gt;</a></small>${tpl.name}</h4>
					<c:if test="${tpl.module eq 'article'}">
		    			<ul><c:forEach items="${fnc:getArticleList(site.id, tpl.id, 5, '')}" var="article">
							<li><span class="pull-right"><fmt:formatDate value="${article.updateDate}" pattern="yyyy.MM.dd"/></span><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbr(article.title,40)}</a></li>
						</c:forEach></ul>
					</c:if>
					<c:if test="${tpl.module eq 'link'}">
		    			<ul><c:forEach items="${fnc:getLinkList(site.id, tpl.id, 5, '')}" var="link">
							<li><span class="pull-right"><fmt:formatDate value="${link.updateDate}" pattern="yyyy.MM.dd"/></span><a target="_blank" href="${link.href}" style="color:${link.color}">${fns:abbr(link.title,40)}</a></li>
						</c:forEach></ul>
					</c:if>
		    	</div>
		    	${index % 2 ne 0 ? '</div>':''}
			</c:if>
		
		  </c:forEach>
		  -->
		  </div>
	   </div>
	</div>   
		
		<footer class="color-F00"> 
			CopyRight© 2014-2015知识库管理 Powered By yonyou V1.0.0
		</footer>
  

</body>
</html>