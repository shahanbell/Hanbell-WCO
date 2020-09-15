/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "configproperties")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfigProperties.findAll", query = "SELECT c FROM ConfigProperties c"),
    @NamedQuery(name = "ConfigProperties.findById", query = "SELECT c FROM ConfigProperties c WHERE c.id = :id"),
    @NamedQuery(name = "ConfigProperties.findByConfigkey", query = "SELECT c FROM ConfigProperties c WHERE c.configkey = :configkey"),
    @NamedQuery(name = "ConfigProperties.findByConfigvalue", query = "SELECT c FROM ConfigProperties c WHERE c.configvalue = :configvalue"),
    @NamedQuery(name = "ConfigProperties.findByRemark", query = "SELECT c FROM ConfigProperties c WHERE c.remark = :remark")})
public class ConfigProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "configkey")
    private String configkey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "configvalue")
    private String configvalue;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public ConfigProperties() {
    }

    public ConfigProperties(Integer id) {
        this.id = id;
    }

    public ConfigProperties(Integer id, String configkey, String configvalue) {
        this.id = id;
        this.configkey = configkey;
        this.configvalue = configvalue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigkey() {
        return configkey;
    }

    public void setConfigkey(String configkey) {
        this.configkey = configkey;
    }

    public String getConfigvalue() {
        return configvalue;
    }

    public void setConfigvalue(String configvalue) {
        this.configvalue = configvalue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof ConfigProperties)) {
            return false;
        }
        ConfigProperties other = (ConfigProperties) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.wco.entity.ConfigProperties[ id=" + id + " ]";
    }
    
}
