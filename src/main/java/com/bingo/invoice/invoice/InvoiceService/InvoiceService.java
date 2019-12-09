package com.bingo.invoice.invoice.InvoiceService;

import com.bingo.invoice.invoice.common.InvoiceData;
import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.vo.DetailVo;
import com.bingo.invoice.invoice.entity.vo.InvoiceVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lizk
 * @Date: 2019/4/28 14:29
 * @Description:
 */
public interface InvoiceService {
    InvoiceData extract(String filename);
    //插入数据库
    void insertInvoice(InvoiceData data,String reiId);

    void insertInvoiceError(String reiId);

    List<Invoice> getInvoiceListByReiId(String reiId);

    List<InvoiceVo> getInvoiceVoListByReiId(String reiId);
    //根据发票号码查询list
    List<Invoice> getInvoiceListByNo(String fphm);

    void invoicesIdentify(String ids);

    void deleteById(String id);

    //多条件分页查询发票列表
    PageInfo<InvoiceVo> getInvoicePageByParams(Integer pageNum, Integer pageSize,Map<String,Object> map);
    //多条件不分页查询发票列表
    List<InvoiceVo> getInvoicesByParams(Map<String,Object> map);

    //更新报销单状态，算出总价格
    void updateReimbursement(String reiId);
    //分页查询明细
    PageInfo<DetailVo> getDetailPageList(Integer pageNum, Integer pageSize, Map<String,Object> map);
    //不分页查询明细，计算汇总信息
    List<DetailVo> getDetailVoList(Map<String,Object> map);

    //更新报销单信息和发票识别状态

    void updateInvoiceStatus(String fphm);

    //定时任务清除冗余数据

    void taskDeleteData();


}
