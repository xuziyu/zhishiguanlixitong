<%@ page contentType="text/html;charset=UTF-8"%>
    <%@ include file="/WEB-INF/views/include/taglib.jsp"%>
        <%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
            <html>

            <head>
                <title>轮播图修改</title>

                <%@ include file="/WEB-INF/views/include/head.jsp"%>
                    <!-- Baidu tongji analytics -->
                    <script>
                        var _hmt = _hmt || [];
                        (function() {
                            var hm = document.createElement("script");
                            hm.src = "//hm.baidu.com/hm.js?8695d378a6e7e43400b08b7a6dc28a69";
                            var s = document.getElementsByTagName("script")[0];
                            s.parentNode.insertBefore(hm, s);
                        })();
                    </script>
                    <sitemesh:head />
                    <meta name="decorator" content="default" />
                    <script type="text/javascript">
                        $(function() {

                            var all_length = $("#topicWord").val().length;
                            $('.numofword').text(10 - all_length);

                            $("#topicWord").bind('input', function() {
                                var length = $("#topicWord").val().length;
                                var num = 10 - length;
                                if (length > 10) {
                                    num = -num;
                                    $("#num1").empty();
                                    $("#num1").append("已超出<span class='numofword red'></span>字");
                                    $("#btnSubmit").attr("disabled", "disabled");
                                } else {
                                    $("#num1").empty();
                                    $("#num1").append("还可以输入<span class='numofword'></span>字");
                                    $("#btnSubmit").removeAttr("disabled");
                                }
                                $(".numofword").text(num);

                            });

                            var all_length2 = $("#detailExplanation").val().length;
                            $('.numofword2').text(66 - all_length2);
                            $("#detailExplanation").bind('input', function() {
                                var length = $("#detailExplanation").val().length;
                                var num = 66 - length;
                                if (length > 66) {
                                    num = -num;
                                    $("#num2").empty();
                                    $("#num2").append("已超出<span class='numofword2 red'></span>字");
                                    $("#btnSubmit").attr("disabled", "disabled");
                                } else {
                                    $("#num2").empty();
                                    $("#num2").append("还可以输入<span class='numofword2'></span>字");
                                    $("#btnSubmit").removeAttr("disabled");
                                }
                                $(".numofword2").text(num);

                            });


                            $("#btnSubmit").click(function() {

                            });
                        });
                    </script>
                    <style>
                        #num1 {
                            color: #808080;
                            font-weight: bold;
                        }
                        
                        #num2 {
                            color: #808080;
                            font-weight: bold;
                        }
                        
                        .num span {
                            font-weight: bold;
                            font-size: 22px;
                            font-family: 宋体;
                        }
                        
                        .red {
                            color: #FA3C3C;
                        }
                        
                        .green {
                            color: #7BFA3C;
                        }
                        
                        .float_div {
                            float: left;
                        }
                    </style>
            </head>

            <body>
                <ul class="nav nav-tabs">
                    <li>
                        <a href="${ctx_a}/cms/frontswitch">首页轮播</a>
                    </li>
                    <li class="active">
                        <a href="${ctx_a}/cms/frontswitchmodify">轮播图修改</a>
                    </li>
                </ul>
                <br />

                <form action="${ctx_a}/cms/frontswitchsave" method="post" class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label">
                            知识链接:
                        </label>
                        <input type="text" id="articleUrl" name="articleUrl" value="${switch_modify.articleUrl}" />
                        <input type="hidden" name="id" id="id" value="${switch_modify.id}" />

                    </div>
                    <div class="control-group">
                        <label class="control-label">
                            图片路径:
                        </label>
                        <div class="controls">
                            <div id="fileQueue"></div>
                            <div id="fileInfo"></div>
                            <input type="file" name="uploadify" id="uploadify" />
                            <span style="font-size: 10px; color: #333"> 请选择 <span
						style="font-size: 10px; color: #f00">700px X 310px </span> 的图像上传&nbsp;&nbsp;&nbsp;&nbsp; 支持图片大小： <span style="font-size: 10px; color: #00f"> 10MB </span> 以内。 </span>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="float_div">
                            <label class="control-label">
                                主题概要:
                            </label>
                            <input type="text" id="topicWord" name="topicWord" value="${switch_modify.topicWord}" /> &nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="num" id="num1"> 还可以输入 <span class="numofword">10</span>字
                            </span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">
                            详细描述:
                        </label>
                        <textarea id="detailExplanation" name="detailExplanation">${switch_modify.detailExplanation}</textarea>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <span class="num" id="num2"> 还可以输入 <span class="numofword2">66</span>字
                        </span>
                    </div>
                    <input name="updateDate" disabled="true" type="hidden" value="${switch_modify.updateDate}" />
                    <input name="updateBy.name" disabled="true" type="hidden" value="${switch_modify.updateBy.name}" /> &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
                    <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存" />
                    <form>
                        <%
					String path = request.getContextPath()+"/";
					//String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
				%>
                            <c:set var="ctx1" value="<%=path%>" />

                            <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/uploadify.css"></link>
                            <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery1.8.2.js"></script>
                            <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.uploadify.min.js"></script>
                            <script type="text/javascript">
                                function setCookie(c_name, value, expiredays) {
                                    var exdate = new Date()
                                    exdate.setDate(exdate.getDate() + expiredays)
                                    document.cookie = c_name + "=" + escape(value) +
                                        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
                                }

                                function getCookie(name) {
                                    alert(name);
                                    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
                                    if (arr = document.cookie.match(reg))
                                        return unescape(arr[2]);
                                    else
                                        return null;
                                }

                                function newGuid() {
                                    var guid = "";
                                    for (var i = 1; i <= 32; i++) {
                                        var n = Math.floor(Math.random() * 16.0).toString(16);
                                        guid += n;
                                        if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
                                            guid += "";
                                    }
                                    return guid;
                                }

                                function cutstr(str, len) {
                                    var str_length = 0;
                                    var str_len = 0;
                                    str_cut = new String();
                                    str_len = str.length;
                                    for (var i = 0; i < str_len; i++) {
                                        a = str.charAt(i);
                                        str_length++;
                                        if (escape(a).length > 4) {
                                            //中文字符的长度经编码之后大于4
                                            str_length++;
                                        }
                                        str_cut = str_cut.concat(a);
                                        if (str_length >= len) {
                                            str_cut = str_cut.concat("...");
                                            return str_cut;
                                        }
                                    }
                                    //如果给定字符串小于指定长度，则返回源字符串；
                                    if (str_length < len) {
                                        return str;
                                    }
                                }



                                $(document).ready(function() {


                                    //附件标记->对应知识保存状态
                                    var attfile_temp_guid = newGuid()
                                        //$("#attfile_temp_guid").val(attfile_temp_guid);

                                    var cookie_guid = newGuid();

                                    //$("#cookie_guid").val(cookie_guid);
                                    //var category_id =$("#current_category_id").val();

                                    //var current_article_id = $("#current_article_id").val();


                                    var uploadify_onSelectError = function(file, errorCode, errorMsg) {
                                        var msgText = "上传失败\n";
                                        switch (errorCode) {
                                            /*
	            case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:  
	                //this.queueData.errorMsg = "每次最多上传 " + this.settings.queueSizeLimit + "个文件";  
	                msgText += "每次最多上传 " + this.settings.queueSizeLimit + "个文件";  
	                break;
	             */
                                            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                                                msgText += "文件大小超过限制( " + this.settings.fileSizeLimit + " )";
                                                break;
                                            default:
                                                msgText += "错误代码：" + errorCode + "\n" + errorMsg;
                                        }
                                        alert(msgText);
                                    };


                                    $("#uploadify").uploadify({
                                        'swf': '${ctx1}resources/js/uploadify.swf',
                                        'cancelImg': '${ctx1}resources/img/uploadify-cancel.png',
                                        'uploader': '${ctx1}a/cms/article/uploadpicture',
                                        'onSelect': function(file) {
                                            category_id = $("#categoryId").val();
                                        },
                                        'queueID': 'fileQueue', //文件在页面的显示队列的id
                                        'queueSizeLimit': 8, //可上传文件的个数
                                        'auto': true,
                                        'fileTypeExts': '*.jpg;*.png;*.gif',
                                        'method': 'post',
                                        'width': '72',
                                        'height': '23',
                                        'progressData': 'percentage', //显示进度条
                                        /*'removeTimeout'	:	0.5, 如果设置了任务完成后自动从队列中移除，则可以规定从完成到被移除的时间间隔。*/
                                        'removeCompleted': false,
                                        'multi': true,
                                        'successTimeout': 50,
                                        'fileSizeLimit': '10MB', //限制上传文件大小
                                        'buttonText': '选择文件',
                                        'buttonClass': '',
                                        /*'overrideEvents'  : 	[ 'onDialogClose', 'onUploadSuccess', 'onUploadError', 'onSelectError' ], */
                                        /*'onSelect'        : 	uploadify_onSelect, */
                                        'overrideEvents': ['onUploadSuccess', 'onUploadStart', 'onSelectError'],
                                        "formData": {
                                            "switch_id": $('#id').val()
                                        },
                                        // 'onUploadStart' 	: 	function(file) {$("#uploadify").uploadify("settings", "formData", {'userName':'huangmj');}, 
                                        'onCancel': function(file) {
                                            alert('文件：' + file.name + '取消！')
                                        },
                                        'onSelectError': uploadify_onSelectError,
                                        'onUploadSuccess': function(file, data, response) { //成功提醒 

                                        }
                                    });
                                });
                            </script>
            </body>

            </html>