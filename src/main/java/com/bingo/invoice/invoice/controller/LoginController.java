package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.LdapService;
import com.bingo.invoice.invoice.common.AjaxResult;
import com.bingo.invoice.invoice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: lizk
 * @Date: 2019/5/14 10:09
 * @Description:
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LdapService ldapService;
    @Value("${uploads}")
    private String uploads;
    // 访问路径
    @Value("${staticServer}")
    private String staticServer;
    @RequestMapping("/submit")
    @ResponseBody
    public AjaxResult login(String userName, String password, HttpServletRequest request){
        AjaxResult result=new AjaxResult();
        boolean f=false;
        if(userName.equals("lizk")){
            f=true;
        }
        //boolean f=ldapService.authenticate(userName,password);
        //List<String> userList=ldapService.getDepartmentList("dc=bingosoft,dc=net",null);
        String msg="";
        if(f){
            //登录状态存入session
            HttpSession session=request.getSession();
            session.setAttribute("userName",userName);
            session.setAttribute("uploads",uploads);
            session.setAttribute("staticServer",staticServer);
            msg="yes";
            result.setCode(AjaxResult.RESULT_CODE_0000);
            result.setMessage(msg);
        }else{
            result.setCode(AjaxResult.RESULT_CODE_0001);
            result.setMessage(msg);
        }
        return result;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
       HttpSession session = request.getSession(false);//防止创建Session
        if(session==null){
            return "login/login";
        }
       session.removeAttribute("userName");
        return "login/login";
    }
}
