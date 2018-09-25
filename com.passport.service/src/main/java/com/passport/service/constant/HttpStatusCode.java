package com.passport.service.constant;

/**
 * @author Luo
 * @date 2018/9/22 9:15
 */
public class HttpStatusCode {
    /**************************错误码2XX****************************/
    public static final String CODE_OK = "200";// 请求成功code
    public static final String CODE_ACCEPTED = "202";// 请求成功,但未执行

    public static final String MSG_OK = "success";// 请求成功信息


    /**************************错误码4XX****************************/

    public static final String CODE_BAD_REQUEST = "400";// 请求参数有误
    public static final String CODE_UNAUTHORIZED = "401";// 对象不存在、没有权限访问
    public static final String CODE_REQUEST_TIMEOUT = "408";// 请求超时

    public static final String MSG_BAD_REQUEST = "请求参数有误";
    public static final String MSG_UNAUTHORIZED = "对象不存在";
    public static final String MSG_REQUEST_TIMEOUT = "请求超时";

    /**************************错误码5XX****************************/
    public static final String CODE_INTERNAL_SERVER_ERROR = "500";// 系统内部错误

    public static final String MSG_INTERNAL_SERVER_ERROR = "系统内部错误";
}
