/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.WechatMessage;
import javax.ejb.Stateless;

/**
 *
 * @author C2082
 */
@Stateless
public class WechatMessageBean extends SuperBean<WechatMessage>{
    
    public WechatMessageBean() {
        super(WechatMessage.class);
    }
    
}
