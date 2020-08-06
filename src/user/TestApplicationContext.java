package user;

import com.mysql.cj.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@ContextConfiguration
@ImportResource("/test-applicationContext.xml")
public class TestApplicationContext {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/testdb?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }



}
