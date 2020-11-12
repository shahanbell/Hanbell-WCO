/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.PersonnelChange;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C2082
 */
public class PersonnelChangeModel extends BaseLazyModel<PersonnelChange>{
    
    public PersonnelChangeModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }
}
