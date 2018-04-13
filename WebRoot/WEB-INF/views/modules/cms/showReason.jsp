<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>审核不通过原因</title>
   	<meta name="decorator" content="default"/>
	<script type="text/javascript" >
/*		$(document).ready(function(){
			article.
		});*/
	</script>
	<style type="text/css">
		.*{
			padding: 0px;
			margin: 0px;
		}
		.showreason{
			margin-top: 30px;
			margin-left:30px;
			font-size: 15px;
		 /*	text-align:center;*/
		}
	</style>
  </head>
  
  <body>
  	<div class="showreason">
  		${article.reason }
  	</div>
  </body>
</html>
