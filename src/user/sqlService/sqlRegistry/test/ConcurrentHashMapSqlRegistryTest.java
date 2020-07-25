package user.sqlService.sqlRegistry.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.sqlService.exception.SqlNotFoundException;
import user.sqlService.exception.SqlUpdateFailureException;
import user.sqlService.sqlRegistry.ConcurrentHashMapRegistry;
import user.sqlService.sqlRegistry.UpdatableSqlRegistry;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentHashMapSqlRegistryTest {
    private UpdatableSqlRegistry sqlRegistry;

    @Before
    public void setUp() {
        sqlRegistry = new ConcurrentHashMapRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    @Test
    public void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    private void checkFindResult(String expected1, String expected2, String expected3) {
        Assert.assertEquals(sqlRegistry.findSql("KEY1"), expected1);
        Assert.assertEquals(sqlRegistry.findSql("KEY2"), expected2);
        Assert.assertEquals(sqlRegistry.findSql("KEY3"), expected3);
    }

    @Test(expected = SqlNotFoundException.class)
    public void unknownKey() {
        sqlRegistry.findSql("SQL9999!@#$");
    }

    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void updateMulti() {
        Map<String, String> sqlmap = new HashMap<String, String>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test(expected = SqlUpdateFailureException.class)
    public void updateWithNotExistingKey() {
        sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
    }
}
