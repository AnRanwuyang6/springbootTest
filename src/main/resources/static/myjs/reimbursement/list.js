var opts = {
    num_edge_entries: 2,
    num_display_entries: 4,
    items_per_page: 10,
    current_page: 0
};
$(function(){
    $('.reimbursement_00').find('ul').attr("class","nav nav-second-level collapse in");
    $('.reimbursement_00').find('ul').attr("expanded","true");
    $('.reimbursement_00').addClass('active')
  /*  $('.reimbursement_00').attr("class", "active");*/
    $('.reimbursement_01').attr("class", "active");
    $("#doSearch").on('click',function () {
        queryData(opts.current_page + 1,opts.items_per_page);
    })
    queryData(opts.current_page + 1,opts.items_per_page);
})

var queryData = function(page,size){
    var url = '/reimbursement/listAjax'
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data:{
            // 需要传到后台的值，可带参进行分页
            pageNum: page,
            pageSize: size,
            reiPersion:$("#reiPersion").val(),
            beginTime:$("#beginTime").val(),
            endTime:$("#endTime").val(),
        },
        success: function(data){
            //加载内容到页面中
            var html = '';
            $('#content').text(html)
            if(data.code == '0000'){
                var content = data.data.list
                for (i in content){
                    html += '<tr class="gradeA">'
                    html += ('<td>'+content[i].reiPersion+'</td>')
                    html += ('<td>'+content[i].reiDep+'</td>')
                    html += ('<td>'+dateFtt("yyyy-MM-dd hh:mm:ss",new Date(content[i].createDate))+'</td>')
                    html += ('<td>'+content[i].responsiblePersion+'</td>')
                    html += ('<td>'+content[i].reiProject+'</td>')
                    html += ('<td>'+content[i].totalMoney+'</td>')
                    html += ('<td>'+content[i].totalSe+'</td>')
                    html += ('<td>'+content[i].createBy+'</td>')
                    html += ('<td style="text-align: center">')
                    html += ('<div class="ui mini button" onclick="toDetail(\''+content[i].id+'\')">查看详情</div>')
                    html += ('</td>')
                }
                $('#content').append(html)
            }else{
                alert(data.message)
            }
            //mybatis需要将data.data.pageNum 减 1，JPA不需要做操作
            opts.current_page = data.data.pageNum - 1;
            //每次成功请求后会重新初始化分页控件
            $('#page').pagination(data.data.total,opts);
        }
    });
}


var  addReimbursement=function () {
    window.location.href='/reimbursement/addReimbursement'
}

var toDetail=function (reiId) {
    window.location.href='/reimbursement/toDetail?reiId='+reiId;
}
var dateFtt=function (fmt,date) {
    var o = {
        "M+" : date.getMonth()+1,
        "d+" : date.getDate(),
        "h+" : date.getHours(),
        "m+" : date.getMinutes(),
        "s+" : date.getSeconds(),
        "q+" : Math.floor((date.getMonth()+3)/3),
        "S"  : date.getMilliseconds()
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

