package user.sqlService.sqlRegistry.test;

import user.sqlService.sqlRegistry.ConcurrentHashMapRegistry;
import user.sqlService.sqlRegistry.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapRegistry();
    }
}
