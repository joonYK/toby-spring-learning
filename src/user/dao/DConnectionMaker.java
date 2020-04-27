package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        //D 사의 독자적인 방법으로 Connection을 생성하는 코드가 들어감.
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/toby_spring_learning?serverTimezone=UTC", "root", "1234");
    }

}
