package com.liuyang.spring.springbootmail.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuyang.spring.springbootmail.monitor.IotMonitorServices;
import com.liuyang.spring.springbootmail.service.MailServices;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Component
public class TaskSchedule {
    public  static final Logger logger = LoggerFactory.getLogger(TaskSchedule.class);


    private static final String RESOURCE_URL = "http://PG-JPA-DBPAAS-SCHEDULER/pg/resources/query";

    @Resource
    RestTemplate restTemplate;

    @Resource
    MailServices mailServices;

    @Resource
    IotMonitorServices iotMonitorServices;

    @Scheduled(cron="*/10 * *  * * ?")
    public void monitorReport(){
        iotMonitorServices.monitorReport();
        logger.info("executed iotMonitorServices.monitorReport();");
    }


    @Scheduled(cron = "0 30 7 * * ? ")
    public void postMail(){
        logger.info("postMail");
        JSONObject response = restTemplate.getForObject(RESOURCE_URL, JSONObject.class);
        logger.info("response:{}", response);

        if(! StringUtils.equals(response.getString("code"),"0")){
            return;
        }
        JSONObject data = response.getJSONObject("data");

        StringBuffer stringBuffer = new StringBuffer();
        for(String title : data.keySet()){
            stringBuffer.append("<h1>").append(title).append("</h1>");

            JSONArray contents = data.getJSONArray(title);
            for(Object content : contents){
                stringBuffer.append("<p>").append(content).append("</p>");
            }
        }

        String mailContent = stringBuffer.toString();
        logger.info("mainContent {}", mailContent);

        mailServices.PostResourceMail(mailContent);
        logger.info("done");
    }


}
