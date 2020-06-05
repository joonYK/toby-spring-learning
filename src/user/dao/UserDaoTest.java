package user.dao;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.domain.User;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("gyumee", "박성철", "springno1");
        user2 = new User("leegw700", "이길원", "springno2");
        user3 = new User("bumgin", "박범진", "springno3");
    }

    @Test
    public void addAndGet() throws SQLException {
        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        dao.add(user2);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(2));

        User userget1 = dao.get(user1.getId());
        Assert.assertThat(user1.getName(), CoreMatchers.is(userget1.getName()));
        Assert.assertThat(user1.getPassword(), CoreMatchers.is(userget1.getPassword()));

        User userget2 = dao.get(user2.getId());
        Assert.assertThat(user2.getName(), CoreMatchers.is(userget2.getName()));
        Assert.assertThat(user2.getPassword(), CoreMatchers.is(userget2.getPassword()));

    }

    @Test
    public void count() throws SQLException {
        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(1));

        dao.add(user2);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(2));

        dao.add(user3);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.get("unknown_id");
    }

}
