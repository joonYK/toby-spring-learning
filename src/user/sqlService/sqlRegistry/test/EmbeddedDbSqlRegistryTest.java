package user.sqlService.sqlRegistry.test;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import user.sqlService.exception.SqlUpdateFailureException;
import user.sqlService.sqlRegistry.EmbeddedDbSqlRegistry;
import user.sqlService.sqlRegistry.UpdatableSqlRegistry;

import java.util.HashMap;
import java.util.Map;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("user/sqlService/sqlRegistry/sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void transactionUpdate() {
        /*
         * 초기 상태를 확인한다. 이미 슈퍼클래스의 다른 테스트 메서드에서 확인하긴 했지만,
         * 트낼잭션 롤백 후와 비교돼서 트낼잭션이 적용됐다는것을 확인하기 위해.
         */
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");

        //롤백이 일어나는지 확인하기위해, 두 번째 SQL의 키를 존재하지 않는 것으로 지정한다.
        sqlmap.put("KEY9999!@#$", "Modified9999");

        try {
            sqlRegistry.updateSql(sqlmap);
        } catch (SqlUpdateFailureException e) {}

        //롤백되었는지 다시 확인.
        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
