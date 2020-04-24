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
@Table(name = "wechatrole")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wechatrole.findAll", query = "SELECT w FROM Wechatrole w"),
    @NamedQuery(name = "Wechatrole.findById", query = "SELECT w FROM Wechatrole w WHERE w.id = :id"),
    @NamedQuery(name = "Wechatrole.findByRoleno", query = "SELECT w FROM Wechatrole w WHERE w.roleno = :roleno"),
    @NamedQuery(name = "Wechatrole.findByRolename", query = "SELECT w FROM Wechatrole w WHERE w.rolename = :rolename"),
    @NamedQuery(name = "Wechatrole.findByStatus", query = "SELECT w FROM Wechatrole w WHERE w.status = :status"),
    @NamedQuery(name = "Wechatrole.findByCfmuser", query = "SELECT w FROM Wechatrole w WHERE w.cfmuser = :cfmuser"),
    @NamedQuery(name = "Wechatrole.findByCredate", query = "SELECT w FROM Wechatrole w WHERE w.credate = :credate"),
    @NamedQuery(name = "Wechatrole.findByOptuser", query = "SELECT w FROM Wechatrole w WHERE w.optuser = :optuser"),
    @NamedQuery(name = "Wechatrole.findByCfmdate", query = "SELECT w FROM Wechatrole w WHERE w.cfmdate = :cfmdate"),
    @NamedQuery(name = "Wechatrole.findByCreator", query = "SELECT w FROM Wechatrole w WHERE w.creator = :creator"),
    @NamedQuery(name = "Wechatrole.findByOptdate", query = "SELECT w FROM Wechatrole w WHERE w.optdate = :optdate")})
public class Wechatrole implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "roleno")
    private String roleno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "rolename")
    private String rolename;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 20)
    @Column(name = "cfmuser")
    private String cfmuser;
    @Size(max = 20)
    @Column(name = "optuser")
    private String optuser;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date credate;
    @Column(name = "cfmdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cfmdate;
    @Column(name = "optdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optdate;

    public Wechatrole() {
    }

    public Wechatrole(Integer id) {
        this.id = id;
    }

    public Wechatrole(Integer id, String roleno, String rolename, String status) {
        this.id = id;
        this.roleno = roleno;
        this.rolename = rolename;
        this.status = status;
    }

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

    public Date getCfmdate() {
        return cfmdate;
    }

    public void setCfmdate(Date cfmdate) {
        this.cfmdate = cfmdate;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
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
        if (!(object instanceof Wechatrole)) {
            return false;
        }
        Wechatrole other = (Wechatrole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getRoleno() {
        return roleno;
    }

    public void setRoleno(String roleno) {
        this.roleno = roleno;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCfmuser() {
        return cfmuser;
    }

    public void setCfmuser(String cfmuser) {
        this.cfmuser = cfmuser;
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
