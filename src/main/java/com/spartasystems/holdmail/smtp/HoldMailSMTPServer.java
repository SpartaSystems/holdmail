package com.spartasystems.holdmail.smtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.subethamail.smtp.server.SMTPServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class HoldMailSMTPServer {

    private SMTPServer smtpServer;

    @Autowired
    private SMTPHandlerFactory handlerFactory;

    @Value("${holdmail.smtp.port:25000}")
    private int smtpServerPort;

    @PostConstruct
    public void startup() {

        smtpServer = new SMTPServer(handlerFactory);
        smtpServer.setSoftwareName(HoldMailSMTPServer.class.getSimpleName() + " SMTP");
        smtpServer.setPort(smtpServerPort);
        smtpServer.start();

    }

    @PreDestroy
    public void shutdown() {
        smtpServer.stop();
    }

}
