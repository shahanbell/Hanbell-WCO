/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.wco.ejb.WeChatTokenBean;
import cn.hanbell.wco.entity.WeChatToken;
import cn.hanbell.wco.lazy.WeChatTokenModel;
import cn.hanbell.wco.web.SuperSingleBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "wechatTokenManagedBean")
@SessionScoped
public class WeChatTokenManagedBean extends SuperSingleBean<WeChatToken> {

    @EJB
    private WeChatTokenBean wechatTokenBean;

    public WeChatTokenManagedBean() {
        super(WeChatToken.class);
    }

    @Override
    public void init() {
        superEJB = wechatTokenBean;
        model = new WeChatTokenModel(wechatTokenBean);
        model.getSortFields().put("app", "ASC");
        super.init();
    }

}
