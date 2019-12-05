/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.SuperEntity;
import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author C1879
 */
@Entity
@Table(name = "dineinfo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DineInfo.findAll", query = "SELECT d FROM DineInfo d")
    , @NamedQuery(name = "DineInfo.findById", query = "SELECT d FROM DineInfo d WHERE d.id = :id")
    , @NamedQuery(name = "DineInfo.updateForStatusByDinedate", query = "SELECT d FROM DineInfo d WHERE  d.dinedate = :dinedate ")
    , @NamedQuery(name = "DineInfo.notExist", query = "SELECT d FROM DineInfo d WHERE d.userid = :userid AND d.dinedate = :dinedate AND d.status <> 'W' ")
    , @NamedQuery(name = "DineInfo.getListByUseridAndDinedate", query = "SELECT d FROM DineInfo d WHERE d.userid = :userid and d.status <> 'W' and  d.dinedate BETWEEN :begin AND :end ORDER BY d.dinedate ASC ")
    , @NamedQuery(name = "DineInfo.getDineinfoList", query = "SELECT COUNT(d) FROM DineInfo d WHERE d.address = :address and d.dinedate=:dinedate and d.status='V' ")})
public class DineInfo extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "userid")
    private String userid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "username")
    private String username;
    @Size(max = 8)
    @Column(name = "deptid")
    private String deptid;
    @Size(max = 20)
    @Column(name = "deptname")
    private String deptname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lunch")
    private boolean lunch;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dinner")
    private boolean dinner;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dinedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dinedate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "address")
    private String address;

    public DineInfo() {
    }

    public DineInfo(Integer id) {
        this.id = id;
    }

    public DineInfo(Integer id, String userid, String username, boolean lunch, boolean dinner, Date dinedate, String address, String status) {
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.lunch = lunch;
        this.dinner = dinner;
        this.dinedate = dinedate;
        this.address = address;
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public boolean getLunch() {
        return lunch;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public boolean getDinner() {
        return dinner;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }

    public Date getDinedate() {
        return dinedate;
    }

    public void setDinedate(Date dinedate) {
        this.dinedate = dinedate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        if (!(object instanceof DineInfo)) {
            return false;
        }
        DineInfo other = (DineInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.wco.entity.DineInfo[ id=" + id + " ]";
    }

}
