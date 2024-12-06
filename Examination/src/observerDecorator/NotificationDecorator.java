package observerDecorator;

public abstract class NotificationDecorator extends NotificationManager{
	protected NotificationManager notification;

	public NotificationDecorator(NotificationManager notification) {
        super(notification.getMessage());
        this.notification = notification;
    }
	@Override
    public String getMessage() {
        return notification.getMessage();
    }
}
