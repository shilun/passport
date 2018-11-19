package com.passport.web.controller;

import com.common.upload.UploadUtil;
import com.common.util.GlosseryEnumUtils;
import com.common.web.IExecute;
import com.passport.domain.SoftWare;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.service.SoftWareService;
import com.passport.web.AbstractClientController;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("AppDownload")
public class AppDownloadController extends AbstractClientController {
    @Resource
    private SoftWareService softWareService;

    @RequestMapping()
    public String down(HttpServletResponse response, Integer osType, String appSign) {
        response.setContentType("application/x-download");
        AgentTypeEnum type = null;
        if (osType != null) {
            type = (AgentTypeEnum) GlosseryEnumUtils.getItem(AgentTypeEnum.class, osType);
        } else {
            type = getAgentType();
        }
        return "redirect:" + softWareService.findLastInfo(getDomain().getId(),type, appSign).getUrl();
    }

    @RequestMapping(value = "/download")
    @ApiOperation(value = "下载", notes = "下载")
    @ResponseBody
    public Map<String, Object> download(Integer osType, String version) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                AgentTypeEnum type = null;
                if (osType != null) {
                    type = (AgentTypeEnum) GlosseryEnumUtils.getItem(AgentTypeEnum.class, osType);
                } else {
                    type = getAgentType();
                }
                return softWareService.findLastInfo(getDomain().getId(),type, version).getUrl();
            }
        });
    }

    @RequestMapping("download.plist")
    public String buildDownloadPage(HttpServletResponse response, String appSign) {
        response.setContentType("text/plain");
        response.setHeader("Content-type", "text/plain;charset=UTF-8");
        AgentTypeEnum type = AgentTypeEnum.IOS;
        SoftWare lastInfo = softWareService.findLastInfo(getDomain().getId(),type, appSign);
        getRequest().setAttribute("url", lastInfo.getUrl());
        getRequest().setAttribute("version", lastInfo.getVersion());
        return "downloadIOS";
    }

    @RequestMapping("page")
    public String handle() {
        return "download";
    }

    public ResponseEntity<byte[]> download(byte[] info, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(info,
                headers, HttpStatus.CREATED);
    }

    public AgentTypeEnum getAgentType() {
        String agent = getRequest().getHeader("user-agent").toLowerCase();
        if (agent.indexOf(AgentTypeEnum.Android.name().toLowerCase()) != -1) {
            return AgentTypeEnum.Android;
        }
        if (agent.indexOf("iPhone".toLowerCase()) != -1 || agent.indexOf("iPod".toLowerCase()) != -1 || agent.indexOf("iPad".toLowerCase()) != -1) {
            return AgentTypeEnum.IOS;
        }
        return AgentTypeEnum.Other;
    }


}
