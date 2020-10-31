/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.SalarySend;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author C2082
 */
public class SalarySendModel extends BaseLazyModel<SalarySend> {

    private  List<SalarySend> selectData=new ArrayList<>();
    public SalarySendModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

    public SalarySend getRowData(String rowKey) {
        if (this.dataList != null) {
            Iterator var2 = this.dataList.iterator();
            while (var2.hasNext()) {
                SalarySend SalarySend = (SalarySend) var2.next();
                if (rowKey.equals(String.valueOf(SalarySend.getId()))) {
                    selectData.add(SalarySend);
                    return SalarySend;
                }
            }
        }
        return null;
    }

    public Object getRowKey(SalarySend object) {
        return super.getRowKey(object);
    }

    public List<SalarySend> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SalarySend> selectData) {
        this.selectData = selectData;
    }

}
