/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
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
@Table(name = "operationstatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperationStatus.findAll", query = "SELECT o FROM OperationStatus o"),
    @NamedQuery(name = "OperationStatus.findById", query = "SELECT o FROM OperationStatus o WHERE o.id = :id"),
    @NamedQuery(name = "OperationStatus.findByAgentIdAndUserId", query = "SELECT o FROM OperationStatus o WHERE o.agentId = :agentId AND o.userId = :userId"),
    @NamedQuery(name = "OperationStatus.findByCheckCode", query = "SELECT o FROM OperationStatus o WHERE o.checkCode = :checkCode"),
    @NamedQuery(name = "OperationStatus.findByStatus", query = "SELECT o FROM OperationStatus o WHERE o.status = :status")})
public class OperationStatus extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "agentId")
    private int agentId;
    @Size(max = 45)
    @Column(name = "userId")
    private String userId;
    @Size(max = 45)
    @Column(name = "process")
    private String process;
    @Size(max = 45)
    @Column(name = "unit")
    private String unit;
    @Size(max = 200)
    @Column(name = "formid")
    private String formid;
    @Size(max = 45)
    @Column(name = "formtype")
    private String formtype;
    @Size(max = 100)
    @Column(name = "sn")
    private String sn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qty")
    private BigDecimal qty;
    @Size(max = 45)
    @Column(name = "checkCode")
    private String checkCode;

    public OperationStatus() {

    }

    /**
     * @return the agentId
     */
    public int getAgentId() {
        return agentId;
    }

    /**
     * @param agentId the agentId to set
     */
    public void setAgentId(int agentId) {
        this.agentId = agentId;
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
        if (!(object instanceof OperationStatus)) {
            return false;
        }
        OperationStatus other = (OperationStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.gxxx.wco.entity.OperationStatus[ id=" + id + " ]";
    }

    public OperationStatus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
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

    /**
     * @return the checkCode
     */
    public String getCheckCode() {
        return checkCode;
    }

    /**
     * @param checkCode the checkCode to set
     */
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

}
