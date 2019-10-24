/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WeChatSession;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
public class WeChatSessionBean extends SuperBean<WeChatSession> {

    public WeChatSessionBean() {
        super(WeChatSession.class);
    }

    public WeChatSession findByCheckCode(String openId, String sessionKey, String checkCode) {
        Query query = getEntityManager().createNamedQuery("WeChatSession.findByCheckCode");
        query.setParameter("openId", openId);
        query.setParameter("sessionKey", sessionKey);
        query.setParameter("checkCode", checkCode);
        try {
            Object o = query.getSingleResult();
            return (WeChatSession) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<WeChatSession> findByOpenIdAndSessionKey(String openId, String sessionKey) {
        Query query = getEntityManager().createNamedQuery("WeChatSession.findByOpenIdAndSessionKey");
        query.setParameter("openId", openId);
        query.setParameter("sessionKey", sessionKey);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean has(String openId, String sessionKey) {
        List<WeChatSession> list = findByOpenIdAndSessionKey(openId, sessionKey);
        if (list == null || list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void persistIfNotExist(String openId, String sessionKey) {
        if (!has(openId, sessionKey)) {
            WeChatSession wcs = new WeChatSession(openId, sessionKey);
            wcs.setExpiresIn(-1);
            wcs.setStatus("V");
            wcs.setCredateToNow();
            wcs.setCreatorToSystem();
            persist(wcs);
        }
    }

}
