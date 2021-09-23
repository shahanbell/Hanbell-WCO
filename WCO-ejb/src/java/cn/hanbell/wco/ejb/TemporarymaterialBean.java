/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.Temporarymaterial;
import cn.hanbell.wco.entity.WeChatUser;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
public class TemporarymaterialBean extends SuperBean<Temporarymaterial> {

    public TemporarymaterialBean() {
        super(Temporarymaterial.class);
    }

    public Temporarymaterial findByUrl(String url) {
        Query query = getEntityManager().createNamedQuery("Temporarymaterial.findByUrl");
        query.setParameter("url", url);
        try {
            Object o = query.getSingleResult();
            return (Temporarymaterial) o;
        } catch (Exception ex) {
            return null;
        }
    }
}
