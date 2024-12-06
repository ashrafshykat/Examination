package userFactory;

public class StudentFactory implements UserFactory {
    @Override
    public User createUser(String name, String email, String password) {
        return new Student(name, email, password);
    }
}
