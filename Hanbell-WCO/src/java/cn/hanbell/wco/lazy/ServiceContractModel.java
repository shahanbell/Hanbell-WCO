/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.ServiceContract;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author C2082
 */
public class ServiceContractModel extends BaseLazyModel<ServiceContract>{
    
   private  List<ServiceContract> selectData=new ArrayList<>();
    
    public ServiceContractModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

    public ServiceContract getRowData(String rowKey) {
        if (this.dataList != null) {
            Iterator var2 = this.dataList.iterator();
            while (var2.hasNext()) {
                ServiceContract ServiceContract = (ServiceContract) var2.next();
                if (rowKey.equals(String.valueOf(ServiceContract.getId()))) {
                    selectData.add(ServiceContract);
                    return ServiceContract;
                }
            }
        }
        return null;
    }

    public Object getRowKey(ServiceContract object) {
        return super.getRowKey(object);
    }

    public List<ServiceContract> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<ServiceContract> selectData) {
        this.selectData = selectData;
    }
}
