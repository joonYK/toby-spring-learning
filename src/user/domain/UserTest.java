package user.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.domain.Level;
import user.domain.User;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null)
                continue;

            user.setLevel(level);
            user.upgradeLevel();
            Assert.assertEquals(user.getLevel(), level.nextLevel());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null)
                continue;

            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
