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
public class Pub9780bcc30275Bean extends WeChatPubBean {

    //sh-gxxx
    public Pub9780bcc30275Bean() {
        appID = "wx969e91d1c3157ea2";
        appSecret = "961b3fa00acd1df791f064529199d883";
    }

    @Override
    public String getAppID() {
        return appID;
    }

    @Override
    public String getAppSecret() {
        return appSecret;
    }

}
