/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open  the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.wco.comm.MiniProgramSession;

/**
 *
 * @author C0160
 */
public class ResponseMessage {

    protected String code;
    protected String msg;

    public ResponseMessage() {

    }

    public ResponseMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
