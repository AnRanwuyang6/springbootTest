package com.bingo.invoice.invoice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lizk
 * @Date: 2019/5/14 10:08
 * @Description:
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "login/login";
    }

    @RequestMapping("merge")
    public String merge(){
        return "mergePdf/print";
    }
}
