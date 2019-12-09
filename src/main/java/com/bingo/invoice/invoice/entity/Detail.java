package com.bingo.invoice.invoice.entity;

import java.math.BigDecimal;

public class Detail {
    private String seq;

    private String invoiceSeq;

    private String wwmc;

    private String ggxh;

    private String dw;

    private BigDecimal count;

    private BigDecimal dj;

    private BigDecimal je;

    private BigDecimal sl;

    private BigDecimal se;

    private String fwmc1;

    private String fwmc2;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq == null ? null : seq.trim();
    }

    public String getInvoiceSeq() {
        return invoiceSeq;
    }

    public void setInvoiceSeq(String invoiceSeq) {
        this.invoiceSeq = invoiceSeq == null ? null : invoiceSeq.trim();
    }

    public String getWwmc() {
        return wwmc;
    }

    public void setWwmc(String wwmc) {
        this.wwmc = wwmc == null ? null : wwmc.trim();
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh == null ? null : ggxh.trim();
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw == null ? null : dw.trim();
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getDj() {
        return dj;
    }

    public void setDj(BigDecimal dj) {
        this.dj = dj;
    }

    public BigDecimal getJe() {
        return je;
    }

    public void setJe(BigDecimal je) {
        this.je = je;
    }

    public BigDecimal getSl() {
        return sl;
    }

    public void setSl(BigDecimal sl) {
        this.sl = sl;
    }

    public BigDecimal getSe() {
        return se;
    }

    public void setSe(BigDecimal se) {
        this.se = se;
    }

    public String getFwmc1() {
        return fwmc1;
    }

    public void setFwmc1(String fwmc1) {
        this.fwmc1 = fwmc1 == null ? null : fwmc1.trim();
    }

    public String getFwmc2() {
        return fwmc2;
    }

    public void setFwmc2(String fwmc2) {
        this.fwmc2 = fwmc2 == null ? null : fwmc2.trim();
    }
}