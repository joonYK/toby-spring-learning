package user.sqlService.sqlReader;

import user.sqlService.sqlRegistry.SqlRegistry;

public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}
