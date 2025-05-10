/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "wechatmessage")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WechatMessage.findAll", query = "SELECT w FROM WechatMessage w"),
    @NamedQuery(name = "WechatMessage.findById", query = "SELECT w FROM WechatMessage w WHERE w.id = :id"),
    @NamedQuery(name = "WechatMessage.findByUserid", query = "SELECT w FROM WechatMessage w WHERE w.userid = :userid"),
    @NamedQuery(name = "WechatMessage.findByStatus", query = "SELECT w FROM WechatMessage w WHERE w.status = :status"),
    @NamedQuery(name = "WechatMessage.findByCreator", query = "SELECT w FROM WechatMessage w WHERE w.creator = :creator"),
    @NamedQuery(name = "WechatMessage.findByCredate", query = "SELECT w FROM WechatMessage w WHERE w.credate = :credate"),
    @NamedQuery(name = "WechatMessage.findByOptuser", query = "SELECT w FROM WechatMessage w WHERE w.optuser = :optuser"),
    @NamedQuery(name = "WechatMessage.findByOptdate", query = "SELECT w FROM WechatMessage w WHERE w.optdate = :optdate"),
    @NamedQuery(name = "WechatMessage.findByCfmuser", query = "SELECT w FROM WechatMessage w WHERE w.cfmuser = :cfmuser"),
    @NamedQuery(name = "WechatMessage.findByCfmdate", query = "SELECT w FROM WechatMessage w WHERE w.cfmdate = :cfmdate")})
public class WechatMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "userid")
    private String userid;
    @Lob
    @Size(max = 65535)
    @Column(name = "requestcontent")
    private String requestcontent;
    @Lob
    @Size(max = 65535)
    @Column(name = "responecontent")
    private String responecontent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    @Column(name = "credate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credate;
    @Size(max = 20)
    @Column(name = "optuser")
    private String optuser;
    @Column(name = "optdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optdate;
    @Size(max = 20)
    @Column(name = "cfmuser")
    private String cfmuser;
    @Column(name = "cfmdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cfmdate;

    public WechatMessage() {
    }

    public WechatMessage(String userid, String requestcontent, String creator) {
        this.userid = userid;
        this.requestcontent = requestcontent;
        this.creator = creator;
        this.status = "N";
        this.creator = creator;
        this.credate = BaseLib.getDate();
    }

    
    public WechatMessage(Integer id) {
        this.id = id;
    }

    public WechatMessage(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequestcontent() {
        return requestcontent;
    }

    public void setRequestcontent(String requestcontent) {
        this.requestcontent = requestcontent;
    }

    public String getResponecontent() {
        return responecontent;
    }

    public void setResponecontent(String responecontent) {
        this.responecontent = responecontent;
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
        if (!(object instanceof WechatMessage)) {
            return false;
        }
        WechatMessage other = (WechatMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.wco.entity.WechatMessage[ id=" + id + " ]";
    }
    
}
