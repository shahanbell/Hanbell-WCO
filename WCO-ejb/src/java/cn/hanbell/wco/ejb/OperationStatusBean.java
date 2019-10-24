/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.OperationStatus;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
public class OperationStatusBean extends SuperBean<OperationStatus> {

    public OperationStatusBean() {
        super(OperationStatus.class);
    }

    public OperationStatus findByAgentIdAndUserId(int agentId, String userId) {
        Query query = this.getEntityManager().createNamedQuery("OperationStatus.findByAgentIdAndUserId");
        query.setParameter("agentId", agentId);
        query.setParameter("userId", userId);
        try {
            Object o = query.getSingleResult();
            return (OperationStatus) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public OperationStatus findByCheckCode(String taskId) {
        Query query = this.getEntityManager().createNamedQuery("OperationStatus.findByCheckCode");
        query.setParameter("checkCode", taskId);
        try {
            Object o = query.getSingleResult();
            return (OperationStatus) o;
        } catch (Exception ex) {
            return null;
        }
    }

}
