package observerDecorator;

public class UrgentNotificationDecorator extends NotificationDecorator {

	public UrgentNotificationDecorator(NotificationManager notification) {
		super(notification);
		// TODO Auto-generated constructor stub
	}

	    @Override
	    public String getMessage() {
	        return "URGENT: " + notification.getMessage();
	    }
	}
