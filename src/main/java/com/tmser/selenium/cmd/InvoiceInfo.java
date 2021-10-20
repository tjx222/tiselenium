package com.tmser.selenium.cmd;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "invoice")
public class InvoiceInfo implements Serializable {


    // raido xpath input[@value="EINVOICE"]
    // 地址类型 : EINVOICE  增值税普通电子发票， SPECIALPAPERINVOICE  增值税专用纸质发票
    private String type = "EINVOICE";

    // raido xpath input[@value="INDIVIDUAL"]
    // 地址类型 : INDIVIDUAL  个人， COMPANY  公司
    private String recipientType;

    //text id invoiceTitle
    //发票抬头
    private String invoiceTitle;

    //text id taxRegNo
    //公司： 税务登记号
    private String taxRegNo;

    //text id regAddress
    //公司： 登记地址
    private String regAddress;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getTaxRegNo() {
        return taxRegNo;
    }

    public void setTaxRegNo(String taxRegNo) {
        this.taxRegNo = taxRegNo;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }


    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("type",type)
                .append("recipientType",recipientType)
                .append("invoiceTitle",invoiceTitle)
                .append("taxRegNo",taxRegNo)
                .append("regAddress",regAddress)
                .toString();
    }

}
