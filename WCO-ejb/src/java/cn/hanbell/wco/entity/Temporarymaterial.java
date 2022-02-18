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
 * @author C2082
 */
@Entity
@Table(name = "temporarymaterial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Temporarymaterial.findAll", query = "SELECT t FROM Temporarymaterial t"),
    @NamedQuery(name = "Temporarymaterial.findById", query = "SELECT t FROM Temporarymaterial t WHERE t.id = :id"),
    @NamedQuery(name = "Temporarymaterial.findByMediaId", query = "SELECT t FROM Temporarymaterial t WHERE t.mediaId = :mediaId"),
    @NamedQuery(name = "Temporarymaterial.findByUrl", query = "SELECT t FROM Temporarymaterial t WHERE t.url = :url"),
    @NamedQuery(name = "Temporarymaterial.findByRemark", query = "SELECT t FROM Temporarymaterial t WHERE t.remark = :remark"),
    @NamedQuery(name = "Temporarymaterial.findByStatus", query = "SELECT t FROM Temporarymaterial t WHERE t.status = :status"),
    @NamedQuery(name = "Temporarymaterial.findByCreator", query = "SELECT t FROM Temporarymaterial t WHERE t.creator = :creator"),
    @NamedQuery(name = "Temporarymaterial.findByCredate", query = "SELECT t FROM Temporarymaterial t WHERE t.credate = :credate"),
    @NamedQuery(name = "Temporarymaterial.findByOptuser", query = "SELECT t FROM Temporarymaterial t WHERE t.optuser = :optuser"),
    @NamedQuery(name = "Temporarymaterial.findByOptdate", query = "SELECT t FROM Temporarymaterial t WHERE t.optdate = :optdate"),
    @NamedQuery(name = "Temporarymaterial.findByCfmuser", query = "SELECT t FROM Temporarymaterial t WHERE t.cfmuser = :cfmuser"),
    @NamedQuery(name = "Temporarymaterial.findByCfmdate", query = "SELECT t FROM Temporarymaterial t WHERE t.cfmdate = :cfmdate")})
public class Temporarymaterial  extends SuperEntity {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "media_id")
    private String mediaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "url")
    private String url;
    @Size(max = 400)
    @Column(name = "remark")
    private String remark;

    public Temporarymaterial() {
    }

    public Temporarymaterial(Integer id) {
        this.id = id;
    }

    public Temporarymaterial(Integer id, String mediaId, String url) {
        this.id = id;
        this.mediaId = mediaId;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof Temporarymaterial)) {
            return false;
        }
        Temporarymaterial other = (Temporarymaterial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.wco.entity.Temporarymaterial[ id=" + id + " ]";
    }
    
}
