package context;

import com.mysql.cj.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.dao.UserDao;
import user.dao.UserDaoJdbc;
import user.service.DummyMailSender;
import user.service.UserService;
import user.service.UserServiceImpl;
import user.service.UserServiceTest;
import user.sqlService.OxmSqlService;
import user.sqlService.SqlService;
import user.sqlService.sqlRegistry.EmbeddedDbSqlRegistry;
import user.sqlService.sqlRegistry.SqlRegistry;

import javax.sql.DataSource;

@ContextConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
public class AppContext {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/testdb?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Autowired
    DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public SqlService sqlService() {
        return new OxmSqlService();
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        return  new EmbeddedDbSqlRegistry();
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("user.sqlService.jaxb");
        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("user/sqlService/sqlRegistry/sqlRegistrySchema.sql")
                .build();
    }


}
