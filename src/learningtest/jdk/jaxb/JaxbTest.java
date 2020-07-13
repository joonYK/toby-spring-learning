package learningtest.jdk.jaxb;

import org.junit.Assert;
import org.junit.Test;
import user.sqlService.jaxb.SqlType;
import user.sqlService.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;

public class JaxbTest {

    /**
     * JAXB를 이용해 XML 문서의 내용이 자바오브젝트로 변환되는지 확인.
     */
    @Test
    public void readSqlmap() throws JAXBException, IOException {
        String contextPath = Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath);

        //언마샬러 생성
        Unmarshaller unmarshaller = context.createUnmarshaller();

        //언마샬을 하면 매핑된 오브젝트 트리의 루트인 Sqlmap을 돌려준다.
        Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(getClass().getResource("sqlmap.xml"));

        List<SqlType> sqlList = sqlmap.getSql();

        //List에 담겨 있는 Sql 오브젝트를 가져와 XML문서와 같은 정보를 갖고 있는지 확인.
        Assert.assertEquals(sqlList.size(), 3);
        Assert.assertEquals(sqlList.get(0).getKey(), "add");
        Assert.assertEquals(sqlList.get(0).getValue(), "insert");
        Assert.assertEquals(sqlList.get(1).getKey(), "get");
        Assert.assertEquals(sqlList.get(1).getValue(), "select");
        Assert.assertEquals(sqlList.get(2).getKey(), "delete");
        Assert.assertEquals(sqlList.get(2).getValue(), "delete");

    }
}
