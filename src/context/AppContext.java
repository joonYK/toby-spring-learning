package context;

import com.mysql.cj.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.service.DummyMailSender;
import user.service.UserService;
import user.service.UserServiceTest;

import javax.sql.DataSource;

@ContextConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
@Import({SqlServiceContext.class, AppContext.TestAppContext.class, AppContext.ProductionAppContext.class})
public class AppContext {

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/testdb?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Configuration
    @Profile("production")
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("mail.mycompany.com");
            return mailSender;
        }
    }

    @Configuration
    @Profile("test")
    public static class TestAppContext {

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }

        @Bean
        public UserService testUserService() {
            return new UserServiceTest.TestUserService();
        }
    }

}
