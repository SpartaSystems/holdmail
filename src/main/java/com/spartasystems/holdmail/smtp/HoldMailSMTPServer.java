/*******************************************************************************
 * Copyright 2016 Sparta Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

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
