package user.sqlService;

import user.sqlService.sqlRegistry.UpdatableSqlRegistry;

public class SqlAdminService {
    private UpdatableSqlRegistry updatableSqlRegistry;

    public void setUpdatableSqlRegistry(UpdatableSqlRegistry updatableSqlRegistry) {
        this.updatableSqlRegistry = updatableSqlRegistry;
    }
}
