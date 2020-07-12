package user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import user.domain.Level;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao {

    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
            user.setLevel(Level.valueOf(resultSet.getInt("level")));
            user.setLogin(resultSet.getInt("login"));
            user.setRecommend(resultSet.getInt("recommend"));
            user.setEmail(resultSet.getString("email"));

            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        jdbcTemplate.update(sqlMap.get("add"),
                user.getId(), user.getName(), user.getPassword(), user.getEmail(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend());

    }

    public User get(String id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(sqlMap.get("get"),
                new Object[]{id}, userRowMapper);
    }

    public List<User> getAll() {
        return jdbcTemplate.query(sqlMap.get("getAll"),
                userRowMapper);
    }

    public void deleteAll() {
        jdbcTemplate.update(sqlMap.get("deleteAll"));
    }

    public int getCount() {
        return jdbcTemplate.query(
                connection -> connection.prepareStatement(sqlMap.get("getCount")),
                resultSet -> {
                    resultSet.next();
                    return resultSet.getInt(1);
                });

    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlMap.get("update"),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId()
        );
    }
}
