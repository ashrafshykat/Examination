package observerDecorator;

public class ReminderNotificationDecorator extends NotificationDecorator {

	public ReminderNotificationDecorator(NotificationManager notification) {
		super(notification);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessage() {
		return "ATTENTION: (Reminder) " + notification.getMessage();
	}
}
