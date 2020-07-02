package user.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static user.service.UserServiceImpl.MIN_LOGOUT_FOR_SILVER;
import static user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    //upgradeAllOrNothing 메서드에서 팩토리 빈을 가져오려면 필요.
    @Autowired
    private ApplicationContext context;

    static class TestUserService extends UserServiceImpl {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            //미리 지정한 id값과 일치하는 User면 예외 발생.
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        };
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private MailSender mailSender;

    private List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGOUT_FOR_SILVER-1, 0, "bumgin@email.com"),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGOUT_FOR_SILVER, 0, "joytouch@email.com"),
                new User("erwins", "박범진", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "erwins@email.com"),
                new User("madnite1", "박범진", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "madnite1@email.com"),
                new User("green", "박범진", "p5", Level.GOLD, 100, 100, "green@email.com")
        );
    }

    @Test
    public void upgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        Assert.assertEquals(users.get(1).getLevel(), Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        Assert.assertEquals(users.get(3).getLevel(), Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        Assert.assertEquals(Objects.requireNonNull(mailMessages.get(0).getTo())[0], users.get(1).getEmail());
        Assert.assertEquals(Objects.requireNonNull(mailMessages.get(1).getTo())[0], users.get(3).getEmail());
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

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(this.mailSender);

        //팩토리 빈 가져옴.
        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
        //테스트용 타깃 주입.
        txProxyFactoryBean.setTarget(testUserService);

        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users)
            userDao.add(user);

        try {
            txUserService.upgradeLevels();
            Assert.fail("TestUserServiceException expected");
        } catch (Exception ignored) {}

        //예외가 발생하기 전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었는지 확인.
        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        //level 업그레이드가 된 것인지 아닌지
        if(upgraded)
            Assert.assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        else
            Assert.assertEquals(userUpdate.getLevel(), user.getLevel());

    }
}
