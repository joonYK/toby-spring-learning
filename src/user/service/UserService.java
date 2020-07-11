package user.service;

import org.springframework.transaction.annotation.Transactional;
import user.domain.User;

import java.util.List;

@Transactional //<tx:method name="*" />과 같은 설정 효과
public interface UserService {
    void add(User user);
    void deleteAll();
    void update(User user);
    void upgradeLevels();

    @Transactional(readOnly = true)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();
}
