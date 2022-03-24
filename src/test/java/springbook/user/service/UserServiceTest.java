package springbook.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.toby.UserConfiguration;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserConfiguration.class)
class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserDao userDao;
    List<User> users;

    @Test
    void bean() {
        Assertions.assertThat(this.userService).isNotNull();
    }

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("userA", "박일", "springNo1", Level.BASIC, 49, 0),
                new User("userB", "김이", "springNo2", Level.BASIC, 50, 0),
                new User("userC", "성삼", "springNo3", Level.SILVER, 60, 29),
                new User("userD", "이사", "springNo4", Level.SILVER, 60, 30),
                new User("userE", "오오", "springNo5", Level.GOLD, 100, 100)
        );
    }

    @Test
    void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        Assertions.assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        Assertions.assertThat(userWithoutLevelRead.getLevel()).isEqualTo(userWithoutLevel.getLevel());
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        Assertions.assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }
}