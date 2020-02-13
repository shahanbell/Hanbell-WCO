/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.web;

import cn.hanbell.eap.ejb.SystemNameBean;
import cn.hanbell.wco.jrs.ResponseMessage;
import com.lightshell.comm.SuperEJB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C0160
 * @param <T>
 */
public abstract class SuperRESTForWCO<T> {

    protected Class<T> entityClass;

    protected abstract SuperEJB getSuperEJB();

    @Inject
    protected cn.hanbell.eap.ejb.SystemNameBean systemNameBean;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.wco");

    public SuperRESTForWCO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseMessage create(T entity, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                getSuperEJB().persist(entity);
                return new ResponseMessage("200", "更新成功");
            } catch (Exception ex) {
                log4j.error(ex);
                return new ResponseMessage("500", "系统错误更新失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseMessage edit(@PathParam("id") PathSegment id, T entity, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                getSuperEJB().update(entity);
                return new ResponseMessage("200", "更新成功");
            } catch (Exception ex) {
                log4j.error(ex);
                return new ResponseMessage("500", "系统错误更新失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseMessage remove(@PathParam("id") PathSegment id, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                T t = (T) getSuperEJB().findById(Integer.parseInt(id.getPath()));
                if (t == null) {
                    return new ResponseMessage("404", "内容为空");
                }
                getSuperEJB().delete(t);
                return new ResponseMessage("200", "更新成功");
            } catch (Exception ex) {
                log4j.error(ex);
                return new ResponseMessage("500", "系统错误更新失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<T> findAll(@QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                return getSuperEJB().findAll();
            } catch (Exception ex) {
                log4j.error(ex);
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("{offset}/{pageSize}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<T> findAll(@PathParam("offset") Integer offset, @PathParam("pageSize") Integer pageSize, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                return getSuperEJB().findAll(offset, pageSize);
            } catch (Exception ex) {
                log4j.error(ex);
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public T findById(@PathParam("id") PathSegment id, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                return (T) getSuperEJB().findById(Integer.parseInt(id.getPath()));
            } catch (Exception ex) {
                log4j.error(ex);
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("{filters}/{sorts}/{offset}/{pageSize}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<T> findByFilters(@PathParam("filters") PathSegment filters, @PathParam("sorts") PathSegment sorts, @PathParam("offset") Integer offset, @PathParam("pageSize") Integer pageSize, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                MultivaluedMap<String, String> filtersMM = filters.getMatrixParameters();
                MultivaluedMap<String, String> sortsMM = sorts.getMatrixParameters();
                Map<String, Object> filterFields = new HashMap<>();
                Map<String, String> sortFields = new HashMap<>();
                String key, value;
                if (filtersMM != null) {
                    for (Map.Entry<String, List<String>> entrySet : filtersMM.entrySet()) {
                        key = entrySet.getKey();
                        value = entrySet.getValue().get(0);
                        filterFields.put(key, value);
                    }
                }
                if (sortsMM != null) {
                    for (Map.Entry<String, List<String>> entrySet : sortsMM.entrySet()) {
                        key = entrySet.getKey();
                        value = entrySet.getValue().get(0);
                        sortFields.put(key, value);
                    }
                }
                int size = getSuperEJB().getRowCount(filterFields);
                return getSuperEJB().findByFilters(filterFields, offset, pageSize, sortFields);
            } catch (Exception ex) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    protected boolean isAuthorized(String appid, String token) {
        return systemNameBean.isAuthorized(appid, token);
    }

}
