package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

public class SoftWareDto extends AbstractDTO {
    /**
     * 名称
     */
    private String name;

    /**
     * 代理商
     */
    private Long proxyId;
    /**
     * 版本说明
     */
    private String versionDescribes;
    /**
     * 软件类型 1 android 2 iso
     */
    private Integer osType;
    /**
     * 下载地址
     */
    private String url;
    /**
     * 版本
     */
    private String version;
    /**
     * 版本类型 1增量 2全量
     */
    private Integer versionType;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否强制更新 1：是  2：否
     */
    private Integer mandatory;

    /***
     * app标识 用于跟新app版本，由app上报
     */
    private String appSign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getVersionDescribes() {
        return versionDescribes;
    }

    public void setVersionDescribes(String versionDescribes) {
        this.versionDescribes = versionDescribes;
    }

    public Integer getOsType() {
        return osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getVersionType() {
        return versionType;
    }

    public void setVersionType(Integer versionType) {
        this.versionType = versionType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMandatory() {
        return mandatory;
    }

    public void setMandatory(Integer mandatory) {
        this.mandatory = mandatory;
    }

    public String getAppSign() {
        return appSign;
    }

    public void setAppSign(String appSign) {
        this.appSign = appSign;
    }
}
