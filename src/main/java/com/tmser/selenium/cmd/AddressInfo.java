package com.tmser.selenium.cmd;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "addr")
public class AddressInfo implements Serializable {

    // raido xpath input[@value="Business"]
    // 地址类型 : Business 企业， Other 个人/其他
    private String type;

    //select  id  select-country-list-id
     //option value=CN
    //国家
    private String country;

    //select id paid-address-list-id
    // option value=0 新地址
    // 是否使用旧地址 value 为旧地址id
    private String oldAddressId;

    //text id firstName
    private String firstName;

    //text id lastName
    private String lastName;

    //text id paid-line1
    // 地址1
    private String address1;

    //text id  line2
    // 地址2
    private String address2;

    //text id townCity
    // 市
    private String townCity;

    //text id state-input
    // 省
    private String state;

    //text id postcode
    private String postcode;

    //text id companyName
    private String companyName;

    //text id shipping_companyUrl
    private String companyUrl;

    //checkbox id phoneIsMobile
    private Boolean phoneIsMobile = true;

    //select id phoneCountryPrefix
    //option value = 86-CN
    private String phoneCountryPrefix;

    //text id phoneNumber
    private String phoneNumber;

    //text id phoneExtension
    private String phoneExtension;

    //text id email
    private String email;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOldAddressId() {
        return oldAddressId;
    }

    public void setOldAddressId(String oldAddressId) {
        this.oldAddressId = oldAddressId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTownCity() {
        return townCity;
    }

    public void setTownCity(String townCity) {
        this.townCity = townCity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public Boolean getPhoneIsMobile() {
        return phoneIsMobile;
    }

    public void setPhoneIsMobile(Boolean phoneIsMobile) {
        this.phoneIsMobile = phoneIsMobile;
    }

    public String getPhoneCountryPrefix() {
        return phoneCountryPrefix;
    }

    public void setPhoneCountryPrefix(String phoneCountryPrefix) {
        this.phoneCountryPrefix = phoneCountryPrefix;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("type",type)
                .append("country",country)
                .append("oldAddressId",oldAddressId)
                .append("firstName",firstName)
                .append("lastName",lastName)
                .append("state",state)
                .append("address2",address2)
                .append("address1",address1)
                .append("country",country)
                .append("townCity",townCity)
                .append("postcode",postcode)
                .append("companyName",companyName)
                .append("companyUrl",companyUrl)
                .append("phoneIsMobile",phoneIsMobile)
                .append("phoneCountryPrefix",phoneCountryPrefix)
                .append("phoneNumber",phoneNumber)
                .append("phoneExtension",phoneExtension)
                .append("email",email)
                .toString();
    }
}
