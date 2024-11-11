package com.liuyang.spring.springbootmail;

import com.liuyang.spring.springbootmail.service.MailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class SpringbootmailApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringbootmailApplication.class, args);

	}

}
