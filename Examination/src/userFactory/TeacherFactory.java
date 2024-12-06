package userFactory;

public class TeacherFactory implements UserFactory {

	@Override
	public User createUser(String name, String email, String password) {
        return new Teacher(name, email, password);
    }
}
