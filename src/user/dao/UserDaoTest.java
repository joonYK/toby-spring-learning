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

import java.util.List;

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
    public void addAndGet() {
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
    public void count() {
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
    public void getUserFailure() {
        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        Assert.assertThat(users0.size(), CoreMatchers.is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        Assert.assertThat(users1.size(), CoreMatchers.is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        Assert.assertThat(users2.size(), CoreMatchers.is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        Assert.assertThat(users3.size(), CoreMatchers.is(3));
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));

    }

    private void checkSameUser(User user1, User user2) {
        Assert.assertThat(user1.getId(), CoreMatchers.is(user2.getId()));
        Assert.assertThat(user1.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(user1.getPassword(), CoreMatchers.is(user2.getPassword()));
    }


}
