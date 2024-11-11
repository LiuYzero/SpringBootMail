package com.liuyang.spring.springbootmail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service
public class MailServices {

    private static final Logger logger =  LoggerFactory.getLogger(MailServices.class);

    @Autowired
    JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    String mailUserName;


    @Value("${spring.mail.markup}")
    String personal;

    @Value("${spring.mail.sendTo}")
    String sendTo;

    @Value("${spring.mail.sendSubject}")
    String sendSubject;

    public void test(){

        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();

        // 创建 MimeMessageHelper
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, false);
            // 发件人邮箱和名称
            helper.setFrom(mailUserName, personal);
            // 收件人邮箱
            helper.setTo(sendTo);
            // 邮件标题
            helper.setSubject(sendSubject);
            // 邮件正文，第二个参数表示是否是HTML正文
            helper.setText("Hello <strong> World</strong>！", true);

            // 发送
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("邮件发送失败", e);
        }


    }


    public void PostResourceMail(String mailContent){

        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();

        // 创建 MimeMessageHelper
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, false);
            // 发件人邮箱和名称
            helper.setFrom(mailUserName, personal);
            // 收件人邮箱
            helper.setTo(sendTo);
            // 邮件标题
            helper.setSubject(sendSubject);
            // 邮件正文，第二个参数表示是否是HTML正文
            helper.setText(mailContent, true);

            // 发送
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("邮件发送失败", e);
        }


    }
}
