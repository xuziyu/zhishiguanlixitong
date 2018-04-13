<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<title>知识库</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="${ctxStatic}/source-index/knowledge.css"
			type="text/css">
		<link rel="stylesheet" href="${ctxStatic}/dist/css/sangarSlider.css"
			type="text/css" media="all">
		<link rel="stylesheet"
			href="${ctxStatic}/dist/themes/default/default.css" type="text/css"
			media="all">
		<script src="${ctxStatic}/source-index/jquery-1.7.2.min.js"
			type="text/javascript"></script>
		<style type="text/css">
.abbreviate {
	white-space: nowrap;
	word-break: keep-all;
	overflow: hidden;
	text-overflow: ellipsis
}

.abbreviate.off{
	color:#000;
	background: #abd8dd;}
.juzhong {
	position: absolute;
	top: 30%;
	left: 40%;
	text-align: center;
}

.center_c_c ul li a:hover {
	background: #eee;
	color: #1489c9;
}

.center_c_r ul li a:hover {
	background: #eee;
	color: #1489c9;
}

.more_href {
	float: left;
	margin-left: -35px;
	color: #999999;
}

.more_href:hover {
	color: #1489c9;
}

.banner {
	position: absolute;
	top: 76px;
}

#sangar-example {
	position: absolute;
	top: 0px;
	z-index: 2;
	width: 701px;
	height: 309px;
}

.input3 {
	position: absolute;
	top: 75px;
	left: 115px;
	font-size: 42px;
	line-height: 42px;
	color: #fff;
}

.input4 {
	display: inline-block;
	position: absolute;
	top: 120px;
	left: 115px;
	font-size: 24px;
	line-height: 24px;
	margin: 0 40px 0 0;
	color: #fff;
}

#test1 {
	display: none;
	position: absolute;
	top: -63px;
	left: -30px;
	width: 731px;
	height: 371px;
	Z-index: 100
}

.submenu1 {
	width: 701px;
	height: 355px;
	padding: 14px 0 0 30px;
	margin: 1px 0 0 0;
	border-top: 1px solid #27baf1;
	border-right: 1px solid #27baf1;
	border-bottom: 1px solid #27baf1;
	
}

img {
	cursor: pointer;
}

.leftdiv1 {
	float: left;
	width: 701px;
}

.leftdiv1 dl {
	display: block;
	padding-bottom: 27px;
	overflow: hidden;
}

.leftdiv1 dl dt {
	display: block;
	float: left;
	width: 121px;
	text-align: center;
	height: 27px;
	line-height: 27px;
	margin-right: 17px;
	margin-top: 10px;
	background: #04669c;
}

.leftdiv1 dl dt a {
	display: block;
	
	font-size: 13px;
	cursor: pointer;
	width: 100px;
	padding: 0 15px 0 15px;
	white-space: nowrap;
	word-break: keep-all;
	overflow: hidden;
	text-overflow: ellipsis;
}

.leftdiv1 dl dd {
	display: block;
	overflow: hidden;
}

.leftdiv1 dl dd a {
	display: block;
	float: left;
	
	color: #b3b3b3;
	font-size: 13px;
	text-align: left;
	height: 27px;
	line-height: 27px;
	margin: 10px 15px;
	cursor: auto;
	padding: 0 14px 0 14px;
}

.leftdiv1 dl dd a:hover {
	text-decoration: none;
}

#test1 .blue {
	background: #ddf0f7;
}

#test1 .blue a {
	border-bottom: none;
}

#test1 .blue .submenu1 {
	background: #ddf0f7;
	top: -1px;
}

#test1 .blue .leftdiv1 dl dt {
	background: #ddf0f7;
}

#test1 .blue .leftdiv1 dl dd a {
	border-color: #04669c
}

#test1 .green {
	background:  #ddf0f7;
}

#test1 .green a {
	border-bottom: none;
}

#test1 .green .submenu1 {
	background:  #ddf0f7;
	top: -1px;
}

#test1 .green .leftdiv1 dl dt {
	background:  #ddf0f7;
}

#test1 .green .leftdiv1 dl dd a {
	border-color: #267864;
}

