var uploads=$("#uploads").val();
var staticServer=$("#staticServer").val();

$(function(){
    $('.reimbursement_00').find('ul').attr("class","nav nav-second-level collapse in");
    $('.reimbursement_00').find('ul').attr("expanded","true");
    $('.reimbursement_00').addClass('active')
  /*  $('.reimbursement_00').attr("class", "active");*/
    $('.reimbursement_03').attr("class", "active");
    var reiId=$("#reiId").val();

    queryData(reiId)

    //图片上传插件
    $("#input-id").fileinput({
        language: 'zh',                                 //中文
        uploadUrl:'/upload/files?reiId='+reiId,
        showPreview: true,				//展前预览
        showUpload: true,				//不显示上传按钮
        showCaption: false,				//不显示文字表述
        uploadAsync:false,                             //采用同步上传
        removeFromPreviewOnError:true,                 //当文件不符合规则，就不显示预览
        maxFileCount: 5,
        maxFileSize: 1024*10*1024,                          //单位为kb，如果为0表示不限制文件大小
        allowedFileExtensions: ["pdf"],
    }).on("filebatchuploadsuccess", function(event, data) {	//当上传成功回调函数
        var response = data.response;
        if(response.code=='0000'){
            queryData(reiId);
            idenInvoice()
        }
    });
})

var pre=function () {
    var reiId=$("#reiId").val();
    window.location.href='/reimbursement/addReimbursement?reiId='+reiId;
}

var invoiceSumbit=function () {
    var reiId=$("#reiId").val();
    $.ajax({
        url: "/invoice/sumbit",
        type: 'post',
        async:false,
        dataType: 'json',
        data:{
            // 需要传到后台的值，可带参进行分页
            reiId:reiId
        },
        beforeSend:function () {
            if(countCanUseInvoice()==0){
                swal({
                    title:"温馨提示",
                    text:"必须选择可用发票才能提交"
                })
                return false;
            }if(countCanUseInvoice()==-1){
                swal({
                    title:"温馨提示",
                    text:"发票重复"
                })
                return false;
            }
            return true;
        },
        success: function(data){
            //加载内容到页面中
            if(data.code == '0000'){
                window.location.href='/reimbursement/list';
            }else{
                alert(data.message)
            }
        }
    });
}

var queryData=function (reiId) {
    $.ajax({
        url: "/invoice/listByReiId",
        type: 'post',
        async:false,
        dataType: 'json',
        data:{
            // 需要传到后台的值，可带参进行分页
            reiId:reiId
        },
        success: function(data){
            //加载内容到页面中
            var html = '';
            $('#tableContent').text(html)
            if(data.code == '0000'){
                var content = data.data
                for (i in content){
                    var status;
                    var errorMsg;
                    if(content[i].idenDtatus!=null){
                        status= content[i].idenDtatus.substring(0,1);
                        if(status=='2'){
                            errorMsg=content[i].idenDtatus.substring(1);
                        }
                    }
                    html += '<tr class="gradeA">'
                    html += ('<td style="display: none"><input type="text" name="invoiceId" value="'+content[i].seq+'"></td>')
                    html += ('<td>'+content[i].fphm+'</td>')
                    html += ('<td>'+content[i].kprq+'</td>')
                    /*    html += ('<td>'+content[i].mc1+'</td>')*/
                    html += ('<td>'+content[i].hjje+'</td>')
                    html += ('<td>'+content[i].hjse+'</td>')
                    html += ('<td>'+content[i].jshjxx+'</td>')
                    html += ('<td>'+content[i].mc2+'</td>')
                    html += ('<td>')
                    if(status=='0'){
                        html += ('<span style="color: red">不是发票文件</span>')
                    }else if(status=='1'){
                        html += ('<span style="color: red">不是本公司的发票</span>')
                    }else if(status=='2'){
                        html += ('<span style="color: red">已使用过的发票+'+errorMsg+'</span>')
                    }else if(status=='3'){
                        html += ('<span style="color: green">校验成功</span>')
                    }else if(status=='4'){
                        html += ('<span style="color: red">无效发票</span>')
                    }else{
                        html += ('未识别')
                    }
                    html += ('</td>')
                    html += ('<td>'+content[i].details+'</td>')
                    html += ('<td style="width: 20%">')
                    html +=('<div class="ui mini  button" onclick="viewFile(\''+content[i].file+'\')">查看文件</div>')
                    html +=('<div class="ui mini primary button" onclick="checkInvoice(\''+content[i].fpdm+'\',\''+content[i].fphm+'\',\''+content[i].jym+'\',\''+content[i].kprq+'\')">发票核验</div>')
                    html +=('<div class="ui mini red button" onclick="deleteById(\''+content[i].seq+'\')">删除</div>')
                    /*if(content[i].idenDtatus !='3'){
                        html +=('<div class="ui mini red button" onclick="deleteById(\''+content[i].seq+'\')">删除</div>')
                    }*/
                    html += ('</td>')
                    html += '</tr>'
                }
                $('#tableContent').append(html)
            }else{
                alert(data.message)
            }
        }
    });
}
var idenInvoice=function () {
    var ids="";
    $('input[name="invoiceId"]').each(function () {
        ids=ids+$(this).val()+",";
    })
    var reiId=$("#reiId").val();
    $.ajax({
        url: "/invoice/identify",
        type: 'post',
        dataType: 'json',
        data:{
            // 需要传到后台的值，可带参进行分页
            ids:ids
        },
        success: function(data){
            //加载内容到页面中
            if(data.code == '0000'){
                queryData(reiId)
            }else{
                alert(data.message)
            }
        }
    });
}

