package com.example.sns;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnsConfirmationRequest extends SnsMsg {
    private String token;
    private String subscribeURL;

    @JsonProperty("Token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("SubscribeURL")
    public String getSubscribeURL() {
        return subscribeURL;
    }

    public void setSubscribeURL(String subscribeURL) {
        this.subscribeURL = subscribeURL;
    }
}
