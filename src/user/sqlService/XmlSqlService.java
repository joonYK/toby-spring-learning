package user.sqlService;

import user.dao.UserDao;
import user.sqlService.exception.SqlNotFoundException;
import user.sqlService.exception.SqlRetrievalFailureException;
import user.sqlService.jaxb.SqlType;
import user.sqlService.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    //sqlMapFile은 SqlReader 구현의 일부가 된다. 따라서 SQlReader 구현 메서드를 통하지 않고는 접근하면 안 된다.
    private String sqlmapFile;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    //읽어온 SQL을 저장해둘 MAP
    private Map<String, String> sqlMap = new HashMap<String, String>();

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    /**
     * loadSql()에 있던 코드를 SqlReader 메서드로 가져온다.
     * 초기화를 위해 무엇을 할 것인가와 SQL을 어떻게 읽는지를 분리한다.
     */
    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            //프로퍼티 설정을 통해 제공받은 파일 이름을 사용.
            InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);


            for (SqlType sql : sqlmap.getSql())
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());

        } catch (JAXBException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null)
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        else
            return sql;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

}
