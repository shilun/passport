package com.passport.web.controller;

import com.common.qrcode.QRCodeUtil;
import com.common.util.StringUtils;
import com.passport.domain.ClientUserInfo;
import com.passport.service.ClientUserInfoService;
import com.passport.web.AbstractClientController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/qrCode", method = {RequestMethod.GET})
public class QRCodeController extends AbstractClientController {

    @Resource
    private ClientUserInfoService clientUserInfoService;

    @RequestMapping("build")
    public void down(HttpServletResponse response, String pin)
            throws Exception {
        if (StringUtils.isBlank(pin)) {
            return;
        }
        ServletOutputStream outputStream = null;
        try {
            response.setContentType("image/jpeg");
            outputStream = response.getOutputStream();
            if (StringUtils.isNumeric(pin)) {
                ClientUserInfo byId = clientUserInfoService.findById(Long.valueOf(pin));
                if (byId == null) {
                    return;
                }
                pin = byId.getPin();
            }
            String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
            String[] domains = getDomain().getDomain();
            if (domains.length >= 2 && domains[0].equals(domain)) {
                QRCodeUtil.encode("https://passport." + domains[1] + "/login/reg?q=" + pin, outputStream);
            }

            QRCodeUtil.encode("https://passport." + domain + "/login/reg?q=" + pin, outputStream);
        } finally {
            outputStream.close();
        }
        return;
    }
}
