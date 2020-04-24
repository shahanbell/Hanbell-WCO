/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.Wechatauthority;
import cn.hanbell.wco.entity.Wechatrole;
import cn.hanbell.wco.entity.WechatroleWechatauthority;
import cn.hanbell.wco.entity.WechatroleWechatuser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sun.font.EAttribute;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class WechatroleWechatauthorityBean extends SuperBean<WechatroleWechatauthority> {

    public WechatroleWechatauthorityBean() {
        super(WechatroleWechatauthority.class);
    }

    public List<Wechatauthority> findAllAuth(Integer id) {
        Query query = getEntityManager().createNamedQuery("WechatroleWechatauthority.findByWechatroleid");
        query.setParameter("wechatroleid", id);
        try {
            Object o = query.getResultList();
            List<WechatroleWechatauthority> ra=(List<WechatroleWechatauthority>) o;
            List<Wechatauthority> was=new ArrayList<Wechatauthority>();
            for(WechatroleWechatauthority w:ra){
                was.add(w.getWechatauthorityid());
        }
            return was;
        } catch (Exception e) {
        }
        return null;
    }
}
