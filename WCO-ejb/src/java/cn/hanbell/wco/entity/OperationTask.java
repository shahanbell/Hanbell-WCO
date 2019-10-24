/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.SuperEntity;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author C0160
 */
@Entity
@Table(name = "operationtask")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperationTask.findAll", query = "SELECT o FROM OperationTask o"),
    @NamedQuery(name = "OperationTask.findById", query = "SELECT o FROM OperationTask o WHERE o.id = :id"),
    @NamedQuery(name = "OperationTask.findByAgentId", query = "SELECT o FROM OperationTask o WHERE o.agentId = :agentId"),
    @NamedQuery(name = "OperationTask.findByUserId", query = "SELECT o FROM OperationTask o WHERE o.userId = :userId"),
    @NamedQuery(name = "OperationTask.findByTaskId", query = "SELECT o FROM OperationTask o WHERE o.taskId = :taskId"),
    @NamedQuery(name = "OperationTask.findByStatus", query = "SELECT o FROM OperationTask o WHERE o.status = :status")})
public class OperationTask extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "agentId")
    private int agentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "userId")
    private String userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "taskId")
    private String taskId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "createTime")
    private long createTime;
    @Size(max = 45)
    @Column(name = "process")
    private String process;
    @Size(max = 45)
    @Column(name = "unit")
    private String unit;
    @Size(max = 200)
    @Column(name = "formid")
    private String formid;
    @Size(max = 100)
    @Column(name = "sn")
    private String sn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qty")
    private BigDecimal qty;

    public OperationTask() {
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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
        if (!(object instanceof OperationTask)) {
            return false;
        }
        OperationTask other = (OperationTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.gxxx.wco.entity.OperationTask[ id=" + id + " ]";
    }

}
