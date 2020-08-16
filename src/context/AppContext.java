package context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.dao.UserDao;
import user.service.DummyMailSender;
import user.service.UserService;
import user.service.UserServiceTest;
import user.sqlService.SqlMapConfig;
import user.sqlService.UserSqlMapConfig;

import javax.sql.DataSource;
import java.sql.Driver;

@ContextConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
@Import({SqlServiceContext.class, AppContext.TestAppContext.class, AppContext.ProductionAppContext.class})
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }

    @Autowired
    Environment env;

    @Value("${db.driverClass}") Class<? extends Driver> driverClass;
    @Value("${db.url}") String url;
    @Value("${db.username}") String username;
    @Value("${db.password}") String password;

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

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
