package com.bingo.invoice.invoice.InvoiceService.impl;

import com.bingo.invoice.invoice.InvoiceService.ReimbursementService;
import com.bingo.invoice.invoice.dao.InvoiceMapper;
import com.bingo.invoice.invoice.dao.ReimbursementMapper;
import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.Reimbursement;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: lizk
 * @Date: 2019/4/29 17:21
 * @Description:
 */
@Service
public class ReimbursementServiceImpl implements ReimbursementService {
    private final Logger logger = LoggerFactory.getLogger(ReimbursementServiceImpl.class);
    @Autowired
    private ReimbursementMapper reimbursementMapper;
    @Override
    public String insert(Reimbursement reimbursement) {
        String id= UUID.randomUUID().toString();
        reimbursement.setCreateDate(new Date());
        reimbursement.setId(id);
        reimbursement.setStatus("0");
        reimbursementMapper.insertSelective(reimbursement);
        return id;
    }

    @Override
    public void update(Reimbursement reimbursement) {
      reimbursementMapper.updateByPrimaryKeySelective(reimbursement);
    }

    @Override
    public PageInfo<Reimbursement> pageList(Integer pageNum, Integer pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum,pageSize);
        List<Reimbursement> reimbursementList=reimbursementMapper.listPageByParams(map);
        PageInfo<Reimbursement> pageInfo=new PageInfo<Reimbursement>(reimbursementList);
        return pageInfo;
    }
}
