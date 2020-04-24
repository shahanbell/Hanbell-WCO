/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "wechatauthority")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wechatauthority.findAll", query = "SELECT w FROM Wechatauthority w"),
    @NamedQuery(name = "Wechatauthority.findById", query = "SELECT w FROM Wechatauthority w WHERE w.id = :id"),
    @NamedQuery(name = "Wechatauthority.findByName", query = "SELECT w FROM Wechatauthority w WHERE w.name = :name"),
    @NamedQuery(name = "Wechatauthority.findByUrl", query = "SELECT w FROM Wechatauthority w WHERE w.url = :url"),
    @NamedQuery(name = "Wechatauthority.findByImgUrl", query = "SELECT w FROM Wechatauthority w WHERE w.imgUrl = :imgUrl"),
    @NamedQuery(name = "Wechatauthority.findByParentid", query = "SELECT w FROM Wechatauthority w WHERE w.parentid = :parentid"),
    @NamedQuery(name = "Wechatauthority.findByPageroute", query = "SELECT w FROM Wechatauthority w WHERE w.pageroute = :pageroute"),
    @NamedQuery(name = "Wechatauthority.findByCredate", query = "SELECT w FROM Wechatauthority w WHERE w.credate = :credate"),
    @NamedQuery(name = "Wechatauthority.findByOptuser", query = "SELECT w FROM Wechatauthority w WHERE w.optuser = :optuser"),
    @NamedQuery(name = "Wechatauthority.findByOptdate", query = "SELECT w FROM Wechatauthority w WHERE w.optdate = :optdate"),
    @NamedQuery(name = "Wechatauthority.findByCfmuser", query = "SELECT w FROM Wechatauthority w WHERE w.cfmuser = :cfmuser"),
    @NamedQuery(name = "Wechatauthority.findByCfmdate", query = "SELECT w FROM Wechatauthority w WHERE w.cfmdate = :cfmdate"),
    @NamedQuery(name = "Wechatauthority.findByCreator", query = "SELECT w FROM Wechatauthority w WHERE w.creator = :creator")})
public class Wechatauthority implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "url")
    private String url;
    @Size(max = 200)
    @Column(name = "imgUrl")
    private String imgUrl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "parentid")
    private int parentid;
    @Size(max = 200)
    @Column(name = "pageroute")
    private String pageroute;
    @Size(max = 20)
    @Column(name = "optuser")
    private String optuser;
    @Size(max = 20)
    @Column(name = "cfmuser")
    private String cfmuser;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "credate")
    @Temporal(TemporalType.DATE)
    private Date credate;
    @Column(name = "optdate")
    @Temporal(TemporalType.DATE)
    private Date optdate;
    @Column(name = "cfmdate")
    @Temporal(TemporalType.DATE)
    private Date cfmdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }


    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
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
        if (!(object instanceof Wechatauthority)) {
            return false;
        }
        Wechatauthority other = (Wechatauthority) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getPageroute() {
        return pageroute;
    }

    public void setPageroute(String pageroute) {
        this.pageroute = pageroute;
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public String getCfmuser() {
        return cfmuser;
    }

    public void setCfmuser(String cfmuser) {
        this.cfmuser = cfmuser;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
