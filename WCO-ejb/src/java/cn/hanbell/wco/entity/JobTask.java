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
@Table(name = "jobtask")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobTask.findAll", query = "SELECT j FROM JobTask j"),
    @NamedQuery(name = "JobTask.findById", query = "SELECT j FROM JobTask j WHERE j.id = :id"),
    @NamedQuery(name = "JobTask.findByLeaderId", query = "SELECT j FROM JobTask j WHERE j.leaderId = :leaderId"),
    @NamedQuery(name = "JobTask.findByLeaderIdAndStatus", query = "SELECT j FROM JobTask j WHERE j.leaderId =:leaderId AND j.status = :status")})
public class JobTask extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "leaderId")
    private String leaderId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "leader")
    private String leader;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "plannedStartDate")
    private String plannedStartDate;
    @Size(max = 5)
    @Column(name = "plannedStartTime")
    private String plannedStartTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "plannedFinishDate")
    private String plannedFinishDate;
    @Size(max = 5)
    @Column(name = "plannedFinishTime")
    private String plannedFinishTime;
    @Size(max = 10)
    @Column(name = "actualStartDate")
    private String actualStartDate;
    @Size(max = 5)
    @Column(name = "actualStartTime")
    private String actualStartTime;
    @Size(max = 10)
    @Column(name = "actualFinishDate")
    private String actualFinishDate;
    @Size(max = 5)
    @Column(name = "actualFinishTime")
    private String actualFinishTime;
    @Size(max = 2)
    @Column(name = "priority")
    private String priority;
    @Size(max = 2)
    @Column(name = "level")
    private String level;
    @Column(name = "sortid")
    private Integer sortid;
    @Size(max = 300)
    @Column(name = "location")
    private String location;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    public JobTask() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(String plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public String getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(String plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public String getPlannedFinishDate() {
        return plannedFinishDate;
    }

    public void setPlannedFinishDate(String plannedFinishDate) {
        this.plannedFinishDate = plannedFinishDate;
    }

    public String getPlannedFinishTime() {
        return plannedFinishTime;
    }

    public void setPlannedFinishTime(String plannedFinishTime) {
        this.plannedFinishTime = plannedFinishTime;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualFinishDate() {
        return actualFinishDate;
    }

    public void setActualFinishDate(String actualFinishDate) {
        this.actualFinishDate = actualFinishDate;
    }

    public String getActualFinishTime() {
        return actualFinishTime;
    }

    public void setActualFinishTime(String actualFinishTime) {
        this.actualFinishTime = actualFinishTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getSortid() {
        return sortid;
    }

    public void setSortid(Integer sortid) {
        this.sortid = sortid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
        if (!(object instanceof JobTask)) {
            return false;
        }
        JobTask other = (JobTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.gxxx.wco.entity.JobTask[ id=" + id + " ]";
    }

}
