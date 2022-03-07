package springbook.user.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

class UserDaoTest {

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("0");
        user.setName("medium");
        user.setPassword("1123");
        userDao.add(user);

        User result = userDao.get("0");
        Assertions.assertThat(result.getId()).isEqualTo("0");
    }

}