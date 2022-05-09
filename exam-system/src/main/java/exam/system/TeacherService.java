package exam.system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import exam.system.models.ExamTask;
import exam.system.models.Task;
import exam.system.models.User;

public class TeacherService {

    private User user;
    private ScannerService scannerService;
    private DatabaseService databaseService;
    private FileService fileService;

    public TeacherService(User user, ScannerService scannerService) {
        this.user = user;
        this.scannerService = scannerService;
        this.databaseService = new DatabaseService();
        this.fileService = new FileService();
    }

    /** */
    public void runContent() throws FileNotFoundException, IOException, ParseException {
        while (true) {
            int action = this.scannerService.scanNumber(
                    "\nNauja uzduotis - 1, Uzduotys - 2, Naikinti uzduoti - 3, Egzaminu rezultatai - 4, Atsijungti - 5. Nurodykite skaiciu: ",
                    5);

            // New task
            if (action == 1)
                newTask();

            // Tasks
            if (action == 2)
                getTasks();

            // Delete task
            if (action == 3)
                deleteTask();

            // Exams rezults
            if (action == 4)
                System.out.println("Egzaminu failus rasite - " + this.fileService.getDirectoryExamPath());

            // Logout
            if (action == 5)
                break;

        }
    }

    /** Delete task */
    public void deleteTask() throws FileNotFoundException, IOException, ParseException {
        String taskTitle = this.scannerService
                .scanText("Nurodykite uzduoties pavadinima kuria norite sunaikinti: ");

        JSONObject task = this.databaseService.getTask(this.user.getNickname(), taskTitle);
        if (task == null)
            System.out.println("Tokios uzduoties nera.");
        if (task != null) {
            this.databaseService.deleteTask(this.user.getNickname(), taskTitle);
            System.out.println("Uzduotis panaikinta.");
        }
    }

    /** Get tasks*/
    public void getTasks() throws IOException, ParseException {
        JSONArray tasks = this.databaseService.getTasks(this.user.getNickname());

        Iterator<JSONObject> iterator = tasks.iterator();
        int index = 0;
        // Print tasks
        while (iterator.hasNext()) {
            if (index == 0)
                System.out.println("\nUzduotys:");
            JSONObject task = (JSONObject) iterator.next();
            String taskTitle = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
            index++;
            System.out.println(index + ". " + taskTitle);
        }

        // No tasks
        if (index == 0) {
            System.out.println("Uzduociu nera.");
        }

        // It is tasks
        if (index > 0) {
            int actionTask = this.scannerService.scanNumber("Prasome pasirinkti uzduoti. Nurodykite skaiciu: ",
                    index);

            // Find task
            index = 0;
            iterator = tasks.iterator();
            while (iterator.hasNext()) {
                index++;
                JSONObject task = (JSONObject) iterator.next();

                // Print task data to console
                if (index == actionTask) {
                    String taskTitle = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
                    System.out.println("\nUzduoties pavadinimas - " + taskTitle + ".");

                    JSONArray taskList = (JSONArray) task.get(DatabaseService.FIELD_TASK_EXAMS);

                    Iterator iterator1 = taskList.iterator();

                    int index1 = 0;
                    while (iterator1.hasNext()) {
                        index1++;
                        JSONObject jsonObject = (JSONObject) iterator1.next();

                        System.out
                                .println(index1 + ". "
                                        + jsonObject.get(DatabaseService.FIELD_TASK_EXAM_QUESTION));
                        System.out.println("a): " + jsonObject.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_A));
                        System.out.println("b): " + jsonObject.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_B));
                        System.out.println("c): " + jsonObject.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_C));
                        System.out.println("d): " + jsonObject.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_D));
                        System.out.println("Teisingas atsakymas: "
                                + jsonObject.get(DatabaseService.FIELD_TASK_EXAM_ANSWER_CORRECT));

                    }

                    if (index1 == 0)
                        System.out.println("Uzduotyje klausimu nera.");

                    while (true) {
                        // Actions
                        int actionQuestion = this.scannerService.scanNumber(
                                "\nPrideti klausima - 1, Naikinti klausima - 2, Atgal - 3. Pasirinkite skaiciu: ",
                                3);

                        // Add question and answers
                        if (actionQuestion == 1) {
                            String question = this.scannerService.scanLine("Klausimas: ");
                            String answerA = this.scannerService.scanLine("Atsakymas a): ");
                            String answerB = this.scannerService.scanLine("Atsakymas b): ");
                            String answerC = this.scannerService.scanLine("Atsakymas c): ");
                            String answerD = this.scannerService.scanLine("Atsakymas d): ");
                            String answerCorrect = this.scannerService.scanText("Teisingo atsakymo raide: ");

                            ExamTask examTask = new ExamTask(question, answerA, answerB, answerC, answerD,
                                    answerCorrect);

                            // Add exam task
                            this.databaseService.addExamTask(this.user.getNickname(), taskTitle, examTask);
                            System.out.println("Klausimas pridetas sekmingai.");
                        }

                        // Delete question
                        if (actionQuestion == 2) {
                            int examTaskCount = this.databaseService.getExamTaskCount(this.user.getNickname(),
                                    taskTitle);
                            int actionQuestionDelete = this.scannerService
                                    .scanNumber("Nurodykite naikinamo klausimo numeri: ", examTaskCount);
                            this.databaseService.deleteExamTask(this.user.getNickname(), taskTitle,
                                    actionQuestionDelete);
                            System.out.println("Egzamino klausimas sekmingai panaikintas.");
                        }

                        // Back
                        if (actionQuestion == 3)
                            break;

                    }

                    break;
                }

            }

        }
    }

    /** New task */
    public void newTask() throws FileNotFoundException, IOException, ParseException {
        while (true) {
            System.out.println("Prasome nurodyti uzduoties duomenys:");
            String taskTitle = this.scannerService.scanLine("Pavadinimas: ");
            JSONObject task = this.databaseService.getTask(this.user.getNickname(), taskTitle);

            // Task exist
            if (task != null)
                System.out.println("Uzduotis egzistuoja.");

            // Create task
            if (task == null) {
                this.databaseService.addTask(this.user.getNickname(), new Task(taskTitle));
                System.out.println("Uzduotis sukurta sekmingai.");
                break;
            }
        }
    }

}