/*删除功能*/
var deleteById = function(id){
    var reiId=$("#reiId").val();
    var url = "/invoice/delete/" + id;
    $.ajax({
        url:url,
        type:"post",
        dataType: 'json',
        success:function(data){
            if(data.code == "0000"){
                swal({
                    title: "温馨提示",
                    text: "操作成功"
                });
                queryData(reiId);
            }else{
                alert(data.message)
            }
        }
    })

}
//报销单提交前计算校验成功发票数
var countCanUseInvoice=function () {
    var count=0;
    var s=[];
    $("#tableContent tr").each(function (e) {
        var checkinfo = $(this).find("td").eq(7).text();
        var invoiceNo=$(this).find("td").eq(1).text();
        if(checkinfo=='校验成功'){
            count=count+1;
            s.push(invoiceNo)
        }
    });
    //判断数组是否有相同元素
    var s1=s.slice().sort();
    for(var i=0;i<s1.length;i++){
        if (s1[i]==s1[i+1]){
            count=-1;
        }
    }
    return count;
}


//打开核验发票弹窗
var checkInvoice=function (fpdm,fphm,jym,kprq) {
    var s=getSwjg(fpdm,0);
    $.ajax({
        url: "/checkInvoice/getImg",
        type: 'post',
        async:false,
        dataType: 'json',
        data:{
            InvCode:fpdm,
            InvNo:fphm,
            url_prefix:s[1]
        },
        success: function(data){
            //加载内容到页面中
            if(data.code == '0000'){
                var content = data.data
                var base="data:image/jpeg;base64,"+content.key1
                //[{"00":"所有"},{"01":"红色"},{"03":"蓝色"},{"02":"黄色"}]
                $("#checkImg").attr("src",base)
                $("#key2").val(content.key2)
                $("#key3").val(content.key3)
                $("#cookie").val(content.cookie)
                var tip=tipCare(content.key4)
                $("#tip").html(tip)
                //赋值
                $("#add_fpdm").val(fpdm);
                $("#add_fphm").val(fphm);
                $("#add_jym").val(jym.substring(jym.length-6, jym.length));
                $("#add_kprq").val(kprq);
                $('#addModal').modal('setting', 'closable', false).modal('show')
            }else{
                alert(data.message)
            }
        }
    });

}
//刷新图片
var refreshImg=function () {
    var s=getSwjg($("#add_fpdm").val(),0);
    $.ajax({
        url: "/checkInvoice/getImg",
        type: 'post',
        async:false,
        dataType: 'json',
        data:{
            InvCode:$("#add_fpdm").val(),
            InvNo:$("#add_fphm").val(),
            url_prefix:s[1]
        },
        success: function(data){
            //加载内容到页面中
            if(data.code == '0000'){
                var content = data.data
                var base="data:image/jpeg;base64,"+content.key1
                //[{"00":"所有"},{"01":"红色"},{"03":"蓝色"},{"02":"黄色"}]
                var tip=tipCare(content.key4)
                $("#checkImg").attr("src",base)
                $("#key2").val(content.key2)
                $("#key3").val(content.key3)
                $("#cookie").val(content.cookie)
                $("#tip").html(tip)
            }else{
                alert(data.message)
            }
        }
    });
}
//提交核验
var checkSumbit=function () {
    var fpdm=$("#add_fpdm").val();
    $("#fplx").val(getFplx(fpdm))
    var fphm=$("#add_fphm").val();
    //访问路径
    var s=getSwjg(fpdm,0);
    $("#url_prefix").val(s[1]);
    $.ajax({
        url: "/checkInvoice/getInvoiceInfo",
        type: 'post',
        async:false,
        dataType: 'json',
        data:$('#checkForm').serialize(),
        success: function(data){
            //加载内容到页面中
            if(data.code == '0000'){
                var content = data.data;
                var key1=content.key1;
                var info=modalInfo(key1)
                //更新发票和报销单信息
                if(key1=='009'){
                    updateInvoiceStatus(fphm);
                }
                swal({
                    title: "温馨提示",
                    text:info
                });
            }else{
                alert(data.message)
            }
        }
    });
}

