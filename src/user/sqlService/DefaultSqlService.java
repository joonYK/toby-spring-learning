package user.sqlService;

import user.sqlService.sqlReader.JaxbXmlReader;
import user.sqlService.sqlRegistry.HashMapSqlRegistry;

public class DefaultSqlService extends BaseSqlService {
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
