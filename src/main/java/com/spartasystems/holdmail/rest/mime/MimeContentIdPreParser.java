package com.spartasystems.holdmail.rest.mime;

import org.springframework.stereotype.Component;

@Component
public class MimeContentIdPreParser {

    public String replaceWithRestPath(long messageId, String input) {

        return input.replaceAll("cid:", "/rest/messages/" + messageId + "/content/");
    }

}
