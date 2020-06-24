/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.wco.app.MSGApplication;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.entity.WeChatUser;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @POST
    @Path("send")
    @Produces({"application/json"})
    public ResponseMessage complaintMessage(MSGApplication entity) {
        if (entity.getUserId() == null || entity.getMsg() == null || entity.getOpenid() == null || entity.getSessionkey() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            if (wechatSessionBean.has(entity.getOpenid(), entity.getSessionkey())) {
                agent1000002Bean.initConfiguration();
                String errmsg = agent1000002Bean.sendMsgToUser(1000002, entity.getUserId(), "text", entity.getMsg());
                return new ResponseMessage("200", errmsg);
            } else {
                return new ResponseMessage("401", "会话异常");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }

}
