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
 * @author C2082
 */
@Entity
@Table(name = "birthdayuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BirthdayUser.findAll", query = "SELECT b FROM BirthdayUser b"),
    @NamedQuery(name = "BirthdayUser.findByFormid", query = "SELECT b FROM BirthdayUser b WHERE b.formid = :formid"),
    @NamedQuery(name = "BirthdayUser.findByFormdate", query = "SELECT b FROM BirthdayUser b WHERE b.formdate = :formdate"),
    @NamedQuery(name = "BirthdayUser.findByUserid", query = "SELECT b FROM BirthdayUser b WHERE b.userid = :userid"),
    @NamedQuery(name = "BirthdayUser.findByUsername", query = "SELECT b FROM BirthdayUser b WHERE b.username = :username"),
    @NamedQuery(name = "BirthdayUser.findByYear", query = "SELECT b FROM BirthdayUser b WHERE b.year = :year"),
    @NamedQuery(name = "BirthdayUser.findByJdcard", query = "SELECT b FROM BirthdayUser b WHERE b.jdcard = :jdcard"),
    @NamedQuery(name = "BirthdayUser.findByJdpassword", query = "SELECT b FROM BirthdayUser b WHERE b.jdpassword = :jdpassword"),
    @NamedQuery(name = "BirthdayUser.findByUseridAndYear", query = "SELECT b FROM BirthdayUser b WHERE b.userid = :userid and b.year=:year"),
    @NamedQuery(name = "BirthdayUser.findByStatus", query = "SELECT b FROM BirthdayUser b WHERE b.status = :status")})
public class BirthdayUser extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "formid")
    private String formid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "formdate")
    @Temporal(TemporalType.DATE)
    private Date formdate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "userid")
    private String userid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptname")
    private String deptname;
    @Column(name = "year")
    private Integer year;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "jdcard")
    private String jdcard;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "jdpassword")
    private String jdpassword;
    @Basic(optional = false)
    @Size(min = 1, max = 500)
    @Column(name = "msgid")
    private String msgid;

    public BirthdayUser() {
    }

    public BirthdayUser(Integer id) {
        this.id = id;
    }

    public BirthdayUser(Integer id, String formid, Date formdate, String userid, String username, String jdcard, String jdpassword, String status) {
        this.id = id;
        this.formid = formid;
        this.formdate = formdate;
        this.userid = userid;
        this.username = username;
        this.jdcard = jdcard;
        this.jdpassword = jdpassword;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public Date getFormdate() {
        return formdate;
    }

    public void setFormdate(Date formdate) {
        this.formdate = formdate;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
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

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
    
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getJdcard() {
        return jdcard;
    }

    public void setJdcard(String jdcard) {
        this.jdcard = jdcard;
    }

    public String getJdpassword() {
        return jdpassword;
    }

    public void setJdpassword(String jdpassword) {
        this.jdpassword = jdpassword;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
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
        if (!(object instanceof BirthdayUser)) {
            return false;
        }
        BirthdayUser other = (BirthdayUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.wco.entity.BirthdayUser[ id=" + id + " ]";
    }

}
