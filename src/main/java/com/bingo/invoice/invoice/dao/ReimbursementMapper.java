package com.bingo.invoice.invoice.dao;

import com.bingo.invoice.invoice.entity.Reimbursement;

import java.util.List;
import java.util.Map;

public interface ReimbursementMapper {
    int deleteByPrimaryKey(String id);

    int insert(Reimbursement record);

    int insertSelective(Reimbursement record);

    Reimbursement selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Reimbursement record);

    int updateByPrimaryKey(Reimbursement record);
	 //分页查询报销单
    List<Reimbursement> listPageByParams(Map<String, Object> map);

    //查询未提交的报销单（状态为0）
    List<Reimbursement> getNoSubmitData(Map<String, Object> map);

    //批量删除报销单
    void deleteBatch(List<String> reiIds);

}