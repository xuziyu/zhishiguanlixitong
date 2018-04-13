<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>
  		<script src="${ctxStatic}/source-index/jquery-1.7.2.min.js"
			type="text/javascript"></script>
  </head>
  	<script type="text/javascript">
  		$(function(){
  			$("#btn").click(function(){
  				 var searchtext = encodeURIComponent($(".txt").val());
                        if (searchtext != "") {
                            var href = "${ctx_f}/getFrontSearchText" + "?searchtext=" + searchtext;
                            window.location.href = encodeURI(href);
                      }
  			});
  		});
  	</script>
  <body>
  	<input type="text" name="searchText" class="txt"/><button id="btn">搜索</button>
  </body>
</html>
