package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.StringUtils;
import com.mongodb.*;
import com.passport.domain.LogLoginInfo;
import com.passport.service.LogLoginService;
import com.passport.service.util.Tool;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
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
    private static Logger logger = Logger.getLogger(LogLoginServiceImpl.class);

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
                //获取ip物理地址
                URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
                JSONObject jsonObject = tool.httpGet(url);
                if (jsonObject.getInt("code") == 0) {
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
    public Long QueryActiveUsers(Long proxyId, Date startTime, Date endTime) {
        /*db.logLoginInfo.aggregate
                ([

                    {$match:{
                        "loginDay" : {$gte : ISODate("2018-09-26T02:26:13.149Z"),$lte : ISODate("2018-09-26T04:26:13.149Z")}
                            }
                     },
                    {$group:{"_id":"$pin"}},
                    {$group:{"_id":null,"count":{$sum:1}}}
                ])
         */
        DBCollection collection = template.getCollection("logLoginInfo");
        BasicDBObject loginValueObj = new BasicDBObject();
        loginValueObj.append("$gte", startTime);
        loginValueObj.append("$lte", endTime);

        BasicDBObject matchValueObj = new BasicDBObject("loginDay", loginValueObj);

        BasicDBObject matchObj = new BasicDBObject("$match", matchValueObj);

        BasicDBObject id_1 = new BasicDBObject("_id", "$pin");
        BasicDBObject group_1 = new BasicDBObject("$group", id_1);

        BasicDBObject group2ValueObj = new BasicDBObject();
        group2ValueObj.append("_id", null);
        BasicDBObject sumObj = new BasicDBObject("$sum", 1);
        group2ValueObj.append("count", sumObj);
        BasicDBObject group_2 = new BasicDBObject("$group", group2ValueObj);

        List<BasicDBObject> list = new ArrayList<>();
        list.add(matchObj);
        list.add(group_1);
        list.add(group_2);

        //指定输出方式
        AggregationOptions build = AggregationOptions.builder()
                .outputMode(AggregationOptions.OutputMode.CURSOR)
                .build();
        Cursor cursor = collection.aggregate(list, build);
        Long value = 0L;
        DBObject obj = null;
        if (cursor.hasNext()) {
            obj = cursor.next();
            value = Long.parseLong(obj.get("count").toString());
        }
        return value;
    }

    @Override
    public Long QueryLoginUsersByRegDate(Long proxyId, Date loginStartTime, Date loginEndTime, Date regStartTime, Date regEndTime) {
        /*db.logLoginInfo.aggregate
                ([
                    {$match:{
                            "loginDay" : {$gte : ISODate("2018-09-26T02:26:13.149Z"),$lte : ISODate("2018-09-26T04:26:13.149Z")},
                            "registerDate" : {$gte : ISODate("2018-09-26T02:26:13.149Z"),$lte : ISODate("2018-09-26T04:26:13.149Z")}
                            }
                    },
                    {$group:{"_id":"$pin"}},
                    {$group:{"_id":null,"count":{$sum:1}}}
                ])
        */
        DBCollection collection = template.getCollection("logLoginInfo");
        BasicDBObject loginValueObj = new BasicDBObject();
        loginValueObj.append("$gte", loginStartTime);
        loginValueObj.append("$lte", loginEndTime);

        BasicDBObject regValueObj = new BasicDBObject();
        regValueObj.append("$gte", regStartTime);
        regValueObj.append("$lte", regEndTime);

        BasicDBObject matchValueObj = new BasicDBObject();
        matchValueObj.append("loginDay", loginValueObj);
        matchValueObj.append("registerDate", regValueObj);

        BasicDBObject matchObj = new BasicDBObject("$match", matchValueObj);

        BasicDBObject id_1 = new BasicDBObject("_id", "$pin");
        BasicDBObject group_1 = new BasicDBObject("$group", id_1);

        BasicDBObject group2ValueObj = new BasicDBObject();
        group2ValueObj.append("_id", null);
        BasicDBObject sumObj = new BasicDBObject("$sum", 1);
        group2ValueObj.append("count", sumObj);
        BasicDBObject group_2 = new BasicDBObject("$group", group2ValueObj);

        List<BasicDBObject> list = new ArrayList<>();
        list.add(matchObj);
        list.add(group_1);
        list.add(group_2);

        //指定输出方式
        AggregationOptions build = AggregationOptions.builder()
                .outputMode(AggregationOptions.OutputMode.CURSOR)
                .build();
        Cursor cursor = collection.aggregate(list, build);
        Long value = 0L;
        DBObject obj = null;
        if (cursor.hasNext()) {
            obj = cursor.next();
            value = Long.parseLong(obj.get("count").toString());
        }
        return value;
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
        query.addCriteria(Criteria.where("pin").is(pin));
        query.with(new Sort(Sort.Direction.DESC, "loginDay"));
        query.limit(1);
        List<LogLoginInfo> list = template.find(query, LogLoginInfo.class);
        if(list == null || list.size() <= 0){
            return null;
        }
        return list.get(0);
    }
}
