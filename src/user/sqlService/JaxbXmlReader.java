package user.sqlService;

import user.dao.UserDao;
import user.sqlService.jaxb.SqlType;
import user.sqlService.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class JaxbXmlReader implements SqlReader {

    private String sqlmapFile;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
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
}
