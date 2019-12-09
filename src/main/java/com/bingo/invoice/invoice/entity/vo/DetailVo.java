package com.bingo.invoice.invoice.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: lizk
 * @Date: 2019/5/8 17:27
 * @Description:
 */
public class DetailVo {

    private String fwmc1;
    private String fwmc2;
    private BigDecimal se;
    private BigDecimal je;//金额
    private String title;
    private String fphm;
    private BigDecimal hjse;
    private BigDecimal hjje;//合计金额
    private String reiPersion;
    private String createDate;
    private String invId;
    private String reiId;

    public String getFwmc1() {
        return fwmc1;
    }

    public void setFwmc1(String fwmc1) {
        this.fwmc1 = fwmc1;
    }

    public String getFwmc2() {
        return fwmc2;
    }

    public void setFwmc2(String fwmc2) {
        this.fwmc2 = fwmc2;
    }

    public BigDecimal getSe() {
        return se;
    }

    public void setSe(BigDecimal se) {
        this.se = se;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFphm() {
        return fphm;
    }

    public void setFphm(String fphm) {
        this.fphm = fphm;
    }

    public BigDecimal getHjse() {
        return hjse;
    }

    public void setHjse(BigDecimal hjse) {
        this.hjse = hjse;
    }

    public String getReiPersion() {
        return reiPersion;
    }

    public void setReiPersion(String reiPersion) {
        this.reiPersion = reiPersion;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getReiId() {
        return reiId;
    }

    public void setReiId(String reiId) {
        this.reiId = reiId;
    }

    public BigDecimal getJe() {
        return je;
    }

    public void setJe(BigDecimal je) {
        this.je = je;
    }

    public BigDecimal getHjje() {
        return hjje;
    }

    public void setHjje(BigDecimal hjje) {
        this.hjje = hjje;
    }
}
