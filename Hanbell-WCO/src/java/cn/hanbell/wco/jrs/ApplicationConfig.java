/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author kevindong
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cn.hanbell.wco.jrs.AssetcardFacadeREST.class);
        resources.add(cn.hanbell.wco.jrs.CORSFilter.class);
        resources.add(cn.hanbell.wco.jrs.EquipmentRepairFacadeREST.class);
        resources.add(cn.hanbell.wco.jrs.Prg9f247ab6d5e4FacadeREST.class);
    }

}
