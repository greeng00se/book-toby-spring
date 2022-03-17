package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
class UserDaoTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao userDao;

    @BeforeEach
    void beforeEach() {
        System.out.println("this.context = " + this.context);
        System.out.println("this = " + this);
    }

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setId("0");
        user.setName("medium");
        user.setPassword("1123");
        userDao.add(user);

        User result = userDao.get("0");
        assertThat(result.getId()).isEqualTo("0");
    }

    @Test
    void getUserFailure() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        assertThatThrownBy(() -> userDao.get("unknown_id"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void getAll() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setId("userA");
        user2.setId("userB");
        user3.setId("userC");
        user1.setName("userA");
        user2.setName("userB");
        user3.setName("userC");
        user1.setPassword("userA");
        user2.setPassword("userB");
        user3.setPassword("userC");

        List<User> users0 = userDao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}