/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.comm;

import com.lightshell.comm.SuperEJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kevindong
 * @param <T>
 */
public abstract class SuperBean<T> extends SuperEJB<T> {

    @PersistenceContext(unitName = "WeChatOpenPU")
    private EntityManager em;

    protected final Logger logger = LogManager.getLogger("cn.hanbell.wco");

    public SuperBean(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
