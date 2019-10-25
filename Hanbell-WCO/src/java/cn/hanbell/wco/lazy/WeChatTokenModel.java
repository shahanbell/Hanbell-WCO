/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.wco.entity.WeChatToken;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C0160
 */
public class WeChatTokenModel extends BaseLazyModel<WeChatToken> {

    public WeChatTokenModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

}
