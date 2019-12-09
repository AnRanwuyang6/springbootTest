var opts = {
    num_edge_entries: 2,
    num_display_entries: 4,
    items_per_page: 10,
    current_page: 0
};
$(function(){
    $('.deduct_00').find('ul').attr("class","nav nav-second-level collapse in");
    $('.deduct_00').find('ul').attr("expanded","true");
    $('.deduct_00').addClass('active')
    $('.deduct_01').attr("class", "active");
    queryData(opts.current_page + 1,opts.items_per_page);
    $("#doSearch").on('click',function () {
        queryData(opts.current_page + 1,opts.items_per_page);
    })
})

var queryData = function(page,size){
    var url = '/deduct/listPageDetail'
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
                var content = data.pageInfo.list
                for (i in content){
                    html += '<tr class="gradeA">'
                    html += ('<td>'+content[i].fwmc1+'</td>')
                    html += ('<td>'+content[i].fwmc2+'</td>')
                    html += ('<td>'+content[i].se+'</td>')
                    html += ('<td>'+content[i].je+'</td>')
                    html += ('<td>'+content[i].title+'</td>')
                    html += ('<td>'+content[i].fphm+'</td>')
                    html += ('<td>'+content[i].hjse+'</td>')
                    html += ('<td>'+content[i].hjje+'</td>')
                    html += ('<td>'+content[i].reiPersion+'</td>')
                    html += ('<td>'+content[i].createDate+'</td>')
                    html += ('<td style="text-align: center">')
                    html += ('<div class="ui mini button" onclick="toDetail(\''+content[i].reiId+'\')">查看详情</div>')
                    html += ('</td>')
                }
                $('#content').append(html)
                $("#totalJe").text(data.totalJe)
                $("#totalSe").text(data.totalSe)
            }else{
                alert(data.message)
            }
            //mybatis需要将data.data.pageNum 减 1，JPA不需要做操作
            opts.current_page = data.pageInfo.pageNum - 1;
            //每次成功请求后会重新初始化分页控件
            $('#page').pagination(data.pageInfo.total,opts);
        }
    });
}
var exportExcle=function () {
    var searchText=$("#searchText").val();
    var beginTime=$("#beginTime").val();
    var endTime=$("#endTime").val();
    window.location.href='/deduct/export?searchText='+searchText+'&beginTime='+beginTime+'&endTime='+endTime;
}
var toDetail=function (reiId) {
    window.location.href='/reimbursement/toDetail?reiId='+reiId;
}

