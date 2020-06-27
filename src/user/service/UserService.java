package user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

public class UserService {

    public static final int MIN_LOGOUT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;

    private PlatformTransactionManager transactionManager;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() {
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();

            for (User user : users) {
                if(canUpgradeLevel(user))
                    upgradeLevel(user);
            }

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }

    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return user.getLogin() >= MIN_LOGOUT_FOR_SILVER;
            case SILVER: return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User user) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.ksug.org");
        Session s = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(s);
        try {
            message.setFrom(new InternetAddress("useradmin@ksug.org"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(User user) {
        if (user.getLevel() == null)
            user.setLevel(Level.BASIC);

        userDao.add(user);
    }

}
