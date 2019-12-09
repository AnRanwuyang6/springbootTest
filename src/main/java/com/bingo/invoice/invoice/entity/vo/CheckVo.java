package com.bingo.invoice.invoice.entity.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: lizk
 * @Date: 2019/5/7 10:29
 * @Description:
 */
public class CheckVo implements Serializable{
    private String fpdm;//发票代码
    private String fphm;//发票号码
    private String kprq;//开票日期中文
    private String jym;//校验码后六位
    private String yzm;//验证码
    private String key2;//验证码生成时间
    private String key3;
    private String invDate;//开票日期数字
    private Date key2Date; //
    private String fplx;

    private String cookie;//验证码图片请求的cookie

    private String url_prefix;

    public String getFpdm() {
        return fpdm;
    }

    public void setFpdm(String fpdm) {
        this.fpdm = fpdm;
    }

    public String getFphm() {
        return fphm;
    }

    public void setFphm(String fphm) {
        this.fphm = fphm;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getJym() {
        return jym;
    }

    public void setJym(String jym) {
        this.jym = jym;
    }

    public String getYzm() {
        return yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public Date getKey2Date() {
        return key2Date;
    }

    public void setKey2Date(Date key2Date) {
        this.key2Date = key2Date;
    }

    public String getFplx() {
        return fplx;
    }

    public void setFplx(String fplx) {
        this.fplx = fplx;
    }

    public String getUrl_prefix() {
        return url_prefix;
    }

    public void setUrl_prefix(String url_prefix) {
        this.url_prefix = url_prefix;
    }
}
