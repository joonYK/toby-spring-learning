package user.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import user.TestApplicationContext;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static user.service.UserServiceImpl.MIN_LOGOUT_FOR_SILVER;
import static user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "/test-applicationContext.xml")
@ContextConfiguration(classes= TestApplicationContext.class)
public class UserServiceTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 포인트컷의 클래스 필터에 선정되도록 이름을 ~ServiceImpl로 변경.
     */
    public static class TestUserService extends UserServiceImpl {
        //테스트 픽스처의 uers(3)의 id값을 고정시킴.
        private String id = "madnite1";

        @Override
        protected void upgradeLevel(User user) {
            //미리 지정한 id값과 일치하는 User면 예외 발생.
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        @Override
        public List<User> getAll() {
            for(User user : super.getAll()) {
                //읽기 전용 메서드에 강제로 쓰기를 시도해서 예외가 발생하는 확인.
                super.update(user);
            }
            return null;
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @Autowired
    private UserService userService;

    /**
     * 같은 타입의 빈이 두 개 존재하기 때문에 필드 이름을 기준으로 주입될 빈이 결정.
     * 자동 프록시 생성기에 의해 트랜잭션 부가기능이 testUserService 빈에 적용됐는지를 확인하는 것이 목적이다.
     */
    @Autowired
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

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
    public void upgradeAllOrNothing() {
        userDao.deleteAll();
        for (User user : users)
            userDao.add(user);

        try {
            testUserService.upgradeLevels();
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

    @Test
    public void advisorAutoProxyCreator() {
        Assert.assertThat(testUserService, CoreMatchers.instanceOf(java.lang.reflect.Proxy.class));
    }

    @Test(expected = TransientDataAccessResourceException.class)
    public void readOnlyTransactionAttribute() {
        testUserService.getAll();
    }

    /**
     * 테스트 메서드를 하나의 트랜잭션 그룹으로 묶음으로써,
     * DB 작업이 포함되는 테스트를 원하는 대로 제어하면서 효과적인 테스틀 작성 가능.
     */
    @Test(expected = TransientDataAccessResourceException.class)
    public void transactionSync() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(true);

        /*
            트랜잭션 매니저에게 트랜잭션을 요청한다. 기존에 시작된 트랜잭션이 없으니 새로운 트랜잭션을 시작시키고
            트랜잭션 정보를 돌려준다. 동시에 만들어진 트랜잭션을 다른 곳에서도 사용할 수 있도록 동기화한다.
         */
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        userDao.deleteAll();

        userService.add(users.get(0));
        userService.add(users.get(1));

        transactionManager.commit(transactionStatus);
    }

    /**
     * 테스트에도 @Transactional 적용할 수 있다.
     * 테스트 클래스에 적용하면 모든 테스트 메서드에 공통적으로 적용 가능.
     */
    @Test(expected = TransientDataAccessResourceException.class)
    @Transactional(readOnly = true)
    public void transactionAnnotationSync() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    /**
     * 테스트에서 @Transactional 사용 시, 기본적으로 테스트 수행 후에 Rollback 된다.
     * commit하려면 @Rollback(value = false) 속성을 주면 된다.
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void transactionSyncRollbackFalse() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }
}
