<@page>
<@showTitle title="舜联PAY联通官网批充工具">
<div class="get">
    <@form action="/unicomBatchRecharge" onsubmit="return $form.submit(this,_callback);" target="_rpc" enctype="multipart/form-data">
        <input type="file" name="file" id="_file" style="display:none;" onchange="$('#_form').submit()">
        <a href="javascript:void(0)" class="btn1" onclick="$('#_file').click()">导 入</a>
    </@form>
</div>
</@showTitle>

<@showContent>
<iframe src="about:blank" style="width:1px;height:1px;top:-10000px;position:absolute;" id="_rpc" name="_rpc"></iframe>
<div class="tab-black">
    <table class="tab1" id="_record" style="display:none">
        <thead>
        <tr>            
            <th width=40>编号</th>
            <th width=100>充值号码</th>
            <th width=70>充值金额</th>
            <th width=180>卡密</th>
            <th width=160>线上系统流水号</th>
            <th width=90>提交时间</th>
            <th width=90>成功时间</th>
            <th width=80>充值状态</th>
            <th>备注</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
  </div>
  <div class="tab-btn" id="_menus" style="display:none">
    <a id="_recharge" href="javascript:void(0);">充值</a>
    <a id="_pause" href="javascript:void(0);">暂停</a>
    <a id="_commitFails" href="javascript:void(0);">失败重新充值</a>
    <a target="_rpc" href="${url("/downUnicomBatchRecharge")}">结果导出</a>
    <a id="_cancelTask" href="javascript:void(0);">清除任务</a>
  </div>
  <p class="text1" id="_status"  style="display:none">成功导入 <span class="red" id="count">0</span> 条，总金额：<span class="red" id="totalMoney">0</span> 元</p>
  <div class="tab-btn">
  每次提交任务数(1-100)：<input id="_batchSize" type="text" value="5" style="width:30px;margin-right:50px"/> 每批任务间隔时间(秒)：<input id="_batchDelay" type="text" value="3" style="width:30px;margin-right:50px"/>
 单个任务重试次数(“失败重新充值”模式有效)：<input id="_retryTimes" type="text" value="3" style="width:30px;"/>
  </div>
</@showContent>
</@page>
<@htmlFooter>
<script type="text/javascript">
var _status={
    "0":"成功",
    "1":"失败",
    "2":"准备中",
    "3":"处理中"
};
var _callback=function(_response){   
    $form.reset('#_form');
    $("#_record").show();
    $("#_record tbody tr").remove();
    var total=0;
    for (var i=0;i<_response.data.length;i++) {
        var _data=_response.data[i];
        total+=parseInt(_data.money);
        var _tr =$('<tr></tr>');
        _tr.append($('<th></th>').html(_data.id));
        _tr.append($('<td></td>').html(_data.phone));
        _tr.append($('<td></td>').html(_data.money));
        _tr.append($('<td></td>').html(_data.card));
        _tr.append($('<td></td>').html(_data.olsn));
        _tr.append($('<td></td>').html((_data.commitDate || "").split(" ").join("<br/>")).attr("name","commitDate"));
        _tr.append($('<td></td>').html((_data.endDate || "").split(" ").join("<br/>")).attr("name","endDate"));
        _tr.append($('<td></td>').html(_status[_data.status]).attr("name","status"));
        _tr.attr("id",_data.id).attr("status",_data.status).attr("name","data").attr("rTimes",0);;
        _tr.append($('<td style="text-align:left"></td>').html(_data.remark || "").attr("name","remark"));
        $("#_record tbody").append(_tr);
    }
    $("#count").html(_response.data.length);
    $("#totalMoney").html(total);
    $("#_menus").show();
    $("#_status").show();
}

$("#_cancelTask").click(function(){
    if (!confirm("任务清除后状态不可恢复，确认清除任务？")) return;
    $.ajax({
        url:"${url("/cancelUnicomBatchRecharge")}", 
        cache:false, 
        timeout:10000,
        dataType:'json',
        success:function (response) {
             if (response.success) {
                 $form.reset('#_form');
                 $("#_record tbody tr").remove();
                 $("#_record").hide();
                 $("#_menus").hide();
                 $("#_status").hide();
                 alert("成功取消任务。");
            } else {
                alert(response.message);
            }
        }, 
        error:function () {
            alert("取消任务失败,请稍候重试!");
        }
    });
});
window.setInterval(function(){
    $.ajax({
        url:"${url("/heartBeat")}", 
        cache:false
    });
},1000*60*3);
var _isExecute=false;
var _queryTimeout=null;
var _isCommitFails=false;
var _isPause=false;

function valid() {
    if (_isExecute) {       
        if (_queryTimeout==null) {
           _queryTimeout=window.setTimeout(function(){
                queryTaskDetail();
            },1);
        }
        alert("任务未执行完毕，请稍候重试。");
        return false;
    }
    return true;
}

$("#_recharge").click(function(){
    if (!valid()) return;
    _isPause=false;
    _isCommitFails=false;
    $("#_pause").html("暂停");
    commit();   
});

