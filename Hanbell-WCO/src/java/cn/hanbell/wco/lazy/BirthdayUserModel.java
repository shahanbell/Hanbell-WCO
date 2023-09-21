/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.Attendance;
import cn.hanbell.wco.entity.BirthdayUser;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author C2082
 */
public class BirthdayUserModel extends BaseLazyModel<BirthdayUser> {

    public BirthdayUserModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

    @Override
    public List<BirthdayUser> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        this.setDataList(this.superEJB.findByFilters(this.filterFields, first, pageSize, this.sortFields));
        this.setRowCount(this.superEJB.getRowCount(this.filterFields));
        return this.dataList;
    }
}
