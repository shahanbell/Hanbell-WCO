/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.corp;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 *
 * @author kevindong
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReqEncryptMessage implements Serializable {

    @XmlCDATA
    private String ToUserName;
    @XmlCDATA
    private int AgentID;
    @XmlCDATA
    private String Encrypt;

    /**
     * @return the ToUserName
     */
    public String getToUserName() {
        return ToUserName;
    }

    /**
     * @param ToUserName the ToUserName to set
     */
    public void setToUserName(String ToUserName) {
        this.ToUserName = ToUserName;
    }

    /**
     * @return the AgentID
     */
    public int getAgentID() {
        return AgentID;
    }

    /**
     * @param AgentID the AgentID to set
     */
    public void setAgentID(int AgentID) {
        this.AgentID = AgentID;
    }

    /**
     * @return the Encrypt
     */
    public String getEncrypt() {
        return Encrypt;
    }

    /**
     * @param Encrypt the Encrypt to set
     */
    public void setEncrypt(String Encrypt) {
        this.Encrypt = Encrypt;
    }

}
