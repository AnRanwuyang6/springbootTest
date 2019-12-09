$(function () {
    $('.reimbursement_00').find('ul').attr("class", "nav nav-second-level collapse in");
    $('.reimbursement_00').find('ul').attr("expanded", "true");
    $('.reimbursement_00').addClass('active')
    /* $('.reimbursement_00').attr("class", "active");*/
    $('.reimbursement_02').attr("class", "active");
    $("#addSumbit").on('click',function () {
        addReimbursement();
    })
})
var  addReimbursement=function () {
    var url = "/reimbursement/insert";
    $.ajax({
        url:url,
        type:'post',
        dataType:'json',
        data:$('#eimbursementrForm').serialize(),
        beforeSend:function () {
            if($("#reiPersionAdd").val()==""){
                swal({
                    title: "温馨提示",
                    text: "报销人不能为空"
                });
                return false
            }
            if($("#reiDepAdd").val()==""){
                swal({
                    title: "温馨提示",
                    text: "报销部门不能为空"
                });
                return false
            }
            if($("#reiProjectAdd").val()==""){
                swal({
                    title: "温馨提示",
                    text: "报销项目不能为空"
                });
                return false
            }
            if($("#responsiblePersionAdd").val()==""){
                swal({
                    title: "温馨提示",
                    text: "负责人不能为空"
                });
                return false
            }

            return true;
        },
        success:function (data) {
            if(data.code=='0000'){
                var reiId=data.data;
                window.location.href='/reimbursement/addinvoice?reiId='+reiId;
            }else {
                alert(data.message);
            }
        }
    });
}
