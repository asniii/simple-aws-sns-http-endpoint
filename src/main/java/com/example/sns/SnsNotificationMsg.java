package com.example.sns;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnsNotificationMsg extends SnsMsg {
    private String subject;
    private String unsubscribeURL;

    @JsonProperty("Subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("UnsubscribeURL")
    public String getUnsubscribeURL() {
        return unsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL) {
        this.unsubscribeURL = unsubscribeURL;
    }
}
