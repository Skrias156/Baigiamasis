package exam.system.models;

public class ExamTask {

    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correct;

    public ExamTask(String question, String answerA, String answerB, String answerC, String answerD, String correct){
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correct = correct;
    }

    /**Get question */
    public String getQuestion() {
        return question;
    }

    /**Get answer A */
    public String getAnswerA() {
        return answerA;
    }

    /**Get answer B */
    public String getAnswerB() {
        return answerB;
    }

    /**Get answer C */
    public String getAnswerC() {
        return answerC;
    }

    /**Get answer D */
    public String getAnswerD() {
        return answerD;
    }

    /**get correct answer */
    public String getCorrect() {
        return correct;
    }
    
}
