package com.example.sns;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SnsUtils {

    public static SnsMsg create(String message, Class<? extends SnsMsg> messageType) {
        try {
            return (new ObjectMapper()).readValue(message, messageType);
        } catch (IOException e) {
            System.out.println("Unable to convert message to JSON");
            throw new BadRequestException(e);
        }

    }

    public static boolean isMessageSignatureValid(SnsMsg msg) {
        try {
            URL url = new URL(msg.getSigningCertURL());
            InputStream inStream = url.openStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
            inStream.close();

            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(cert.getPublicKey());
            sig.update(getMessageBytesToSign(msg));
            return sig.verify(Base64.decodeBase64(msg.getSignature()));
        }
        catch (Exception e) {
            throw new SecurityException("Verify method failed.", e);
        }
    }

    private static byte [] getMessageBytesToSign (SnsMsg msg) {
        byte [] bytesToSign = null;
        if (msg.getType().equals("Notification"))
            bytesToSign = buildNotificationStringToSign((SnsNotificationMsg)msg).getBytes();
        else if (msg.getType().equals("SubscriptionConfirmation") || msg.getType().equals("UnsubscribeConfirmation"))
            bytesToSign = buildSubscriptionStringToSign((SnsConfirmationRequest)msg).getBytes();
        return bytesToSign;
    }

    //Build the string to sign for Notification messages.
    public static String buildNotificationStringToSign(SnsNotificationMsg msg) {
        String stringToSign = null;

        //Build the string to sign from the values in the message.
        //Name and values separated by newline characters
        //The name value pairs are sorted by name
        //in byte sort order.
        stringToSign = "Message\n";
        stringToSign += msg.getMessage() + "\n";
        stringToSign += "MessageId\n";
        stringToSign += msg.getMessageId() + "\n";
        if (msg.getSubject() != null) {
            stringToSign += "Subject\n";
            stringToSign += msg.getSubject() + "\n";
        }
        stringToSign += "Timestamp\n";
        stringToSign += msg.getTimestamp() + "\n";
        stringToSign += "TopicArn\n";
        stringToSign += msg.getTopicArn() + "\n";
        stringToSign += "Type\n";
        stringToSign += msg.getType() + "\n";
        return stringToSign;
    }

    //Build the string to sign for SubscriptionConfirmation
//and UnsubscribeConfirmation messages.
    public static String buildSubscriptionStringToSign(SnsConfirmationRequest msg) {
        String stringToSign = null;
        //Build the string to sign from the values in the message.
        //Name and values separated by newline characters
        //The name value pairs are sorted by name
        //in byte sort order.
        stringToSign = "Message\n";
        stringToSign += msg.getMessage() + "\n";
        stringToSign += "MessageId\n";
        stringToSign += msg.getMessageId() + "\n";
        stringToSign += "SubscribeURL\n";
        stringToSign += msg.getSubscribeURL() + "\n";
        stringToSign += "Timestamp\n";
        stringToSign += msg.getTimestamp() + "\n";
        stringToSign += "Token\n";
        stringToSign += msg.getToken() + "\n";
        stringToSign += "TopicArn\n";
        stringToSign += msg.getTopicArn() + "\n";
        stringToSign += "Type\n";
        stringToSign += msg.getType() + "\n";
        return stringToSign;
    }

    public static void confirmSubscription(SnsConfirmationRequest confirmation) {
        String subscribeUrl = confirmation.getSubscribeURL();
        try {
            URL url = new URL(subscribeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            String responseMessage = connection.getResponseMessage();
        } catch (IOException e) {
            System.out.println("Unable to confirm subscription");
            throw new BadRequestException(e);
        }
    }
}
