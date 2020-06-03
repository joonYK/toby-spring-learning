package user.dao;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(1));

        User user2 = dao.get(user.getId());

        Assert.assertThat(user2.getName(), CoreMatchers.is(user.getName()));
        Assert.assertThat(user2.getPassword(), CoreMatchers.is(user.getPassword()));
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("gyumee", "박성철", "springno1");
        User user2 = new User("leegw700", "이길원", "springno2");
        User user3 = new User("bumgin", "박범진", "springno3");

        dao.deleteAll();
        Assert.assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(1));

        dao.add(user2);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(2));

        dao.add(user3);
        Assert.assertThat(dao.getCount(), CoreMatchers.is(3));
    }

}
