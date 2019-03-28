package com.passport.web.controller;

import com.common.util.GlosseryEnumUtils;
import com.common.web.IExecute;
import com.passport.domain.SoftWare;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.service.SoftWareService;
import com.passport.web.AbstractClientController;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
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
import java.util.Map;

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
        return getContentBody(lastInfo.getUrl(), lastInfo.getVersion(), lastInfo.getName());
    }

    protected static String getContentBody(String url, String version, String name) {
        StringBuilder context = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        context.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
        context.append("<plist version=\"1.0\">\n");
        context.append("    <dict>\n");
        context.append("        <key>items</key>\n");
        context.append("        <array>\n");
        context.append("            <dict>\n");
        context.append("                <key>assets</key>\n");
        context.append("                <array>\n");
        context.append("                    <dict>\n");
        context.append("                        <key>kind</key>\n");
        context.append("                        <string>software-package</string>\n");
        context.append("                        <key>url</key>\n");
        context.append("                        <string>" + url + "</string>\n");
        context.append("                    </dict>\n");
        context.append("                </array>\n");
        context.append("                <key>metadata</key>\n");
        context.append("                <dict>\n");
        context.append("                    <key>bundle-identifier</key>\n");
        context.append("                    <string>com.fandou</string>\n");
        context.append("                    <key>bundle-version</key>\n");
        context.append("                    <string>" + version + "</string>\n");
        context.append("                    <key>kind</key>\n");
        context.append("                    <string>software</string>\n");
        context.append("                    <key>title</key>\n");
        context.append("                    <string>" + name + "</string>\n");
        context.append("                </dict>\n");
        context.append("            </dict>\n");
        context.append("        </array>\n");
        context.append("    </dict>\n");
        context.append("</plist>");
        return context.toString();
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
