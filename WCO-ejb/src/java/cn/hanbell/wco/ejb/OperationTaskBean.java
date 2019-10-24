/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.OperationTask;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
public class OperationTaskBean extends SuperBean<OperationTask> {

    public OperationTaskBean() {
        super(OperationTask.class);
    }

}
