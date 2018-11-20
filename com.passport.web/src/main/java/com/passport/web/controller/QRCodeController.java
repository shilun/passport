package com.passport.web.controller;

import com.common.qrcode.QRCodeUtil;
import com.common.util.StringUtils;
import com.passport.web.AbstractClientController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/qrCode", method = {RequestMethod.GET})
public class QRCodeController extends AbstractClientController {

    @RequestMapping("build")
    public void down(HttpServletResponse response,String pin)
            throws Exception {
        if(StringUtils.isBlank(pin)){
            return;
        }
        ServletOutputStream outputStream = null;
        try {
            response.setContentType("image/jpeg");
            outputStream = response.getOutputStream();
            String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
            QRCodeUtil.encode("http://passport." + domain + "/login/reg?q=" + pin, outputStream);
        } finally {
            outputStream.close();
        }
        return;
    }
}
