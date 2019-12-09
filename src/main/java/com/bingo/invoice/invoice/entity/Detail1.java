package com.bingo.invoice.invoice.entity;

import java.math.BigDecimal;

public class Detail1 {
    private String seq;

    private String invoiceSeq;

    private String xmmc;

    private String cph;

    private String lx;

    private String rqq;

    private String rqz;

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

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc == null ? null : xmmc.trim();
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph == null ? null : cph.trim();
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx == null ? null : lx.trim();
    }

    public String getRqq() {
        return rqq;
    }

    public void setRqq(String rqq) {
        this.rqq = rqq == null ? null : rqq.trim();
    }

    public String getRqz() {
        return rqz;
    }

    public void setRqz(String rqz) {
        this.rqz = rqz == null ? null : rqz.trim();
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