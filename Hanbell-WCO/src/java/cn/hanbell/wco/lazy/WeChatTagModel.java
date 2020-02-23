/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.WeChatTag;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C1879
 */
public class WeChatTagModel extends BaseLazyModel<WeChatTag> {

    public WeChatTagModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

}
