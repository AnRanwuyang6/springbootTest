package com.bingo.invoice.invoice.common;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Auther: lizk
 * @Date: 2019/5/9 17:55
 * @Description:
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private InvoiceService invoiceService;
    //3.添加定时任务
    //每月1号凌晨1点执行一次
    @Scheduled(cron = "0 0 1 1 * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        //执行清除数据任务
        System.out.println(LocalDateTime.now()+"清除数据开始"+"--------------");

        //invoiceService.taskDeleteData();

        System.out.println(LocalDateTime.now()+"清除数据结束"+"--------------");
    }
    //自定义逻辑
}
