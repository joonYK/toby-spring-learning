package user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMarker implements ConnectionMaker {

    private int counter = 0;
    private ConnectionMaker realConnectionMarker;

    public CountingConnectionMarker(ConnectionMaker realConnectionMarker) {
        this.realConnectionMarker = realConnectionMarker;
    }

    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return realConnectionMarker.makeNewConnection();
    }

    public int getCounter() {
        return counter;
    }
}