$("#_commitFails").click(function(){
    if (!valid()) return;
    _isPause=false;
    _isCommitFails=true;
    $("#_pause").html("暂停");
    var _rows=$("#_record tbody tr[name=data]"); 
    var _rTimes=3;
    try {
        _rTimes=parseInt($("#_retryTimes").val());
    } catch(e) {
        $("#_retryTimes").val("3");
    }
    for (var i=0;i<_rows.length;i++) {
        var _row=$(_rows[i]);
        if (_row.attr("status")==1) {
            _row.attr("rTimes",_rTimes);
        }
    }
    commit();   
});

function commit() {
    var batchCount=5;
    try {
        batchCount=parseInt($("#_batchSize").val());
    } catch(e){
        $("#_batchSize").val("5");
    }
    if (batchCount>100) {
        $("#_batchSize").val("100");
        batchCount=100;
    }
    var _rows=$("#_record tbody tr[name=data]");
    var ids=[];
    var __status=_isCommitFails?1:2;
    var firstTr=null;    
    for (var i=0;i<_rows.length;i++) {
        var _row=$(_rows[i]);
        if (_row.attr("status")==1 && _row.attr("rTimes")==0) continue;        
        if (_row.attr("status")==__status) {
            if (__status==1) _row.attr("rTimes",(parseInt(_row.attr("rTimes"))-1));
            if (firstTr==null) firstTr=_row;
            $($(_row).find("td[name=status]")).attr("bgcolor","lightgreen");
            ids.push(_row.attr("id"));
            if (ids.length>=batchCount) break;
        }
    }
    if (ids.length==0) {
        window.setTimeout(function(){
            alert("批处理执行完毕！");
        },1);
        return;
    }
    var _headerHeight=$("#_record thead").height();
    var _lineHeight=$(firstTr).height();
    $(".tab-black").scrollTop(parseFloat(_headerHeight)+($(firstTr).attr("id")-1)*_lineHeight);
    $.ajax({
        url:"${url("/executeUnicomBatchRecharge")}?ids="+ids.join("|")+"&f="+(_isCommitFails?"1":"0"), 
        cache:false, 
        timeout:10000,
        dataType:'json',
        success:function (response) {
             if (response.success) {
                 _queryTimeout=window.setTimeout(function(){
                    queryTaskDetail();
                },2000);
            } else {
                alert(response.message);
            }
        }, 
        error:function () {
            alert("充值失败，请稍候重试！");
        }
    });
    _isExecute=true;
}

function queryTaskDetail() {
    $.ajax({
        url:"${url("/queryUnicomBatchRecharge")}",
        cache:false, 
        timeout:10000,
        dataType:'json',
        success:function (response) {
             if (response.success) {                 
                 var _datas=response.data;
                 var finished=true;
                 for (var i=0;i<_datas.length;i++) {
                    var tr=$("#_record tbody tr[id="+_datas[i].id+"]");
                    tr.attr("status",_datas[i].status);
                    $(tr.find("td[name=status]")).html(_status[_datas[i].status]);
                    $(tr.find("td[name=commitDate]")).html((_datas[i].commitDate || "").split(" ").join("<br/>"));
                    $(tr.find("td[name=endDate]")).html((_datas[i].endDate || "").split(" ").join("<br/>"));
                    $(tr.find("td[name=remark]")).html(_datas[i].remark);
                    if (_datas[i].status==0 || _datas[i].status==2) {
                         $(tr.find("td[name=status]")).attr("bgcolor","");                        
                    } else if (_datas[i].status==1) {
                         $(tr.find("td[name=status]")).attr("bgcolor","red");
                    } else if (_datas[i].status==3) {
                        finished=false;
                    }
                 }
                 if (finished) {
                    _isExecute=false;
                    _queryTimeout=null;
                    var batchDelay=3000;
                    try {
                        batchDelay=$("#_batchDelay").val()*1000;
                    } catch(e) {
                        $("#_batchDelay").val("3");
                    }
                    if (!_isPause) {
                        window.setTimeout(function(){
                            commit();                        
                        },batchDelay);
                        
                    }
                 } else {
                    _queryTimeout=window.setTimeout(function(){
                        queryTaskDetail();
                    },2000);
                 }
            } else {
                alert(response.message);
            }
        }, 
        error:function () {
            alert("查询任务状态失败，请稍候重试！");
        }
    });
}

$("#_pause").click(function(){
    var _text=$(this).text();
    if (_text=="暂停") {
        $.ajax({
            url:"${url("/pauseUnicomBatchRecharge")}", 
            cache:false,
            timeout:10000,
            dataType:'json',
            success:function (response) {
                 if (response.success) {
                    _isPause=true;
                    $("#_pause").html("继续充值");
                    alert("成功暂停任务！");
                } else {
                    alert(response.message);
                }
            }, 
            error:function () {
                alert("暂停失败，请稍候重试！");
            }
        });
    } else if (_text=="继续充值") {
        if (!valid()) return;
        _isPause=false;
        commit();
        $(this).html("暂停");
    }
    
});
</script>
</@htmlFooter>