package user.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import java.util.Arrays;
import java.util.List;
import static user.service.UserService.MIN_LOGOUT_FOR_SILVER;
import static user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGOUT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGOUT_FOR_SILVER, 0),
                new User("erwins", "박범진", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "박범진", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "박범진", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();

        for (User user : users)
            userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);

    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        //level 업그레이드가 된 것인지 아닌지
        if(upgraded)
            Assert.assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        else
            Assert.assertEquals(userUpdate.getLevel(), user.getLevel());

    }

    @Test
    public void add() {
        userDao.deleteAll();

        //GOLD로 적용되있어서 Level 초기화 안함.
        User userWithLevel = users.get(4);

        //Level null로 BASIC로 초기화 되야함.
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        Assert.assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
        Assert.assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
    }
}
