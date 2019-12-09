package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.common.AjaxResult;
import com.bingo.invoice.invoice.common.ExportUtil;
import com.bingo.invoice.invoice.entity.vo.DetailVo;
import com.bingo.invoice.invoice.entity.vo.InvoiceVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: lizk
 * @Date: 2019/5/8 17:04
 * @Description:
 */
@Controller
@RequestMapping("/deduct")
public class DeductController {

    private final String begin_suffix=" 00:00:00";
    private final String end_suffix=" 23:59:59";
    private final String exportFileName="可抵扣发票明细项";

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping("/list")
    public String reimbursement(){
        return "deduct/Frame";
    }
    /**
     *
     * 功能描述: 分页查询发票明细，汇总信息
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/5/8 17:41
     */
    @RequestMapping("/listPageDetail")
    @ResponseBody
    private Map<String,Object> listPageDetail(Integer pageNum,Integer pageSize,String beginTime,
                                   String endTime,String searchText){
        AjaxResult result=new AjaxResult();
        Map<String,Object> map=new HashMap<String,Object>();
        try {
            if(StringUtils.isNotEmpty(beginTime)){
                map.put("beginTime",beginTime+begin_suffix);
            }if(StringUtils.isNotEmpty(endTime)){
                map.put("endTime",endTime+end_suffix);
            }
            map.put("searchText",searchText);
            PageInfo<DetailVo> pageInfo=invoiceService.getDetailPageList(pageNum,pageSize,map);
            //计算汇总信息
            List<DetailVo> detailVoList=invoiceService.getDetailVoList(map);
            BigDecimal totalJe=new BigDecimal(0);
            BigDecimal totalSe=new BigDecimal(0);
            for(DetailVo detailVo:detailVoList){
                totalJe=totalJe.add(detailVo.getJe());
                totalSe=totalSe.add(detailVo.getSe());
            }
            map.clear();
            map.put("pageInfo",pageInfo);
            map.put("totalJe",totalJe);
            map.put("totalSe",totalSe);
            map.put("code",AjaxResult.RESULT_CODE_0000);
            return map;
        }catch (Exception e){
            map.put("code",AjaxResult.RESULT_CODE_0001);
            return map;
        }
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response,String beginTime,
                       String endTime,String searchText) throws Exception{
            Map<String,Object> map=new HashMap<String,Object>();
            if(StringUtils.isNotEmpty(beginTime)){
                map.put("beginTime",beginTime+begin_suffix);
            }if(StringUtils.isNotEmpty(endTime)){
                map.put("endTime",endTime+end_suffix);
            }
            map.put("searchText",searchText);
            //导出对象
           List<DetailVo> detailVoList=invoiceService.getDetailVoList(map);
           List<List<Object>> list=new ArrayList<>();
           //添加头
           List<Object> header=new ArrayList<>(Arrays.asList("服务类别","服务名称","税额","金额","发票名称","发票号码",
                   "合计税额","合计金额","报销人","报销时间"));
           list.add(header);
           for(DetailVo detailVo:detailVoList){
               //存放对象每一个属性
              List<Object> attributes=new ArrayList<>();
               attributes.add(detailVo.getFwmc1());
               attributes.add(detailVo.getFwmc2());
               attributes.add(detailVo.getSe());
               attributes.add(detailVo.getJe());
               attributes.add(detailVo.getTitle());
               attributes.add(detailVo.getFphm());
               attributes.add(detailVo.getHjje());
               attributes.add(detailVo.getHjse());
               attributes.add(detailVo.getReiPersion());
               attributes.add(detailVo.getCreateDate());
               //将对象放入list
               list.add(attributes);
           }
           //计算汇总信息
            BigDecimal totalJe=new BigDecimal(0);
            BigDecimal totalSe=new BigDecimal(0);
            for(DetailVo detailVo:detailVoList){
                totalJe=totalJe.add(detailVo.getJe());
                totalSe=totalSe.add(detailVo.getSe());
            }
            List<Object> footer=new ArrayList<>(Arrays.asList("总税额:"+totalSe,"","","","","总金额:"+totalJe));
            list.add(footer);
            //合并
            Integer [] integers={list.size()-1,list.size(),0,4};
            Integer [] integers1={list.size()-1,list.size(),5,9};
            List<Integer[]>  mergeList=new ArrayList<>();
            mergeList.add(integers);
            mergeList.add(integers1);
           //导出
        ExportUtil.exportExcel(response,exportFileName,list,mergeList);
    }

}
