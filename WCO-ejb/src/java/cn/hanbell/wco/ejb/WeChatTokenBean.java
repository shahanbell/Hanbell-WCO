/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WeChatToken;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
public class WeChatTokenBean extends SuperBean<WeChatToken> {

    public WeChatTokenBean() {
        super(WeChatToken.class);
    }

    public List<WeChatToken> findByAppId(String appId) {
        Query query = getEntityManager().createNamedQuery("WeChatToken.findByAppId");
        query.setParameter("appId", appId);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
