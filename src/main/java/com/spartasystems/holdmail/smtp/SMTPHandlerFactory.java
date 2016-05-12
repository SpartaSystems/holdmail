package com.spartasystems.holdmail.smtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.subethamail.smtp.MessageContext;

@Component
public class SMTPHandlerFactory implements org.subethamail.smtp.MessageHandlerFactory {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public SMTPHandler create(MessageContext ctx) {

        SMTPHandler smtpHandler = new SMTPHandler(ctx);
        beanFactory.autowireBean(smtpHandler);
        return smtpHandler;
    }
}
