<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() +
            ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>"/>
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            // 如果当前登录窗口不是顶层窗口，那么将顶层窗口设置为登录窗口
            if (window.top !== window) {
                window.top.location = window.location;
            }
            // 当页面重新加载，清空页面内容
            $("#loginAct").val("");
            $("#loginPwd").val("");
            // 页面加载完毕后，用户名文本框自动获得焦点
            $("#loginAct").focus();
            // 绑定按钮单击事件
            $("#submitBtn").on("click", function () {
                login();
            });
            // 当敲下回车，执行登录操作
            $(window).keydown(function (event) {
                if (event.keyCode === 13) {
                    login();
                }
            })

            // 当帐号/密码重新获取焦点时，清空错误提示
            $("#loginPwd").on("click", clearMsg);
            $("#loginAct").on("click", clearMsg);
        })

        // 普通自定义的function方法，一定要写在$(function(){})的外面
        function clearMsg() {
            if ($("#msg").html() !== "") {
                $("#msg").html(" ");
            }
        }

        function login() {
            // 帐号密码不能为空
            var loginAct = $.trim($("#loginAct").val());
            var loginPwd = $.trim($("#loginPwd").val());
            if (loginAct === "" || loginPwd === "") {
                $("#msg").html("帐号或者密码不能为空");
            } else {
                $.ajax({
                    url: "settings/user/login.do",
                    data: {
                        "loginAct": loginAct,
                        "loginPwd": loginPwd
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        /*
                        希望得到的data数据：
                        data: {
                            success : true/false,
                            msg : 错误的信息
                        }
                         */
                        if (data.success) {
                            // 登录成功，则跳转到欢迎界面
                            window.location.href = "workbench/index.jsp";
                        }
                        if (!data.success) {
                            // 登录失败，显示错误信息
                            $("#msg").html(data.msg);
                        }
                    }
                })
            }
        }


    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" id="loginAct">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" id="loginPwd">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" style="color: red"></span>

                </div>
                <%--
                    【注意】： 按钮写在form表单中，默认的行为就是提交表单
                        我们一定要将按钮的类型设置为 button
                        按钮所触发的 行为应该是由我们自己手动用 js 代码来决定的
                --%>
                <button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>