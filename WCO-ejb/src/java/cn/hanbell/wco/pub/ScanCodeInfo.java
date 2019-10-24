/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.pub;

/**
 *
 * @author kevindong
 */
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlRootElement(name = "ScanCodeInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScanCodeInfo implements Serializable {

    @XmlCDATA
    private String ScanType;
    @XmlCDATA
    private String ScanResult;

    public ScanCodeInfo() {

    }

    public ScanCodeInfo(String type, String result) {
        this.ScanType = type;
        this.ScanResult = result;
    }

    /**
     * @return the ScanType
     */
    public String getScanType() {
        return ScanType;
    }

    /**
     * @param ScanType the ScanType to set
     */
    public void setScanType(String ScanType) {
        this.ScanType = ScanType;
    }

    /**
     * @return the ScanResult
     */
    public String getScanResult() {
        return ScanResult;
    }

    /**
     * @param ScanResult the ScanResult to set
     */
    public void setScanResult(String ScanResult) {
        this.ScanResult = ScanResult;
    }

}
