/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.WeChatToken;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author kevindong
 */
@Startup
@Singleton
public class Agent1000002Bean extends WeChatCorpBean {

    public Agent1000002Bean() {
        agentId = 1000002;
    }

    @Override
    public void initConfiguration() {
        WeChatToken token = getWeChatToken(String.valueOf(getAgentId()));
        if (token != null && !isConfigured) {
            // 单例只需初始化一次
            initWeChatCrypt(token.getAppASEKey());
            this.appSecret = token.getAppSecret();
            this.appToken = token.getAppToken();
            this.isConfigured = true;
        }
    }

    @Override
    public String getAppID() {
        return "ww94e6447967583b32";
        //return "ww6678f44a19cb2f3c";
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
