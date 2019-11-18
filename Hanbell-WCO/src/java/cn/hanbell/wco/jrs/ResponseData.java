/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import java.util.List;

/**
 *
 * @author C0160
 * @param <T>
 */
public class ResponseData<T> extends ResponseMessage {

    protected Integer count;
    protected List<T> data;

    public ResponseData() {

    }

    public ResponseData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data) {
        this.data = data;
    }

}
