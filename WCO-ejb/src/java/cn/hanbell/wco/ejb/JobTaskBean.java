/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.JobTask;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class JobTaskBean extends SuperBean<JobTask> {

    public JobTaskBean() {
        super(JobTask.class);
    }

    public List<JobTask> findByLeaderId(String leaderId) {
        Query query = getEntityManager().createNamedQuery("JobTask.findByLeaderId");
        query.setParameter("leaderId", leaderId);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<JobTask> findByLeaderIdAndStatus(String leaderId, String status) {
        Query query = getEntityManager().createNamedQuery("JobTask.findByLeaderIdAndStatus");
        query.setParameter("leaderId", leaderId);
        query.setParameter("status", status);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
