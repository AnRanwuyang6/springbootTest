package com.bingo.invoice.invoice.dao;

import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.vo.DetailVo;
import com.bingo.invoice.invoice.entity.vo.InvoiceVo;

import java.util.List;
import java.util.Map;

public interface InvoiceMapper {
    int deleteByPrimaryKey(String seq);

    int insert(Invoice record);

    int insertSelective(Invoice record);

    Invoice selectByPrimaryKey(String seq);

    int updateByPrimaryKeySelective(Invoice record);

    int updateByPrimaryKey(Invoice record);
	List<Invoice> getListByParams(Map<String, Object> map);
    List<Invoice> getInvoiceListByReiId(Map<String, Object> map);

    List<InvoiceVo> getInvoiceVoListByReiId(Map<String, Object> map);
    //查出识别成功的发票
    List<Invoice> getInvoiceListByReiIdAndStatus(Map<String, Object> map);

    List<Invoice> getInvoiceListByNo(Map<String, Object> map);
    //明细列表
    List<DetailVo> getDetailByParams(Map<String, Object> map);

    //多条件查询发票列表
    List<InvoiceVo> getInvoicePageByParams(Map<String, Object> map);

    //批量删除
    void deleteBatchInvoice(List<String> reiIds);
}