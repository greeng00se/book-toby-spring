package springbook.toby;

import springbook.user.dao.connection.ConnectionMaker;
import springbook.user.dao.connection.DConnectionMaker;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
    }
}
