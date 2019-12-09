package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.ReimbursementService;
import com.bingo.invoice.invoice.common.AjaxResult;
import com.bingo.invoice.invoice.dao.ReimbursementMapper;
import com.bingo.invoice.invoice.entity.Reimbursement;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Auther: lizk
 * @Date: 2019/4/26 14:22
 * @Description:
 */
@Controller
@RequestMapping("/reimbursement")
public class ReimbursementController {

    private final Logger logger= LoggerFactory.getLogger(ReimbursementController.class);
    private final String begin_suffix=" 00:00:00";
    private final String end_suffix=" 23:59:59";

    @Autowired
    private ReimbursementService reimbursementService;
    @Autowired
    private ReimbursementMapper reimbursementMapper;
    @RequestMapping("/list")
    public String reimbursement(){
        return "reimbursement/Frame";
    }

    /**
     *
     * 功能描述: 报销单列表
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/30 10:59
     */
    @RequestMapping("listAjax")
    @ResponseBody
    public AjaxResult listAjax(Integer pageNum, Integer pageSize, String beginTime,
                               String endTime,String reiPersion){
        AjaxResult result=new AjaxResult();
        try {
            Map<String,Object> map=new HashMap<String,Object>();
            if(StringUtils.isNotEmpty(beginTime)){
                map.put("beginTime",beginTime+begin_suffix);
            }if(StringUtils.isNotEmpty(endTime)){
                map.put("endTime",endTime+end_suffix);
            }
            map.put("reiPersion",reiPersion);
            PageInfo<Reimbursement> pageInfo=reimbursementService.pageList(pageNum,pageSize,map);
            result.setData(pageInfo);
            result.setCode(AjaxResult.RESULT_CODE_0000);
        }catch (Exception e){
            logger.info(e.getMessage());
            result.setMessage(e.getMessage());
            result.setCode(AjaxResult.RESULT_CODE_0001);
        }
        return  result;
    }
    @RequestMapping("/toDetail")
    public String toDetail(String reiId,Model model){
        Reimbursement reimbursement=reimbursementMapper.selectByPrimaryKey(reiId);
        model.addAttribute("reimbursement",reimbursement);
        return "reimbursementDetail/Frame";
    }
    /**
     *
     * 功能描述: 跳转到添加报销单页面
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/29 17:12
     */
    @RequestMapping("/addReimbursement")
    public String addReimbursement(String reiId,Model model){
        if (StringUtils.isNotEmpty(reiId)){
            Reimbursement reimbursement=reimbursementMapper.selectByPrimaryKey(reiId);
            model.addAttribute("id",reimbursement.getId());
            model.addAttribute("reiPersion",reimbursement.getReiPersion());
            model.addAttribute("reiDep",reimbursement.getReiDep());
            model.addAttribute("reiProject",reimbursement.getReiProject());
            model.addAttribute("responsiblePersion",reimbursement.getResponsiblePersion());
        }
        return "addReimbursement/Frame";
    }
    /**
     *
     * 功能描述: 跳转到添加电子发票页面
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/29 17:13
     */
    @RequestMapping("/addinvoice")
    public String addinvoice(String reiId, Model model){
        model.addAttribute("reiId",reiId);
        return "addinvoice/Frame";
    }

    @RequestMapping("/insert")
    @ResponseBody
    public AjaxResult insert(Reimbursement reimbursement, HttpServletRequest request){
        AjaxResult result=new AjaxResult();
        String reiId="";
        try {
            if(StringUtils.isNotEmpty(reimbursement.getId())){
                //更新
                reimbursement.setLastUpdateAt(new Date());
                reimbursement.setLastUpdateBy(request.getSession().getAttribute("userName").toString());
                reimbursementService.update(reimbursement);
                reiId=reimbursement.getId();
            }else{
                //新增
                reimbursement.setCreateBy(request.getSession().getAttribute("userName").toString());
                reimbursement.setCreateAt(new Date());
             reiId=reimbursementService.insert(reimbursement);
            }
            result.setData(reiId);
            result.setCode(AjaxResult.RESULT_CODE_0000);
        }catch (Exception e){
            result.setCode(AjaxResult.RESULT_CODE_0001);
            logger.info(e.getMessage());
        }
        return result;
    }

}
