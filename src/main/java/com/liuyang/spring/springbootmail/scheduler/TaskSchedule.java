package com.liuyang.spring.springbootmail.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuyang.spring.springbootmail.entity.WeatherInfoEntity;
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
    private static final String WEATHER_URL = "http://RESOURCEGATHERING/weather/qeuryDongGuan";

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


    @Scheduled(cron = "0 0 7  * * ?")
    public void postMail(){
        StringBuffer stringBuffer = new StringBuffer();

        logger.info("postMail");

        // 查询天气信息
        JSONObject weatherInfoResponse = restTemplate.postForObject(WEATHER_URL, new JSONObject(), JSONObject.class);
        logger.info("weatherInfo:{}", weatherInfoResponse);
        WeatherInfoEntity weatherInfo = JSONObject.toJavaObject(weatherInfoResponse.getJSONObject("data"), WeatherInfoEntity.class);
        logger.info("weatherInfo:{}", weatherInfo);
        stringBuffer.append("<h1>").append("天气信息").append("</h1>");
        stringBuffer.append("<p>").append("地点：").append(weatherInfo.getAddress()).append("</p>");
        stringBuffer.append("<p>").append("日期：").append(weatherInfo.getDate()).append("</p>");
        stringBuffer.append("<p>").append("最低温度：").append(weatherInfo.getMinTemperature()).append("</p>");
        stringBuffer.append("<p>").append("最高温度：").append(weatherInfo.getMaxTemperature()).append("</p>");
        stringBuffer.append("<p>").append("最低湿度：").append(weatherInfo.getMinHumidity()).append("</p>");
        stringBuffer.append("<p>").append("最高湿度：").append(weatherInfo.getMaxHumidity()).append("</p>");
        stringBuffer.append("<p>").append("风力信息：").append(weatherInfo.getWind()).append("</p>");
        stringBuffer.append("<p>").append("天气信息：").append(weatherInfo.getWeather()).append("</p>");
        stringBuffer.append("<p>").append("详细信息：").append(weatherInfo.getDetailsInfo()).append("</p>");
        stringBuffer.append("<p>").append("附加信息：").append(weatherInfo.getAdditionalInfo()).append("</p>");

        String mailContent = stringBuffer.toString();
        logger.info("mainContent {}", mailContent);

        mailServices.PostResourceMail(mailContent,"WeatherInfo");
        logger.info("done");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("",e);
        }


//         查询PG信息 新闻联播and视频字幕信息
        stringBuffer = new StringBuffer();
        JSONObject response = restTemplate.getForObject(RESOURCE_URL, JSONObject.class);
        logger.info("response:{}", response);

        if(! StringUtils.equals(response.getString("code"),"0")){
            return;
        }
        JSONObject data = response.getJSONObject("data");
        for(String title : data.keySet()){
            stringBuffer.append("<h1>").append(title).append("</h1>");

            JSONArray contents = data.getJSONArray(title);
            for(Object content : contents){
                stringBuffer.append("<p>").append(content).append("</p>");
            }
        }



        mailContent = stringBuffer.toString();
        logger.info("mainContent {}", mailContent);

        mailServices.PostResourceMail(mailContent,"");
        logger.info("done");
    }


}
