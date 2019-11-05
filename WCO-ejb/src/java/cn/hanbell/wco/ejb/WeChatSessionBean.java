/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WeChatSession;
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

    public WeChatSession findByCheckCode(String openId, String sessionId, String checkCode) {
        Query query = getEntityManager().createNamedQuery("WeChatSession.findByCheckCode");
        query.setParameter("openId", openId);
        query.setParameter("sessionId", sessionId);
        query.setParameter("checkCode", checkCode);
        try {
            Object o = query.getSingleResult();
            return (WeChatSession) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public WeChatSession findByOpenIdAndSessionId(String openId, String sessionId) {
        Query query = getEntityManager().createNamedQuery("WeChatSession.findByOpenIdAndSessionId");
        query.setParameter("openId", openId);
        query.setParameter("sessionId", sessionId);
        try {
            Object o = query.getSingleResult();
            return (WeChatSession) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean has(String openId, String sessionId) {
        WeChatSession s = findByOpenIdAndSessionId(openId, sessionId);
        if (s == null) {
            return false;
        } else {
            return true;
        }
    }

    public void persistIfNotExist(String openId, String sessionKey, String sessionId) {
        if (!has(openId, sessionId)) {
            WeChatSession wcs = new WeChatSession(openId, sessionKey, sessionId);
            wcs.setExpiresIn(-1);
            wcs.setStatus("N");
            wcs.setCreatorToSystem();
            wcs.setCredateToNow();
            persist(wcs);
        }
    }

}
