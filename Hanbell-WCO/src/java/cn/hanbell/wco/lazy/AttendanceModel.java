/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.Attendance;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.Iterator;

/**
 *
 * @author C2082
 */
public class AttendanceModel extends BaseLazyModel<Attendance> {

    public AttendanceModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

    public Attendance getRowData(String rowKey) {
        if (this.dataList != null) {
            Iterator var2 = this.dataList.iterator();

            while (var2.hasNext()) {
                Attendance entity = (Attendance) var2.next();
                if (entity.getId().equals(rowKey)) {
                    return entity;
                }
            }
        }

        return null;
    }

    public Object getRowKey(Attendance object) {
        return super.getRowKey(object);
    }
}
