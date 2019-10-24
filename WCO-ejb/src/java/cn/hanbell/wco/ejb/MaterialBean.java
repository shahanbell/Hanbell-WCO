/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.Material;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

/**
 *
 * @author kevindong
 */
@Stateless
@Named
public class MaterialBean extends SuperBean<Material> {

    public MaterialBean() {
        super(Material.class);
    }

    public Material findByMediaId(String mediaId) {
        Query query = this.getEntityManager().createNamedQuery("Material.findByMediaId");
        query.setParameter("mediaId", mediaId);
        try {
            Object o = query.getSingleResult();
            return (Material) o;
        } catch (Exception ex) {
            return null;
        }
    }

}
