package userFactory;

public interface UserFactory {
    User createUser(String name, String email, String password);
}
