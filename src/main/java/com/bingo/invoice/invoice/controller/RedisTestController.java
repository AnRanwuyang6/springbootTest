package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.common.JacksonUtils;
import com.bingo.invoice.invoice.common.RedisUtil;
import com.bingo.invoice.invoice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件名
 * Created at 2020/1/15
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisTestController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/string/set")
    public String set(){
        try {
            User user=new User();
            user.setRealname("lizk");
            user.setUsername("张三");
            String jsonstr= JacksonUtils.obj2json(user);
            redisUtil.set("stringTest1",jsonstr);
            return "success";
        }catch (Exception e){
            log.info(e.toString());
            return "error";
        }
    }
    @RequestMapping("/string/del")
    public String del(String key){
        try {
            redisUtil.del(key);
            return "success";
        }catch (Exception e){
            log.info(e.toString());
            return "error";
        }
    }
}
