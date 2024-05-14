/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.SuperEntity;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "wechatuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeChatUser.findAll", query = "SELECT o FROM WeChatUser o"),
    @NamedQuery(name = "WeChatUser.findById", query = "SELECT o FROM WeChatUser o WHERE o.id = :id"),
    @NamedQuery(name = "WeChatUser.findByOpenId", query = "SELECT o FROM WeChatUser o WHERE o.openId = :openid"),
    @NamedQuery(name = "WeChatUser.findByEmployeeId", query = "SELECT o FROM WeChatUser o WHERE o.employeeId = :employeeId"),
    @NamedQuery(name = "WeChatUser.findByStatus", query = "SELECT o FROM WeChatUser o WHERE o.status = :status")})
public class WeChatUser extends SuperEntity {

    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 45)
    @Column(name = "openId")
    private String openId;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "nickname")
    private String nickname;
    @Size(max = 10)
    @Column(name = "sex")
    private String sex;
    @Size(max = 45)
    @Column(name = "born")
    private String born;
    @Size(max = 45)
    @Column(name = "mobile")
    private String mobile;
    @Size(max = 20)
    @Column(name = "language")
    private String language;
    @Size(max = 45)
    @Column(name = "city")
    private String city;
    @Size(max = 45)
    @Column(name = "province")
    private String province;
    @Size(max = 45)
    @Column(name = "country")
    private String country;
    @Column(name = "subscribe")
    private Boolean subscribe;
    @Column(name = "subscribeTime")
    private Integer subscribeTime;
    @Size(max = 45)
    @Column(name = "unionId")
    private String unionId;
    @Size(max = 45)
    @Column(name = "appId")
    private String appId;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "authorized")
    private Boolean authorized;
    @Size(max = 45)
    @Column(name = "employeeId")
    private String employeeId;
    @Size(max = 45)
    @Column(name = "employeeName")
    private String employeeName;
    @Size(max = 2)
    @Column(name = "role")
    private String role;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "profile")
    private String profile;

    public WeChatUser() {
        this.authorized = false;
        this.setStatusToNew();
    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the born
     */
    public String getBorn() {
        return born;
    }

    /**
     * @param born the born to set
     */
    public void setBorn(String born) {
        this.born = born;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the subscribe
     */
    public Boolean getSubscribe() {
        return subscribe;
    }

    /**
     * @param subscribe the subscribe to set
     */
    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    /**
     * @return the subscribeTime
     */
    public Integer getSubscribeTime() {
        return subscribeTime;
    }

    /**
     * @param subscribeTime the subscribeTime to set
     */
    public void setSubscribeTime(Integer subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    /**
     * @return the unionId
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * @param unionId the unionId to set
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the authorized
     */
    public Boolean getAuthorized() {
        return authorized;
    }

    /**
     * @param authorized the authorized to set
     */
    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeChatUser)) {
            return false;
        }
        WeChatUser other = (WeChatUser) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return Objects.equals(this.getOpenId(), other.getOpenId());
    }

    @Override
    public String toString() {
        return "cn.gxxx.wechat.entity.WeChatUser[ id=" + id + " ]";
    }

}
