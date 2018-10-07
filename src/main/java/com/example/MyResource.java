package com.example;

import com.example.sns.SnsConfirmationRequest;
import com.example.sns.SnsMsg;
import com.example.sns.SnsNotificationMsg;
import com.example.sns.SnsUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/myresource")
public class MyResource {

    /**
     * This is the sns-endpoint. You sns should hit this endpoint.
     */
    @Path("/snsmessage/")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String snsEndPoint(String snsRequest, @Context HttpHeaders headers) {

        //First we will check from where we have invoked this http endpoint.
        String messageType = headers.getHeaderString("x-amz-sns-message-type");
        if (messageType == null) {
            System.out.println("This is not hit by sns.");
            return "Plz hit this endpoint from amazon simple notification service.";
        } else {
            SnsMsg snsMsg;
            switch (messageType) {
                case "SubscriptionConfirmation":
                    snsMsg = SnsUtils.create(snsRequest, SnsConfirmationRequest.class);
                    break;
                case "Notification":
                    snsMsg = SnsUtils.create(snsRequest, SnsNotificationMsg.class);
                    break;
                default:
                    System.out.println("Unsupported SNS message type: " + messageType);
                    throw new BadRequestException();
            }

            // This is basically checking the request message is correct or not.
            // The signature is based on SignatureVersion 1.
            // If the sig version is something other than 1,
            // throw an exception.
            if (snsMsg.getSignatureVersion().equals("1")) {
                // Check the signature and throw an exception if the signature verification fails.
                if (SnsUtils.isMessageSignatureValid(snsMsg)) {
                    System.out.println(">>Signature verification succeeded");
                } else {
                    System.out.println(">>Signature verification failed");
                    throw new SecurityException("Signature verification failed.");
                }
            } else {
                System.out.println(">>Unexpected signature version. Unable to verify signature.");
                throw new SecurityException("Unexpected signature version. Unable to verify signature.");
            }

            //Now handling the message
            switch (messageType) {
                //If it is a subscription confirmation request message. Then we hit back at the confirmation URL.
                case "SubscriptionConfirmation":
                    SnsUtils.confirmSubscription((SnsConfirmationRequest) snsMsg);
                    System.out.println("Subscribed to sns topic.");
                    break;
                case "Notification":
                    System.out.println("If u are getting this message at catalina.out. Then everything works fine. Work done.");
                    System.out.println("message::  " + snsMsg.getMessage());
            }

        }
        return "Got it!";
    }

}
