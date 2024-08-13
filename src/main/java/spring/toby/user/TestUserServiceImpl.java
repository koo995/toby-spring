package spring.toby.user;

public class TestUserServiceImpl extends UserServiceImpl{
    private String id = "madnite1";

    public TestUserServiceImpl(UserDao userDao) {
        super(userDao);
    }

    protected void upgradeLevel(User user) {
        if (user.getId().equals(id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }

    public static class TestUserServiceException extends RuntimeException {
    }
}
