/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.SalarySend;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
public class SalarySendBean extends SuperBean<SalarySend> {

    public SalarySendBean() {
        super(SalarySend.class);
    }

    public List<SalarySend> findByTaskidAndDeptno(String taskid, String deptno) {
        Query query = getEntityManager().createNamedQuery("SalarySend.findByTaskidAndDeptno");
        query.setParameter("taskid", taskid);
        query.setParameter("deptno", deptno);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }
       public SalarySend findByTaskidAndEmployeeid(String taskid, String employeeid) {
        Query query = getEntityManager().createNamedQuery("SalarySend.findByTaskidAndEmployeeid");
        query.setParameter("taskid", taskid);
        query.setParameter("employeeid", employeeid);
        try {
            return (SalarySend)query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
