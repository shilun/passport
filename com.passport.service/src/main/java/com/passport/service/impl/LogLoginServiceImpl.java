package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.StringUtils;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.passport.domain.LogLoginInfo;
import com.passport.service.LogLoginService;
import com.passport.service.util.Tool;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Luo
 * @date 2018/9/18 15:50
 */
@Service
public class LogLoginServiceImpl extends AbstractMongoService<LogLoginInfo> implements LogLoginService {
    private static Logger logger = LoggerFactory.getLogger(LogLoginServiceImpl.class);

    @Override
    protected Class getEntityClass() {
        return LogLoginInfo.class;
    }

    @Resource
    private Tool tool;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Override
    public Boolean addLoginLog(String pin, Long proxyId, Date registerDate, String ip, Long userCode) {
        if (StringUtils.isBlank(pin) || proxyId == null) {
            return false;
        }
        cachedThreadPool.submit(() -> {
            StringBuffer sb = new StringBuffer();
            try {
                // 获取ip物理地址
                URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
                JSONObject jsonObject = tool.httpGet(url);
                if(jsonObject != null && jsonObject.containsKey("code") && jsonObject.getInt("code") == 0){
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data.containsKey("country")) {
                        sb.append(data.getString("country")).append(",");
                    }
                    if (data.containsKey("region")) {
                        sb.append(data.getString("region")).append(",");
                    }
                    if (data.containsKey("city")) {
                        sb.append(data.getString("city"));
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }

            try {
                LogLoginInfo info = new LogLoginInfo();
                info.setPin(pin);
                info.setProxyId(proxyId);
                info.setLoginDay(new Date());
                info.setRegisterDate(registerDate);
                info.setIp(ip);
                info.setUserCode(userCode);
                info.setIpAddress(sb.toString());
                save(info);

            } catch (Exception e) {
                logger.error("", e);
            }
        });
        Boolean flag = false;

        return flag;
    }

    @Override
    public Page<LogLoginInfo> queryByIp(Long proxyId, String ip, Pageable pageable) {
        LogLoginInfo info = new LogLoginInfo();
        info.setProxyId(proxyId);
        info.setIp(ip);
        return queryByPage(info, pageable);
    }

    @Override
    public LogLoginInfo getUserLastLoginInfo(Long proxyId, String pin) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("proxyId").is(proxyId));
        query.addCriteria(Criteria.where("pin").is(pin));
        query.with(new Sort(Sort.Direction.DESC, "loginDay"));
        query.limit(1);
        List<LogLoginInfo> list = secondaryTemplate.find(query, LogLoginInfo.class);
        if(list == null || list.size() <= 0){
            return null;
        }
        return list.get(0);
    }
}
