package user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws SQLException {
        jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException, EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setId(resultSet.getString("id"));
                        user.setName(resultSet.getString("name"));
                        user.setPassword(resultSet.getString("password"));

                        return user;
                    }
                });
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                return resultSet.getInt(1);
            }
        });

    }

}
