/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.SuperEntity;
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
@Table(name = "wechatsession")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeChatSession.findAll", query = "SELECT w FROM WeChatSession w"),
    @NamedQuery(name = "WeChatSession.findById", query = "SELECT w FROM WeChatSession w WHERE w.id = :id"),
    @NamedQuery(name = "WeChatSession.findByOpenId", query = "SELECT w FROM WeChatSession w WHERE w.openId = :openId"),
    @NamedQuery(name = "WeChatSession.findByOpenIdAndSessionId", query = "SELECT w FROM WeChatSession w WHERE w.openId = :openId AND w.sessionId = :sessionId"),
    @NamedQuery(name = "WeChatSession.findByCheckCode", query = "SELECT w FROM WeChatSession w WHERE w.openId = :openId AND w.sessionId = :sessionId AND w.checkCode = :checkCode AND w.status='N' ")})
public class WeChatSession extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "openId")
    private String openId;
    @Size(max = 45)
    @Column(name = "sessionKey")
    private String sessionKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "sessionId")
    private String sessionId;
    @Size(max = 45)
    @Column(name = "unionId")
    private String unionId;
    @Size(max = 10)
    @Column(name = "checkCode")
    private String checkCode;
    @Column(name = "expiresIn")
    private Integer expiresIn;

    public WeChatSession() {
        this.status = "N";
    }

    public WeChatSession(String openId, String sessionKey, String sessionId) {
        this.openId = openId;
        this.sessionKey = sessionKey;
        this.sessionId = sessionId;
        this.status = "N";
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
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
        if (!(object instanceof WeChatSession)) {
            return false;
        }
        WeChatSession other = (WeChatSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.gxxx.wco.entity.WeChatSession[ id=" + id + " ]";
    }

}
