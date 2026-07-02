/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;

/**
 *
 * @author kevindong
 */
@Startup
@Singleton
@Named
public class TwPrg9f247ab6d5e4Bean extends WeChatPrgBean {

    public TwPrg9f247ab6d5e4Bean() {

    }

    @Override
    public String getAppID() {
        return "wx2756d001918111f9";
    }

    @Override
    public String getAppSecret() {
        return "a75704a3d1c6907fe98ccdbe208dfba1";
    }

}
