package com.bingo.invoice.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Reimbursement {
    private String id;

    private String reiPersion;

    private String reiDep;

    private Date createDate;

    private String responsiblePersion;

    private String reiProject;

    private BigDecimal totalJe;

    private BigDecimal totalSe;

    private BigDecimal totalMoney;

    private String status;

    private String createBy;

    private Date createAt;

    private String lastUpdateBy;

    private Date lastUpdateAt;

    private String str1;

    private String str2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getReiPersion() {
        return reiPersion;
    }

    public void setReiPersion(String reiPersion) {
        this.reiPersion = reiPersion == null ? null : reiPersion.trim();
    }

    public String getReiDep() {
        return reiDep;
    }

    public void setReiDep(String reiDep) {
        this.reiDep = reiDep == null ? null : reiDep.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getResponsiblePersion() {
        return responsiblePersion;
    }

    public void setResponsiblePersion(String responsiblePersion) {
        this.responsiblePersion = responsiblePersion == null ? null : responsiblePersion.trim();
    }

    public String getReiProject() {
        return reiProject;
    }

    public void setReiProject(String reiProject) {
        this.reiProject = reiProject == null ? null : reiProject.trim();
    }

    public BigDecimal getTotalJe() {
        return totalJe;
    }

    public void setTotalJe(BigDecimal totalJe) {
        this.totalJe = totalJe;
    }

    public BigDecimal getTotalSe() {
        return totalSe;
    }

    public void setTotalSe(BigDecimal totalSe) {
        this.totalSe = totalSe;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy == null ? null : lastUpdateBy.trim();
    }

    public Date getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Date lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1 == null ? null : str1.trim();
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2 == null ? null : str2.trim();
    }
}