package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.exception.BizException;
import com.common.upload.UploadUtil;
import com.common.util.BeanCoper;
import com.common.web.AbstractController;
import com.common.web.IExecute;
import com.passport.domain.SoftWare;
import com.passport.main.controller.dto.SoftWareDto;
import com.passport.service.SoftWareService;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "/software")
public class SoftworeController extends AbstractController {
    private Logger logger = Logger.getLogger(SoftworeController.class);

    @Resource
    private SoftWareService softWareService;

    @Resource(name = "softUploadUtil")
    private UploadUtil fileUploadUtil;

    @RoleResource(resource = "passport")
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> file(@RequestPart("file") MultipartFile file) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                try {
                    String s = "http://" + fileUploadUtil.getDomainName() + "/" + fileUploadUtil.getScode() + "/" + fileUploadUtil.uploadFile(file.getBytes(), file.getOriginalFilename()).getModule();
                    return s;
                } catch (Exception e) {
                    logger.error("上付文件失败", e);
                    throw new BizException("upload.error", "文件上传失败");
                }
            }
        });
    }

    @RoleResource(resource = "passport")
    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody SoftWareDto dto) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                SoftWare query = BeanCoper.copyProperties(SoftWare.class, dto);
                return softWareService.queryByPage(query, dto.getPageinfo().getPage());
            }
        });
    }

    @RoleResource(resource = "passport")
    @RequestMapping(value = "/view")
    @ResponseBody
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return softWareService.findById(getIdByJson(content));
            }
        });
    }

    @RoleResource(resource = "passport")
    @RequestMapping(value = "/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody SoftWareDto dto) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                SoftWare entity = BeanCoper.copyProperties(SoftWare.class, dto);
                softWareService.save(entity);
                return null;
            }
        });
    }


}
