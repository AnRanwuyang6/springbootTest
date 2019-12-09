package com.bingo.invoice.invoice;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lizk
 * @Date: 2019/5/14 10:44
 * @Description:
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userName = request.getSession().getAttribute("userName");
        if (null == userName || !(userName instanceof String)) {
            // 未登录，重定向到登录页
            response.sendRedirect("/");
            return false;
        }
        String user = (String) userName;
        System.out.println("当前用户已登录，登录的用户名为： " + user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
