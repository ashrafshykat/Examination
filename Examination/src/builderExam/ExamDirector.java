package builderExam;

import java.util.List;

public class ExamDirector {
    private ExamBuilder builder;

    public void setBuilder(ExamBuilder builder) {
        this.builder = builder;
    }

    public Exam constructExam(String title, String instructions, List<String> questions, List<String> options, int courseId) {
        builder.setTitle(title);
        builder.setInstruction(instructions);
        builder.setQuestions(questions);
        if (builder instanceof MCQExamBuilder) {
            builder.setOptions(options);
        }
        //builder.setDuration(duration);
        builder.setCourseId(courseId);
        return builder.getExam();
    }
}
