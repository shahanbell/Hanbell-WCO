/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WeChatUser;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
public class WeChatUserBean extends SuperBean<WeChatUser> {

    public WeChatUserBean() {
        super(WeChatUser.class);
    }

    public WeChatUser findByEmployeeId(String employeeId) {
        Query query = getEntityManager().createNamedQuery("WeChatUser.findByEmployeeId");
        query.setParameter("employeeId", employeeId);
        try {
            Object o = query.getSingleResult();
            return (WeChatUser) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public WeChatUser findByOpenId(String openid) {
        Query query = getEntityManager().createNamedQuery("WeChatUser.findByOpenId");
        query.setParameter("openid", openid);
        try {
            Object o = query.getSingleResult();
            return (WeChatUser) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean has(String openid) {
        WeChatUser o = findByOpenId(openid);
        if (o != null) {
            return true;
        } else {
            return false;
        }
    }

}
