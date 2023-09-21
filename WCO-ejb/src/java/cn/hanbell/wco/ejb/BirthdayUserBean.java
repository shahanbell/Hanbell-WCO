/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.BirthdayUser;
import com.lightshell.comm.BaseLib;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
public class BirthdayUserBean extends SuperBean<BirthdayUser> {

    public BirthdayUserBean() {
        super(BirthdayUser.class);
    }

    public BirthdayUser findByUseridAndYear(String userid, int year) {
        Query query = getEntityManager().createNamedQuery("BirthdayUser.findByUseridAndYear");
        query.setParameter("userid", userid);
        query.setParameter("year", year);
        try {
            return (BirthdayUser) query.getSingleResult();
        } catch (Exception ex) {
           return null;
        }
    }

}
