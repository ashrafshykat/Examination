package builderExam;

import java.util.List;

public interface ExamBuilder {
    void setTitle(String title);
    void setInstruction(String instructions);
    void setQuestions(List<String> questions);
    void setOptions(List<String> options); // Only applicable for MCQs
    //void setDuration(int duration);
    void setCourseId(int courseId); // Added courseId setter
    Exam getExam();
}
