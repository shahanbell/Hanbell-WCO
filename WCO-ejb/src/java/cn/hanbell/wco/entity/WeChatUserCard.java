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
@Table(name = "wechatusercard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeChatUserCard.getRowCountByOpenId", query = "SELECT COUNT(w) FROM WeChatUserCard w WHERE w.openid = :openid"),
    @NamedQuery(name = "WeChatUserCard.findAll", query = "SELECT w FROM WeChatUserCard w"),
    @NamedQuery(name = "WeChatUserCard.findById", query = "SELECT w FROM WeChatUserCard w WHERE w.id = :id"),
    @NamedQuery(name = "WeChatUserCard.findByOpenid", query = "SELECT w FROM WeChatUserCard w WHERE w.openid = :openid"),
    @NamedQuery(name = "WeChatUserCard.findByCardid", query = "SELECT w FROM WeChatUserCard w WHERE w.cardid = :cardid"),
    @NamedQuery(name = "WeChatUserCard.findByCardcode", query = "SELECT w FROM WeChatUserCard w WHERE w.cardcode = :cardcode"),
    @NamedQuery(name = "WeChatUserCard.findByStatus", query = "SELECT w FROM WeChatUserCard w WHERE w.status = :status")})
public class WeChatUserCard extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "openid")
    private String openid;
    @Size(max = 45)
    @Column(name = "cardid")
    private String cardid;
    @Size(max = 45)
    @Column(name = "cardcode")
    private String cardcode;

    public WeChatUserCard() {
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
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
        if (!(object instanceof WeChatUserCard)) {
            return false;
        }
        WeChatUserCard other = (WeChatUserCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.gxxx.wechat.entity.WeChatCard[ id=" + id + " ]";
    }

}
