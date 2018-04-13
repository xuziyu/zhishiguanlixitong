<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <style type="text/css">
  <title>无标题文档</title>
<style type="text/css">

table {
	font-family:Verdana, Arial, Helvetica, sans-serif;
	font-size:12px;
	border-collapse:collapse;
	width:200%; margin:10px auto;
}
th,td {
	border:1px solid #ccc;
	padding:5px 10px;
}
td span{display:block; border-left:1px #999 solid;}
th {
	background:url(table_th.gif) repeat-x;
}
.blue {
	background:#DBECF4;
}
tr:hover {
	color:#000;
}
</style>
</head>

<body>
  <h1 align="center"><font color="#2FA4E7">按时间统计</font></h1>
	<table align="left" border="0" bordercolor="#2FA4E7" cellspacing=0
		cellpadding=0>
		<tr>
			<td width="15%"><select name="year"
				width="120%">
					<option value="1" selected>2010</option>
					<option value="2">2011</option>
					<option value="3">2012</option>
					<option value="4">2013</option>
					<option value="5">2014</option>
					<option value="5">2015</option>
					
			</select><br /></td>
			<td>年</td>
			
			<td width="15%"><select  name="quarter" width="120%">
				
					<option value="1" selected>第一</option>
					<option value="2">第二</option>
					<option value="3">第三</option>
					<option value="4">第四</option>
			</select><br /></td>
			<td>季度</td>
			
			<td width="15%"><select  name="mouth" width="120%">
				
					<option value="1" selected>1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					
					
			</select><br /></td>
			<td>月</td>
			<td>-------</td>
			
			<td width="15%"><select  name="year" width="120%"
				>
					<option value="1" selected>2010</option>
					<option value="2">2011</option>
					<option value="3">2012</option>
					<option value="4">2013</option>
					<option value="5">2014</option>
					<option value="5">2015</option>
			</select><br /></td>
			<td>年</td>
			
			<td width="15%"><select  name="quarter" width="120%">
				
					<option value="1" selected>第一</option>
					<option value="2">第二</option>
					<option value="3">第三</option>
					<option value="4">第四</option>
			</select><br /></td>
			<td>季度</td>
			
			<td width="15%"><select  name="mouth" width="120%">
				
					<option value="1" selected>1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					
					
			</select><br /></td>
			<td>月</td>
			<td><input colar="blue" type="submit" value="查询" name="chaxun"></td>
		</tr>
	</table>
	<table width="100%">
<tr style="background:#2FA4E7">
<th width="25%">序号</th>
<th width="25%">标题</th>
<th width="25%">首页显示</th>
<th width="25%">操作</th>
</tr>
<tr>
<td>1</td>
<td>1</td>
<td><font color=\"red\">显示</font></td>
<td><a href="#">修改</a> | <a href="#">删除</a></td>
</tr>
<tr class="blue">
<td>2</td>
<td>2</td>
<td><font color=\"red\">显示</font></td>
<td><a href="#">修改</a> | <a href="#">删除</a></td>
</tr>
<tr>
<td>3</td>
<td>3</td>
<td><font color=\"red\">显示</font></td>
<td><a href="#">修改</a> | <a href="#">删除</a></td>
</tr>
<tr class="blue">
<td>4</td>
<td>4</td>
<td><font color=\"red\">显示</font></td>
<td><a href="#">修改</a> | <a href="#">删除</a></td>
</tr>
</table>
</body>
</html>
