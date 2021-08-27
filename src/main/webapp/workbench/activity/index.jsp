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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <%--    datatimepicker 日历拾取器，生成 一个日历控件的--%>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <%--    导入分页插件--%>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        $(function () {
            //    在页面加载完毕后，触发一个方法，将市场活动信息列表展示在页面
            pageList(1, 2);
            //    为创建按钮绑定事件，打开添加操作的模态窗口
            $("#addBtn").on("click", function () {
                // 日期拾取器
                $(".time").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });
                /**
                 * 打开模态窗口的方式：
                 *      找到 需要操作的 模态窗口的jquery对象，调用modal方法，为该方法传递参数
                 *          参数有show，打开模态窗口； hide，关闭模态窗口
                 */
                $.ajax(
                    {
                        url: "workbench/activity/getUserList.do",
                        dataType: "json", // 期望返回来的数据格式
                        type: "get", // 请求方式
                        // 请求成功后要执行的操作
                        success: function (data) {
                            /**
                             * data是一个userList
                             * data : [{user}, {2}, {3}, ……]
                             * @type {string}
                             */
                            var html = "<option></option>";
                            /**
                             * data：要遍历的数据
                             * index：data的下标
                             * element：data里面的每个对象
                             */
                            $.each(data, function (index, element) {
                                html += "<option value='" + element.id + "'>" + element.name + "</option>";
                            })
                            $("#create-marketActivityOwner").html(html);
                            // 在JS中，EL表达式必须放在字符串中
                            $("#create-marketActivityOwner").val("${user.id}");

                        }
                    }
                )
                $("#createActivityModal").modal("show");
            })
            //    模态窗口中保存添加数据
            $("#saveBtn").on("click", function () {
                $.ajax({
                    url: "workbench/activity/save.do",
                    data: {
                        "owner": $.trim($("#create-marketActivityOwner").val()),
                        "name": $.trim($("#create-marketActivityName").val()),
                        "startDate": $.trim($("#create-startDate").val()),
                        "endDate": $.trim($("#create-endDate").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-description").val())
                    },
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        /**
                         * data = {success:true/false}
                         */
                        if (data.success) {
                            //    1、添加成功
                            //    2、局部刷新活动信息列表
                            // pageList(1, 2);

                            /**
                             * $("#activityPage").bs_pagination('getOption', 'currentPage')
                             *  表示操作后停留在当前页
                             * $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
                             *  表示操作后维持设置好的 每页展现记录数
                             *
                             *      这两个参数不需要进行任何操作，如有需要，直接使用
                             *
                             * */

                            pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            //    在关闭模态窗口前，清空模态窗口中的数据
                            /**
                             * 【注意】：
                             *      我们拿到了form表单的jquery对象
                             *      对于表单的jquery对象，提供了submit()方法，让我们提交表单
                             *      但是，虽然reset()方法有提示，但表单的jquery对象并没有提供该方法，该方法无效！！
                             *
                             *      我们可以使用原生的js代码使用reset()方法
                             */
                            // $("#activityAddForm").reset();  并没有这个方法
                            $("#activityAddForm")[0].reset();
                            //    3、关闭添加的模态窗口
                            $("#createActivityModal").modal("hide");

                        } else {
                            alert("添加市场活动失败")
                        }
                    }
                })
            })
            // 查询信息
            $("#searchBtn").on("click", function () {
                /**
                 * 点击查询按钮的时候，我们应当将搜索筐中的信息保存起来，保存到隐藏域中
                 */
                $("#hidden-name").val($.trim($("#search-name").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-startDate").val($.trim($("#search-startDate").val()));
                $("#hidden-endDate").val($.trim($("#search-endDate").val()));
                pageList(1, 2);
            })
            // 删除操作
            $("#deleteBtn").on("click", function () {
                var $xzs = $("input[name=xz]:checked");
                if ($xzs.length === 0) {
                    alert("请选择需要删除的活动");
                } else {
                    // 可能选择了多个对象，遍历所有情况，取出所有id
                    if (window.confirm("确定要删除吗？")) {
                        var id = "";
                        for (var i = 0; i < $xzs.length; i++) {
                            id += "id=" + $xzs[i].value;
                            if (i !== $xzs.length - 1) {
                                id += "&";
                            }
                        }
                        // alert(id);
                        $.ajax({
                            url: "workbench/activity/delete.do",
                            data: id,
                            type: "post",
                            dataType: "json",
                            success: function (data) {
                                /**
                                 * data = {success : true/false}
                                 */
                                if (data.success) {
                                    window.confirm("删除成功");
                                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                                } else {
                                    alert("删除失败");
                                }
                            }
                        })
                    }
                }
            })

            // 修改操作
            $("#editBtn").on("click", function () {
                var $xz = $("input[name=xz]:checked");
                if ($xz.length === 0) {
                    alert("请选择要修改的列表");
                } else if ($xz.length !== 1) {
                    alert("每次只能修改一个活动列表，请重新选择");
                } else {
                    $.ajax({
                        url: "workbench/activity/getUserListAndActivity.do",
                        data: {
                            "id": $xz.val()
                        },
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            /**
                             * data = {
                             *     "userList" : [{use1}, {user2},]
                             *     "activity" : {activity的属性和属性值}
                             * }
                             **/
                                // 处理所有者 下拉框
                            var html = "<option></option>";
                            $.each(data.userList, function (index, element) {
                                html += "<option value='" + element.id + "'>" + element.name + "</option>";
                            })
                            $("#edit-marketActivityOwner").html(html);

                            // 处理单挑activity
                            $("#edit-id").val(data.activity.id);
                            $("#edit-marketActivityName").val(data.activity.name);
                            $("#edit-marketActivityOwner").val(data.activity.owner);
                            $("#edit-startTime").val(data.activity.startDate);
                            $("#edit-endTime").val(data.activity.endDate);
                            $("#edit-cost").val(data.activity.cost);
                            $("#edit-describe").val(data.activity.description);

                            // 在所有值都填写好之后 ，就可以打开修改模态窗口了
                            $("#editActivityModal").modal("show");

                        }

                    })
                }
            })

            // 执行更新操作
            $("#updateBtn").click(function () {
                $.ajax({
                    url: "workbench/activity/update.do",
                    data: {
                        "id": $("#edit-id").val(),
                        "owner": $.trim($("#edit-marketActivityOwner").val()),
                        "name": $.trim($("#edit-marketActivityName").val()),
                        "startDate": $.trim($("#edit-startTime").val()),
                        "endDate": $.trim($("#edit-endTime").val()),
                        "cost": $.trim($("#edit-cost").val()),
                        "description": $.trim($("#edit-describe").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                , $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            $("#editActivityModal").modal("hide");
                            window.confirm("更新成功");
                        } else {
                            window.confirm("更新失败");
                        }
                    },
                    error: function () {
                        alert("失败失败失败");
                    }

                })
            })


            $("#checkAll").on("click", function () {
                $("input[name=xz]").prop("checked", this.checked);
            })
            /**
             以下 这两种方式 的绑定 都 是 不 行 的！
             $("input[name=xz]").on("click", function (){
                alert("12345")
            })
             $("input[name=xz]").click(function (){
                alert("click");
            })
             动态生成的元素，必须以以下的方式绑定
             ：
             $(需要绑定元素 的 有效 外层元素).on(绑定事件的方式, 需要绑定的元素的jquery对象, function())

             */

            $("#activityBody").on("click", $("input[name=xz]"), function () {
                // 如果可选的复选框的个数 === 选择的个数  则 全选
                $("#checkAll").prop("checked", $("input[name=xz]").length === $("input[name=xz]:checked").length);
            })

        });

        /**
         * 分业查询
         * @param pageNo    第几页
         * @param pageSize  一页显示多少条数据
         */
        /**
         * 需要调用pageList()方法刷新市场活动列表的情形：
         *  1、市场活动页面加载
         *  2、添加、修改、删除活动
         *  3、页数的切换（ 页码切换，显示条数变更，直接跳转到第几页
         *  4、查询（动态SQL）
         *
         */
        function pageList(pageNo, pageSize) {
            // 每次在 刷新列表时， 把全选的复选框 取消
            $("#checkAll").prop("checked", false);
            // 每一次查询，将隐藏域中的信息取出，放在搜索框中
            $("#search-name").val($("#hidden-name").val());
            $("#search-owner").val($("#hidden-owner").val());
            $("#search-startDate").val($("#hidden-startDate").val());
            $("#search-endDate").val($("#hidden-endDate").val());

            // alert("展现市场信息列表");
            $.ajax({
                url: "workbench/activity/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#search-name").val()),
                    "owner": $.trim($("#search-owner").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    /*
                    data 需要用到的信息 total是分页查询需要用到的，activityList是要展示的信息
                    data =
                    {
                        total: num
                        activityList:[{市场活动对象1}, {2}, {3} ...]
                    }
                     */
                    var html = "";
                    $.each(data.dataList, function (index, data) {
                        html += '<tr class="active">';
                        html += '    <td><input type="checkbox" name="xz" value="' + data.id + '" "/></td>';
                        html += '    <td><a style="text-decoration: none; cursor: pointer;" ' +
                            'onclick="window.location.href=\'workbench/activity/detail.do?id=' + data.id + '\';">' + data.name + '</a></td>';
                        html += '    <td>' + data.owner + '</td>';
                        html += '    <td>' + data.startDate + '</td>';
                        html += '    <td>' + data.endDate + '</td>';
                        html += '</tr>';
                    })
                    $("#activityBody").html(html);
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
                    // 处理完数据后，结合分页插件，在前端展示分页结果
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        // 该回调函数是在点击 分页组件的时候触发的
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });
                }
            })
        }


    </script>
