package com.example.sns;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnsMsg {
    private String type;
    private String messageId;
    private String topicArn;
    private String message;
    private String timestamp;
    private String signatureVersion;
    private String signature;
    private String signingCertURL;

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("MessageId")
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @JsonProperty("TopicArn")
    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("Timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("SignatureVersion")
    public String getSignatureVersion() {
        return signatureVersion;
    }

    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    @JsonProperty("Signature")
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonProperty("SigningCertURL")
    public String getSigningCertURL() {
        return signingCertURL;
    }

    public void setSigningCertURL(String signingCertURL) {
        this.signingCertURL = signingCertURL;
    }
}
