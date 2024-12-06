package course;
import java.util.List;

public class Question {
    private String questionText; // The text of the question
    private List<String> options; // List of options for the question
    private String questionType;
    private String correctAnswer;
    public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	// Constructor
    public Question(String questionText, List<String> options) {
        this.questionText = questionText;
        this.options = options;
    }
    public Question(String questionText) {
		super();
		this.questionText = questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	
    public String getQuestionText() {
        return questionText;
    }
    public String getQuestionType() {
		return questionType;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public List<String> getOptions() {
        return options;
    }
    @Override
    public String toString() {
        return "Question: " + questionText + "\nOptions: " + String.join(", ", options);
    }
	
	public String getCorrectAnswer() {
		// TODO Auto-generated method stub
		return correctAnswer;
	}

}