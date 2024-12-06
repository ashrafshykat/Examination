package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import singletonConnection.SingletonConnection;
import userFactory.StudentFactory;
import userFactory.TeacherFactory;
import userFactory.User;
import userFactory.UserFactory;

public class LoginController implements Initializable {
	@FXML
	private ComboBox<String> comboBox; // For login: Student or Teacher
	@FXML
	private ComboBox<String> comboBox1; // For sign-up: Student or Teacher

	@FXML
	private TextField loginEmailField;
	@FXML
	private PasswordField loginPasswordField;

	@FXML
	private TextField signupNameField;
	@FXML
	private TextField signupEmailField;
	@FXML
	private PasswordField signupPasswordField;

	public Stage homepage;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		ObservableList<String> list = FXCollections.observableArrayList("Student", "Teacher");
		comboBox.setItems(list);
		comboBox1.setItems(list);
	}

	@FXML
	void SelectLogin(ActionEvent e) {
		// String s = comboBox.getSelectionModel().getSelectedItem().toString();
		// Student.setText(s);

	}

	@FXML
	void SelectSignUp(ActionEvent e) {
		// String s = comboBox1.getSelectionModel().getSelectedItem().toString();
		// Teacher.setText(s);
	}

	@FXML
	public void handleLogin(ActionEvent event) {
		String userType = comboBox.getValue();
		String email = loginEmailField.getText();
		String password = loginPasswordField.getText();

		if (userType == null || email.isEmpty() || password.isEmpty()) {
			showAlert(AlertType.ERROR, "Login Error", "Please fill in all fields.");
			return;
		}

		try (Connection connection = SingletonConnection.getCon()) {
			connection.setAutoCommit(false);

			// Query to authenticate the user based on email and password
			String query = "SELECT * FROM users WHERE email = ? AND password = ? AND role = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, password);
			statement.setString(3, userType.toLowerCase()); // role can be 'student' or 'teacher'

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String name = resultSet.getString("full_name"); // Assuming 'name' column exists
				showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + name);
				int userId = resultSet.getInt("user_id");
				UserSession.getInstance().setUserId(userId);
				
				String q = "SELECT * FROM courses";
				PreparedStatement p = connection.prepareStatement(q);
				ResultSet r = p.executeQuery();
				if (r.next()) {
					int courseId = r.getInt("course_id");
					UserSession.getInstance().setUserId(courseId);
				}
				redirectToHomePage(userType, event);
			} else {
				showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Login Error", "Database connection error.");
		}
	}

	@FXML
	public void handleSignup(ActionEvent event) {
		String userType = comboBox1.getValue();
		String name = signupNameField.getText();
		String email = signupEmailField.getText();
		String password = signupPasswordField.getText();

		if (userType == null || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
			showAlert(AlertType.ERROR, "Signup Error", "Please fill in all fields.");
			return;
		}

		// Use of factory design pattern for creating users
		UserFactory factory = userType.equals("Student") ? new StudentFactory() : new TeacherFactory();
		User user = factory.createUser(name, email, password);

		try (Connection connection = SingletonConnection.getCon()) {
			connection.setAutoCommit(false);
			int userCount = 0;
			String q = "SELECT COUNT(*) FROM users";
			PreparedStatement s = connection.prepareStatement(q);
			ResultSet resultSet = s.executeQuery();
			
			if (resultSet.next()) { // Move cursor to the first row
				userCount = resultSet.getInt(1)+1; // The first column in the result is the count
			}
			String query = "INSERT INTO users (user_id, email, password, role, full_name) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userCount);
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setString(4, userType.toLowerCase());
			statement.setString(5, user.getName());

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				connection.commit();
				showAlert(AlertType.INFORMATION, "Signup Successful", "Account created successfully!");
				redirectToHomePage(userType, event);
			} else {
				connection.rollback();
				showAlert(AlertType.ERROR, "Signup Failed", "Could not create account. Please try again.");
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Signup Error", "Database connection error.");
		}
	}

	private void redirectToHomePage(String userType, ActionEvent event) {
		try {
			String fxmlFile = userType.equals("Student") ? "/application/StudentHomepage.fxml"
					: "/application/TeacherHomepage.fxml";
			URL fxmlLocation = getClass().getResource(fxmlFile);
			if (fxmlLocation == null) {
				throw new RuntimeException("FXML file not found at the specified location.");
			}
			Parent root = FXMLLoader.load(fxmlLocation);
			homepage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			homepage.setTitle(userType + " Homepage");
			homepage.setScene(new Scene(root));
			homepage.show();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Navigation Error", "Failed to load the homepage.");
		}
	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
