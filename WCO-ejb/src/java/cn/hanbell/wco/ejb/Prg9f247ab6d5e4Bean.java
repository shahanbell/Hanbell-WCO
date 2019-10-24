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
public class Prg9f247ab6d5e4Bean extends WeChatPrgBean {

    public Prg9f247ab6d5e4Bean() {

    }

    @Override
    public String getAppID() {
        return "wxaeb50a87e7bce793";
    }

    @Override
    public String getAppSecret() {
        return "e96e296444730d1a308ea95f3b2e091a";
    }

}
