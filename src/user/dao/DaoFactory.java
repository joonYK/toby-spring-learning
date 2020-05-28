package user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMarker());
    }

    @Bean
    public ConnectionMaker connectionMarker() {
        return new CountingConnectionMarker(realConnectionMarker());
    }

    @Bean
    public ConnectionMaker realConnectionMarker() {
        return new DConnectionMaker();
    }
}
