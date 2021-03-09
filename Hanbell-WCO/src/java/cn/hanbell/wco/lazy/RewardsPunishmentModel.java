/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.lazy;

import cn.hanbell.eap.entity.RewardsPunishment;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author C2082
 */
public class RewardsPunishmentModel extends BaseLazyModel<RewardsPunishment> {

    private List<RewardsPunishment> selectData = new ArrayList<>();

    public RewardsPunishmentModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

    public RewardsPunishment getRowData(String rowKey) {
        if (this.dataList != null) {
            Iterator var2 = this.dataList.iterator();
            while (var2.hasNext()) {
                RewardsPunishment re = (RewardsPunishment) var2.next();
                if (rowKey.equals(String.valueOf(re.getId()))) {
                    selectData.add(re);
                    return re;
                }
            }
        }
        return null;
    }

    public Object getRowKey(RewardsPunishment object) {
        return super.getRowKey(object);
    }

    public List<RewardsPunishment> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<RewardsPunishment> selectData) {
        this.selectData = selectData;
    }
}