//验证码提示信息
var tipCare=function (key4) {
    var tip="";
    if(key4=='00'){
        tip="请输入验证码图片中文字"
    }else if(key4=='01'){
        tip+=('请输入验证码图片中')
        tip+=('<span style=" color: red;background-color: gray">红色</span>')
        tip+=('文字')
    }else if(key4=='02'){
        tip+=('请输入验证码图片中')
        tip+=('<span style="color: yellow;background-color: gray">黄色</span>')
        tip+=('文字')
    }else if(key4=='03'){
        tip+=('请输入验证码图片中')
        tip+=('<span style="color: blue;background-color: gray">蓝色</span>')
        tip+=('文字')
    }
    return tip;
}
//核验提示信息
var modalInfo=function (key1) {
    var tip="";
    if(key1=='001'){
        tip="核验成功"
    }else if(key1=='002'){
        tip="当日核验次数已达上限"
    }else if(key1=='007'){
        tip="验证码失效"
    }else if(key1=='008'){
        tip=" 验证码错误"
    } else if(key1=='009'){
        tip="查无此票"
    }
    return tip;
}

function getSwjg(fpdm, ckflag){
    var flag = "";
    eval(function(p,a,c,k,e,d){e=function(c){return(c<a?"":e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)d[e(c)]=k[c]||e(c);k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1;};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p;}('24 X=[{\'7\':\'12\',\'8\':\'13\',\'6\':\'0://3.G.4.2.1:5\',\'9\':\'0://3.G.4.2.1:5\'},{\'7\':\'14\',\'8\':\'Y\',\'6\':\'0://3.L.2.1:5\',\'9\':\'0://3.L.2.1:5\'},{\'7\':\'1j\',\'8\':\'1g\',\'6\':\'0://3.U.4.2.1\',\'9\':\'0://3.U.4.2.1\'},{\'7\':\'1k\',\'8\':\'1f\',\'6\':\'0://3.K.4.2.1:5\',\'9\':\'0://3.K.4.2.1:5\'},{\'7\':\'1a\',\'8\':\'18\',\'6\':\'0://3.R.4.2.1:5\',\'9\':\'0://3.R.4.2.1:5\'},{\'7\':\'1e\',\'8\':\'1h\',\'6\':\'0://3.m.4.2.1:5\',\'9\':\'0://3.m.4.2.1:5\'},{\'7\':\'1c\',\'8\':\'1b\',\'6\':\'0://3.q.2.1:5\',\'9\':\'0://3.q.2.1:5\'},{\'7\':\'1d\',\'8\':\'17\',\'6\':\'0://3.j.4.2.1:d\',\'9\':\'0://3.j.4.2.1:d\'},{\'7\':\'19\',\'8\':\'1l\',\'6\':\'0://3.f-n-a.2.1:5\',\'9\':\'0://3.f-n-a.2.1:5\'},{\'7\':\'1n\',\'8\':\'1m\',\'6\':\'0://B.a.A.2.1:z\',\'9\':\'0://B.a.A.2.1:z\'},{\'7\':\'1i\',\'8\':\'W\',\'6\':\'0://3.F.4.2.1:b\',\'9\':\'0://3.F.4.2.1:b\'},{\'7\':\'V\',\'8\':\'10\',\'6\':\'0://3.u.4.2.1:5\',\'9\':\'0://3.u.4.2.1:5\'},{\'7\':\'16\',\'8\':\'Z\',\'6\':\'0://3.v.4.2.1:5\',\'9\':\'0://3.v.4.2.1:5\'},{\'7\':\'11\',\'8\':\'15\',\'6\':\'0://3.w.4.2.1:5\',\'9\':\'0://3.w.4.2	.1:5\'},{\'7\':\'1o\',\'8\':\'1T\',\'6\':\'0://3.x.4.2.1:5\',\'9\':\'0://3.x.4.2.1:5\'},{\'7\':\'1S\',\'8\':\'1U\',\'6\':\'0://3.y.4.2.1\',\'9\':\'0://3.y.4.2.1\'},{\'7\':\'1W\',\'8\':\'1V\',\'6\':\'0://3.s.4.2.1:t\',\'9\':\'0://3.s.4.2.1:t\'},{\'7\':\'1R\',\'8\':\'1N\',\'6\':\'0://3.D.4.2.1:5\',\'9\':\'0://3.D.4.2.1:5\'},{\'7\':\'1M\',\'8\':\'1O\',\'6\':\'0://3.E.4.2.1:5\',\'9\':\'0://3.E.4.2.1:5\'},{\'7\':\'1Q\',\'8\':\'1P\',\'6\':\'0://3.C.4.2.1\',\'9\':\'0://3.C.4.g	23.1\'},{\'7\':\'25\',\'8\':\'27\',\'6\':\'0://3.r-n-a.2.1:5\',\'9\':\'0://3.r-n-a.2.1:5\'},{\'7\':\'26\',\'8\':\'22\',\'6\':\'0://3.h.4.2.1:e\',\'9\':\'0://3.h.4.2.1:e\'},{\'7\':\'1Y\',\'8\':\'1X\',\'6\':\'0://3.i-n-a.2.1:5\',\'9\':\'0://3.i-n-a.2.1:5\'},{\'7\':\'1Z\',\'8\':\'21\',\'6\':\'0://3.c.4.2.1:5\',\'9\':\'0://3.c.4.2.1:5\'},{\'7\':\'20\',\'8\':\'1L\',\'6\':\'0://3.o.4.2.1:p\',\'9\':\'0://3.o.4.2.1:p\'},{\'7\':\'1w\',\'8\':\'1v\',\'6\':\'0://3.k.4.2.1:5\',\'9\':\'0://3.k.4.2.1:5\'},{\'7\':\'1x\',\'8\':\'1z\',\'6\':\'0://3.l.4.2.1:b\',\'9\':\'0://3.l.4.2.1:b\'},{\'7\':\'1y\',\'8\':\'1u\',\'6\':\'0://3.Q.4.2.1:5\',\'9\':\'0://3.Q.4.2.1:5\'},{\'7\':\'1q\',\'8\':\'1p\',\'6\':\'0://3.T-n-a.2.1:b\',\'9\':\'0://3.T-n-a.2.1:b\'},{\'7\':\'1r\',\'8\':\'1t\',\'6\':\'0://3.J.2.1:5\',\'9\':\'0://3.J.2.1:5\'},{\'7\':\'1s\',\'8\':\'1H\',\'6\':\'0://3.H.2.1:O\',\'9\':\'0://3.H.2.1:O\'},{\'7\':\'1G\',\'8\':\'1I\',\'6\':\'0://3.N.4.2.1:5\',\'9\':\'0://3.N.4.2.1:5\'},{\'7\':\'1K\',\'8\':\'1J\',\'6\':\'0://3.M.4.2.1:5\',\'9\':\'0://3.M.4.2.1:5\'},{\'7\':\'1F\',\'8\':\'1B\',\'6\':\'0://3.P.4.2.1:5\',\'9\':\'0://3.P.4.2.1:5\'},{\'7\':\'1A\',\'8\':\'1C\',\'6\':\'0://3.I.4.2.1:5\',\'9\':\'0://3.I.4.2.1:5\'},{\'7\':\'1E\',\'8\':\'1D\',\'6\':\'0://3.S-n-a.2.1:5\',\'9\':\'0://3.S-n-a.2.1:5\'}];',62,132,'https|cn|gov|fpcy|chinatax|443|Ip|code|sfmc|address|tax|80|shenzhen|4432|8083|hl||hunan|gd|jilin|hainan|chongqing|liaoning||guangxi|8200|dlntax|hb|jiangxi|82|zhejiang|ningbo|anhui|fujian|xiamen|1001|sh|fpcyweb|henan|shandong|qingdao|jiangsu|beijing|xztax|ningxia|yngs|shanxi|tjsat|gansu|shaanxi|81|qinghai|sichuan|neimenggu|xj|gz|hebei|3300|江苏|citys|天津|宁波|浙江|3400|1100|北京|1200|安徽|3302|吉林|内蒙古|2300|1500|大连|2102|2200|2100|山西|河北|辽宁|3200|1300|1400|黑龙江|上海|3100|3500|贵州|5200|5300|5400|云南|四川|海南|4600|5000|5100|重庆|6400|青海|宁夏|新疆|6500|6300|6100|西藏|陕西|甘肃|6200|广西|3702|山东|青岛|河南|4100|3700|3502|福建|厦门|江西|3600|广东|4400|4403|4500|深圳|湖南|ov|var|4200|4300|湖北'.split('|'),0,{}))
    var dqdm=null;var swjginfo=new Array();if(fpdm.length==12){dqdm=fpdm.substring(1,5)}else{dqdm=fpdm.substring(0,4)}if(dqdm!="2102"&&dqdm!="3302"&&dqdm!="3502"&&dqdm!="3702"&&dqdm!="4403"){dqdm=dqdm.substring(0,2)+"00"}for(var i=0;i<citys.length;i++){if(dqdm==citys[i].code){swjginfo[0]=citys[i].sfmc;if(flag=='debug'){}else{swjginfo[1]=citys[i].Ip+"/WebQuery";swjginfo[2]=dqdm}break}}
    return swjginfo;
}

