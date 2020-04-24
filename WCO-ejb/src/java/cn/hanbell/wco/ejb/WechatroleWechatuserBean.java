/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WeChatUser;
import cn.hanbell.wco.entity.WechatroleWechatuser;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class WechatroleWechatuserBean extends SuperBean<WechatroleWechatuser> {
    
    
    public WechatroleWechatuserBean() {
        super(WechatroleWechatuser.class);
    }
    
  public List<WechatroleWechatuser> findAllByUserid(String userid){
        Query query = getEntityManager().createNamedQuery("WechatroleWechatuser.findByWechatuserid");
        query.setParameter("wechatuserid", userid);
        try {
            Object o = query.getResultList();
            return (List<WechatroleWechatuser>) o;
        } catch (Exception ex) {
            return null;
        }
    }
}
