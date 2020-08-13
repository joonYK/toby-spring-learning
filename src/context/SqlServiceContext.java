package context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import user.sqlService.OxmSqlService;
import user.sqlService.SqlMapConfig;
import user.sqlService.SqlService;
import user.sqlService.UserSqlMapConfig;
import user.sqlService.sqlRegistry.EmbeddedDbSqlRegistry;
import user.sqlService.sqlRegistry.SqlRegistry;

import javax.sql.DataSource;

@Configuration
public class SqlServiceContext {

    @Autowired
    private SqlMapConfig sqlMapConfig;

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setSqlmap(sqlMapConfig.getSqlMapResource());
        return sqlService;
    }

    @Bean
    public SqlMapConfig sqlMapConfig() {
        return new UserSqlMapConfig();
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
