/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WeChatUserCard;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
public class WeChatUserCardBean extends SuperBean<WeChatUserCard> {

    public WeChatUserCardBean() {
        super(WeChatUserCard.class);
    }

    public int getCountByOpenId(String openid) {
        Query query = getEntityManager().createNamedQuery("WeChatUserCard.getRowCountByOpenId");
        query.setParameter("openid", openid);
        try {
            return Integer.valueOf(query.getSingleResult().toString());
        } catch (Exception ex) {
            logger.error(ex);
        }
        return -1;
    }

}
