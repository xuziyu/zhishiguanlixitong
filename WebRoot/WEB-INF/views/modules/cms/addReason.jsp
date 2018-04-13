<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>下架理由</title>
   	<meta name="decorator" content="default"/>
	<script type="text/javascript" >
	</script>
	<style type="text/css">
		.*{
			padding: 0px;
			margin: 0px;
		}
		.addreason{
			margin-top: 70px;
		 	/* margin-left: 15px;  */
		 	text-align:center;
		}
		 .jbox-label{
			font-size: 18px;
			vertical-align: top;
		} 
	</style>
  </head>
  
  <body>
  	<div class="addreason">
  		<label class="jbox-label">理由：</label>
  		<textarea name="reason" rows="5" cols="20"></textarea>
  	</div>
  </body>
</html>
