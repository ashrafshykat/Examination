package userFactory;

import observerDecorator.Observer;

public class Student extends User implements Observer {
	
	public Student(String name, String email, String password) {
		super(name, email, password);
	}
	
	@Override
	public void update(String message) {
		System.out.println("You recieved a notification:" + message);
	}
}
