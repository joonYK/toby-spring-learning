package user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

    private String host;

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

    }
}
