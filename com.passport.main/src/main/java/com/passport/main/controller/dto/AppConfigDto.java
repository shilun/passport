package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

@Data
public class AppConfigDto extends AbstractDTO {
    private Long proxyId;
    String content;
}