#test1 .orange {
	background:  #ddf0f7;
}

#test1 .orange a {
	border-bottom: none;
}

#test1 .orange .submenu1 {
	background: #ddf0f7;
	top: -1px;
}

#test1 .orange .leftdiv1 dl dt {
	background:  #ddf0f7;
}

#test1 .orange .leftdiv1 dl dd a {
	border-color: #c87800;
}
.people_touxiang{
width:56px;
height:56px;
border-radius:90px;
background-color:#fff;
margin:6px 0px 6px 32px;
}
.PersonalCenter{
width:96px;
height:18px;
top:-84px;
left:88px;
background-color:#999;
position: relative;
font-size:15px;
line-height:15px;
text-align:center;
padding: 25px 0;
}
.top_content_r .people_btn{ float:left;width:88px; height:68px; line-height:41px; color:#fff;text-align:left; font-size:14px;background:#999999;cursor: pointer; }
#HotKnowledge ul li a:hover{
background: #eee;
color: #1489c9;
}
</style>
		<script type="text/javascript">
                function geren(x) {
                    $("body").html("<div class='juzhong'><img src='${ctxStatic}/source-index/images/loading.gif'/><p>正在加载中,请稍候...</p></div>");
                    $(window.parent.document).find("#main span:contains('个人中心')").trigger("click");
                }

                function houtai(x) {
                    $("body").html("<div class='juzhong'><img src='${ctxStatic}/source-index/images/loading.gif'/><p>正在加载中,请稍候...</p></div>");
                    $(window.parent.document).find("#main span:contains('后台管理')").trigger("click");
                }

                function xiaoxi() {
                    $("body").html("<div class='juzhong'><img src='${ctxStatic}/source-index/images/loading.gif'/><p>正在加载中,请稍候...</p></div>");
                    $(window.parent.document).find("#main span:contains('个人中心')").trigger("click");
                }

                function More(x) {
                    var typeid = $(x).attr("id");
                    var href = "${pageContext.request.contextPath}${fns:getFrontPath()}/type-" + "" + typeid + "";
                    window.location.href = href;
                }

                function imageSwitch(x) {

                    var href = $(x).attr('article_url');
                    if (href != "") {
                        //window.location.href=href;
                        window.open(href, "_blank");
                    }
                }

                function sleep(numberMillis) {
                    var now = new Date();
                    var exitTime = now.getTime() + numberMillis;
                    while (true) {
                        now = new Date();
                        if (now.getTime() > exitTime) return;
                    }
                }


                $(function() {
                    //配色方案是静态的,li_1,li_2,li_3,通过动态读取
                    var imageArr=[];
					var imageId=[];
					var secondlistId=[];
					<c:forEach items="${fnc:getCategoryTreeListMap()}" var="list"
						varStatus="state">
						if("${list.image}"!=""&&"${list.image}"!="0"&&"${list.image}"!="1"&&"${list.image}"!="2"&&"${list.image}"!="3"){
						imageArr.push("${list.image}");
						imageId.push("${list.id}");
											}
                    </c:forEach>
                    
                    /**
                    *	这里写对二级知识分类的处理.
                    */
                    var global_num;
					var ID;
					// 初始化用户自己设置的知识库图片
					 $("#topmenu li").each(function(){
					 	var id=$(this).attr("id");
					 	var x="#"+id;
					 	for(var i=0;i<imageId.length;i++){
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	if(imageId[i]==id){
                        	$(x).find("a").css("background","url("+imageArr[i].split(";")[0]+") no-repeat 6px center");
                        	}
                        }
					 });
					 
                    function addcolor(a) {
                        var x = '#' + a;
                        var num = $(x).index();
                        global_num = num;
                        $(x).addClass("lihover").siblings().removeClass("lihover");
                        var d = $("#topmenu li:eq(" + num + ") a").attr("class");
                        if (d[3] % 3 == 0) {
                            $(x).addClass('blue');
                        } else if (d[3] % 3 == 1) {
                            $(x).addClass('green');
                        } else if (d[3] % 3 == 2) {
                            $(x).addClass('orange');
                        }
                        $("#test1").show();
                        var c = '#submenu' + num;
                        $(".submenu1").hide();
                        if (num % 3 == 0) {
                            $(c).addClass('blue');
                        } else if (num % 3 == 1) {
                            $(c).addClass('green');
                        } else if (num % 3 == 2) {
                            $(c).addClass('orange');
                        }
                        $(c).show();
                         for(var i=0;i<imageId.length;i++){
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	if(imageId[i]==a){
                        	//悬停时,知识库换背景图片
                        	//$(x).find("a").css("background","url("+imageArr[i].split(";")[1]+") no-repeat 6px center");
                        	//移除掉当前li的class,加上lihover和对应用户设置的悬浮层的背景色.
                        	$(x).removeClass();
                        	$(x).addClass("lihover").siblings().removeClass("lihover");    	
                        	$(x).addClass(""+imageArr[i].split(";")[2]+"");
                        	//移除悬浮层的class,加上lihover和对应用户设置的悬浮层的背景色.
                        	$(c).removeClass();
                        	$(c).addClass("submenu1");
                        	$(c).addClass(""+imageArr[i].split(";")[2]+"");
                        	}
                        	$(x).css("borderRight","1px solid #ddf0f7").siblings().css("borderRight","1px solid #27baf1");
                        	$(x).css("borderTop","1px solid #27baf1").css("borderLeft","1px solid #27baf1").next().css("borderTop","1px solid #27baf1");
                        }
                    }

                    $("#topmenu li").on({
                        'mouseenter': function() {
                            var id = $(this).attr("id");
                           	ID=id;
                            //$(".test1").css("style","display:block;");
                            timeout = setTimeout(function() {
                                addcolor("" + id + "");
                            }, 200);
                        },
                        'mouseleave': function() {
                            clearTimeout(timeout);
                            $(this).removeClass();
                            
                            //鼠标滑开li时,恢复默认的用户设定的知识库背景图片
                           	for(var i=0;i<imageId.length;i++){
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	if(imageId[i]==ID){
                        	$(this).find("a").css("background","url("+imageArr[i].split(";")[0]+") no-repeat 6px center");
                        	
                        	}
                        	$(this).css("borderRight","none").siblings().css("borderRight","none");
                        	$(this).css("borderTop","1px solid #b3b3b3").css("borderLeft","1px solid #b3b3b3").next().css("borderTop","1px solid #b3b3b3");
                        }
                        //鼠标滑开li时,悬浮层隐藏
                            $("#test1").hide();
                        }
                    });
                    $("#test1").on({
                    
                    //鼠标移入悬浮层时.
                        'mouseenter': function() {
                            $(this).show();
                            //$("#topmenu li:eq(" + global_num + ") a").addClass("lihover").siblings().removeClass("lihover")
                            if (global_num % 3 == 0) {
                                $("#topmenu li:eq(" + global_num + ")").addClass('lihover blue');
                            } else if (global_num % 3 == 1) {
                                $("#topmenu li:eq(" + global_num + ")").addClass('lihover green');
                            } else if (global_num % 3 == 2) {
                                $("#topmenu li:eq(" + global_num + ")").addClass('lihover orange');
                            }
                            //因为之前移除li,清空了样式,移入悬浮层时,就要重新添加之前的样式.
                            for(var i=0;i<imageId.length;i++){
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	var x="#"+ID;
                        	if(imageId[i]==ID){
                        	
                        	//$(x).find("a").css("background","url("+imageArr[i].split(";")[1]+") no-repeat 6px center");
                        	$(x).removeClass();
                        	$(x).addClass("lihover").siblings().removeClass("lihover");
                        	
                        	$(x).addClass(""+imageArr[i].split(";")[2]+"");
                        	var c = '#submenu' +global_num;
                        	$(c).removeClass();
                        	$(c).addClass("submenu1");
                        	$(c).addClass(""+imageArr[i].split(";")[2]+"");
                        	}
                        	
                        	$(x).css("borderRight","1px solid #ddf0f7").siblings().css("borderRight","1px solid #27baf1");
                        	$(x).css("borderTop","1px solid #27baf1").css("borderLeft","1px solid #27baf1").next().css("borderTop","1px solid #27baf1");
                        }
							
                        },
                        //移除悬浮层时,清空li和悬浮层所有的样式,恢复默认设定的知识库图标.
                        'mouseleave': function() {
                            $(this).hide();
                            	for(var i=0;i<imageId.length;i++){
                            	var x="#"+ID;
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	if(imageId[i]==ID){
                        	
                        	$(x).find("a").css("background","url("+imageArr[i].split(";")[0]+") no-repeat 6px center");
                        	
                        	}
                        	$(x).css("borderRight","none").siblings().css("borderRight","none");
                        	$(x).css("borderTop","1px solid #b3b3b3").css("borderLeft","1px solid #b3b3b3").next().css("borderTop","1px solid #b3b3b3");
                        }
                            $("#topmenu li:eq(" + global_num + ")").removeClass();
                            
                        }

                    });
                    $(".btn").click(function() {
                        var searchtext = encodeURIComponent($(".txt").val());
                        if (searchtext != "") {
                            var href = "${ctx_f}/getFrontSearchText" + "?searchtext=" + searchtext;
                            window.location.href = encodeURI(href);
                        }
                    });
                    $(".txt").keydown(function(e) {
                        if (e.which == 13) {
                            $(".btn").trigger("click");
                        }
                    });
                   
					
					
					
                });
            </script>
	</head>

	<body>
		<!--div class="know_nav">
			<p>
				粤海知识库，知识的海洋
			</p>
			<a href=""><img src="${ctxStatic}/source-index/images/HOME.png"
					width="23" height="22"> </a>
		</div-->
		<div class="konw_content">
			<div class="top_content">
				<ul class="top_content_l topmenu" id="topmenu"
					style="overflow-y: auto; overflow-x: hidden;">
					<c:forEach items="${fnc:getCategoryTreeListMap()}" var="list"
						varStatus="state">
						<c:if test="${state.index%3==0}">
							<li id="${list.id}">
								<a
									href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${list.id}"
									class="li_0 abbreviate" title="${list.name}">${list.name}</a>
								<!--  div class="submenu" style="overflow-y: scroll">
									<div class="leftdiv">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a
														href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${firstlist.id}"
														title="${firstlist.name}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
														<a
															href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${secondlist.id}"
															class="abbreviate" title="${secondlist.name}">${secondlist.name}
														</a>
													</c:forEach>

												</dd>
											</dl>
										</c:forEach>
									</div>
								</div-->
							</li>
						</c:if>

						<c:if test="${state.index%3==1}">
							<li id="${list.id}">
								<a
									href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${list.id}"
									class="li_1 abbreviate" title="${list.name}">${list.name}</a>
								<!--  div class="submenu"
									style="overflow-y: scroll; overflow-x: hidden">
									<div class="leftdiv">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a
														href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${firstlist.id}"
														title="${firstlist.name}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
														<a
															href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${secondlist.id}"
															class="abbreviate" title="${secondlist.name}">${secondlist.name}
														</a>
													</c:forEach>

												</dd>
											</dl>
										</c:forEach>
									</div>
								</div-->
							</li>
						</c:if>

						<c:if test="${state.index%3==2}">
							<li id="${list.id}">
								<a
									href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${list.id}"
									class="li_2 abbreviate" title="${list.name}">${list.name}</a>
								<!--  div class="submenu"
									style="overflow-y: scroll; overflow-x: hidden;">
									<div class="leftdiv">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a
														href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${firstlist.id}"
														title="${firstlist.name}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
														<a
															href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${secondlist.id}"
															class="abbreviate" title="${secondlist.name}">${secondlist.name}
														</a>
													</c:forEach>

												</dd>
											</dl>
										</c:forEach>
									</div>
								</div-->
							</li>
						</c:if>

					</c:forEach>
				</ul>
				<div class="top_content_c">
					<div class="search">
						<input type="text" class="txt">
						<input type="button" class="btn" value="知识搜索">
					</div>
					<div class="banner">
						<div class='sangar-slideshow-container ' id='sangar-example'>
							<div class='sangar-content-wrapper'>
								<c:forEach items="${frontswitch}" var="list">
									<div class='sangar-content'>
										<img article_url="${list.articleUrl}"
											src="${list.imageUrl}"
											onclick="imageSwitch(this)" />
										<span class="input3">${list.topicWord}</span>
										<span class="input4">${list.detailExplanation}</span>
									</div>
								</c:forEach>
							</div>
						</div>
						<script type="text/javascript"
							src="${ctxStatic}/dist/js/jquery.touchSwipe.min.js"></script>
						<script type="text/javascript"
							src="${ctxStatic}/dist/js/imagesloaded.min.js"></script>
						<script type="text/javascript"
							src="${ctxStatic}/dist/js/sangarSlider.js"></script>
						<script type="text/javascript">
                                var sangar = $('#sangar-example').sangarSlider({
                                    animation: 'fade', // horizontal-slide, vertical-slide, fade
                                    animationSpeed: 1000, // how fast animtions are
                                    timer: true, // true or false to have the timer
                                    width: 701, // slideshow width
                                    height: 309, // slideshow height
                                    advanceSpeed: 3000 //Set time interval 
                                });
                                $(function() {

                                });
                            </script>
						<div id="test1">
							<c:forEach items="${fnc:getCategoryTreeListMap()}" var="list"
								varStatus="state">
								<div id="submenu${state.index}" class="submenu1"
									style="overflow-y: auto; overflow-x: hidden">
									<div id="leftdiv${state.index}" class="leftdiv1">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a
														href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${firstlist.id}"
														title="${firstlist.name}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
													<c:if test="${secondlist.hasArticle>'0'}">
														<a
															href="${pageContext.request.contextPath}${fns:getFrontPath()}/categoryid-${secondlist.id}"
															title="${secondlist.name}" class="abbreviate off">${secondlist.name}
														</a>
													</c:if>
													<c:if test="${secondlist.hasArticle=='0'}">
														<a class="abbreviate">
															${secondlist.name}
														</a>
													</c:if>
													</c:forEach>
												</dd>
											</dl>
										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				<c:choose>
					<c:when test="${fns:IsOrdinaryUser()=='false'}">
						<div class="top_content_r">
							<div class="h_n noborder_bottom">
								<h2>
									${name}
								</h2>
								<h4 title="${role}" class="abbreviate">
									${role}
								</h4>
								<div class="people_btn" onclick="geren(this);return false">
								<c:choose>
									<c:when test="${user1.photo=='1'}">
										<img class="people_touxiang" src="${ctxStatic}/images/kms_userPhoto.png"/>
									</c:when>
									<c:otherwise>
										<img class="people_touxiang" src="${user1.photo}"></img>
									</c:otherwise>
								</c:choose>
								<div class="PersonalCenter">个人中心</div>
								</div>
							</div>
							<div class="upload upload2">
								<ul>
									<li>
										<a href="" class="up_left" onclick="houtai(this);return false"><span>${fnc:getNumOfExamingArticleByAdmin()}</span>
											<p>
												您待审核的知识数
											</p> </a>
									</li>
									<li>
										<a href="" class="up_right"
											onclick="houtai(this);return false">后台管理</a>
									</li>
								</ul>
								<a href="a/cms/article/add_article" class="myknow">上传知识</a>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="top_content_r">
							<div class="h_n">
								<h2>
									${name}
								</h2>
								<h4 class="abbreviate" title="${role}">
									${role}
								</h4>
								<div class="people_btn" onclick="geren(this);return false">
								
								<c:choose>
									<c:when test="${user1.photo=='1'}">
										<img class="people_touxiang" src="${ctxStatic}/images/kms_userPhoto.png"/>
									</c:when>
									<c:otherwise>
										<img class="people_touxiang" src="${user1.photo}"></img>
									</c:otherwise>
								</c:choose>
								
								<div class="PersonalCenter">个人中心</div>
								</div>
							</div>
							<div class="upload upload2">
								<a href="" onclick="xiaoxi();return false">
									<h5>
										${fnc:getCurrentUserUnReadMsg()}
									</h5>
									<p>
										知识库您待阅读的消息数
									</p> </a>
								<a href="a/cms/article/add_article" class="myknow">上传我的知识</a>
							</div>
						</div>
					</c:otherwise>


				</c:choose>
			</div>

			<div class="center_content">
				<div class="center_c_l">
					<div class="title">
						<h3>
							知识大咖
						</h3>
						<span>知识贡献数</span>
					</div>
					<ul>
						<c:forEach items="${personcontributionlist}" var="list">
							<c:if test="${list.countarticle>0}">
								<li>
									<a style="text-decoration: none; color: black;"
										title="${fnc:getUserOfficeName(userid)}">
										<span>${list.countarticle}&nbsp;&nbsp;</span>
										${list.username}
									</a>
								</li>
							</c:if>
						</c:forEach>


					</ul>
				</div>
				<div class="center_c_l center_c_c">
					<div class="title">
						<h3>
							专题栏
						</h3>
						<a class="more_href" href="${ctx_a}/cms/featurePackage/moreFeaturePackage">更多</a>
						<ol>
							<li style="padding: 0 34px 0 37px;">
								知识数
							</li>
							<li style="padding: 0 34px 0 37px;">
								最近更新日期
							</li>
						</ol>
					</div>
					<ul>
						<c:forEach items="${featurePackageList}" var="list"
							varStatus='status'>
							<li>
								<!--  -->
								<a
									href="${ctx_f}/getArticleFromFeaturePackage?featurePackageId=${list.id}"
									><span><fmt:formatDate value="${list.updateDate}"  type="both" pattern="yyyy-MM-dd HH-mm-ss"/></span><span>${list.count}</span>${status.index+1}.
									${list.name} </a>
							</li>
						</c:forEach>

					</ul>
				</div>
				<div class="center_c_l center_c_r">
					<div class="title">
						<h3>
							热门标签
						</h3>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;知识数</span>
					</div>
					<ul>
						<c:forEach items="${labellist}" var="list">
							<c:if test="${list.countarticle>0}">
								<li>
									<a
										href="${pageContext.request.contextPath}${fns:getFrontPath()}/label-${list.id}"
										id="${list.id}"><span>${list.countarticle}</span>${list.labelvalue
										}</a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
			<!--3-->
			<div class="center_content bottom_centent">
				<div class="center_c_l" id="HotKnowledge">
					<div class="title">
						<h3>
							热门知识
						</h3>
						<span style="margin-left: 17px;">点击数</span>
					</div>
					<ul>
						<c:forEach items="${articlecountlist}" var="list"
							varStatus='status'>
							<li>
								<!--  -->
								<a
									href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${fnc:getArticlecid(list.articleid)}-${list.articleid}${fns:getUrlSuffix()}"
									target="_blank"><span>${list.countclick}</span>
									${list.articletitle} </a>
							</li>
						</c:forEach>

					</ul>
				</div>
				<div class="center_c_l center_c_c">
					<div class="title">
						<h3>
							最新知识
						</h3>
						<a class="more_href" href="" onclick="More(this);return false"
							id="NewArticle">更多</a>
						<ol>

							<li>
								点击数
							</li>
							<li>
								推荐数
							</li>
							<li>
								评论数
							</li>
						</ol>
					</div>
					<ul>
						<c:forEach items="${articlenewlist}" var="list" varStatus='status'>
							<li>
								<!--  -->
								<a
									href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${fnc:getArticlecid(list.articleid)}-${list.articleid}${fns:getUrlSuffix()}"
									target="_blank"><span>${list.countcomm}</span><span>${list.countreco}</span><span>${list.countclick}</span>${status.index+1}.
									${list.articletitle} </a>
							</li>
						</c:forEach>

					</ul>
				</div>
				<div class="center_c_l center_c_r">
					<div class="title">
						<h3>
							热门分享
						</h3>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;分享数</span>
					</div>
					<ul>
						<c:forEach items="${articlesharelist}" var="list">
							<c:if test="${list.countshare>0}">
								<li>
									<a
										href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${fnc:getArticlecid(list.articleid)}-${list.articleid}${fns:getUrlSuffix()}"
										target="_blank"><span>${list.countshare}</span>${list.articletitle}
									</a>
								</li>
							</c:if>
						</c:forEach>

					</ul>
				</div>
			</div>
		</div>
	</body>

</html>
