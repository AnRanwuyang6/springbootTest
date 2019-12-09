package com.bingo.invoice.invoice.InvoiceService;

import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.Reimbursement;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;

import java.util.Map;

/**
 * @Auther: lizk
 * @Date: 2019/4/29 17:21
 * @Description:
 */
public interface ReimbursementService {
    String insert(Reimbursement reimbursement);
    void update(Reimbursement reimbursement);
    PageInfo<Reimbursement> pageList(Integer pageNum, Integer pageSize, Map<String,Object> map);
}
