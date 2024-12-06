package builderExam;

import java.util.List;

public class CQExamBuilder implements ExamBuilder {
    private Exam exam;

    public CQExamBuilder() {
        this.exam = new Exam();
        this.exam.setExamType("CQ");
    }

    @Override
    public void setTitle(String title) {
        exam.setTitle(title);
    }

    @Override
    public void setInstruction(String instructions) {
        exam.setInstruction(instructions);
    }

    @Override
    public void setQuestions(List<String> questions) {
        exam.setQuestions(questions);
    }

    @Override
    public void setOptions(List<String> options) {
        throw new UnsupportedOperationException("CQ exams do not have options.");
    }

	/*
	 * @Override public void setDuration(int duration) { exam.setDuration(duration);
	 * }
	 */

    @Override
    public void setCourseId(int courseId) {
        exam.setCourseId(courseId);
    }

    @Override
    public Exam getExam() {
        return exam;
    }
}
