package context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import user.service.DummyMailSender;
import user.service.UserService;
import user.service.UserServiceTest;

@Configuration
public class TestAppContext {

    @Bean
    public MailSender mailSender() {
         return new DummyMailSender();
    }

    @Bean
    public UserService testUserService() {
        return new UserServiceTest.TestUserService();
    }

}
