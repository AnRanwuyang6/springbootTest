package com.bingo.invoice.invoice.dao;

import com.bingo.invoice.invoice.entity.Detail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface DetailMapper {
    int deleteByPrimaryKey(String seq);

    int insert(Detail record);

    int insertSelective(Detail record);

    Detail selectByPrimaryKey(String seq);

    int updateByPrimaryKeySelective(Detail record);

    int updateByPrimaryKey(Detail record);
    List<Detail> getDetails(Map<String,Object> map);

    int deleteByInvoiceId(Map<String,Object> map);
}