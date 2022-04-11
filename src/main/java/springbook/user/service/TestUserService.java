package springbook.user.service;

import springbook.user.domain.User;

public class TestUserService extends UserServiceImpl {
    private String id = "userD";

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }
}