//发票类型
function getFplx(fpdm) {
    var a =fpdm;
    var b;
    var c = "99";
    if (a.length == 12) {
        b = a.substring(7, 8);
        /* for (var i = 0; i < code.length; i++) {
             if (a == code[i]) {
                 c = "10";
                 break
             }
         }*/
        if (c == "99") {
            if (a.charAt(0) == '0' && a.substring(10, 12) == '11') {
                c = "10"
            }
            if (a.charAt(0) == '0' && (a.substring(10, 12) == '04' || a.substring(10, 12) == '05')) {
                c = "04"
            }
            if (a.charAt(0) == '0' && (a.substring(10, 12) == '06' || a.substring(10, 12) == '07')) {
                c = "11"
            }
            if (a.charAt(0) == '0' && a.substring(10, 12) == '12') {
                c = "14"
            }
        }
        if (c == "99") {
            if (a.substring(10, 12) == '17' && a.charAt(0) == '0') {
                c = "15"
            }
            if (c == "99" && b == 2 && a.charAt(0) != '0') {
                c = "03"
            }
        }
    } else if (a.length == 10) {
        b = a.substring(7, 8);
        if (b == 1 || b == 5) {
            c = "01"
        } else if (b == 6 || b == 3) {
            c = "04"
        } else if (b == 7 || b == 2) {
            c = "02"
        }
    }
    return c
}

//发票核验失败，更新发票识别状态
var updateInvoiceStatus=function (fphm) {
    var reiId=$("#reiId").val();
    $.ajax({
        url: "/invoice/updateInvoiceStatus",
        type: 'post',
        async:false,
        dataType: 'json',
        data:{
            fphm:fphm
        },
        success: function(data){
            //加载内容到页面中
            if(data.code == '0000'){
                queryData(reiId)
            }else{
                alert(data.message)
            }
        }
    });

}

var viewFile=function (url) {
    var filepath=staticServer+url.substring(uploads.length)
    window.open(filepath)
}
    


