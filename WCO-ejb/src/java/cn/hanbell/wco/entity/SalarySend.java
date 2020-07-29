/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "salarysend")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SalarySend.findAll", query = "SELECT s FROM SalarySend s"),
     @NamedQuery(name = "SalarySend.findByTaskidAndDeptno", query = "SELECT s FROM SalarySend s WHERE s.salarySendPK.taskid = :taskid AND s.deptno = :deptno"),
      @NamedQuery(name = "SalarySend.findByTaskidAndEmployeeid", query = "SELECT s FROM SalarySend s WHERE s.salarySendPK.taskid =:taskid AND s.salarySendPK.employeeid=:employeeid"),
    @NamedQuery(name = "SalarySend.findByTaskid", query = "SELECT s FROM SalarySend s WHERE s.salarySendPK.taskid = :taskid"),
    @NamedQuery(name = "SalarySend.findByTaskname", query = "SELECT s FROM SalarySend s WHERE s.taskname = :taskname"),
    @NamedQuery(name = "SalarySend.findBySendtime", query = "SELECT s FROM SalarySend s WHERE s.sendtime = :sendtime"),
    @NamedQuery(name = "SalarySend.findByEmployeeid", query = "SELECT s FROM SalarySend s WHERE s.salarySendPK.employeeid = :employeeid"),
    @NamedQuery(name = "SalarySend.findByEmployeename", query = "SELECT s FROM SalarySend s WHERE s.employeename = :employeename"),
    @NamedQuery(name = "SalarySend.findByDeptno", query = "SELECT s FROM SalarySend s WHERE s.deptno = :deptno"),
    @NamedQuery(name = "SalarySend.findByDept", query = "SELECT s FROM SalarySend s WHERE s.dept = :dept"),
    @NamedQuery(name = "SalarySend.findByStatus", query = "SELECT s FROM SalarySend s WHERE s.status = :status"),
    @NamedQuery(name = "SalarySend.findByConfirmtime", query = "SELECT s FROM SalarySend s WHERE s.confirmtime = :confirmtime")})
public class SalarySend implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SalarySendPK salarySendPK;
    @Size(max = 20)
    @Column(name = "taskname")
    private String taskname;
    @Column(name = "sendtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendtime;
    @Size(max = 8)
    @Column(name = "employeename")
    private String employeename;
    @Size(max = 10)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 20)
    @Column(name = "dept")
    private String dept;
    @Size(max = 1)
    @Column(name = "status")
    private String status;
    @Column(name = "confirmtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmtime;

    public SalarySend() {
    }

    public SalarySend(SalarySendPK salarySendPK) {
        this.salarySendPK = salarySendPK;
    }

    public SalarySend(String taskid, String employeeid) {
        this.salarySendPK = new SalarySendPK(taskid, employeeid);
    }

    public SalarySendPK getSalarySendPK() {
        return salarySendPK;
    }

    public void setSalarySendPK(SalarySendPK salarySendPK) {
        this.salarySendPK = salarySendPK;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(Date confirmtime) {
        this.confirmtime = confirmtime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (salarySendPK != null ? salarySendPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalarySend)) {
            return false;
        }
        SalarySend other = (SalarySend) object;
        if ((this.salarySendPK == null && other.salarySendPK != null) || (this.salarySendPK != null && !this.salarySendPK.equals(other.salarySendPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.wco.entity.SalarySend[ salarySendPK=" + salarySendPK + " ]";
    }
    
}
