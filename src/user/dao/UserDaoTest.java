package user.dao;

import context.AppContext;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.domain.Level;
import user.domain.User;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppContext.class)
@ActiveProfiles("test")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0, "gyumee@email.com");
        user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10, "leegw700@email.com");
        user3 = new User("bumgin", "박범진", "springno3", Level.GOLD, 100, 40, "bumgin@email.com");
    }

    @Test
    public void addAndGet() {
        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        dao.add(user2);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(user1, userget1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(user2, userget2);
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

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);

        //다른 데이터도 같이 업데이트 되지 않았는지 확인하기 위해 테스트 보완.
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2) {
        Assert.assertThat(user1.getId(), CoreMatchers.is(user2.getId()));
        Assert.assertThat(user1.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(user1.getPassword(), CoreMatchers.is(user2.getPassword()));
        Assert.assertThat(user1.getLevel(), CoreMatchers.is(user2.getLevel()));
        Assert.assertThat(user1.getLogin(), CoreMatchers.is(user2.getLogin()));
        Assert.assertThat(user1.getRecommend(), CoreMatchers.is(user2.getRecommend()));
        Assert.assertThat(user1.getEmail(), CoreMatchers.is(user2.getEmail()));
    }

}
