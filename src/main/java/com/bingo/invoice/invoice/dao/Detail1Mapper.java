package com.bingo.invoice.invoice.dao;

import com.bingo.invoice.invoice.entity.Detail1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface Detail1Mapper {
    int deleteByPrimaryKey(String seq);

    int insert(Detail1 record);

    int insertSelective(Detail1 record);

    Detail1 selectByPrimaryKey(String seq);

    int updateByPrimaryKeySelective(Detail1 record);

    int updateByPrimaryKey(Detail1 record);
    List<Detail1> getDetail1s(Map<String,Object> map);

    int deleteByInvoiceId(Map<String,Object> map);
}