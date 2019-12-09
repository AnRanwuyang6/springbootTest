var opts = {
    num_edge_entries: 2,
    num_display_entries: 4,
    items_per_page: 10,
    current_page: 0
};
var uploads=$("#uploads").val();
var staticServer=$("#staticServer").val();
$(function(){
    $('.reimbursement_00').find('ul').attr("class","nav nav-second-level collapse in");
    $('.reimbursement_00').find('ul').attr("expanded","true");
    $('.reimbursement_00').addClass('active')
    $('.reimbursement_03').attr("class", "active");
    queryData(opts.current_page + 1,opts.items_per_page);
    $("#doSearch").on('click',function () {
        queryData(opts.current_page + 1,opts.items_per_page);
    })
})

var queryData = function(page,size){
    var url = '/invoice/manageList'
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data:{
            // 需要传到后台的值，可带参进行分页
            pageNum: page,
            pageSize: size,
            searchText:$("#searchText").val(),
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
                    html += ('<td>'+content[i].title+'</td>')
                    html += ('<td>'+content[i].fpdm+'</td>')
                    html += ('<td>'+content[i].fphm+'</td>')
                    html += ('<td>'+content[i].kprq+'</td>')
                   /* html += ('<td>'+content[i].mc2+'</td>')*/
                    html += ('<td>'+content[i].hjje+'</td>')
                    html += ('<td>'+content[i].hjse+'</td>')
                    html += ('<td>')
                    if(content[i].type=='01'){
                        html +="通行费"
                    }else{
                        html +="其他"
                    }
                    html += ('</td>')
                    html += ('<td>'+content[i].reiPersion+'</td>')
                    html += ('<td>'+content[i].reiDate+'</td>')
                    html += ('<td style="text-align: center">')
                    html += ('<div class="ui mini button" onclick="viewPdf(\''+content[i].file+'\')">查看详情</div>')
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
//查看pdf文件
var viewPdf=function (url) {
    var filepath=staticServer+url.substring(uploads.length)
    window.open(filepath)
}

var exportExcle=function () {
    var searchText=$("#searchText").val();
    var beginTime=$("#beginTime").val();
    var endTime=$("#endTime").val();
    window.location.href='/invoice/export?searchText='+searchText+'&beginTime='+beginTime+'&endTime='+endTime;
}


