<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<title>知识库</title>
		<link rel="stylesheet"
			href="${ctxStatic}/source-classify/knowledge.css" type="text/css">
		<style type="text/css">
	      .abbreviate.off {
	        color: #000;
	        background: #abd8dd;
	      }     
	      @ -webkit-keyframes niceIn {
	        0% {
	          opacity: 1;
	          -webkit-transform: scale(1);
	          transform: scale(1)
	        }
	        50% {
	          opacity: 1;
	          -webkit-transform: scale (1 .5);
	          transform: scale (1 .5)
	        }
	        70% {
	          -webkit-transform: scale ( .8);
	          transform: scale ( .8)
	        }
	        100% {
	          opacity: 1;
	          -webkit-transform: scale (1);
	          transform: scale (1)
	        }
	      }   
	      @ keyframes niceIn {
	        0% {
	          opacity: 1;
	          -webkit-transform: scale(1);
	          -ms-transform: scale(1);
	          transform: scale(1)
	        }
	        50% {
	          opacity: 1;
	          -webkit-transform: scale (1 .5);
	          -ms-transform: scale (1 .5);
	          transform: scale (1 .5)
	        }
	        70% {
	          -webkit-transform: scale ( .8);
	          -ms-transform: scale ( .8);
	          transform: scale ( .8)
	        }
	        100% {
	          opacity: 1;
	          -webkit-transform: scale (1);
	          -ms-transform: scale (1);
	          transform: scale (1)
	        }
	      }
	      
	      @ -o-keyframes niceIn {
	        0% {
	          opacity: 1;
	          -o-transform: scale(1);
	          transform: scale(1)
	        }
	        50% {
	          opacity: 1;
	          -o-transform: scale (1 .5);
	          transform: scale (1 .5)
	        }
	        70% {
	          -o-transform: scale ( .8);
	          transform: scale ( .8)
	        }
	        100% {
	          opacity: 1;
	          -o-transform: scale (1);
	          transform: scale (1)
	        }
	      }
	      
	      @ -moz-keyframes niceIn {
	        0% {
	          opacity: 1;
	          -moz-transform: scale(1);
	          transform: scale(1)
	        }
	        50% {
	          opacity: 1;
	          -moz-transform: scale (1 .5);
	          transform: scale (1 .5)
	        }
	        70% {
	          -o-transform: scale ( .8);
	          transform: scale ( .8)
	        }
	        100% {
	          opacity: 1;
	          -moz-transform: scale (1);
	          transform: scale (1)
	        }
	      }
	      
	      .niceIn {
	        -webkit-animation: niceIn 0.8s .2s ease;
	        -moz-animation: niceIn 0.8s .2s ease;
	        -o-animation: niceIn 0.8s .2s ease;
	        animation: niceIn 0.8s .2s ease;
	      }
	      
	      .abbreviate {
	        white-space: nowrap;
	        word-break: keep-all;
	        overflow: hidden;
	        text-overflow: ellipsis
	      }
	      
	      .npage {
	        border: 1px solid;
	      }
	      
	      .banner {
	        position: absolute;
	        top: 76px;
	      }
	      
	      #test1 {
	        display: none;
	        position: absolute;
	        top: -63px;
	        left: -30px;
	        width: 719px;
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
	        font-size: 13px;
	        cursor: pointer;
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
	        width: 100px;
	        text-align: center;
	        height: 27px;
	        line-height: 27px;
	        margin: 10px 15px;
	        cursor: auto;
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
	        background: #1489c9;
	        top: -1px;
	      }
	      
	      #test1 .blue .leftdiv1 dl dt {
	        background: #ddf0f7;
	      }
	      
	      #test1 .blue .leftdiv1 dl dd a {
	        border-color: #04669c
	      }
	      
	      #test1 .green {
	        background: #ddf0f7;
	      }
	      
	      #test1 .green a {
	        border-bottom: none;
	      }
	      
	      #test1 .green .submenu1 {
	        background: #ddf0f7;
	        top: -1px;
	      }
	      
	      #test1 .green .leftdiv1 dl dt {
	        background: #ddf0f7;
	      }
	      
	      #test1 .green .leftdiv1 dl dd a {
	        border-color: #267864;
	      }
	      
	      #test1 .orange {
	        background: #ddf0f7;
	      }
	      
	      #test1 .orange a {
	        border-bottom: none;
	      }
	      
	      #test1 .orange .submenu1 {
	        background: #ddf0f7;
	        top: -1px;
	      }
	      
	      #test1 .orange .leftdiv1 dl dt {
	        background: #ddf0f7;
	      }
	      
	      #test1 .orange .leftdiv1 dl dd a {
	        border-color: #c87800;
	      }
	      </style>
		<script src="${ctxStatic}/source-classify/jquery-1.7.2.min.js"
			type="text/javascript"></script>
		<script defer type="text/javascript">
                (function($) {
                                    	/**
                    	*  新增需求  第三方的不能看到两边 
                    	**/
                    	
                    	/**
                    	*
                    	*/
                    $.extend({
                        tipsBox: function(options) {
                            options = $.extend({
                                obj: null, //jq对象，要在那个html标签上显示
                                str: '+1', //字符串，要显示的内容;也可以传一段html，如: "<b style='font-family:Microsoft YaHei;'>+1</b>"
                                startSize: "12px", //动画开始的文字大小
                                endSize: "30px", //动画结束的文字大小
                                interval: 600, //动画时间间隔
                                color: "red", //文字颜色
                                callback: function() {} //回调函数
                            }, options);
                            $("body").append("<span class='num'>" + options.str + "</span>");
                            var box = $(".num");
                            var left = options.obj.offset().left + options.obj.width() / 2;
                            var top = options.obj.offset().top - options.obj.height();
                            box.css({
                                "position": "absolute",
                                "left": left + "px",
                                "top": top + "px",
                                "z-index": 9999,
                                "font-size": options.startSize,
                                "line-height": options.endSize,
                                "color": options.color
                            });
                            box.animate({
                                "font-size": options.endSize,
                                "opacity": "0",
                                "top": top - parseInt(options.endSize) + "px"
                            }, options.interval, function() {
                                box.remove();
                                options.callback();
                            });
                        }
                    });
                })(jQuery);

                function niceIn(prop) {
                    prop.find('i').addClass('niceIn');
                    setTimeout(function() {
                        prop.find('i').removeClass('niceIn');
                    }, 1000);
                }

                function change(x) {
                    $(".toPage").attr("toPage", "" + $(x).val() + "");
                }

                function upload(x) {
                    var id = $(x).attr("id");
                    var categoryid = "${categorysecondid}";
                    if (categoryid != "") {
                        var href = "${ctx_a}/cms/article/add_article/?category.id=" + "" + categoryid + "";
                        window.location.href = href;
                    } else if (id != "") {
                        var href = "${ctx_a}/cms/article/add_article/?category.id=" + "" + id + "";
                        window.location.href = href;
                    } else {
                        window.location.href = "${ctx_a}/cms/article/add_article/?category.id="
                    }
                }

                function dl_mouseover(x) {
                    $(x).css("background", "#eee");
                }

                function dl_mouseout(x) {
                    $(x).css("background", "#fff");
                }

                function love_click(x) {
                    var favoriteflag = $(x).attr("favoriteflag"); //当前点击项
                    var articleid = $(x).attr("markId");
                    if (favoriteflag == 1) {
                        $.ajax({
                            type: 'GET',
                            url: '${ctx_f}/collect',
                            data: {
                                articleId: articleid
                            },
                            beforeSend: function() {
                                $.tipsBox({
                                    obj: $(x),
                                    str: "+1",
                                    callback: function() {}
                                });
                                niceIn($(this));
                            },
                            success: function() {
                                $(x).removeClass("unfavorite"); //归零
                                $(x).addClass("favorite");
                                $(x).attr("favoriteflag", "0");
                            }
                        });
                    } else {
                        $.ajax({
                            type: 'GET',
                            url: '${ctx_f}/collectdelete',
                            data: {
                                articleId: articleid
                            },
                            success: function() {
                                $(x).addClass("unfavorite");
                                $(x).removeClass("favorite");
                                $(x).attr("favoriteflag", "1");
                            }
                        });
                    }
                }

                //ajax中可重复的方法.
                function success_ajax(data,searchtext) {
                    $('.page').text(1);
                    pageCurrentPage = 1;
                    var str = "";
                    var tt = JSON.stringify(data.pageList);
                    if(searchtext!=""){
                    var searchtext2="<span style='color:red;font-size:14px;font-weight:bold'>"+searchtext+"</span>";
                    }
                    var tt1 = eval("(" + tt + ")");
                    if (tt1 != null) {
                        for (var i = 0; i < tt1.length; i++) {
                            var hasAttFile = "";
                            var hasStore = "<span class='love unfavorite' favoriteflag='1'  onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                           	var examiner="审批人:";
                            var exaname=tt1[i].examiner.name;
                            if(exaname){
                            	examiner=examiner+exaname;
                            }
                            if (tt1[i].articleData.hasAttFile > 0) {
                                hasAttFile = "<span><img src='${ctxStatic}/source-classify/images/zhen.png' width='13' height='14'></span>";
                            }
                            if (tt1[i].articleData.hasStore > 0) {
                                hasStore = "<span class='love favorite' favoriteflag='0' onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                            }
                            if(searchtext!="")
                            {
                            str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title.replace(''+searchtext+'',''+searchtext2+'') + "</a><span  class='fl'>上传时间:<i>" + tt1[i].updateDate +
                                "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                            }
                            else
                            {
                            	str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title + "</a><span  class='fl'>上传时间:<i>" + tt1[i].updateDate +
                                "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                            }
                        }
                    }
                    $(".list").empty();
                    if (str == "") {
                        str = str + "<p>没有检索到相关知识，请重新指定搜索关键字,知识库,或者标签</p>";
                    }
                    $(".list").html(str);
                    if (tt1 != null) {
                        if ((data.currentPage == 1) && (data.currentPage == data.pageNum)) {
                            str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                        } else
                        if (data.currentPage >= data.pageNum) {
                            str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                        } else
                        if (data.currentPage == 1) {
                            str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                        } else
                        if ((data.currentPage != 1) && (data.currentPage != data.pageNum)) {
                            str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                        }
                    } else {
                        $(".list").html("没有检索到相关知识，请重新指定搜索关键字,知识库,或者标签");
                        str = "<a href='javascript:;' class='firstPage' >首页</a>" + "<a href='javascript:;' class='prePage'>上一页</a>" + "<a href='javascript:;' class='nextPage' >下一页</a>" + "<a href='javascript:;' class='lastPage' >尾页</a>"
                    }
                    $(".fenye").empty();
                    $(".fenye").html(str);
                }

                function fail() {
                    $(".list").html("没有检索到相关知识，请重新指定搜索关键字,知识库,或者标签");
                    str = "<a href='javascript:;' class='firstPage' >首页</a>" + "<a href='javascript:;' class='prePage'>上一页</a>" + "<a href='javascript:;' class='nextPage' >下一页</a>" + "<a href='javascript:;' class='lastPage' >尾页</a>"
                    $(".fenye").empty();
                    $(".fenye").html(str);
                }

                function beforeSend() {
                    $(".list").html("... ...");
                }

                $(function() {
                     //配色方案是静态的,li_1,li_2,li_3,通过动态读取
                    var imageArr=[];
					var imageId=[];
					<c:forEach items="${fnc:getCategoryTreeListMap()}" var="list"
						varStatus="state">
						if("${list.image}"!=""&&"${list.image}"!="0"&&"${list.image}"!="1"&&"${list.image}"!="2"&&"${list.image}"!="3"){
						imageArr.push("${list.image}");
						imageId.push("${list.id}");
											}
                              </c:forEach>
                    var global_num;
					var ID;
					// 初始化用户自己设置的知识库图片
					 $("#topmenu li").each(function(){
					 	var id=$(this).attr("id");
					 	var x="#"+id;
					 	for(var i=0;i<imageId.length;i++){
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	if(imageId[i]==id){
                        	$(x).find("a").css("background","url(${ctx1}"+imageArr[i].split(";")[0]+") no-repeat 6px center");
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
                        	//$(x).find("a").css("background","url(${ctx1}"+imageArr[i].split(";")[1]+") no-repeat 6px center");
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
                        	$(this).find("a").css("background","url(${ctx1}"+imageArr[i].split(";")[0]+") no-repeat 6px center");
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
                        	
                        	//$(x).find("a").css("background","url(${ctx1}"+imageArr[i].split(";")[1]+") no-repeat 6px center");
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
                        //如果当前li的id(a)和遍历后符合条件的id数组中的元素匹配
                        	var x="#"+ID;
                        	if(imageId[i]==ID){
                        	
                        	$(x).find("a").css("background","url(${ctx1}"+imageArr[i].split(";")[0]+") no-repeat 6px center");
                        	}
                        	$(x).css("borderRight","none").siblings().css("borderRight","none");
                        	$(x).css("borderTop","1px solid #b3b3b3").css("borderLeft","1px solid #b3b3b3").next().css("borderTop","1px solid #b3b3b3");
                        }
                            $("#topmenu li:eq(" + global_num + ")").removeClass();
                            
                        }

                    });
                });
                <!--right-->
                $(function() {
                    <%--
                    $('.top_content_r .black ul li').each(function() {
                        var i = 1;
                        $(this).click(function() {
                            if (i == 1) {
                                $(this).addClass('current');
                                i = 2;
                            } else if (i == 2) {
                                $(this).removeClass('current');
                                i = 1;
                            }
                        })
                    });--%>
                    //控制从label第几条开始的隐藏
                    /*var $label = $(".top_content_r .black .show li:gt(5)");
                    $label.hide();
                    $('.top_content_r .black .show .click').click(function() {
                        var flag = $(this).attr("flag");
                        if (flag == 1) {
                            $(this).text("隐藏").attr("flag", "0");
                            $label.show();
                            //var blackheight = $(".black").outerHeight() - 20 + "px";
                            //$(this).attr("style", "top:" + blackheight + "");
                        } else {
                            $(this).text("显示").attr("flag", "1");
                            $label.hide();
                            //var showheight = $(".black").outerHeight() - 20 + "px";
                            //$(this).attr("style", "top:" + showheight + "");
                        }
                    });*/
                });
                /*center*/
                $(function() {
                    $('.banner_position .list dl dd .look .love').click(function() {
                        var favoriteflag = $(this).attr("favoriteflag"); //当前点击项
                        if (favoriteflag == 1) {
                            $(this).removeClass("unfavorite"); //归零
                            $(this).addClass("favorite");
                            $(this).attr("favoriteflag", "0");
                        } else {
                            $(this).addClass("unfavorite");
                            $(this).removeClass("favorite");
                            $(this).attr("favoriteflag", "1");
                        }
                    });
                });
                /*	list*/
                $(function() {
                    $('.banner_position .list dl').mouseover(function() {
                        $(this).css('background', '#f7f7f7');
                        //$(this).find('.fenlei').css('display','block');
                    }).mouseout(function() {
                        $(this).css('background', '#fff');
                        //$(this).find('.fenlei').css('display','none');
                    })
                });

                /* articlelist*界面点击事件获取id存在这个界面的变量中*/
                $(function() {
                    //这里声明整个界面的变量
                    var pageLabelID = [];
                    var pageCategoryID = "${categoryid}";
                    var pageCurrentPage = 1;
                    var pageSearchText="";
                    //首页搜索框,热门标签在这里模拟触发.
                    $(function() {
                        var searchcontent = "${searchcontent}";
                        //判断首页进来的searctext是否为空
                        if (searchcontent != "") {
                            $(".txt").val("" + searchcontent + "");
                            //把它记录到pageSearchText，在点击分页时利用这个参数。
                           pageSearchText=searchcontent;
                            var searchtext = encodeURIComponent("" + searchcontent + "");
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: "",
                                    categoryid: "",
                                    searchtext: searchtext,
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data,searchcontent);
                                },
                                error: function() {
                                    fail();
                                }
                            });
                        }
                        var labelid = "${labelid}";
                        var labelids = [];
                        labelids.push(labelid);
                        if (labelid != "") {
                            var c = $(".biaoqian a").length;
                            for (var i = 0; i < c; i++) {
                                var id = $(".biaoqian:eq(" + i + ") a").attr("id");
                                if (labelid == id) {
                                    //上面都是对.show下那一组li的监听事件.
                                    $(".biaoqian").eq("" + i + "").addClass("current");
                                    break;
                                }
                            }
                            pageLabelID = labelids;
                            $.ajax({
                                type: "POST",
                                url: "${ctx_f}/getSearchID",
                                data: {
                                    labelid: labelids.join(),
                                    categoryid: "",
                                    searchtext: "",
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data);
                                },
                                error: function() {
                                    fail();
                                }
                            });
                        }
                    });

                    function indexbyelement(arr, e) {
                        for (var i = 0; i < arr.length; i++) {
                            if (arr[i] == e) {
                                return i;
                            }
                        }
                        return -1;
                    };
                    $(function() {
                        $(".biaoqian").on('click', function() {
                            var labelid = pageLabelID;
                            var num = $(".biaoqian").index(this);
                            var id = $(".biaoqian a:eq(" + num + ")").attr("id");
                            if ($(this).hasClass("current")) {
                                $(this).removeClass("current");
                            } else {
                                $(this).addClass("current");
                            }
                            if (labelid.length == 0) {
                                labelid.push(id);
                            } else {
                                var b = indexbyelement(labelid, id);
                                if (b > -1) {
                                    labelid.splice(b, 1); //如果有该元素,就移出去
                                } else {
                                    labelid.push(id);
                                }
                            }
                            //将此时记录的选中的labelid的状态存入到全局labelid变量中.
                            pageLabelID = labelid;
                            //将全局变量的知识id和文本内容传给现在要传入到controller的变量中.
                            var categoryid = pageCategoryID;
                            // var searchtext=pageSearchText;
                            //点击标签的时候,将搜索框置为空.
                            $(".txt").val("");
                            var searchtext = $(".txt").val();
                            pageSearchText=searchtext;
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data);
                                },
                                error: function() {
                                    fail()
                                }

                            });
                        });
                    });



                    /*articlelist界面点击事件获取库左边的id*/
                    $(function() {
                    
                        //库id的获取
                        $("#topmenu li>a").click(function() {
                            $("#topmenu li").removeClass();
                            $("#test1").hide();
                            var labelid = pageLabelID;
                            //这里也不用取得全局变量中的categoryid。只需要将取得当前的categoryid给全局，在别的事件中获取就行。
                            //var categoryid = pageCategoryID;
                            var categoryid = $(this).attr("id");
                            /*
                            	不需要经过这个处理，因为categoryid是单选事件。
                            if (categoryid == id) {
                                //如果里面已经装有categoryid,此时又与获取的id一致,证明是取消选中的categoryid.
                                categoryid = "";
                            } else {
                                categoryid = id;
                            }
                            */
                            //将此时记录的categoryid存到全局变量中.
                            pageCategoryID = categoryid;
                            // var searchtext=pageSearchText;
                            //点击库id的时候,将搜索框置为空.
                            $(".txt").val("");
                            var searchtext = $(".txt").val();
                            pageSearchText=searchtext;
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data);
                                },
                                error: function() {
                                    fail();
                                }
                            });
                        });

                        //一级分类id的获取
                        $(".leftdiv1 dl >dt>a").click(function() {
                            $("#topmenu li").removeClass();
                            $("#test1").hide();
                            var labelid = pageLabelID;
                            //这里也不用取得全局变量中的categoryid。只需要将取得当前的categoryid给全局，在别的事件中获取就行。
                            //var categoryid = pageCategoryID;
                            var categoryid = $(this).attr("id");
                            /*
                            	不需要经过这个处理，因为categoryid是单选事件。
                            if (categoryid == id) {
                                //如果里面已经装有categoryid,此时又与获取的id一致,证明是取消选中的categoryid.
                                categoryid = "";
                            } else {
                                categoryid = id;
                            }
                            */
                            //将此时记录的categoryid存到全局变量中.
                            pageCategoryID = categoryid;
                            // var searchtext=pageSearchText;
                            //点击库id的时候,将搜索框置为空.
                            $(".txt").val("");
                            var searchtext = $(".txt").val();
                            pageSearchText=searchtext;
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data);
                                },
                                error: function() {
                                    fail();
                                }
                            });
                        });

                        //二级分类id的获取
                        $(".leftdiv1 dl >dd>a").click(function() {
                            $("#topmenu li").removeClass();
                            $("#test1").hide();
                            var labelid = pageLabelID;
                            //这里也不用取得全局变量中的categoryid。只需要将取得当前的categoryid给全局，在别的事件中获取就行。
                            //var categoryid = pageCategoryID;
                            var categoryid = $(this).attr("id");
                            /*
                            	不需要经过这个处理，因为categoryid是单选事件。
                            if (categoryid == id) {
                                //如果里面已经装有categoryid,此时又与获取的id一致,证明是取消选中的categoryid.
                                categoryid = "";
                            } else {
                                categoryid = id;
                            }
                            */
                            //将此时记录的categoryid存到全局变量中.
                            pageCategoryID = categoryid;
                            // var searchtext=pageSearchText;
                            //点击库id的时候,将搜索框置为空.
                            $(".txt").val("");
                            var searchtext = $(".txt").val();
                            pageSearchText=searchtext;
                            //把二级分类的id给上传我的知识.
                            $(".upload_article").attr("id", "" + categoryid + "");
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data);
                                },
                                error: function() {
                                    fail();
                                }
                            });
                        });
                        //搜索按钮的监听事件,按回车也可以触发
                        $(".btn").on('click', function() {
                            var labelid = pageLabelID;
                            var categoryid = pageCategoryID;
                            var searchtext = $(".txt").val();
                            //赋给全局变量
                            pageSearchText = searchtext;
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: 1
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    success_ajax(data,searchtext);
                                },
                                error: function() {
                                    fail();
                                }
                            });
                        });
                        //回车触发事件,搜索框不为空时
                        $(".txt").keydown(function(e) {
                            if (e.which == 13 && $(this).val() != "") {
                                $(".btn").trigger("click");
                            }
                        });
                        //知识分类界面的getSearchID的ajax分页
                        $(".fenye").on('click', function() {
                            var e = event.srcElement ? event.srcElement : event.target;
                            // var e = event.srcelement || event.target;
                            if (!$(e).attr("toPage")) return;
                            var labelid = pageLabelID;
                            var categoryid = pageCategoryID;
                            var searchtext = pageSearchText;
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getSearchID',
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: $(e).attr("toPage")
                                },
                                /*    dataType:'json',*/
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    var str = "";
                                    var tt = JSON.stringify(data.pageList);
                                    var tt1 = eval("(" + tt + ")");
                                     if(searchtext !=null&&searchtext!=""){
                    var searchtext2="<span style='color:red;font-size:14px;font-weight:bold'>"+searchtext+"</span>";
                    }
                                    for (var i = 0; i < tt1.length; i++) {
                                        var hasAttFile = "";
                                        var examiner="审批人:";
                           				 var exaname=tt1[i].examiner.name;
                            			if(exaname){
                            				examiner=examiner+exaname;
                            			}
                                        var hasStore = "<span class='love unfavorite' favoriteflag='1'  onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                                        if (tt1[i].articleData.hasAttFile > 0) {
                                            hasAttFile = "<span><img src='${ctxStatic}/source-classify/images/zhen.png' width='13' height='14'></span>";
                                        }
                                        if (tt1[i].articleData.hasStore > 0) {
                                            hasStore = "<span class='love favorite' favoriteflag='0' onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                                        }
                                        if(searchtext!="")
                            {
                            str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title.replace(''+searchtext+'',''+searchtext2+'') + "</a><span class='fl'>上传时间:<i>" + tt1[i].updateDate +
                                "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                            }
                            else
                            {
                            	str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title + "</a><span  class='fl'>上传时间:<i>" + tt1[i].updateDate +
                                "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                            }
                                        
                                    }
                                    $(".list").empty();
                                    $(".list").html(str);
                                    if ((data.currentPage == 1) && (data.currentPage == data.pageNum)) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if (data.currentPage >= data.pageNum) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if (data.currentPage == 1) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if ((data.currentPage != 1) && (data.currentPage != data.pageNum)) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    }
                                    $(".fenye").empty();
                                    $(".fenye").html(str);

                                },
                                error: function() {
                                    fail()
                                }

                            });
                        });
                        
                        //add by linj begin
                        //知识分类界面的getSearchID的ajax分页
                        $("#articleOfFeaturePackageFenye").on('click', function() {
                            /*var e = event.srcElement ? event.srcElement : event.target;
                            if (!$(e).attr("toPage")) return;
                            var labelid = pageLabelID;
                            var categoryid = pageCategoryID;
                            var searchtext = pageSearchText;
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/getPageList?featurePackageId=${featurePackageId}&currentPage='+currentPage,
                                data: {
                                    labelid: labelid.join(),
                                    categoryid: categoryid,
                                    searchtext: searchtext,
                                    currentPage: $(e).attr("toPage")
                                },
                                beforeSend: beforeSend(),
                                success: function(data) {
                                    var str = "";
                                    var tt = JSON.stringify(data.pageList);
                                    var tt1 = eval("(" + tt + ")");
                                     if(searchtext !=null&&searchtext!=""){
                    var searchtext2="<span style='color:red;font-size:14px;font-weight:bold'>"+searchtext+"</span>";
                    }
                                    for (var i = 0; i < tt1.length; i++) {
                                        var hasAttFile = "";
                                        var examiner="审批人:";
                           				 var exaname=tt1[i].examiner.name;
                            			if(exaname){
                            				examiner=examiner+exaname;
                            			}
                                        var hasStore = "<span class='love unfavorite' favoriteflag='1'  onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                                        if (tt1[i].articleData.hasAttFile > 0) {
                                            hasAttFile = "<span><img src='${ctxStatic}/source-classify/images/zhen.png' width='13' height='14'></span>";
                                        }
                                        if (tt1[i].articleData.hasStore > 0) {
                                            hasStore = "<span class='love favorite' favoriteflag='0' onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                                        }
                                        if(searchtext!="")
                            {
                            str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title.replace(''+searchtext+'',''+searchtext2+'') + "</a><span>上传时间:<i>" + tt1[i].updateDate +
                                "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                            }
                            else
                            {
                            	str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title + "</a><span>上传时间:<i>" + tt1[i].updateDate +
                                "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                            }
                                        
                                    }
                                    $(".list").empty();
                                    $(".list").html(str);
                                    if ((data.currentPage == 1) && (data.currentPage == data.pageNum)) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if (data.currentPage >= data.pageNum) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if (data.currentPage == 1) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if ((data.currentPage != 1) && (data.currentPage != data.pageNum)) {
                                        str = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    }
                                    $(".fenye").empty();
                                    $(".fenye").html(str);

                                },
                                error: function() {
                                    fail()
                                }

                            });*/
                        });
                        //add by linj end
                        
                        //more分页，对于首页更多知识的分页
                        $(".fenyemore").on('click', function() {
                            var e = event.srcElement ? event.srcElement : event.target;
                            //var e = event.srcelement || event.target;
                            if (!$(e).attr("toPage")) return;
                            var tid = "${typeid}";
                            var featurePackageId = "${featurePackageId}";
                            var data = {};
                            if(featurePackageId){
                            	data = {
                                    'typeid': tid,
                                    currentPage: $(e).attr("toPage"),
                                    'featurePackageId' : featurePackageId
                                } 
                            }else{
                            	data = {
                                    'typeid': tid,
                                    currentPage: $(e).attr("toPage"),
                                } 
                            }
                            $.ajax({
                                type: "POST",
                                url: '${ctx_f}/morefenye',
                                data: data,
                                dataType: 'json',
                                beforeSend: beforeSend(),
                                success: function(data1) {
                                    var data = JSON.parse(data1.split);
                                    var str = "";
                                    var st = ""
                                    var tt = JSON.stringify(data.pageList);
                                    var tt1 = eval("(" + tt + ")");
                                    for (var i = 0; i < tt1.length; i++) {
                                        var hasAttFile = "";
                                        var examiner="审批人:";
                            			var exaname=tt1[i].examiner.name;
                            			if(exaname){
                            				examiner=examiner+exaname;
                            			}
                                        var hasStore = "<span class='love unfavorite' favoriteflag='1' onclick='love_click(this)' markId='" + tt1[i].id + "'>收藏</span>";
                                        if (tt1[i].articleData.hasAttFile > 0) {
                                            hasAttFile = "<span><img src='${ctxStatic}/source-classify/images/zhen.png' width='13' height='14'></span>";
                                        }
                                        if (tt1[i].articleData.hasStore > 0) {
                                            hasStore = "<span class='love favorite' favoriteflag='0' onclick='love_click(this)' markId='" + tt1[i].id + "' >收藏</span>";
                                        }
                                        str = str + "<dl onmouseover='dl_mouseover(this)' onmouseout='dl_mouseout(this)'><dt><a href='" + tt1[i].url + "'  target='_blank' class='abbreviate'>" + tt1[i].title + "</a><span  class='fl'>上传时间<i>" + tt1[i].updateDate +
                                            "</i><i style='margin-left:20px'>"+examiner+"</i></span><div class='fenlei'><i title='" + tt1[i].path + "' class='abbreviate'>" + tt1[i].category.name + "</i>" + hasAttFile + "</div></dt><dd><p><i>" + tt1[i].hits +
                                            "</i>人已阅读</p><div class='look'><a href='" + tt1[i].url + "' target='_blank' class='scan'>查看阅读</a>" + hasStore + "</div></dd></dl>"
                                    }
                                    $(".list").empty();
                                    $(".list").html(str);
                                    if ((data.currentPage == 1) && (data.currentPage == data.pageNum)) {
                                        st = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if (data.currentPage >= data.pageNum) {
                                        st = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if (data.currentPage == 1) {
                                        st = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    } else
                                    if ((data.currentPage != 1) && (data.currentPage != data.pageNum)) {
                                        st = "<a href='javascript:;' class='firstPage' toPage='1'>首页</a>" + " <a href='javascript:;' class='prePage' toPage=" + (data.currentPage - 1) + ">上一页</a>" + "<a href='javascript:;' class='nextPage' toPage=" + (data.currentPage + 1) + ">下一页</a>" + "<a href='javascript:;' class='lastPage' toPage=" + (data.pageNum) + ">尾页</a>" + "跳转至" + " <input type='text' name='text' class='npage' onchange='change(this)'  value=" + data.currentPage + "  style='text-align:center'  size='1' >" + "页" + "<a href='javascript:;' class='toPage' toPage=" + (data.currentPage) + ">GO</a>" + "共" + (data.pageNum) + "页"
                                    }
                                    $(".fenyemore").empty();
                                    $(".fenyemore").html(st);


                                },
                                error: function() {
                                    fail()
                                }

                            });
                        });



                    });


                });
            </script>
	</head>

	<body>
		<div class="konw_content knowledge_search">
			<div class="top_content">
				 <c:choose>
				 	<c:when test="${third!='1' }">
		 						<ul class="top_content_l topmenu" id="topmenu"
							style="overflow-y: auto; overflow-x: hidden;">
				 	</c:when>
				 	<c:otherwise>
				 				<ul class="top_content_l topmenu" id="topmenu"
					style="overflow-y: auto; overflow-x: hidden;border:0px solid #FFF;background:#F7F7F7">
				 	</c:otherwise>
				 </c:choose>
					<c:if test="${third!='1' }">
					<c:forEach items="${fnc:getCategoryTreeListMap()}" var="list"
						varStatus="state">
						<c:if test="${state.index%3==0}">
							<li id="${list.id}">
								<a href="#" class="li_0 abbreviate" title="${list.name}"
									id="${list.id}">${list.name}</a>
								<div class="submenu">
									<div class="leftdiv">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a href="#" class="abbreviate" title="${firstlist.name}"
														id="${firstlist.id}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
														<a href="#" class="abbreviate" title="${secondlist.name}"
															id="${secondlist.id}">${secondlist.name} </a>
													</c:forEach>

												</dd>
											</dl>
										</c:forEach>
									</div>
								</div>
							</li>
						</c:if>

						<c:if test="${state.index%3==1}">
							<li id="${list.id}">
								<a href="#" class="li_1 abbreviate" title="${list.name}"
									id="${list.id}">${list.name}</a>
								<div class="submenu">
									<div class="leftdiv">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a href="#" class="abbreviate" title="${firstlist.name}"
														id="${firstlist.id}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
														<a href="#" class="abbreviate" title="${secondlist.name}"
															id="${secondlist.id}">${secondlist.name} </a>
													</c:forEach>

												</dd>
											</dl>
										</c:forEach>
									</div>
								</div>
							</li>
						</c:if>

						<c:if test="${state.index%3==2}">
							<li id="${list.id}">
								<a href="#" class="li_2 abbreviate" title="${list.name}"
									id="${list.id}">${list.name}</a>
								<div class="submenu">
									<div class="leftdiv">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a href="#" class="abbreviate" title="${firstlist.name}"
														id="${firstlist.id}">${firstlist.name}</a>
												</dt>
												<dd>
													<c:forEach items="${firstlist.secondlist}" var="secondlist">
														<a href="#" class="abbreviate" title="${secondlist.name}"
															id="${secondlist.id}">${secondlist.name} </a>
													</c:forEach>

												</dd>
											</dl>
										</c:forEach>
									</div>
								</div>
							</li>
						</c:if>

					</c:forEach>
					</c:if>
				</ul>
				<div class="top_content_c">
					<c:if test="${third!='1'}">
					<div class="search">
						<input type="text" class="txt" id="txt">
						<input type="button" class="btn" value="知识搜索">
					</div>
					</c:if>
					<div class="banner">
						<div id="test1">
							<c:forEach items="${fnc:getCategoryTreeListMap()}" var="list"
								varStatus="state">
								<div id="submenu${state.index}" class="submenu1"
									style="overflow-y: auto; overflow-x: hidden">
									<div id="leftdiv${state.index}" class="leftdiv1">
										<c:forEach items="${list.firstlist}" var="firstlist">
											<dl>
												<dt class="abbreviate">
													<a href="#" title="${firstlist.name}" id="${firstlist.id}">${firstlist.name}</a>
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
					<div class="banner_position">
						<c:choose>
							<c:when test="${typeid=='NewArticle'}">
								<h3>
									搜索结果
								</h3>
								<div class="list">
									<c:if test="${!empty split.pageList}">
										<c:forEach items="${split.pageList}" var="list">
											<dl>
												<dt>
													<a href="${list.url}" target="_blank" class="abbreviate">${list.title}</a><span  class='fl'>上传时间:<i><fmt:formatDate
																value="${list.updateDate}" type="both" />
													</i> 
													<i>
														<c:if test="${not empty list.examiner.name}">
															审批人:${list.examiner.name}
														</c:if>
														<c:if test="${empty list.examiner.name }">
															审批人
														</c:if>
													</i>
													
													</span>
													<div class="fenlei">
														<i title="${list.path}" class="abbreviate">${list.category.name}
														</i>
														<c:if test="${list.articleData.hasAttFile>0}">
															<span><img
																	src="${ctxStatic}/source-classify/images/zhen.png"
																	width="13" height="14"> </span>
														</c:if>
													</div>
												</dt>
												<dd>
													<p>
														<i>${list.hits}</i>人已阅读
													</p>
													<div class="look">
														<a href="${list.url}" target="_blank" class="scan">查看阅读</a><span
															<c:if test="${list.articleData.hasStore==0}">
													<span
														class="love unfavorite" favoriteflag='1'  onclick='love_click(this)' markId='${list.id}'>收藏</span>
                                        </c:if>
															<c:if test="${list.articleData.hasStore>0}">
                                            <span class="love favorite" favoriteflag='0' onclick='love_click(this)' markId='${list.id}'>收藏</span>
                                        </c:if>
													</div>
												</dd>
											</dl>
										</c:forEach>
									</c:if>
									<c:if test="${empty split.pageList}">
										<p>
											暂时无最新知识... ...
										</p>
									</c:if>
								</div>

								<div class="fenye">
									<c:if test="${!empty split.pageList}">
										<div class="fenyemore">
											<a href='javascript:;' class='firstPage' toPage='1'>首页</a>
											<c:if test="${split.currentPage!='1'}">
												<a href='javascript:;' class='prePage'
													toPage="${split.currentPage}">上一页</a>
											</c:if>
											<c:if test="${split.currentPage!=split.pageNum}">
												<a href='javascript:;' class='nextPage'
													toPage="${split.currentPage+1}">下一页</a>
											</c:if>
											<a href='javascript:;' class='lastPage'
												toPage="${split.pageNum}">尾页</a> 跳转至
											<input type='text' name='text' class='npage'
												onchange="change(this)" value="${split.currentPage}"
												style="text-align: center" size="1">
											页
											<a href='javascript:;' class='toPage'
												toPage="${split.currentPage}">GO</a> 共${split.pageNum}页
										</div>
									</c:if>
									<c:if test="${empty split.pageList}">
										<div class="fenyemore">
											<a href='javascript:;' class='firstPage'>首页</a>
											<a href='javascript:;' class='prePage'>上一页</a>
											<a href='javascript:;' class='nextPage'>下一页</a>
											<a href='javascript:;' class='lastPage'>尾页</a>
										</div>
									</c:if>

								</div>
							</c:when>
							
							<c:when test="${typeid=='PopularArticle'}">
								<h3>
									搜索结果
								</h3>
								<div class="list">
									<c:if test="${!empty split.pageList}">
										<c:forEach items="${split.pageList}" var="list">
											<dl>
												<dt>
													<a href="${list.url}" target="_blank" class="abbreviate">${list.title}</a><span  class='fl'>上传时间:<i><fmt:formatDate
																value="${list.updateDate}" type="both" />
													</i> 
													<i>
														<c:if test="${not empty list.examiner.name}">
															审批人:${list.examiner.name}
														</c:if>
														<c:if test="${empty list.examiner.name }">
															审批人
														</c:if>
													</i>
													
													</span>
													<div class="fenlei">
														<i title="${list.path}" class="abbreviate">${list.category.name}
														</i>
														<c:if test="${list.articleData.hasAttFile>0}">
															<span><img
																	src="${ctxStatic}/source-classify/images/zhen.png"
																	width="13" height="14"> </span>
														</c:if>
													</div>
												</dt>
												<dd>
													<p>
														<i>${list.hits}</i>人已阅读
													</p>
													<div class="look">
														<a href="${list.url}" target="_blank" class="scan">查看阅读</a><span
															<c:if test="${list.articleData.hasStore==0}">
													<span
														class="love unfavorite" favoriteflag='1'  onclick='love_click(this)' markId='${list.id}'>收藏</span>
                                        </c:if>
															<c:if test="${list.articleData.hasStore>0}">
                                            <span class="love favorite" favoriteflag='0' onclick='love_click(this)' markId='${list.id}'>收藏</span>
                                        </c:if>
													</div>
												</dd>
											</dl>
										</c:forEach>
									</c:if>
									<c:if test="${empty split.pageList}">
										<p>
											暂时无热门知识... ...
										</p>
									</c:if>
								</div>

								<div class="fenye">
									<c:if test="${!empty split.pageList}">
										<div class="fenyemore">
											<a href='javascript:;' class='firstPage' toPage='1'>首页</a>
											<c:if test="${split.currentPage!='1'}">
												<a href='javascript:;' class='prePage'
													toPage="${split.currentPage}">上一页</a>
											</c:if>
											<c:if test="${split.currentPage!=split.pageNum}">
												<a href='javascript:;' class='nextPage'
													toPage="${split.currentPage+1}">下一页</a>
											</c:if>
											<a href='javascript:;' class='lastPage'
												toPage="${split.pageNum}">尾页</a> 跳转至
											<input type='text' name='text' class='npage'
												onchange="change(this)" value="${split.currentPage}"
												style="text-align: center" size="1">
											页
											<a href='javascript:;' class='toPage'
												toPage="${split.currentPage}">GO</a> 共${split.pageNum}页
										</div>
									</c:if>
									<c:if test="${empty split.pageList}">
										<div class="fenyemore">
											<a href='javascript:;' class='firstPage'>首页</a>
											<a href='javascript:;' class='prePage'>上一页</a>
											<a href='javascript:;' class='nextPage'>下一页</a>
											<a href='javascript:;' class='lastPage'>尾页</a>
										</div>
									</c:if>

								</div>
							</c:when>
							
							<c:when test="${typeid=='featurePackage'}">
								<h3>
									搜索结果
								</h3>
								<div class="list">
									<c:if test="${!empty split.pageList}">
										<c:forEach items="${split.pageList}" var="list">
											<dl>
												<dt>
													<a href="${list.url}" target="_blank" class="abbreviate">${list.title}</a><span  class='fl'>上传时间:<i><fmt:formatDate
																value="${list.updateDate}" type="both" />
													</i> 
													<i>
														<c:if test="${not empty list.examiner.name}">
															审批人:${list.examiner.name}
														</c:if>
														<c:if test="${empty list.examiner.name }">
															审批人
														</c:if>
													</i>
													
													</span>
													<div class="fenlei">
														<i title="${list.path}" class="abbreviate">${list.category.name}
														</i>
														<c:if test="${list.articleData.hasAttFile>0}">
															<span><img
																	src="${ctxStatic}/source-classify/images/zhen.png"
																	width="13" height="14"> </span>
														</c:if>
													</div>
												</dt>
												<dd>
													<p>
														<i>${list.hits}</i>人已阅读
													</p>
													<div class="look">
														<a href="${list.url}" target="_blank" class="scan">查看阅读</a><span
															<c:if test="${list.articleData.hasStore==0}">
													<span
														class="love unfavorite" favoriteflag='1'  onclick='love_click(this)' markId='${list.id}'>收藏</span>
                                        </c:if>
															<c:if test="${list.articleData.hasStore>0}">
                                            <span class="love favorite" favoriteflag='0' onclick='love_click(this)' markId='${list.id}'>收藏</span>
                                        </c:if>
													</div>
												</dd>
											</dl>
										</c:forEach>
									</c:if>
									<c:if test="${empty split.pageList}">
										<p>
											该专题下暂无知识... ...
										</p>
									</c:if>
								</div>

								<div class="fenye" id="articleOfFeaturePackageFenye">
									<c:if test="${!empty split.pageList}">
										<div class="fenyemore">
											<a href='javascript:;' class='firstPage' toPage='1'>首页</a>
											<c:if test="${split.currentPage!='1'}">
												<a href='javascript:;' class='prePage'
													toPage="${split.currentPage}">上一页</a>
											</c:if>
											<c:if test="${split.currentPage!=split.pageNum}">
												<a href='javascript:;' class='nextPage'
													toPage="${split.currentPage+1}">下一页</a>
											</c:if>
											<a href='javascript:;' class='lastPage'
												toPage="${split.pageNum}">尾页</a> 跳转至
											<input type='text' name='text' class='npage'
												onchange="change(this)" value="${split.currentPage}"
												style="text-align: center" size="1">
											页
											<a href='javascript:;' class='toPage'
												toPage="${split.currentPage}">GO</a> 共${split.pageNum}页
										</div>
									</c:if>
									<c:if test="${empty split.pageList}">
										<div class="fenyemore">
											<a href='javascript:;' class='firstPage'>首页</a>
											<a href='javascript:;' class='prePage'>上一页</a>
											<a href='javascript:;' class='nextPage'>下一页</a>
											<a href='javascript:;' class='lastPage'>尾页</a>
										</div>
									</c:if>

								</div>
							</c:when>
							
							<c:otherwise>
								<h3>
									搜索结果
								</h3>
								<div class="list">
									<c:if test="${!empty split.pageList}">
										<c:forEach items="${split.pageList}" var="list">
											<dl>
												<dt>
													<a href="${list.url}" target="_blank" class="abbreviate">${list.title}</a><span  class='fl'>上传时间:<i><fmt:formatDate
																value="${list.updateDate}" type="both" />
													</i> 
													<i>
														<c:if test="${not empty list.examiner.name}">
															审批人:${list.examiner.name}
														</c:if>
														<c:if test="${empty list.examiner.name }">
															审批人
														</c:if>
													</i>
													
													</span>
													<div class="fenlei">
														<i title="${list.path}" class="abbreviate">${list.category.name}
														</i>
														<c:if test="${list.articleData.hasAttFile>0}">
															<span><img
																	src="${ctxStatic}/source-classify/images/zhen.png"
																	width="13" height="14"> </span>
														</c:if>
													</div>
												</dt>
												<dd>
													<p>
														<i>${list.hits}</i>人已阅读
													</p>
													<div class="look">
														<a href="${list.url}" target="_blank" class="scan">查看阅读</a>
														<c:if test="${list.articleData.hasStore==0}">
															<span class="love unfavorite" favoriteflag='1'
																onclick='love_click(this)' markId='${list.id}'>收藏</span>
														</c:if>
														<c:if test="${list.articleData.hasStore>0}">
															<span class="love favorite" favoriteflag='0'
																onclick='love_click(this)' markId='${list.id}'>收藏</span>
														</c:if>
													</div>
												</dd>
											</dl>
										</c:forEach>
									</c:if>
									<c:if test="${empty split.pageList}">
										<p>
											没有对应的知识或没有相应的权限...
										</p>
									</c:if>
								</div>
								<div class="fenye">
									<c:if test="${!empty split.pageList}">
										<a href='javascript:;' class='firstPage' toPage='1'>首页</a>
										<c:if test="${split.currentPage!='1'}">
											<a href='javascript:;' class='prePage'
												toPage="${split.currentPage}">上一页</a>
										</c:if>
										<c:if test="${split.currentPage!=split.pageNum}">
											<a href='javascript:;' class='nextPage'
												toPage="${split.currentPage+1}">下一页</a>
										</c:if>
										<a href='javascript:;' class='lastPage'
											toPage="${split.pageNum}">尾页</a> 跳转至
                                            <input type='text'
											name='text' class='npage' onchange="change(this)"
											value="${split.currentPage}" style="text-align: center;"
											size="1"> 页
                                            <a href='javascript:;'
											class='toPage' toPage="${split.currentPage}">GO</a> 共${split.pageNum}页
                                        </c:if>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				 <c:choose>
				 	<c:when test="${third !='1'}">
				 		<div class="top_content_r">
				 	</c:when>
				 	<c:otherwise>
				 		<div class="top_content_r" style="border:0px solid #FFF;background:#F7F7F7">
				 	</c:otherwise>
				 </c:choose>
				
				<c:if test="${third!='1' }">
					<div class="upload_search">
						<a onclick="upload(this);return false" class="upload_article"
							id="" href="">上传知识</a>
					</div>
					<div class="black">
						<ul class="show" style="overflow-y: auto; overflow-x: hidden">
							<c:forEach items="${fnc:getAllLabel()}" var="list">
								<li class="biaoqian">
									<a id="${list.id}" class=""><span></span>${list.labelvalue}</a>
								</li>
							</c:forEach>
						</ul>
					</div>
					<p class="line"></p>
					</c:if>
				</div>
			</div>


		</div>
	</body>

</html>