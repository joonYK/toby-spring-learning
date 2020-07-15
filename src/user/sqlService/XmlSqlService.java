package user.sqlService;

import user.dao.UserDao;
import user.sqlService.jaxb.SqlType;
import user.sqlService.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService {

    //읽어온 SQL을 저장해둘 MAP
    private Map<String, String> sqlMap = new HashMap<String, String>();

    //스프링이 오브젝트를 만드는 시점에서 SQL을 읽어오도록 생성자를 이용한다.
    public XmlSqlService() {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream("sqlmap.xml");
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
