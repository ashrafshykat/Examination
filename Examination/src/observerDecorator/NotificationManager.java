package observerDecorator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.UserSession;
import singletonConnection.SingletonConnection;

public class NotificationManager {
	private List<Observer> observers = new ArrayList<>();
	private String message;
	private int notificationId;
	private String notificationType;

	public NotificationManager(int notificationId, String message, String notificationType) {
		// TODO Auto-generated constructor stub
		this.message = message;
		this.setNotificationId(notificationId);
		this.setNotificationType(notificationType);
	}
	
	public NotificationManager(String message) {
		super();
		this.message = message;
	}

	public NotificationManager() {
		// TODO Auto-generated constructor stub
	}

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	public void notifyObservers(String message) {
		for (Observer observer : observers) {
			observer.update(this.message);
		}
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public void createNotification(String message) {
		notificationType = "basic";
		String sql = "INSERT INTO notifications (notification_id, student_id, message, notification_type) VALUES (?, ?, ?, ?)";

		try (Connection connection = SingletonConnection.getCon();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			String q = "SELECT COUNT(*) FROM notifications";
			PreparedStatement s = connection.prepareStatement(q);
			ResultSet resultSet = s.executeQuery();
			int notificationCount = 0;
			if (resultSet.next()) { // Move cursor to the first row
				notificationCount = resultSet.getInt(1) + 1; // The first column in the result is the count
			}
			int studentId=UserSession.getInstance().getUserId();
			preparedStatement.setInt(1, notificationCount);
			preparedStatement.setInt(2, studentId);
			preparedStatement.setString(3, message);
			preparedStatement.setString(4, notificationType);

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Notification saved successfully.");
				// After saving, notify all observers
				notifyObservers(message);
			} else {
				System.out.println("Failed to save notification.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		notifyObservers(message);
	}

}