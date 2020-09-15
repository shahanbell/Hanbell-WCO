/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.ConfigProperties;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Startup
@Stateless
public class ConfigPropertiesBean extends SuperBean<ConfigProperties> {

    public ConfigPropertiesBean() {
        super(ConfigProperties.class);
    }

    public ConfigProperties findByKey(String configkey) {
        Query query = getEntityManager().createNamedQuery("ConfigProperties.findByConfigkey");
        query.setParameter("configkey", configkey);
        try {
            Object o = query.getSingleResult();
            return (ConfigProperties) o;
        } catch (Exception ex) {
            return null;
        }
    }
}
