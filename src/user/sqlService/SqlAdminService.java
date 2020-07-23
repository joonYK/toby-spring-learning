package user.sqlService;

import issuetracker.sqlService.UpdatableSqlRegistry;

public class SqlAdminService {
    private UpdatableSqlRegistry updatableSqlRegistry;

    public void setUpdatableSqlRegistry(UpdatableSqlRegistry updatableSqlRegistry) {
        this.updatableSqlRegistry = updatableSqlRegistry;
    }
}
