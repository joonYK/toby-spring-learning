package user.sqlService;

import user.dao.UserDao;
import user.sqlService.jaxb.SqlType;
import user.sqlService.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService {

    private String sqlmapFile;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    //읽어온 SQL을 저장해둘 MAP
    private Map<String, String> sqlMap = new HashMap<String, String>();

    @PostConstruct //빈의 초기화 메서드.
    public void loadSql() {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            //프로퍼티 설정을 통해 제공받은 파일 이름을 사용.
            InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql())
                sqlMap.put(sql.getKey(), sql.getValue());

        } catch (JAXBException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if(sql == null)
            throw  new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");

        return sql;
    }
}
