package builderExam;

import java.util.List;

public class Exam {
	private int examId;
	private String title;
	private String instructions;
	private String examType; // "MCQ" or "CQ"
	private List<String> questions;
	private List<String> options; // For MCQs only
	private int duration; // in Minutes
	private int courseId; // This links to the course

	public Exam(int examId, int courseId, String examType, String title, String instructions) {
		// TODO Auto-generated constructor stub
		super();
		this.examId = examId;
		this.courseId = courseId;
		this.examType = examType;
		this.title = title;
		this.setInstruction(instructions);
		
	}

	public Exam() {
		// TODO Auto-generated constructor stub
	}

	public int getExamId() {
		return this.examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstruction() {
		return instructions;
	}

	public void setInstruction(String instructions) {
		this.instructions = instructions;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public List<String> getQuestions() {
		return questions;
	}

	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "Exam{" + "title='" + title + '\'' + ", description='" + instructions + '\'' + ", type='" + examType + '\''
				+ ", questions=" + questions + ", options=" + options + ", duration=" + duration + ", courseId="
				+ courseId + '}';
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
}
