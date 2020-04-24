 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "wechatrole_wechatuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WechatroleWechatuser.findAll", query = "SELECT w FROM WechatroleWechatuser w"),
    @NamedQuery(name = "WechatroleWechatuser.findById", query = "SELECT w FROM WechatroleWechatuser w WHERE w.id = :id"),
    @NamedQuery(name = "WechatroleWechatuser.findByWechatuserid", query = "SELECT w FROM WechatroleWechatuser w WHERE w.wechatuserid = :wechatuserid"),
    @NamedQuery(name = "WechatroleWechatuser.findByCreator", query = "SELECT w FROM WechatroleWechatuser w WHERE w.creator = :creator"),
    @NamedQuery(name = "WechatroleWechatuser.findByCredate", query = "SELECT w FROM WechatroleWechatuser w WHERE w.credate = :credate"),
    @NamedQuery(name = "WechatroleWechatuser.findByOptuser", query = "SELECT w FROM WechatroleWechatuser w WHERE w.optuser = :optuser"),
    @NamedQuery(name = "WechatroleWechatuser.findByOptdate", query = "SELECT w FROM WechatroleWechatuser w WHERE w.optdate = :optdate"),
    @NamedQuery(name = "WechatroleWechatuser.findByCfmuser", query = "SELECT w FROM WechatroleWechatuser w WHERE w.cfmuser = :cfmuser"),
    @NamedQuery(name = "WechatroleWechatuser.findByCfmdate", query = "SELECT w FROM WechatroleWechatuser w WHERE w.cfmdate = :cfmdate"),
    @NamedQuery(name = "WechatroleWechatuser.findBySeq", query = "SELECT w FROM WechatroleWechatuser w WHERE w.seq = :seq")})
public class WechatroleWechatuser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "wechatuserid")
    private String wechatuserid;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    @Basic(optional = false)
    @NotNull
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
    @Column(name = "seq")
    private Integer seq;

    @JoinColumn(name = "wechatroleid", referencedColumnName = "id")
    @ManyToOne
    private Wechatrole wechatrole;
        
    public WechatroleWechatuser() {
    }

    public WechatroleWechatuser(Integer id) {
        this.id = id;
    }

    public WechatroleWechatuser(Integer id, Date credate) {
        this.id = id;
        this.credate = credate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWechatuserid() {
        return wechatuserid;
    }

    public void setWechatuserid(String wechatuserid) {
        this.wechatuserid = wechatuserid;
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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Wechatrole getWechatrole() {
        return wechatrole;
    }

    public void setWechatrole(Wechatrole wechatrole) {
        this.wechatrole = wechatrole;
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
        if (!(object instanceof WechatroleWechatuser)) {
            return false;
        }
        WechatroleWechatuser other = (WechatroleWechatuser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
