/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.SuperEntity;
import java.util.Date;
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
@Table(name = "wechattoken")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeChatToken.findAll", query = "SELECT w FROM WeChatToken w"),
    @NamedQuery(name = "WeChatToken.findById", query = "SELECT w FROM WeChatToken w WHERE w.id = :id"),
    @NamedQuery(name = "WeChatToken.findByApp", query = "SELECT w FROM WeChatToken w WHERE w.app = :app"),
    @NamedQuery(name = "WeChatToken.findByAppId", query = "SELECT w FROM WeChatToken w WHERE w.appId = :appId"),
    @NamedQuery(name = "WeChatToken.findByAppSecret", query = "SELECT w FROM WeChatToken w WHERE w.appSecret = :appSecret"),
    @NamedQuery(name = "WeChatToken.findByStatus", query = "SELECT w FROM WeChatToken w WHERE w.status = :status")})
public class WeChatToken extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "app")
    private String app;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "appId")
    private String appId;
    @Size(max = 45)
    @Column(name = "appSecret")
    private String appSecret;
    @Size(max = 45)
    @Column(name = "appASEKey")
    private String appASEKey;
    @Size(max = 45)
    @Column(name = "appToken")
    private String appToken;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public WeChatToken() {
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * @return the appASEKey
     */
    public String getAppASEKey() {
        return appASEKey;
    }

    /**
     * @param appASEKey the appASEKey to set
     */
    public void setAppASEKey(String appASEKey) {
        this.appASEKey = appASEKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
    }

    public String getCfmuser() {
        return cfmuser;
    }

    public void setCfmuser(String cfmuser) {
        this.cfmuser = cfmuser;
    }

    public Date getCfmdate() {
        return cfmdate;
    }

    public void setCfmdate(Date cfmdate) {
        this.cfmdate = cfmdate;
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
        if (!(object instanceof WeChatToken)) {
            return false;
        }
        WeChatToken other = (WeChatToken) object;
        if (!this.app.equals(other.app) && !this.appId.equals(other.appId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.gxxx.wco.entity.WeChatToken[ id=" + id + " ]";
    }

}
