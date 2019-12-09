package com.bingo.invoice.invoice.config;

import com.bingo.invoice.invoice.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @Auther: lizk
 * @Date: 2019/5/14 10:46
 * @Description:
 */
@Configuration
public class LoginConfiguration implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        LoginInterceptor loginInterceptor=new LoginInterceptor();
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginInterceptor);
        // 拦截路径
        loginRegistry.addPathPatterns("/**");
        // 排除路径
        loginRegistry.excludePathPatterns("/");
        loginRegistry.excludePathPatterns("/login");
        loginRegistry.excludePathPatterns("/login/submit");
        loginRegistry.excludePathPatterns("/error");
        // 排除资源请求
        loginRegistry.excludePathPatterns("/bootstrap-fileinput/**");
        loginRegistry.excludePathPatterns("/css/**");
        loginRegistry.excludePathPatterns("/email_templates/**");
        loginRegistry.excludePathPatterns("/font/**");
        loginRegistry.excludePathPatterns("/font-awesome/**");
        loginRegistry.excludePathPatterns("/fonts/**");
        loginRegistry.excludePathPatterns("/img/**");
        loginRegistry.excludePathPatterns("/js/**");
        loginRegistry.excludePathPatterns("/locales/**");
        loginRegistry.excludePathPatterns("/myjs/**");
        loginRegistry.excludePathPatterns("/pdf/**");
    }
}
