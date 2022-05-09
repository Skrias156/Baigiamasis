package exam.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class StudentService {

    private ScannerService scannerService;
    private DatabaseService databaseService;
    private FileService fileService;

    public StudentService(ScannerService scannerService) {
        this.scannerService = scannerService;
        this.databaseService = new DatabaseService();
        this.fileService = new FileService();
    }

    /** Content */
    public void runContent() throws FileNotFoundException, IOException, ParseException, java.text.ParseException {
        while (true) {
            int action = this.scannerService.scanNumber("\nDestytojai - 1, Atsijungti - 2. Nurodykite skaiciu: ", 2);

            // Teachers
            if (action == 1) {
                JSONArray teachers = this.databaseService.getTeachers();

                // Print teachers
                printTeachers(teachers);

                // Select teacher
                int actionTeacher = this.scannerService
                        .scanNumber("Pasirinkite destytoja. Nurodykite destytojo numeri: ", teachers.size());

                // Get selected teacher
                JSONObject teacher = getTeacher(teachers, actionTeacher);

                // Get teacher tasks
                JSONArray teacherTasks = this.databaseService
                        .getTasks((String) teacher.get(DatabaseService.FIELD_USER_NICKNAME));

                // Print tasks titles
                printTasksTitles(teacherTasks);

                // while(true){
                // Select task
                int actionTask = this.scannerService.scanNumber("Noredami spresti testa. Nurodykite testo numeri: ",
                        teacherTasks.size());
                JSONObject task = getTeacherTask(teacherTasks, actionTask);

                // Solve the test
                toTask(task);

            }

            // Logout
            if (action == 2)
                break;

        }
    }

    /** Solve the task */
    public void toTask(JSONObject task) throws IOException, ParseException, java.text.ParseException {
        JSONObject student = new JSONObject();

        int answersCorrect = 0;
        int answersWrong = 0;

        System.out.println("\nNetrukus pradesite spresti testa.");
        System.out.println("Nurodykite savo duomenys: ");

        String userName = this.scannerService.scanText("Vardas: ");
        String userSurname = this.scannerService.scanText("Pavarde: ");

        student.put(DatabaseService.FIELD_STUDENT_NAME, userName);
        student.put(DatabaseService.FIELD_STUDENT_SURNAME, userSurname);

        // Current date time
        String dateTimeFormated = new DateTimeService().now();

        student.put(DatabaseService.FIELD_STUDENT_DATE, dateTimeFormated);

        String testTitle = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
        System.out.println(testTitle);


        // Check if student passed exam
        String examTaskDateTime = this.databaseService.getExamTaskDate(testTitle, userName, userSurname);
        String examTaskCorrectPercents = this.databaseService.getExamTaskCorrectPercents(testTitle, userName, userSurname);
            
        // 
        if (examTaskCorrectPercents != null && Integer.parseInt(examTaskCorrectPercents) < 50 && examTaskDateTime != null) {
            int dateCompareResult = new DateTimeService().compare(dateTimeFormated, examTaskDateTime,
                    DateTimeService.MILLISECONDS_24_HOURS);

            
            if (dateCompareResult <= 0) {
                System.out.println("Testa atlikote "+examTaskDateTime+". Perlaikyti galesite tik po 48 val.");
                return;
            }

        }


        student.put(DatabaseService.FIELD_STUDENT_TEST_TITLE, testTitle);

        JSONArray test = new JSONArray();
        JSONArray examTasks = (JSONArray) task.get(DatabaseService.FIELD_TASK_EXAMS);
        Iterator iterator = examTasks.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            index++;
            JSONObject testTask = (JSONObject) iterator.next();

            // Question
            String testQuestion = (String) testTask.get(DatabaseService.FIELD_TASK_EXAM_QUESTION);
            String testAnswerA = (String) testTask.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_A);
            String testAnswerB = (String) testTask.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_B);
            String testAnswerC = (String) testTask.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_C);
            String testAnswerD = (String) testTask.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_D);

            System.out.println("\n" + index + ". " + testQuestion);
            System.out.println("a) " + testAnswerA);
            System.out.println("b) " + testAnswerB);
            System.out.println("c) " + testAnswerC);
            System.out.println("d) " + testAnswerD);

            JSONObject testPart = new JSONObject();
            testPart.put(DatabaseService.FIELD_TASK_TEST_QUESTION, testQuestion);
            testPart.put(DatabaseService.FIELD_TASK_TEST_ANSWER_A, testAnswerA);
            testPart.put(DatabaseService.FIELD_TASK_TEST_ANSWER_B, testAnswerB);
            testPart.put(DatabaseService.FIELD_TASK_TEST_ANSWER_C, testAnswerC);
            testPart.put(DatabaseService.FIELD_TASK_TEST_ANSWER_D, testAnswerD);

            String actionLetter = this.scannerService.scanText("Nurodykite atsakymo raide: ").toLowerCase();
            // testAnswewrs.add("Studento atsakymas - "+actionLetter+"\n");
            testPart.put(DatabaseService.FIELD_TASK_TEST_ANSWER_STUDENT, actionLetter);

            String correctLetter = (String) testTask.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_CORRECT);
            // testAnswewrs.add("Teisingas atsakymas - "+correctLetter+"\n");
            testPart.put(DatabaseService.FIELD_TASK_TEST_ANSWER_CORRECT, correctLetter);

            if (correctLetter.trim().toLowerCase().equals(actionLetter.trim().toLowerCase())) {
                answersCorrect++;
            } else {
                answersWrong++;
            }

            test.add(testPart);

        }

        student.put(DatabaseService.FIELD_STUDENT_TEST, test);
        student.put(DatabaseService.FIELD_TASK_TEST_ANSWER_CORRECTS, answersCorrect);
        student.put(DatabaseService.FIELD_TASK_TEST_ANSWER_WRONGS, answersWrong);

        int userRezult = (int) ((float) answersCorrect / (answersCorrect + answersWrong) * 100);
        student.put(DatabaseService.FIELD_TASK_TEST_ANSWER_CORRECTS_PERCENTS, userRezult);

        // Student file path
        String userFilePath = this.fileService.getDirectoryExamPath() + "\\"
                + dateTimeFormated.replace(" ", "_").replaceAll(":", "-") + ".json";
        // Create student test file
        new File(userFilePath).createNewFile();
        // Write test data to file
        this.fileService.writeData(userFilePath, student);
         

        JSONObject studentResult = new JSONObject();
        studentResult.put(DatabaseService.FIELD_STUDENT_NAME, userName);
        studentResult.put(DatabaseService.FIELD_STUDENT_SURNAME, userSurname);
        studentResult.put(DatabaseService.FIELD_STUDENT_DATE, dateTimeFormated);
        studentResult.put(DatabaseService.FIELD_STUDENT_TEST_TITLE, testTitle);
        studentResult.put(DatabaseService.FIELD_TASK_TEST_FILE_NAME, dateTimeFormated.replace(" ", "_").replaceAll(":", "-") + ".json");
        studentResult.put(DatabaseService.FIELD_TASK_TEST_ANSWER_CORRECTS, answersCorrect);
        studentResult.put(DatabaseService.FIELD_TASK_TEST_ANSWER_WRONGS, answersWrong);
        studentResult.put(DatabaseService.FIELD_TASK_TEST_ANSWER_CORRECTS_PERCENTS, userRezult);

        //Delete student task data
        this.databaseService.deleteResultTask(testTitle, userName, userSurname);

        //Write student task data
        this.databaseService.addResultTask(testTitle, studentResult);

        System.out.println("\nTesto teisingi atsakymai - " + answersCorrect + ", klaidingi - " + answersWrong);
        System.out.println("Teisingai atsakyta - " + userRezult + " proc.");
        System.out.println("Testas issaugotas.");

    }

    /** Get teacher */
    public JSONObject getTeacherTask(JSONArray teacherTasks, int indexTask) {
        int index = 0;
        Iterator iterator = teacherTasks.iterator();

        while (iterator.hasNext()) {
            index++;
            JSONObject task = (JSONObject) iterator.next();
            if (index == indexTask)
                return task;
        }

        return null;
    }

    /** Get teacher */
    public JSONObject getTeacher(JSONArray teachers, int indexTeacher) {
        int index = 0;
        Iterator iterator = teachers.iterator();

        while (iterator.hasNext()) {
            index++;
            JSONObject teacher = (JSONObject) iterator.next();
            if (index == indexTeacher)
                return teacher;
        }

        return null;
    }

    /** Print exams titles */
    public void printTasksTitles(JSONArray teacherTasks) {
        Iterator iterator = teacherTasks.iterator();

        // Print teachers
        int index = 0;
        while (iterator.hasNext()) {
            if (index == 0)
                System.out.println("\nTestai:");
            index++;
            JSONObject task = (JSONObject) iterator.next();

            System.out.println(index + ". Testas - " + task.get(DatabaseService.FIELD_TASK_TITLE) + ".");
        }

    }

    /** Print teachers */
    public void printTeachers(JSONArray teachers) {
        Iterator iterator = teachers.iterator();

        // Print teachers
        int index = 0;
        while (iterator.hasNext()) {
            if (index == 0)
                System.out.println("\nDestytojai:");
            index++;
            JSONObject teacher = (JSONObject) iterator.next();

            System.out.println(index + ". Destytojas - " + teacher.get(DatabaseService.FIELD_USER_NICKNAME));
        }

    }

}
