package course;

import java.util.List;
import builderExam.Exam;

public class Course {
    private int id;
    private int teacher_id;
    private String courseName;
    private String courseDescription;
    private List<Exam> exams;
    
    

    public Course(String courseName, String courseDescription) {
		super();
		this.id = id;
		this.teacher_id = teacher_id;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
}
