/*******************************************************************************
 * Copyright 2016 - 2018 Sparta Systems, Inc
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
import io.restassured.module.mockmvc.response.MockMvcResponse;
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
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.nullValue;

public class MessageControllerIntegrationTest extends BaseIntegrationTest {

    private static final String ENDPOINT_MESSAGES = "/rest/messages";
    private static final String FROM_EMAIL = "john.doe@senderdomain.org";
    private static final String TEXT_BODY = "whatever";
    private static final String TESTRESOURCE_BASIC_TEXT_AND_HTML = "mails/basic-text-and-html.txt";
    private static final String TESTRESOURCE_I18N_WITH_ATTACH = "mails/i18n-with-attach.txt";


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
    public void shouldAcceptAndListMailsForRandomRecipients() {

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
    public void shouldFindMailsForSpecificRecipient() {

        final String email = generateRecipient("find-by-recipient");

        final String queryURIForRecipient = ENDPOINT_MESSAGES + "?recipient=" + email;

        get(queryURIForRecipient).then().assertThat().body("messages.size()", equalTo(0));

        // send a bunch of mails to random recips, but 3 to our target user too
        smtpClient.sendTextEmail(FROM_EMAIL, email, "mail one", TEXT_BODY);
        sendMailToRandomRecipients(2);
        smtpClient.sendTextEmail(FROM_EMAIL, email, "mail two", TEXT_BODY);
        sendMailToRandomRecipients(3);
        smtpClient.sendTextEmail(FROM_EMAIL, email, "mail three", TEXT_BODY);
        sendMailToRandomRecipients(1);

        get(queryURIForRecipient).then().assertThat()
                .body("messages.size()", equalTo(3))
                // mails are listed most recently accepted first
                .body("messages.get(0).recipients", equalTo(email))
                .body("messages.get(0).subject", equalTo("mail three"))
                .body("messages.get(1).recipients", equalTo(email))
                .body("messages.get(1).subject", equalTo("mail two"))
                .body("messages.get(2).recipients", equalTo(email))
                .body("messages.get(2).subject", equalTo("mail one"));
    }

    @Test
    public void shouldConformToMessageListSchema() {

        // ensure there's at least a couple of messages there
        sendMailToRandomRecipients(3);

        get(ENDPOINT_MESSAGES).then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("jsonschema/message-list.json"));

    }

    @Test
    public void shouldFetchMessageSummary() {

        String recipient = generateRecipient("msg-summary");

        smtpClient.sendResourceEmail(TESTRESOURCE_I18N_WITH_ATTACH, FROM_EMAIL, recipient);

        final int messageId = verifySingleHitAndGetMessageId(recipient);

        String HTML_HEADERTEXT = "<h1>This is the HTML message content!</h1>";
        String PLAIN_HEADERTEXT = "This is the plaintext message content!";
        String NONASCII_SAMPLE = "Russian: Мое судно на воздушной подушке полно угрей";

        // "hello world hello world hello world" in japanese, lengthy on purpose to test multitoken encoding
        String SUBJECT = "こんにちは世界 こんにちは世界 こんにちは世界";

        get(ENDPOINT_MESSAGES + "/" + messageId)
                .then().assertThat()
                .body("messageId", equalTo(messageId))
                .body("identifier", notNullValue())
                .body("subject", equalTo(SUBJECT))
                .body("senderEmail", equalTo(FROM_EMAIL))

                .body("messageHeaders.size()", equalTo(10))
                .body("messageHeaders.User-Agent", startsWith("Mozilla/5.0 (Macintosh;"))
                .body("messageHeaders.To", equalTo(recipient))
                .body("messageHeaders.From", equalTo(FROM_EMAIL))
                .body("messageHeaders.Subject", startsWith("=?UTF-8?B?"))
                .body("messageHeaders.MIME-Version", equalTo("1.0"))
                .body("messageHeaders.Content-Type", startsWith("multipart/mixed;"))

                .body("messageBodyHTML", containsString(HTML_HEADERTEXT))
                .body("messageBodyText", containsString(PLAIN_HEADERTEXT))

                // ensure content is limited to that type's text
                .body("messageBodyHTML", not(containsString(PLAIN_HEADERTEXT)))
                .body("messageBodyText", not(containsString(HTML_HEADERTEXT)))

                // ensure unicode characters made it
                .body("messageBodyHTML", containsString("<li>" + NONASCII_SAMPLE + "</li>"))
                .body("messageBodyText", containsString("* " + NONASCII_SAMPLE))
                // attrib going away in v2: https://github.com/SpartaSystems/holdmail/issues/14

                .body("messageRaw", nullValue())
                .body("messageHasBodyHTML", equalTo(true))
                .body("messageHasBodyText", equalTo(true))

                .body("attachments.size()", equalTo(2))

                .body("attachments.get(0).attachmentId", equalTo("3"))
                .body("attachments.get(0).disposition", equalTo("inline"))
                .body("attachments.get(0).filename", equalTo("att-CN-你好世界.png"))
                .body("attachments.get(0).sha256Sum", equalTo("7188be51b2c8a5deb92ae80e359fb27807fb2f5b69848a92e14644ae5ba57859"))
                .body("attachments.get(0).size", equalTo(1275))
                .body("attachments.get(0).contentType", equalTo("image/png"))

                .body("attachments.get(1).attachmentId", equalTo("4"))
                .body("attachments.get(1).disposition", equalTo("attachment"))
                .body("attachments.get(1).filename", equalTo("att-JP-ありがとうございます.png"))
                .body("attachments.get(1).sha256Sum", equalTo("bcfa8cf2ed578694299bff3ba0a7b0fca9aef58459199fcb0e68b00f936201a6"))
                .body("attachments.get(1).size", equalTo(1247))
                .body("attachments.get(1).contentType", equalTo("image/png"));
    }


    @Test
    public void shouldFetchIndividualContentTypesForMsg() {

        final String CONTENT_PLAIN = "this is the boring plaintext content!";
        final String CONTENT_HTML = "<html><body><h1>This is the exciting html content!</h1></body></html>";

        final String recipient = generateRecipient("html-text-and-raw");
        smtpClient.sendResourceEmail(TESTRESOURCE_BASIC_TEXT_AND_HTML, FROM_EMAIL, recipient);
        final int messageId = verifySingleHitAndGetMessageId(recipient);

        // text
        MockMvcResponse resp = get(ENDPOINT_MESSAGES + "/" + messageId + "/text");
        assertThat(resp.contentType()).isEqualTo("text/plain");
        assertThat(resp.body().asString().trim()).isEqualTo(CONTENT_PLAIN);

        // html
        resp = get(ENDPOINT_MESSAGES + "/" + messageId + "/html");
        assertThat(resp.contentType()).isEqualTo("text/html");
        assertThat(resp.body().asString().trim()).isEqualTo(CONTENT_HTML);

        // raw
        resp = get(ENDPOINT_MESSAGES + "/" + messageId + "/raw");
        assertThat(resp.contentType()).isEqualTo("text/plain");
        // a little difficult to match on whole message (dynamic headers), so just check some relevant pieces
        String rawBody = resp.body().asString();
        assertThat(rawBody).contains("boundary=\"------------13D064742F8BA8DF5152F25F\"");
        assertThat(rawBody).contains("This is a multi-part message in MIME format.");
        assertThat(rawBody).contains(CONTENT_PLAIN);
        assertThat(rawBody).contains(CONTENT_HTML);

    }

    @Test
    public void shouldBeAbleToDeleteAnEmail() {

        String recipient = generateRecipient("msg-summary");

        final String queryURIForRecipient = ENDPOINT_MESSAGES + "?recipient=" + recipient;

        get(queryURIForRecipient).then().assertThat().body("messages.size()", equalTo(0));

        smtpClient.sendTextEmail(FROM_EMAIL, recipient, "mail one", TEXT_BODY);

        final int messageId = verifySingleHitAndGetMessageId(recipient);

        delete(ENDPOINT_MESSAGES + "/" + messageId).then().assertThat().statusCode(200);

        get(queryURIForRecipient).then().assertThat().body("messages.size()", equalTo(0));
    }

    // TODO: integration coverage for
    // TODO: message ContentId fetch
    // TODO: attachment content fetch

    private List<Pair<String, String>> sendMailToRandomRecipients(int numMails) {

        List<Pair<String, String>> recipientAndSubjectList = new ArrayList<>();

        for (int i = 0; i < numMails; i++) {

            String toEmail = "user" + i + "@listRandomTest-" + currentTimeMillis() + ".com";
            String subject = "testing mail to " + toEmail;
            smtpClient.sendTextEmail(FROM_EMAIL, toEmail, subject, TEXT_BODY);

            recipientAndSubjectList.add(new ImmutablePair<>(toEmail, subject));
        }
        return recipientAndSubjectList;
    }

    /**
     * Get the message ID if there was a single message found for the specified recipient,
     * otherwise throw an assertion error.
     */
    private int verifySingleHitAndGetMessageId(String recipient) {

        return get(ENDPOINT_MESSAGES + "?recipient=" + recipient).then()
                .assertThat().body("messages.size()", equalTo(1))
                .extract().path("messages.get(0).messageId");
    }

    private String generateRecipient(String useCaseKey) {

        // include a subadress alias to provide coverage for issue #31
        return String.format("%s_test+alias_%d@testdomain.com", useCaseKey, System.currentTimeMillis());

    }
}
