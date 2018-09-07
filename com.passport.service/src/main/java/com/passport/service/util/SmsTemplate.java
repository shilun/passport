//
// \u6b64\u6587\u4ef6\u662f\u7531 JavaTM Architecture for XML Binding (JAXB) \u5f15\u7528\u5b9e\u73b0 v2.2.8-b130911.1802 \u751f\u6210\u7684
// \u8bf7\u8bbf\u95ee <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// \u5728\u91cd\u65b0\u7f16\u8bd1\u6e90\u6a21\u5f0f\u65f6, \u5bf9\u6b64\u6587\u4ef6\u7684\u6240\u6709\u4fee\u6539\u90fd\u5c06\u4e22\u5931\u3002
// \u751f\u6210\u65f6\u95f4: 2017.05.18 \u65f6\u95f4 01:57:22 PM CST 
//


package com.passport.service.util;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type\u7684 Java \u7c7b\u3002
 * 
 * <p>\u4ee5\u4e0b\u6a21\u5f0f\u7247\u6bb5\u6307\u5b9a\u5305\u542b\u5728\u6b64\u7c7b\u4e2d\u7684\u9884\u671f\u5185\u5bb9\u3002
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="template" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="sign-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="regex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="parakeys" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "template"
})
@XmlRootElement(name = "sms-template")
public class SmsTemplate {

    protected List<Template> template;

    /**
     * Gets the value of the template property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the template property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Template }
     * 
     * 
     */
    public List<Template> getTemplate() {
        if (template == null) {
            template = new ArrayList<Template>();
        }
        return this.template;
    }


    /**
     * <p>anonymous complex type\u7684 Java \u7c7b\u3002
     * 
     * <p>\u4ee5\u4e0b\u6a21\u5f0f\u7247\u6bb5\u6307\u5b9a\u5305\u542b\u5728\u6b64\u7c7b\u4e2d\u7684\u9884\u671f\u5185\u5bb9\u3002
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="sign-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="regex" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="parakeys" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "signName",
        "code",
        "regex",
        "parakeys"
    })
    public static class Template {

        @XmlElement(name = "sign-name", required = true)
        protected String signName;
        @XmlElement(required = true)
        protected String code;
        @XmlElement(required = true)
        protected String regex;
        @XmlElement(required = true)
        protected String parakeys;

        /**
         * \u83b7\u53d6signName\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSignName() {
            return signName;
        }

        /**
         * \u8bbe\u7f6esignName\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSignName(String value) {
            this.signName = value;
        }

        /**
         * \u83b7\u53d6code\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCode() {
            return code;
        }

        /**
         * \u8bbe\u7f6ecode\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCode(String value) {
            this.code = value;
        }

        /**
         * \u83b7\u53d6regex\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegex() {
            return regex;
        }

        /**
         * \u8bbe\u7f6eregex\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegex(String value) {
            this.regex = value;
        }

        /**
         * \u83b7\u53d6parakeys\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getParakeys() {
            return parakeys;
        }

        /**
         * \u8bbe\u7f6eparakeys\u5c5e\u6027\u7684\u503c\u3002
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setParakeys(String value) {
            this.parakeys = value;
        }

    }

}
