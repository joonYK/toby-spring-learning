package user.sqlService;

import issuetracker.sqlService.SqlUpdateFailureException;
import issuetracker.sqlService.UpdatableSqlRegistry;
import user.sqlService.exception.SqlNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MyUpdatableSqlRegistry implements UpdatableSqlRegistry {

    //읽어온 SQL을 저장해둘 MAP
    private Map<String, String> sqlMap = new HashMap<String, String>();

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        sqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        this.sqlMap = sqlmap;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null)
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        else
            return sql;
    }
}
