package user.sqlService;

import user.sqlService.exception.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
