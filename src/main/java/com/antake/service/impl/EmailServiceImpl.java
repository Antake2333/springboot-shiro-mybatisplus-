package com.antake.service.impl;

import com.antake.pojo.Email;
import com.antake.mapper.EmailMapper;
import com.antake.service.EmailService;
import com.antake.utils.DataResult;
import com.antake.vo.req.EmailVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Session;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antake
 * @since 2020-05-19
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements EmailService {
    @Autowired
    private EmailMapper emailMapper;
    @Override
    @Transactional(readOnly = true)
    public Email find() {
      return emailMapper.selectById(1);
    }

    @Override
    public DataResult update(Email email) {
        int result = emailMapper.updateById(email.setId(1));
        if (result>0){
            return DataResult.getSuccessResult();
        }else {
            return DataResult.getFailResult();
        }
    }

    @Override
    @Async
    @Transactional(readOnly = true)
    public void send(EmailVO emailVO,Email email) {
        if (email!=null){
            String[] tos = emailVO.getTos();
            if (tos!=null && tos.length>0){
                //设置邮箱配置
                JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
                javaMailSender.setDefaultEncoding("UTF-8");
                javaMailSender.setHost(email.getHost());
                javaMailSender.setPort(email.getPort());
                javaMailSender.setProtocol("smtp");
                javaMailSender.setPassword(email.getPass());
                javaMailSender.setUsername(email.getUser());
                Properties mailProp = new Properties();
                mailProp.setProperty("mail.smtp.user",email.getUsername());
                mailProp.setProperty("mail.smtp.timeout","10000");
                mailProp.setProperty("mail.smtp.starttls.enable",email.getSslEnable().toString());
                mailProp.setProperty("mail.smtp.starttls.required",email.getSslEnable().toString());
                mailProp.setProperty("mail.smtp.ssl.enable",email.getSslEnable().toString());
                mailProp.setProperty("mail.smtp.auth","true");
                javaMailSender.setJavaMailProperties(mailProp);
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(email.getUser());
                simpleMailMessage.setSentDate(new Date());
                simpleMailMessage.setSubject(emailVO.getSubject());
                String[] tos1 = emailVO.getTos();
                String[] strings=new String[tos1.length+1];
                for (int i = 0; i < tos1.length; i++) {
                    strings[i]=tos1[i];
                }
                strings[strings.length-1]=email.getUser();
                simpleMailMessage.setTo(strings);
                simpleMailMessage.setText(emailVO.getContent());
                //异步发送邮件
                try {
                    javaMailSender.send(simpleMailMessage);
                    log.info("sendEmail...邮件发送成功");
                } catch (MailException e) {
                    log.error("sendEmail...error{}",e);
                }
            }
        }
    }
}
