/*******************************************************************************
 * Copyright 2016 - 2017 Sparta Systems, Inc
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

package com.spartasystems.holdmail.integration;

import com.spartasystems.holdmail.util.TestMailClient;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static java.lang.System.currentTimeMillis;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;

public class MessageControllerIntegrationTest extends BaseIntegrationTest {

    public static final String ENDPOINT_MESSAGES = "/rest/messages";
    public static final String FROM_EMAIL        = "john.doe@senderdomain.org";
    public static final String TEXT_BODY         = "whatever";

    @Value("${holdmail.smtp.port:25000}")
    private int smtpServerPort;

    @Autowired
    private WebApplicationContext webAppCtx;

    private TestMailClient smtpClient;

    @Before
    public void setUp() throws Exception {

        super.setUp();

        smtpClient = new TestMailClient(smtpServerPort, "localhost");
        RestAssuredMockMvc.webAppContextSetup(webAppCtx);
    }

    @Test
    public void shouldAcceptAndListMailsForRandomRecipients() throws Exception {

        int countBeforeStart = get(ENDPOINT_MESSAGES).then().extract().path("messages.size()");

        final int numMailsToSend = 5;
        List<Pair<String, String>> expectedMessages = sendMailToRandomRecipients(numMailsToSend);

        ValidatableMockMvcResponse resp = get(ENDPOINT_MESSAGES)
                .then().statusCode(200).body("messages.size()", equalTo(countBeforeStart + numMailsToSend));

        for (int i = 0; i < numMailsToSend; i++) {

            // mails are listed most recently accepted first
            Pair<String, String> recipAndSubject = expectedMessages.get(numMailsToSend - i - 1);

            resp.body("messages.get(" + i + ").senderEmail", equalTo(FROM_EMAIL))
                .body("messages.get(" + i + ").recipients", equalTo(recipAndSubject.getLeft()))
                .body("messages.get(" + i + ").subject", equalTo(recipAndSubject.getRight()));
        }

    }

    @Test
    public void shouldAcceptAndListMailsByRecipientEmail() throws Exception {

        final String EMAIL = "listTestRecipient" + currentTimeMillis() + "@example.org";

        final String queryURIForRecipient = ENDPOINT_MESSAGES + "?recipient=" + EMAIL;

        get(queryURIForRecipient).then().assertThat().body("messages.size()", equalTo(0));

        // send a bunch of mails to random recips, but 3 to our target user too
        smtpClient.sendTextEmail(FROM_EMAIL, EMAIL, "mail one", TEXT_BODY);
        sendMailToRandomRecipients(2);
        smtpClient.sendTextEmail(FROM_EMAIL, EMAIL, "mail two", TEXT_BODY);
        sendMailToRandomRecipients(3);
        smtpClient.sendTextEmail(FROM_EMAIL, EMAIL, "mail three", TEXT_BODY);
        sendMailToRandomRecipients(1);

        get(queryURIForRecipient).then().assertThat()
                                 .body("messages.size()", equalTo(3))
                                 // mails are listed most recently accepted first
                                 .body("messages.get(0).recipients", equalTo(EMAIL))
                                 .body("messages.get(0).subject", equalTo("mail three"))
                                 .body("messages.get(1).recipients", equalTo(EMAIL))
                                 .body("messages.get(1).subject", equalTo("mail two"))
                                 .body("messages.get(2).recipients", equalTo(EMAIL))
                                 .body("messages.get(2).subject", equalTo("mail one"));
    }

    @Test
    public void shouldConformToMessageListSchema() throws Exception {

        // ensure there's at least a couple of messages there
        sendMailToRandomRecipients(3);

        get(ENDPOINT_MESSAGES).then()
                              .assertThat()
                              .contentType(ContentType.JSON)
                              .statusCode(200)
                              .body(matchesJsonSchemaInClasspath("jsonschema/message-list.json"));

    }



    @Test
    public void shouldFetchMessageSummary() throws Exception {

        long startTime = currentTimeMillis();
        final String EMAIL = "messageSummaryTest" + startTime + "@example.org";

        smtpClient.sendResourceEmail("mails/multipart-sample-1.txt", FROM_EMAIL, EMAIL, "multipart mail");

        // should only be one message for this recipient, get the ID from the list API
        int messageId = get(ENDPOINT_MESSAGES + "?recipient=" + EMAIL).then()
                                                                      .assertThat().body("messages.size()", equalTo(1))
                                                                      .extract().path("messages.get(0).messageId");

        get(ENDPOINT_MESSAGES + "/" + messageId)
                .then().assertThat()
                .body("messageId", equalTo(messageId))
                .body("identifier", notNullValue())
                .body("subject", equalTo("multipart mail"))
                .body("senderEmail", equalTo(FROM_EMAIL))
                .body("messageHeaders.size()", equalTo(9))
                .body("messageHeaders.User-Agent", startsWith("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11;"))
                .body("messageHeaders.To", equalTo(EMAIL))
                .body("messageHeaders.From", equalTo(FROM_EMAIL))
                .body("messageHeaders.Subject", equalTo("multipart mail"))
                .body("messageHeaders.MIME-Version", equalTo("1.0"))
                .body("messageHeaders.Content-Type", startsWith("multipart/mixed;"))
                .body("messageBodyHTML", containsString("<h1>This is a header</h1>"))
                .body("messageBodyText", containsString("This is the text content."))
                .body("messageHasBodyHTML", equalTo(true))
                .body("messageHasBodyText", equalTo(true))
                .body("attachments.size()", equalTo(2))
                .body("attachments.get(0).sequence", equalTo(3))
                .body("attachments.get(0).disposition", equalTo("inline"))
                .body("attachments.get(0).filename", equalTo("seal of approval.jpg"))
                .body("attachments.get(0).size", equalTo(43549))
                .body("attachments.get(0).contentType", equalTo("image/jpeg"))
                .body("attachments.get(1).sequence", equalTo(4))
                .body("attachments.get(1).disposition", equalTo("attachment"))
                .body("attachments.get(1).filename", equalTo("cheerio-challenge.jpg"))
                .body("attachments.get(1).size", equalTo(21561))
                .body("attachments.get(1).contentType", equalTo("image/jpeg"));


    }

    @Test
    public void shouldFetchMessageSummaryForAliasEmails() throws Exception {

        long startTime = currentTimeMillis();
        final String email = "testrecipient+alias@testdomain.com";

        smtpClient.sendResourceEmail("mails/multipart-sample-2.txt", FROM_EMAIL, email, "multipart mail");

        // should only be one message for this recipient, get the ID from the list API


        int messageId = get(ENDPOINT_MESSAGES + "?recipient=" + email).then()
                .assertThat().body("messages.size()", equalTo(1))
                .extract().path("messages.get(0).messageId");

        get(ENDPOINT_MESSAGES + "/" + messageId)
                .then().assertThat()
                .body("messageId", equalTo(messageId))
                .body("identifier", notNullValue())
                .body("subject", equalTo("multipart mail"))
                .body("senderEmail", equalTo(FROM_EMAIL))
                .body("messageHeaders.size()", equalTo(9))
                .body("messageHeaders.User-Agent", startsWith("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11;"))
                .body("messageHeaders.To", equalTo(email))
                .body("messageHeaders.From", equalTo(FROM_EMAIL));

    }

    // TODO: integration coverage for
    // TODO: message TEXT fetch
    // TODO: message HTML fetch
    // TODO: message RAW fetch
    // TODO: message ContentId fetch
    // TODO: attachment content fetch

    protected List<Pair<String, String>> sendMailToRandomRecipients(int numMails) {

        List<Pair<String, String>> recipientAndSubjectList = new ArrayList<>();

        for (int i = 0; i < numMails; i++) {

            String toEmail = "user" + i + "@listRandomTest-" + currentTimeMillis() + ".com";
            String subject = "testing mail to " + toEmail;
            smtpClient.sendTextEmail(FROM_EMAIL, toEmail, subject, TEXT_BODY);

            recipientAndSubjectList.add(new ImmutablePair<>(toEmail, subject));
        }
        return recipientAndSubjectList;
    }

}
