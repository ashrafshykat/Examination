package course;

public class GetCourses {
	int courseId;
	int teacherId;
	String courseName;
	String courseDescription;
	/*
	 * public void fetchCoursesFromDatabase() { String query =
	 * "SELECT * FROM courses";
	 * 
	 * try (Connection connection = SingletonConnection.getCon(); PreparedStatement
	 * stmt = connection.prepareStatement(query); ResultSet resultSet =
	 * stmt.executeQuery()) {
	 * 
	 * while (resultSet.next()) { courseId = resultSet.getInt("course_id");
	 * teacherId = resultSet.getInt("teacher_id"); courseName =
	 * resultSet.getString("course_name"); courseDescription =
	 * resultSet.getString("course_description"); }
	 * 
	 * } catch (SQLException e) { e.printStackTrace();
	 * System.out.println("Error fetching courses from the database: " +
	 * e.getMessage()); } }
	 */

	public GetCourses(int courseId, int teacherId, String courseName, String courseDescription) {
		super();
		this.courseId = courseId;
		this.teacherId = teacherId;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public int getCourseId() {
		return courseId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

}