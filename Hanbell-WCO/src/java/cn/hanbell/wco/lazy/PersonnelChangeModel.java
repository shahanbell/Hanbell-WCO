/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.PersonnelChange;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author C2082
 */
public class PersonnelChangeModel extends BaseLazyModel<PersonnelChange>{
    
   private  List<PersonnelChange> selectData=new ArrayList<>();
    
    public PersonnelChangeModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

    public PersonnelChange getRowData(String rowKey) {
        if (this.dataList != null) {
            Iterator var2 = this.dataList.iterator();
            while (var2.hasNext()) {
                PersonnelChange PersonnelChange = (PersonnelChange) var2.next();
                if (rowKey.equals(String.valueOf(PersonnelChange.getId()))) {
                    selectData.add(PersonnelChange);
                    return PersonnelChange;
                }
            }
        }
        return null;
    }

    public Object getRowKey(PersonnelChange object) {
        return super.getRowKey(object);
    }

    public List<PersonnelChange> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<PersonnelChange> selectData) {
        this.selectData = selectData;
    }
}
