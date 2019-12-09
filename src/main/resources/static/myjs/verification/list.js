var opts = {
    num_edge_entries: 2,
    num_display_entries: 4,
    items_per_page: 10,
    current_page: 0
};
$(function(){
    $('.verification_00').find('ul').attr("class","nav nav-second-level collapse in");
    $('.verification_00').find('ul').attr("expanded","true");
    $('.verification_00').addClass('active')
    $('.verification_01').attr("class", "active");

    //图片上传插件
    $("#input-id").fileinput({
        language: 'zh',                                 //中文
        uploadUrl:'/upload/file',
        showPreview: true,				//展前预览
        showUpload: true,				//不显示上传按钮
        showCaption: false,				//不显示文字表述
        uploadAsync:false,                             //采用同步上传
        dropZoneTitle:"拖拽文件到这里",
        removeFromPreviewOnError:true,                 //当文件不符合规则，就不显示预览
        maxFileCount: 1,
        maxFileSize: 1024*10*1024,                          //单位为kb，如果为0表示不限制文件大小
        allowedFileExtensions: ["pdf"],
    }).on("filebatchuploadsuccess", function(event, data) {	//当上传成功回调函数
        var response = data.response;
        if(response.code=='0000'){
            swal({
                title: "校验结果",
                text: response.message
            });
        }
    });
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


