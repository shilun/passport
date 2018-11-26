package com.passport.web.controller;

import com.common.util.GlosseryEnumUtils;
import com.common.util.StringUtils;
import com.common.web.IExecute;
import com.passport.domain.SoftWare;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.service.SoftWareService;
import com.passport.web.AbstractClientController;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping(value = "AppDownload", method = {RequestMethod.GET})
public class AppDownloadController extends AbstractClientController {

    private static Logger logger = Logger.getLogger(AppDownloadController.class);
    @Resource
    private SoftWareService softWareService;


    @RequestMapping()
    public String down(HttpServletResponse response, Integer osType) {
        response.setContentType("application/x-download");
        AgentTypeEnum type = null;
        if (osType != null) {
            type = (AgentTypeEnum) GlosseryEnumUtils.getItem(AgentTypeEnum.class, osType);
        } else {
            type = getAgentType();
        }
        return "redirect:" + softWareService.findLastInfo(getDomain().getId(), type).getUrl();
    }

    @RequestMapping(value = "/download")
    @ApiOperation(value = "下载", notes = "下载")
    @ResponseBody
    public Map<String, Object> download(Integer osType) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                AgentTypeEnum type = null;
                if (osType != null) {
                    type = GlosseryEnumUtils.getItem(AgentTypeEnum.class, osType);
                } else {
                    type = getAgentType();
                }
                return softWareService.findLastInfo(getDomain().getId(), type).getUrl();
            }
        });
    }

    @RequestMapping("download.plist")
    @ResponseBody
    public String buildDownloadPage(HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setHeader("Content-type", "text/plain;charset=UTF-8");
        AgentTypeEnum type = AgentTypeEnum.IOS;
        SoftWare lastInfo = softWareService.findLastInfo(getDomain().getId(), type);
        VelocityContext context = new VelocityContext();
        String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
        context.put("url", "http://images." + domain + lastInfo.getUrl());
        context.put("name", lastInfo.getName());
        context.put("version", lastInfo.getVersion());
        return getContentBody(context, "downloadIOS.html");
    }

    protected static String getContentBody(VelocityContext context, String vmFile) {
        Template template = null;
        try {
            template = Velocity.getTemplate(vmFile);
        } catch (Exception e) {
            logger.error(e);
        }
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            template.merge(context, sw);
            return sw.toString();
        } finally {
            IOUtils.closeQuietly(sw);
        }
    }

    @RequestMapping("page")
    public String index() {
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
