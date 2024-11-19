package app.backendclinic.auth;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber; // Asegúrate de usar este import
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TwilioService {

    private final String fromWhatsAppNumber;
    public TwilioService(
        @Value("${twilio.account.sid}") String accountSid,
        @Value("${twilio.auth.token}") String authToken,
        @Value("${twilio.whatsapp.number}") String fromWhatsAppNumber) {
        Twilio.init(accountSid, authToken);
        this.fromWhatsAppNumber = fromWhatsAppNumber;
    }


    public void sendVerificationCode(String toWhatsAppNumber, String code) {
        Message.creator(
            new PhoneNumber("whatsapp:" + toWhatsAppNumber),
            new PhoneNumber(fromWhatsAppNumber),
            "Tu código de verificación es: " + code
        ).create();
    }

    public void sendCustomMessage(String toWhatsAppNumber, String messageContent) {
        Message.creator(
            new PhoneNumber("whatsapp:" + toWhatsAppNumber),
            new PhoneNumber(fromWhatsAppNumber),
            messageContent
        ).create();
    }
    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}