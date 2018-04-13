<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html>
<head>
	<title><sitemesh:title default="欢迎光临"/> - ${site.title} - Powered By yonyou</title>
	<%@include file="/WEB-INF/views/modules/cms/front/include/head.jsp" %>
	<!-- Baidu tongji analytics --><script>var _hmt=_hmt||[];(function(){var hm=document.createElement("script");hm.src="//hm.baidu.com/hm.js?8695d378a6e7e43400b08b7a6dc28a69";var s=document.getElementsByTagName("script")[0];s.parentNode.insertBefore(hm,s);})();</script>
	<sitemesh:head/>
</head>
<body>
    <style>
    #container_1{
   width:100%;
    }
    </style>
    
	<div class="container" id="container_1">
		<div class="content" >
			<sitemesh:body/>
		</div>
		<!-- <hr style="margin:20px 0 10px;"> -->
      	
    </div> <!-- /container -->
</body>
</html>