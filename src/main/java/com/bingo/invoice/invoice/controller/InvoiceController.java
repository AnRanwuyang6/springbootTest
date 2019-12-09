package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.common.AjaxResult;
import com.bingo.invoice.invoice.common.ExportUtil;
import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.vo.DetailVo;
import com.bingo.invoice.invoice.entity.vo.InvoiceVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: lizk
 * @Date: 2019/4/29 11:55
 * @Description:
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {
    private final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    private final String begin_suffix=" 00:00:00";
    private final String end_suffix=" 23:59:59";
    private final String exportFileName="发票统计";
    @Autowired
    private InvoiceService invoiceService;

    /**
     *
     * 功能描述: 根据报销单id查询发票
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/29 14:21
     */
    @RequestMapping("/listByReiId")
    @ResponseBody
    private AjaxResult listByReiId(String reiId){
        AjaxResult result=new AjaxResult();
        try {
            //List<Invoice> invoiceList=invoiceService.getInvoiceListByReiId(reiId);
            List<InvoiceVo> invoiceVoList=invoiceService.getInvoiceVoListByReiId(reiId);
            result.setCode(AjaxResult.RESULT_CODE_0000);
            result.setData(invoiceVoList);
            return result;
        }catch (Exception e){
            result.setCode(AjaxResult.RESULT_CODE_0001);
            result.setMessage(e.getMessage());
            return result;
        }
    }
    /**
     *
     * 功能描述: 识别发票
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/29 14:22
     */
    @RequestMapping("/identify")
    @ResponseBody
    public AjaxResult identify(String ids){
    AjaxResult result =new AjaxResult();
    try {
        invoiceService.invoicesIdentify(ids);
        result.setCode(AjaxResult.RESULT_CODE_0000);
    }catch (Exception e){
        result.setCode(AjaxResult.RESULT_CODE_0001);
        result.setMessage(e.getMessage());
        logger.info(e.getMessage());
    }
    return result;
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public AjaxResult delete(@PathVariable("id")String id){
        AjaxResult result=new AjaxResult();
        try {
            invoiceService.deleteById(id);
            result.setCode(AjaxResult.RESULT_CODE_0000);
            result.setMessage("成功");
        }catch (Exception e){
            result.setCode(AjaxResult.RESULT_CODE_0001);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    /**
     *
     * 功能描述: 发票提交,更新报销单状态,算出总价格
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/30 10:09
     */
    @RequestMapping("/sumbit")
    @ResponseBody
    public AjaxResult sumbit(String reiId){
        AjaxResult result=new AjaxResult();
        try {
            invoiceService.updateReimbursement(reiId);
            result.setCode(AjaxResult.RESULT_CODE_0000);
            result.setMessage("成功");
        }catch (Exception e){
            result.setCode(AjaxResult.RESULT_CODE_0001);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    /**
     *
     * 功能描述: 
     *
     * @param: 跳转到发票校验页面
     * @return: 
     * @auther: lizk
     * @date: 2019/5/9 15:34
     */

    @RequestMapping("/toCheck")
    public String toCheck(){
        return "verification/Frame";
    }
    /**
     *
     * 功能描述: 
     *
     * @param: 跳转到发票管理耶页面
     * @return: 
     * @auther: lizk
     * @date: 2019/5/9 15:40
     */
    @RequestMapping("/tomanageList")
    public String manageList(){
        return "invoiceManage/Frame";
    }

    @RequestMapping("/manageList")
    @ResponseBody
    public AjaxResult manageList(Integer pageNum, Integer pageSize, String beginTime,
                        String endTime,String searchText){
        AjaxResult result=new AjaxResult();
        try {
            Map<String,Object> map=new HashMap<String,Object>();
            if(StringUtils.isNotEmpty(beginTime)){
                map.put("beginTime",beginTime+begin_suffix);
            }if(StringUtils.isNotEmpty(endTime)){
                map.put("endTime",endTime+end_suffix);
            }
            map.put("searchText",searchText);
            PageInfo<InvoiceVo> pageInfo=invoiceService.getInvoicePageByParams(pageNum,pageSize,map);
            result.setData(pageInfo);
            result.setCode(AjaxResult.RESULT_CODE_0000);
            result.setMessage("成功");

        }catch (Exception e){
            result.setCode(AjaxResult.RESULT_CODE_0001);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    /**
     *
     * 功能描述: 发票核验失败，更新报销单信息和发票识别状态
     *
     * @param: fphm
     * @return: AjaxResult
     * @auther: lizk
     * @date: 2019/5/13 11:04
     */
    @RequestMapping("/updateInvoiceStatus")
    @ResponseBody
    public AjaxResult updateInvoiceStatus(String fphm){
        AjaxResult result=new AjaxResult();
        try {

        }catch (Exception e){

        }
        return result;
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response, String beginTime,
                       String endTime, String searchText) throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(beginTime)){
            map.put("beginTime",beginTime+begin_suffix);
        }if(StringUtils.isNotEmpty(endTime)){
            map.put("endTime",endTime+end_suffix);
        }
        map.put("searchText",searchText);

        List<InvoiceVo> invoiceVoList=invoiceService.getInvoicesByParams(map);
        //导出对象
        List<List<Object>> list=new ArrayList<>();
        //添加头
        List<Object> header=new ArrayList<>(Arrays.asList("发票名称","发票代码","发票号码","开票日期","合计金额","合计税额",
                "发票类型","报销人","报销时间"));
        list.add(header);
        for(InvoiceVo invoiceVo:invoiceVoList){
            //存放对象每一个属性
            List<Object> attributes=new ArrayList<>();
            attributes.add(invoiceVo.getTitle());
            attributes.add(invoiceVo.getFpdm());
            attributes.add(invoiceVo.getFphm());
            attributes.add(invoiceVo.getKprq());
            attributes.add(invoiceVo.getHjje());
            attributes.add(invoiceVo.getHjse());
            if(invoiceVo.getType().equals("01")){
                attributes.add("通行费");
            }else{
                attributes.add("其他");
            }
            attributes.add(invoiceVo.getReiPersion());
            attributes.add(invoiceVo.getReiDate());
            //将对象放入list
            list.add(attributes);
        }
        //导出
        ExportUtil.exportExcel(response,exportFileName,list,null);
    }

}
