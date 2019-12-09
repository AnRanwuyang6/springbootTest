
$(function(){
    $("#submit").on('click',function () {
       loginSubmit();
    })
})

var loginSubmit=function () {
    $.ajax({
        url: '/login/submit',
        type: 'post',
        dataType: 'json',
        data:{
            // 需要传到后台的值，可带参进行分页
            userName: $("#userName").val(),
            password: $("#password").val()
        },
        success: function(data){
            //加载内容到页面中
            if(data.code=='0000'){
                window.location.href="/reimbursement/list"
            }else{
                $("#message").css("display","");
            }
        }
    });
}