</head>
<body>
<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">


<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form" id="activityAddForm">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">

                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">

                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startTime">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endTime">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <%--
                                关于文本域textarea：
                                    1、textarea的标签一定要以 标签对 的形式呈现，正常状态下，标签对要紧紧挨着（如果里面有空白字符，那也会算成textarea的内容）
                                    2、textarea虽然是以标签对的形式呈现，但是 它也是属于 表单元素范畴
                                        我们所有对 textarea的取值和赋值操作，应该统一使用val()方法（而不是html()
                            --%>
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <%--
                创建按钮中的属性和含义：

                    data-toggle="modal"
                        表示触发该按钮，将要打开一个 模态窗口
                    data-target="#createActivityModal"
                        表示打开哪一个模态窗口，通过#id的方式找到要打开的模态窗口

                    将模态窗口要操作的 属性和属性值 写在button中，存在以下问题：
                        1、只能打开模态窗口
                        2、没有办法对按钮的功能进行拓宽

                    所以在未来的开发中，对于触发模态窗口的操作，一定不要写死在元素当中，
                    应该由我们自己写js代码动态控制
                --%>
                <button type="button" class="btn btn-primary" id="addBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
                <%--<tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;"
                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;"
                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage">

            </div>
        </div>

    </div>

</div>
</body>
</html>