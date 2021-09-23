/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.app.MSGApplication;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.Agent1000016Bean;
import cn.hanbell.wco.entity.WeChatUser;
import com.lightshell.comm.BaseLib;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author C2082
 */
@Path("sendmsg/")
@javax.enterprise.context.RequestScoped
public class SendMsgFacadeREST extends WeChatOpenFacade<WeChatUser> {

    @EJB
    private Agent1000002Bean agent1000002Bean;

    public SendMsgFacadeREST() {
        super(WeChatUser.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @POST
    @Path("send")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage sendMessage(MSGApplication entity) {
        if (entity.getUserId() == null || entity.getMsg() == null || entity.getOpenid() == null || entity.getSessionkey() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            if (wechatSessionBean.has(entity.getOpenid(), entity.getSessionkey())) {
                agent1000002Bean.initConfiguration();
                String errmsg = agent1000002Bean.sendMsgToUser(entity.getUserId(), "text", entity.getMsg());
                return new ResponseMessage("200", errmsg);
            } else {
                return new ResponseMessage("401", "会话异常");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }
    @EJB
    private SystemUserBean systemUserBean;
    @EJB
    private Agent1000016Bean agent1000016Bean;

    @GET
    @Path("test")
    @Produces({MediaType.APPLICATION_JSON})
    public void loginFaild(@QueryParam("test") String test) {
        //前端wx.login()请求失败的回调请求，记录请求失败原因
        if ("b".equals(test)) {
            test1();
        } else {
            test2();
        }
    }

    public void test1() {
        agent1000016Bean.initConfiguration();
        String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("WEB-INF");
        String filePath = new String(finalFilePath.substring(1, index));
        String pathString = new String(filePath.concat("rpt/"));

        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeBirthdayDateAndDeptno(selectDate);
        if (list != null && !list.isEmpty()) {
            log4j.info("----- 发送生日祝福开始----------");
            for (SystemUser s : list) {
                if ("V".equals(s.getSyncWeChatStatus())) {
                    try {
                        String materialId = agent1000016Bean.getMaterialId(agent1000016Bean.MEDIA_IMG, pathString.concat(agent1000016Bean.getBirthdatPicteureUrl(s.getDeptno())));
                        //发送消息
                        StringBuffer data = new StringBuffer("{");
                        data.append("'title':'").append(s.getUsername()).append(",生日快乐!").append("',");
                        data.append("'thumb_media_id':'").append(materialId).append("',");
                        data.append("'content':'").append("<img src=\"http://i2.hanbell.com.cn:8480/birthday.png\"></img>").append("',");
                        data.append("'safe':").append(2).append("}");
                        agent1000016Bean.sendMsgToUser("C2082", "mpnews", data.toString());
                        log4j.info(data.toString());
                    } catch (Exception e) {

                        log4j.info(s.getUserid() + "发送失败：" + e.toString());
                    }
                }
            }
            log4j.info("----- 发送生日祝福结束----------");
        }
    }

    public void test2() {
        agent1000016Bean.initConfiguration();
        String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("WEB-INF");
        String filePath = new String(finalFilePath.substring(1, index));
        String pathString = new String(filePath.concat("rpt/"));

        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeWorkingAgeBeginDateAndDeptno(selectDate);
        if (list != null && !list.isEmpty()) {
            log4j.info("----- 发送年资祝福开始----------");
            for (SystemUser s : list) {
                if ("V".equals(s.getSyncWeChatStatus())) {
                    try {
                        String materialId = agent1000016Bean.getMaterialId(agent1000016Bean.MEDIA_IMG, pathString.concat(agent1000016Bean.getWorkingAgePicteureUrl(s.getDeptno())));
                        //计算时间
                        Integer now = Integer.valueOf(BaseLib.formatDate("yyyy", new Date()));
                        Integer workYear = Integer.valueOf(BaseLib.formatDate("yyyy", s.getWorkingAgeBeginDate()));
                        //发送消息
                        StringBuffer data = new StringBuffer("{");
                        data.append("'title':'").append(s.getUsername()).append(",感谢您").append(now - workYear).append("年来在汉钟坚守初心，筑梦前行！").append("',");
                        data.append("'thumb_media_id':'").append(materialId).append("',");
                        data.append("'content':'").append("<img src=\"http://i2.hanbell.com.cn:8480/working.png\"></img>").append("',");
                        data.append("'safe':").append(2).append("}");
                        agent1000016Bean.sendMsgToUser("C2082", "mpnews", data.toString());
                        log4j.info(data.toString());
                    } catch (Exception e) {
                        log4j.info(s.getUserid() + "发送失败：" + e.toString());
                    }
                }
            }
            log4j.info("----- 发送年资祝福结束----------");
        }
    }
}
