/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.comm.SuperBean;
import cn.hanbell.wco.entity.DineInfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class DineInfobean extends SuperBean<DineInfo> {

    protected DateFormat format;

    public DineInfobean() {
        super(DineInfo.class);
        format = new SimpleDateFormat("yyyy/MM/dd");
    }

    /**
     * 取消预约
     *
     * @param id
     * @return
     */
    public Boolean removeForStatus(int id) {
        Query query = getEntityManager().createNamedQuery("DineInfo.findById");
        query.setParameter("id", id);
        try {
            Object o = query.getSingleResult();
            DineInfo di = (DineInfo) o;
            if ("V".equals(di.getStatus())) {
                return false;
            } else {
                di.setStatus("W");
                di.setCfmdateToNow();
                this.update(di);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 更新状态 通知发送用餐信息时全部更改状态为V
     *
     * @return
     */
    public void updateForStatusByDinedate() {
        Query query = getEntityManager().createNamedQuery("DineInfo.updateForStatusByDinedate");
        query.setParameter("dinedate", new Date());
        List<DineInfo> list = query.getResultList();
        for (DineInfo dineInfo : list) {
            dineInfo.setStatus("V");
            dineInfo.setOptdateToNow();
        }
        this.update(list);
    }

    /**
     * 报餐日期只能一天一条，做验证 作废（W）除外
     *
     * @param userid
     * @param date 报餐时间
     * @return 为真不存在
     */
    public boolean notExist(String userid, Date date) {
        Query query = getEntityManager().createNamedQuery("DineInfo.notExist");
        query.setParameter("userid", userid);
        query.setParameter("dinedate", date);
        List<DineInfo> list = query.getResultList();
        return (list.size() == 0 );
    }

    /**
     * 根据用户加载当前用餐信息，根据当日日期，最多显示7条
     *
     * @param userid
     * @return
     */
    public List<DineInfo> getListByUseridAndDinedate(String userid) throws ParseException {
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date());
        cd.add(Calendar.DATE, 7);
        Date begin=format.parse(format.format(new Date()));
        List<DineInfo> list = new ArrayList<>();
        Query query = getEntityManager().createNamedQuery("DineInfo.getListByUseridAndDinedate");
        query.setParameter("userid", userid);
        query.setParameter("begin", begin);
        query.setParameter("end", format.parse(format.format(cd.getTime())));
        List<DineInfo> list1 = query.getResultList();
        if (list1 != null && !list1.isEmpty()) {
            list = list1;
        }
        return list;
    }

    /**
     * 依据地址统计用餐人数
     *
     * @param address
     * @return
     */
    public List<DineInfo> getDineinfoList(String address) {
        List<DineInfo> list = new ArrayList<>();
        Query query = getEntityManager().createNamedQuery("DineInfo.getDineinfoList");
        query.setParameter("address", address);
        query.setParameter("dinedate", format.format(new Date()));
        List<DineInfo> list1 = query.getResultList();
        if (list1 != null && !list1.isEmpty()) {
            list = list1;
        }
        return list;
    }

}
