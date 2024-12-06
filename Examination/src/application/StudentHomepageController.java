package application;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import builderExam.Exam;
import course.GetCourses;
import course.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import observerDecorator.NotificationManager;

public class StudentHomepageController implements Initializable {
	@FXML
	TableView<GetCourses> availableCourses;
	@FXML
	TableColumn<GetCourses, String> availableCourseID;
	@FXML
	TableColumn<GetCourses, String> availableCourseName;
	@FXML
	TableColumn<GetCourses, String> availableCourseDescription;

	@FXML
	TextField textFieldCourseID;
	@FXML
	TextField textFieldStudentID;
	@FXML
	TextField textFieldExamID;
	
	@FXML
	TableView<NotificationManager> notification;
	@FXML
	TableColumn<NotificationManager, String> notificationType;
	@FXML
	TableColumn<NotificationManager, String> notificationMessage;
	
	@FXML
	TableView<Exam> availableExamCourses;
	@FXML
	TableColumn<GetCourses, String> availableExamExamID;
	@FXML
	TableColumn<GetCourses, String> availableExamCourseID;
	@FXML
	TableColumn<GetCourses, String> availableExamCourseTitle;
	@FXML
	TableColumn<GetCourses, String> availableExamCourseType;
	@FXML
	TableColumn<GetCourses, String> availableExamCourseInstruction;

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		List<GetCourses> courses = DBwork.fetchCoursesFromDatabase();
		List<Exam> exam = DBwork.fetchExamInfoFromDatabase();
		List<NotificationManager> notify = DBwork.fetchNotificationsFromDatabase();
		availableCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
		// availableCourseDescription.setCellValueFactory(new
		// PropertyValueFactory<>("teacherId"));
		availableCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		availableCourseDescription.setCellValueFactory(new PropertyValueFactory<>("courseDescription"));

		availableExamExamID.setCellValueFactory(new PropertyValueFactory<>("examId"));
		availableExamCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
		availableExamCourseType.setCellValueFactory(new PropertyValueFactory<>("examType"));
		availableExamCourseTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		availableExamCourseInstruction.setCellValueFactory(new PropertyValueFactory<>("instructions"));
		
		notificationType.setCellValueFactory(new PropertyValueFactory<>("notificationType"));
		notificationMessage.setCellValueFactory(new PropertyValueFactory<>("message"));

		ObservableList<GetCourses> courseList = FXCollections.observableArrayList(courses);
		ObservableList<Exam> examList = FXCollections.observableArrayList(exam);
		ObservableList<NotificationManager> notif = FXCollections.observableArrayList(notify);
		
		notification.setItems(notif);
		availableCourses.setItems(courseList);
		availableExamCourses.setItems(examList);
	}

	@FXML
	private void enrollInACourse(ActionEvent e) throws SQLException {
		int studentID = Integer.parseInt(textFieldStudentID.getText());
		int courseID = Integer.parseInt(textFieldCourseID.getText());

		DBwork.enrollInCourse(studentID, courseID);
		NotificationManager notification = new NotificationManager("Congratulations! You've enrolled in" + courseID);

		System.out.println("Enrolling Successful");
	}

	@FXML
	private void giveExam(ActionEvent e) {
		int examID = Integer.parseInt(textFieldExamID.getText());
		Scanner scanner = new Scanner(System.in);
		List<Question> questions = DBwork.getQuestionsByExamId(examID);

		if (questions.isEmpty()) {
			System.out.println("No questions found for this exam.");
			return;
		}

		int totalQuestions = questions.size();
		int score = 0;

		System.out.println("Starting the exam...");
		for (int i = 0; i < totalQuestions; i++) {
			Question q = questions.get(i);
			System.out.println("\nQuestion " + (i + 1) + ": " + q.getQuestionText());

			if (q.getQuestionType().equals("mcq")) {
				// Handle MCQ
				List<String> options = q.getOptions();
				for (int j = 0; j < options.size(); j++) {
					System.out.println((j + 1) + ". " + options.get(j));
				}

				System.out.print("Enter your answer (1-" + options.size() + "): ");
				int answerIndex = scanner.nextInt();

				if (options.get(answerIndex).equals(q.getCorrectAnswer())) {
					score++;
				}
			} else if (q.getQuestionType().equals("cq")) {

				System.out.print("Enter your response: ");
				String response = scanner.nextLine();
				System.out.println("Your answer has been recorded: " + response);
			}
		}

		System.out.println("\nExam finished!");
		System.out.println("Your score (MCQs only): " + score + "/" + totalQuestions);

	}
}
