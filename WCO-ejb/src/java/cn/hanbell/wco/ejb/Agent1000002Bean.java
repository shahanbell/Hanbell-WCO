/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.WeChatToken;
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
public class Agent1000002Bean extends WeChatCorpBean {

    public Agent1000002Bean() {

    }

    @Override
    public void initConfiguration() {
        agentId = 1000002;
        WeChatToken token = getWeChatToken(String.valueOf(agentId));
        if (token != null && !isConfigured) {
            initWeChatCrypt(token.getAppASEKey());
            this.appSecret = token.getAppSecret();
            this.appToken = token.getAppToken();
            this.isConfigured = true;
        }
    }

    @Override
    public String getAppID() {
        return "ww7f3e1ce36d3bc75e";
    }

    @Override
    public String getAppSecret() {
        return this.appSecret;
    }

    @Override
    protected String getAppToken() {
        return this.appToken;
    }

}
