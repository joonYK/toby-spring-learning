package user.dao;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CountingDataSource extends SimpleDriverDataSource {

    private int counter = 0;
    private DataSource realDataSource;

    public void setRealDataSource(DataSource realDataSource) {
        this.realDataSource = realDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        this.counter++;
        return realDataSource.getConnection();
    }

    public int getCounter() {
        return counter;
    }
}
