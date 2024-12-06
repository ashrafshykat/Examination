package application;

import builderExam.Exam;
import course.Course;
import course.GetCourses;
import course.Question;
import singletonConnection.SingletonConnection;
import observerDecorator.NotificationManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBwork {
	public static void saveExamToDatabase(Exam exam) {
	    String insertExamSQL = "INSERT INTO exams (exam_id, course_id, exam_type, title, instructions) VALUES (?, ?, ?, ?, ?)";
	    try (Connection connection = SingletonConnection.getCon();
	         PreparedStatement preparedStatement = connection.prepareStatement(insertExamSQL)) {

	        // Generating the exam ID based on the current count in the exams table
	        String q = "SELECT COUNT(*) FROM exams";
	        PreparedStatement countStmt = connection.prepareStatement(q);
	        ResultSet resultSet = countStmt.executeQuery();
	        int examId = 0; // Default exam_id if the table is empty
	        if (resultSet.next()) {
	            examId = resultSet.getInt(1) + 1; // Increment count for the new exam_id
	        }

	        preparedStatement.setInt(1, examId); // Set the exam_id
	        preparedStatement.setInt(2, exam.getCourseId()); // Set the course_id
	        preparedStatement.setString(3, exam.getExamType()); // Set the exam_type (MCQ/CQ)
	        preparedStatement.setString(4, exam.getTitle()); // Set the title
	        preparedStatement.setString(5, exam.getInstruction()); // Set the instructions

	        // Execute the SQL statement
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Exam saved to the database successfully.");
	        } else {
	            System.out.println("Failed to save the exam.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error while saving exam to the database: " + e.getMessage());
	    }
	}

	public static void saveQuestionsToDatabase(int examId, List<String> questions, String questionType,
			List<String> options, List<String> correctAnswers) throws SQLException {
		Connection connection = SingletonConnection.getCon();
		String insertQuestionSQL = "INSERT INTO exam_questions (exam_id, question_text, question_type, options, correct_answer) VALUES (?, ?, ?, ?, ?)";
		int examId1 = 0; // Default exam_id if the table is empty
		String q = "SELECT COUNT(*) FROM exams";
        PreparedStatement countStmt = connection.prepareStatement(q);
        ResultSet resultSet = countStmt.executeQuery();
        
        if (resultSet.next()) {
            examId1 = resultSet.getInt(1) + 1; // Increment count for the new exam_id
        }
		try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuestionSQL)) {
			for (int i = 0; i < questions.size(); i++) {
				preparedStatement.setInt(1, examId1);
				preparedStatement.setString(2, questions.get(i));
				preparedStatement.setString(3, questionType);
				preparedStatement.setString(4, options != null ? options.get(i) : null); // Null for CQ
				preparedStatement.setString(5, correctAnswers.get(i));

				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			System.out.println("Questions saved to the database successfully.");
		}
	}

	public static void saveCourseToDatabase(Course course) {
		try (Connection connection = SingletonConnection.getCon();) {

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String query = "INSERT INTO courses (course_id, teacher_id, course_name, description) VALUES (?, ?, ?, ?)";

		try (Connection connection = SingletonConnection.getCon();
				PreparedStatement stmt = connection.prepareStatement(query)) {
			String q = "SELECT COUNT(*) FROM courses";
			PreparedStatement s = connection.prepareStatement(q);
			ResultSet resultSet = s.executeQuery();
			int courseCount = 0;
			if (resultSet.next()) { // Move cursor to the first row
				courseCount = resultSet.getInt(1)+1; // The first column in the result is the count
			}
			int teacherID = UserSession.getInstance().getUserId();
			stmt.setInt(1, courseCount);
			stmt.setInt(2, teacherID);
			stmt.setString(3, course.getCourseName());
			stmt.setString(4, course.getCourseDescription());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Course created and saved to the database successfully.");
			} else {
				System.out.println("Failed to save the course.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error saving course to the database: " + e.getMessage());
		}
	}

	public static List<GetCourses> fetchCoursesFromDatabase() {
		List<GetCourses> courses = new ArrayList<>();
		
		String query = "SELECT * FROM courses";

		try (Connection connection = SingletonConnection.getCon();
				PreparedStatement stmt = connection.prepareStatement(query);
				ResultSet resultSet = stmt.executeQuery()) {

			while (resultSet.next()) {
				int courseId = resultSet.getInt("course_id");
				int teacherId = resultSet.getInt("teacher_id");
				String courseName = resultSet.getString("course_name");
				String courseDescription = resultSet.getString("description");
				
				GetCourses course = new GetCourses(courseId, teacherId, courseName, courseDescription);
				courses.add(course);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error fetching courses from the database: " + e.getMessage());
		}

		return courses;
	}
	public static List<Exam> fetchExamInfoFromDatabase() {
	    List<Exam> exams = new ArrayList<>();
	    String query = "SELECT * FROM exams";

	    try (Connection connection = SingletonConnection.getCon();
	         PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet resultSet = stmt.executeQuery()) {

	        while (resultSet.next()) {
	            int examId = resultSet.getInt("exam_id");
	            int courseId = resultSet.getInt("course_id");
	            String examType = resultSet.getString("exam_type");
	            String title = resultSet.getString("title");
	            String instructions = resultSet.getString("instructions");

	            Exam exam = new Exam(examId, courseId, examType, title, instructions);
	            exams.add(exam);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error fetching exams from the database: " + e.getMessage());
	    }
	    return exams;
	}
	public static List<String> fetchQuestionsFromDatabase(int examId) {
	    List<String> questions = new ArrayList<>();
	    String query = "SELECT question_text FROM exam_questions WHERE exam_id = ?";

	    try (Connection connection = SingletonConnection.getCon();
	         PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, examId);
	        ResultSet resultSet = stmt.executeQuery();

	        while (resultSet.next()) {
	            String questionText = resultSet.getString("question_text");
	            questions.add(questionText);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error fetching questions from the database: " + e.getMessage());
	    }

	    return questions;
	}
	public static List<NotificationManager> fetchNotificationsFromDatabase() {
	    List<NotificationManager> notifications = new ArrayList<>();
	    String query = "SELECT * FROM notifications";

	    try (Connection connection = SingletonConnection.getCon();
	         PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet resultSet = stmt.executeQuery()) {

	        while (resultSet.next()) {
	            //int notificationId = resultSet.getInt("notification_id");
	            int studentId = resultSet.getInt("student_id");
	            String message = resultSet.getString("message");
	            String notificationType = resultSet.getString("notification_type");

	            NotificationManager notification = new NotificationManager(studentId, message, notificationType);
	            notifications.add(notification);
	            //notification.createNotification(studentId, message, notificationType);
	            
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error fetching notifications from the database: " + e.getMessage());
	    }

	    return notifications;
	}
	public static Exam getExamId(int examId) {
        Exam exam = null;
        String query = "SELECT * FROM exams WHERE exam_id = ?";
        
        try (Connection connection = SingletonConnection.getCon();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, examId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String instructions = rs.getString("instructions");
                int courseId = rs.getInt("course_id");
                exam = new Exam(examId, courseId, null, title, instructions);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exam;
    }
	public static List<Question> getQuestionsByExamId(int examId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM exam_questions WHERE exam_id = ?";
        
        try (Connection connection = SingletonConnection.getCon();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, examId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String questionText = rs.getString("question_text");
                String options = rs.getString("options"); // Assuming options are stored as a comma-separated string
                String[] optionsArray = options.split(","); // Split into an array
                questions.add(new Question(questionText, List.of(optionsArray))); // Using List.of to create a List
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
	/*
	 * public static void saveUserAnswers(int examId, List<String> answers) throws
	 * SQLException { String query =
	 * "INSERT INTO user_answers (exam_id, answer) VALUES (?, ?)";
	 * 
	 * try (Connection connection = SingletonConnection.getCon(); PreparedStatement
	 * pstmt = connection.prepareStatement(query)) { for (String answer : answers) {
	 * pstmt.setInt(1, examId); pstmt.setString(2, answer); pstmt.addBatch(); }
	 * pstmt.executeBatch(); } }
	 */
	public static Course getCourseById(int courseId) {
        Course course = null;
        String query = "SELECT * FROM courses WHERE course_id = ?";
        
        try (Connection connection = SingletonConnection.getCon();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String courseName = rs.getString("course_name");
                String courseDesc = rs.getString("description");
                course = new Course(courseName, courseDesc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }
	public static void enrollInCourse(int userId, int courseId) throws SQLException {
        String query = "INSERT INTO course_enrollments (enrollment_id, student_id, course_id) VALUES (?, ?, ?)";
        
        try (Connection connection = SingletonConnection.getCon();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
        	String q = "SELECT COUNT(*) FROM course_enrollments";
			PreparedStatement s = connection.prepareStatement(q);
			ResultSet resultSet = s.executeQuery();
			int courseCount = 0;
			if (resultSet.next()) { 
				courseCount = resultSet.getInt(1)+1; 
			}
			pstmt.setInt(1, courseCount);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, courseId);
            pstmt.executeUpdate();
        }
    }
}
