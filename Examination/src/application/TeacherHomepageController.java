package application;

import course.Course;
import course.GetCourses;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import observerDecorator.NotificationManager;
import observerDecorator.UrgentNotificationDecorator;
import userFactory.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import builderExam.CQExamBuilder;
import builderExam.Exam;
import builderExam.ExamDirector;
import builderExam.MCQExamBuilder;

public class TeacherHomepageController implements Initializable {
	
	@FXML
	private ListView<String> createdExamsList;
	@FXML
	private TableView<?> enrolledStudentsTable;
	@FXML
	private ListView<String> notificationsList;
	@FXML
	private TextField courseNameField;
	@FXML
	private TextArea courseDesriptionField;
	@FXML
	private TableView<GetCourses> courseTable;
	@FXML
	private TableColumn<GetCourses, Integer> courseID;
	@FXML
	private TableColumn<GetCourses, Integer> teacherID;
	@FXML
	private TableColumn<GetCourses, String> courseName;
	@FXML
	private TableColumn<GetCourses, String> courseDescription;
	@FXML
	private TextField examCourseName;
	@FXML
	private TextField examQuestionType;
	@FXML
	TableView<Exam> viewExams;
	@FXML
	TableColumn<GetCourses, String> viewExamID;
	@FXML
	TableColumn<GetCourses, String> viewCourseID;
	@FXML
	TableColumn<GetCourses, String> viewExamTitle;
	@FXML
	TableColumn<GetCourses, String> viewExamType;
	@FXML
	TableColumn<GetCourses, String> viewExamInstruction;

	@FXML
	public Course createCourse(ActionEvent event) {
		String courseName = courseNameField.getText();
		String courseDescription = courseDesriptionField.getText();

		Course course = new Course(courseName, courseDescription);
		DBwork.saveCourseToDatabase(course);
		return course;
	}

	@FXML
	private Exam createMCQExam() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter exam title: ");
		String title = scanner.nextLine();

		System.out.println("Enter exam instructions: ");
		String instructions = scanner.nextLine();

		System.out.println("Enter the course ID: ");
		int courseId = Integer.parseInt(scanner.nextLine());

		/*
		 * System.out.println("Enter duration of the exam (in minutes): "); int duration
		 * = Integer.parseInt(scanner.nextLine());
		 */

		List<String> questions = new ArrayList<>();
		List<String> optionsList = new ArrayList<>();
		List<String> correctAnswers = new ArrayList<>();

		System.out.println("Enter the number of questions: ");
		int numQuestions = Integer.parseInt(scanner.nextLine());

		for (int i = 1; i <= numQuestions; i++) {
			System.out.println("Enter question " + i + ": ");
			String question = scanner.nextLine();
			questions.add(question);

			System.out.println("Enter options for question " + i + " (comma-separated): ");
			String options = scanner.nextLine();
			optionsList.add(options);

			System.out.println("Enter the correct answer: ");
			String correctAnswer = scanner.nextLine();
			correctAnswers.add(correctAnswer);
		}

		// Build the exam using the builder design pattern
		MCQExamBuilder mcqBuilder = new MCQExamBuilder();
		ExamDirector director = new ExamDirector();
		director.setBuilder(mcqBuilder);

		Exam exam = director.constructExam(title, instructions, questions, optionsList, courseId);

		DBwork.saveExamToDatabase(exam);

		// Save questions to the exam_questions table
		try {
			DBwork.saveQuestionsToDatabase(exam.getExamId(), questions, "MCQ", optionsList, correctAnswers);
			System.out.println("Questions saved successfully.");
		} catch (SQLException e) {
			System.err.println("Error while saving questions: " + e.getMessage());
		}
		NotificationManager notificationManager = new NotificationManager();
		
		String userId= UserSession.getInstance().getUserName();
		String email = UserSession.getInstance().getEmail();
		String password = UserSession.getInstance().getPassword();
		
		Student Student = new Student(userId, email, password);
		
		notificationManager.addObserver(Student);
		notificationManager.createNotification("MCQ Exam has been created.");
		NotificationManager notification = new NotificationManager();
		NotificationManager notify = new UrgentNotificationDecorator(notification);
		notificationManager.createNotification(notify.getMessage());
		
		return exam;
	}

	@FXML
	public Exam createCQExam() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter exam title: ");
		String title = scanner.nextLine();

		System.out.println("Enter exam instructions: ");
		String instructions = scanner.nextLine();

		System.out.println("Enter the course ID: ");
		int courseId = Integer.parseInt(scanner.nextLine());

		List<String> questions = new ArrayList<>();
		List<String> correctAnswers = new ArrayList<>();

		System.out.println("Enter the number of questions: ");
		int numQuestions = Integer.parseInt(scanner.nextLine());

		for (int i = 1; i <= numQuestions; i++) {
			System.out.println("Enter question " + i + ": ");
			String question = scanner.nextLine();
			questions.add(question);

			System.out.println("Enter the correct answer for question " + i + ": ");
			String correctAnswer = scanner.nextLine();
			correctAnswers.add(correctAnswer);
		}

		// Build the exam using the builder design pattern
		CQExamBuilder cqBuilder = new CQExamBuilder();
		ExamDirector director = new ExamDirector();
		director.setBuilder(cqBuilder);

		Exam exam = director.constructExam(title, instructions, questions, null, courseId);

		// Save the exam to the database
		DBwork.saveExamToDatabase(exam);

		// Save questions to the exam_questions table
		try {
			DBwork.saveQuestionsToDatabase(exam.getExamId(), questions, "CQ", null, correctAnswers);
			System.out.println("Questions saved successfully.");
		} catch (SQLException e) {
			System.err.println("Error while saving questions: " + e.getMessage());
		}
		
		NotificationManager notificationManager = new NotificationManager();
		String userId= UserSession.getInstance().getUserName();
		String email = UserSession.getInstance().getEmail();
		String password = UserSession.getInstance().getPassword();
		
		Student Student = new Student(userId, email, password);
		
		notificationManager.addObserver(Student);
		notificationManager.createNotification("CQ Exam has been created.");
		NotificationManager notification = new NotificationManager();
		NotificationManager notify = new UrgentNotificationDecorator(notification);
		notificationManager.createNotification(notify.getMessage());
		
		return exam;
	}
	@FXML
	private void createExam(ActionEvent e) {
		String Qtype = examQuestionType.getText();
		
		if(Qtype.equals("CQ")) {
			createCQExam();
		}
		else if(Qtype.equals("MCQ")) {
			
			createMCQExam();
		}
		else {
			showAlert(AlertType.ERROR, "Creating Question Error", "Invalid Question Type");
		}
	}
	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		List<GetCourses> courses = DBwork.fetchCoursesFromDatabase();
		List<Exam> exams = DBwork.fetchExamInfoFromDatabase();

		courseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
		teacherID.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
		courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		courseDescription.setCellValueFactory(new PropertyValueFactory<>("courseDescription"));
		
		viewExamID.setCellValueFactory(new PropertyValueFactory<>("examId"));
		viewCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
		viewExamType.setCellValueFactory(new PropertyValueFactory<>("examType"));
		viewExamTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		viewExamInstruction.setCellValueFactory(new PropertyValueFactory<>("instructions"));
		
		ObservableList<GetCourses> list = FXCollections.observableArrayList(courses);
		courseTable.setItems(list);
		ObservableList<Exam> list2 =FXCollections.observableArrayList(exams);
		viewExams.setItems(list2);
	}
}
