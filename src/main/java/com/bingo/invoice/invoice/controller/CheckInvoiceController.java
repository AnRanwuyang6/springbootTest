package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.common.AjaxResult;
import com.bingo.invoice.invoice.common.GetvImg;
import com.bingo.invoice.invoice.common.InvInfor;
import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.vo.CheckVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lizk
 * @Date: 2019/5/6 16:50
 * @Description:
 */
@Controller
@RequestMapping("/checkInvoice")
public class CheckInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    private final Logger logger=LoggerFactory.getLogger(CheckInvoiceController.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     *
     * 功能描述: 获取(刷新)图片验证码
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/5/7 18:10
     */
    @RequestMapping("/getImg")
    @ResponseBody
    public AjaxResult getImg(String InvCode,String InvNo,String url_prefix){
        AjaxResult ajaxResult=new AjaxResult();
        try {
            InvInfor inv=new InvInfor();
            inv.setInvNo(InvNo);
            inv.setInvCode(InvCode);
            String nowtime=String.valueOf(System.currentTimeMillis());
            String publickey="";
            //得到访问路径
           //String url_prefix=this.getUrlPrefix(InvNo)
            //得到areaCode
            String areaCode=GetvImg.getAreaCode(InvCode);
            //得到验证码图片
            String img_info=GetvImg.getvImg(inv,nowtime,publickey,areaCode, url_prefix);
            String jsonStr=img_info.substring(img_info.indexOf("(")+1,img_info.lastIndexOf(")"));
            Map<String,String> map=GetvImg.convert2Map(jsonStr);
            ajaxResult.setData(map);
            ajaxResult.setCode(AjaxResult.RESULT_CODE_0000);
        }catch (Exception e){
            logger.info("查询错误");
            ajaxResult.setMessage("查询错误");
            ajaxResult.setCode(AjaxResult.RESULT_CODE_0001);
        }
        return ajaxResult;
    }
    /**
     *
     * 功能描述: 校验发票
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/5/7 18:10
     */
    @RequestMapping("/getInvoiceInfo")
    @ResponseBody
    public AjaxResult getInvoiceInfo(CheckVo checkVo){
        AjaxResult ajaxResult=new AjaxResult();
        try {
            Date date=format.parse(checkVo.getKprq());
            String kprq=format2.format(date);
            checkVo.setInvDate(kprq);
            checkVo.setKey2Date(sdf.parse(checkVo.getKey2()));
            //请求前缀，发票类型
            //String url_prefix=this.getUrlPrefix(checkVo.getFphm());
            String url_prefix=checkVo.getUrl_prefix();
            //得到发票areaCode
            String area=GetvImg.getAreaCode(checkVo.getFpdm());
            //得到查验信息
            String info=GetvImg.getInvInfor(url_prefix,checkVo,area);
            String jsonStr=info.substring(info.indexOf("(")+1,info.lastIndexOf(")"));
            Map<String,String> map=GetvImg.convert2Map(jsonStr);
            ajaxResult.setData(map);
            ajaxResult.setCode(AjaxResult.RESULT_CODE_0000);
        }catch (Exception e){
            logger.info(e.getMessage());
            ajaxResult.setCode(AjaxResult.RESULT_CODE_0001);
        }
        return ajaxResult;
    }

    /**
     * 根据发票号码得到访问路径
     */
    private String getUrlPrefix(String fphm){
        String url_prefix="";
        List<Invoice> invoiceList=invoiceService.getInvoiceListByNo(fphm);
        String province=invoiceList.get(0).getTitle().substring(0,2);
        int x=invoiceList.get(0).getFpdm().indexOf("03100");
        if(province.equals("广东")){
            url_prefix="https://fpcy.gd-n-tax.gov.cn/WebQuery";
        }else if(province.equals("江苏")){
            url_prefix="https://fpcy.jiangsu.chinatax.gov.cn:80/WebQuery";
        }else if(province.equals("吉林")){
           url_prefix="https://fpcy.jilin.chinatax.gov.cn:4432/WebQuery";
        }else if(province.equals("上海")){
            url_prefix="https://fpcyweb.tax.sh.gov.cn:1001/WebQuery";
        }else if(x!=-1){
            url_prefix="https://fpcyweb.tax.sh.gov.cn:1001/WebQuery";
        }else{
            url_prefix=GetvImg.getUrlPrefix(province);
        }
        return url_prefix;
    }


}
