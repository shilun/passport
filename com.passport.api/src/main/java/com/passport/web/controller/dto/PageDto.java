package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/19 14:25
 */
@ApiModel(description = "页码")
public class PageDto  implements Serializable {
    private static final long serialVersionUID = -6839023024875059996L;
    private Integer pageNum;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
