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

public class DatabaseService {

    public final static String TABLE_USERS = "Vartotojai";
    public final static String TABLE_TEACHERS = "Destytojai";
    public final static String TABLE_STUDENTS = "Studentai";
    public final static String TABLE_EXAMS = "Egzaminai";

    public final static String FIELD_USER_NICKNAME = "Slapyvardis";
    public final static String FIELD_USER_PASSWORD = "Slaptazodis";
    public final static String FIELD_USER_ROLE = "Role";

    public final static String FIELD_TASK_TITLE = "Pavadinimas";
    public final static String FIELD_TASK_EXAMS = "Egzaminas";
    public final static String FIELD_TASK_EXAM_QUESTION = "Klausimas";
    public final static String FIELD_TASK_EXAM_ANSWER_A = "A";
    public final static String FIELD_TASK_EXAM_ANSWER_B = "B";
    public final static String FIELD_TASK_EXAM_ANSWER_C = "C";
    public final static String FIELD_TASK_EXAM_ANSWER_D = "D";
    public final static String FIELD_TASK_EXAM_ANSWER_CORRECT = "Atsakymas";

    public final static String FIELD_STUDENT_NAME = "Vardas";
    public final static String FIELD_STUDENT_SURNAME = "Pavarde";
    public final static String FIELD_STUDENT_DATE = "Data";
    public final static String FIELD_STUDENT_TEST_TITLE = "Pavadinimas";
    public final static String FIELD_STUDENT_TEST = "Testas";
    public final static String FIELD_TASK_TEST_QUESTION = "Klausimas";
    public final static String FIELD_TASK_TEST_ANSWER_A = "A";
    public final static String FIELD_TASK_TEST_ANSWER_B = "B";
    public final static String FIELD_TASK_TEST_ANSWER_C = "C";
    public final static String FIELD_TASK_TEST_ANSWER_D = "D";
    public final static String FIELD_TASK_TEST_ANSWER_CORRECT = "Teisingas atsakymas";
    public final static String FIELD_TASK_TEST_ANSWER_STUDENT = "Studento atsakymas";
    public final static String FIELD_TASK_TEST_ANSWER_CORRECTS = "Teisingi atsakymai";
    public final static String FIELD_TASK_TEST_ANSWER_WRONGS = "Neteisingi atsakymai";
    public final static String FIELD_TASK_TEST_ANSWER_CORRECTS_PERCENTS = "Teisingai atsakyta proc.";
    public final static String FIELD_TASK_TEST_FILE_NAME = "Failo pavadinimas";

    public final static String VALUE_ROLE_TEACHER = "Destytojas";
    public final static String VALUE_ROLE_STUDENT = "Studentas";

    FileService fileService;

    public DatabaseService() {
        this.fileService = new FileService();
    }

    /** Delete studet result taks */
    public void deleteResultTask(String taskTitle, String userName, String userSurname)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject exams = (JSONObject) this.fileService.readData(this.fileService.getFileResultsPath())
                .get(TABLE_EXAMS);

        JSONArray tasks = (JSONArray) exams.get(taskTitle);
        JSONArray tasksNew = new JSONArray();

        // No task
        if (tasks == null)
            return;

        Iterator<JSONObject> iterator = tasks.iterator();

        // Find task
        while (iterator.hasNext()) {
            JSONObject task = iterator.next();

            if (
                !((String) task.get(DatabaseService.FIELD_STUDENT_NAME)).equals(userName) ||
                !((String) task.get(DatabaseService.FIELD_STUDENT_SURNAME)).equals(userSurname)
            ) tasksNew.add(task);
        }

        exams.put(taskTitle, tasksNew);

        JSONObject main = new JSONObject();
        main.put(DatabaseService.TABLE_EXAMS, exams);

        // Write tasks to file
        this.fileService.writeData(this.fileService.getFileResultsPath(), main);
    }

    /** Task result */
    public String getExamTaskCorrectPercents(String taskTitle, String userName, String userSurname)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject exams = (JSONObject) this.fileService.readData(this.fileService.getFileResultsPath())
                .get(TABLE_EXAMS);

        JSONArray tasks = (JSONArray) exams.get(taskTitle);

        // No task
        if (tasks == null)
            return null;

        Iterator iterator = tasks.iterator();

        // Find task
        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();
            if (((String) task.get(DatabaseService.FIELD_STUDENT_NAME)).equals(userName) &&
                    ((String) task.get(DatabaseService.FIELD_STUDENT_SURNAME)).equals(userSurname))
                return ((long) task.get(DatabaseService.FIELD_TASK_TEST_ANSWER_CORRECTS_PERCENTS)) + "";
        }

        return null;
    }

    /** Get exam task date */
    public String getExamTaskDate(String taskTitle, String userName, String userSurname)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject exams = (JSONObject) this.fileService.readData(this.fileService.getFileResultsPath())
                .get(TABLE_EXAMS);

        JSONArray tasks = (JSONArray) exams.get(taskTitle);

        // No task
        if (tasks == null)
            return null;

        Iterator iterator = tasks.iterator();

        // Find task
        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();
            if (((String) task.get(DatabaseService.FIELD_STUDENT_NAME)).equals(userName) &&
                    ((String) task.get(DatabaseService.FIELD_STUDENT_SURNAME)).equals(userSurname))
                return (String) task.get(DatabaseService.FIELD_STUDENT_DATE);
        }

        return null;
    }

    /** Insert result task to file */
    public void addResultTask(String taskTitle, JSONObject taskResult) throws IOException, ParseException {

        JSONObject exams = (JSONObject) this.fileService.readData(this.fileService.getFileResultsPath())
                .get(TABLE_EXAMS);

        JSONArray tasks = (JSONArray) exams.get(taskTitle);

        // If no task, than create
        if (tasks == null) {
            tasks = new JSONArray();
        }

        // Add task
        tasks.add(taskResult);
        exams.put(taskTitle, tasks);

        // Main object
        JSONObject main = new JSONObject();
        main.put(DatabaseService.TABLE_EXAMS, exams);

        // Write tasks to file
        this.fileService.writeData(this.fileService.getFileResultsPath(), main);
    }

    /** Find exam task count */
    public int getExamTaskCount(String nickname, String taskTitle)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);

        String teacher = nickname.replaceAll(" ", "_");
        JSONArray tasks = (JSONArray) teachers.get(teacher);

        Iterator iterator = tasks.iterator();

        // Find task
        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();

            String title = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
            if (title.equals(taskTitle)) {
                JSONArray examTasks = (JSONArray) task.get(DatabaseService.FIELD_TASK_EXAMS);
                return examTasks.size();
            }
        }
        return 0;
    }

    /** Delete exam task */
    public void deleteExamTask(String nickname, String taskTitle, int examTask)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);

        String teacher = nickname.replaceAll(" ", "_");
        JSONArray tasks = (JSONArray) teachers.get(teacher);
        JSONArray tasksNew = new JSONArray();

        Iterator iterator = tasks.iterator();

        // Find task
        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();

            String title = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
            if (title.equals(taskTitle)) {
                JSONArray examTasks = (JSONArray) task.get(DatabaseService.FIELD_TASK_EXAMS);
                JSONArray examTasksNew = new JSONArray();
                Iterator iterator1 = examTasks.iterator();
                int index = 0;

                while (iterator1.hasNext()) {
                    index++;
                    JSONObject examTask1 = (JSONObject) iterator1.next();
                    if (index != examTask)
                        examTasksNew.add(examTask1);
                }

                task.put(DatabaseService.FIELD_TASK_EXAMS, examTasksNew);

            }
            tasksNew.add(task);
        }

        teachers.put(teacher, tasksNew);

        // Main object
        JSONObject main = new JSONObject();
        main.put(TABLE_TEACHERS, teachers);

        // Write tasks to file
        this.fileService.writeData(this.fileService.getFileTeachersPath(), main);
    }

    /** Delete task */
    public void deleteTask(String nickname, String taskTitle)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);

        String teacher = nickname.replaceAll(" ", "_");
        JSONArray tasks = (JSONArray) teachers.get(teacher);
        JSONArray tasksNew = new JSONArray();

        Iterator iterator = tasks.iterator();

        // Find task
        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();

            String title = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
            if (!title.equals(taskTitle)) {
                tasksNew.add(task);
            }
        }

        teachers.put(teacher, tasksNew);

        // Main object
        JSONObject main = new JSONObject();
        main.put(TABLE_TEACHERS, teachers);

        // Write tasks to file
        this.fileService.writeData(this.fileService.getFileTeachersPath(), main);
    }

    /** Create exam task json format */
    public JSONObject createExamTask(ExamTask examTask) {
        JSONObject jsonExamTask = new JSONObject();
        jsonExamTask.put(DatabaseService.FIELD_TASK_EXAM_QUESTION, examTask.getQuestion());
        jsonExamTask.put(DatabaseService.FIELD_TASK_EXAM_ANSWER_A, examTask.getAnswerA());
        jsonExamTask.put(DatabaseService.FIELD_TASK_EXAM_ANSWER_B, examTask.getAnswerB());
        jsonExamTask.put(DatabaseService.FIELD_TASK_EXAM_ANSWER_C, examTask.getAnswerC());
        jsonExamTask.put(DatabaseService.FIELD_TASK_EXAM_ANSWER_D, examTask.getAnswerD());
        jsonExamTask.put(DatabaseService.FIELD_TASK_EXAM_ANSWER_CORRECT, examTask.getCorrect());
        return jsonExamTask;
    }

    /** Insert exam task to database */
    public void addExamTask(String nickname, String taskTitle, ExamTask examTask) throws IOException, ParseException {

        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);

        String teacher = nickname.replaceAll(" ", "_");

        JSONArray tasks = (JSONArray) teachers.get(teacher);
        JSONArray tasksNew = new JSONArray();

        Iterator iterator = tasks.iterator();
        // Find task
        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();

            String title = (String) task.get(DatabaseService.FIELD_TASK_TITLE);
            if (title.equals(taskTitle)) {
                JSONArray exam = (JSONArray) task.get(DatabaseService.FIELD_TASK_EXAMS);
                exam.add(createExamTask(examTask));
                task.put(DatabaseService.FIELD_TASK_EXAMS, exam);
                tasksNew.add(task);
            } else {
                tasksNew.add(task);
            }
        }

        teachers.put(teacher, tasksNew);

        // Main object
        JSONObject main = new JSONObject();
        main.put(TABLE_TEACHERS, teachers);

        // Write tasks to file
        this.fileService.writeData(this.fileService.getFileTeachersPath(), main);
    }

    /** Get tasks from database */
    public JSONArray getTasks(String nickname) throws FileNotFoundException, IOException, ParseException {

        // Get teachers
        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);
        if (teachers == null)
            return null;

        return (JSONArray) teachers.get(nickname.replaceAll(" ", "_"));
    }

    /** Get task from database */
    public JSONObject getTask(String nickname, String taskTitle)
            throws FileNotFoundException, IOException, ParseException {

        // Get teachers
        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);
        if (teachers == null)
            return null;

        // Get teacher
        JSONArray tasks = (JSONArray) teachers.get(nickname.replaceAll(" ", "_"));
        if (tasks == null)
            return null;

        Iterator iterator = tasks.iterator();

        while (iterator.hasNext()) {
            JSONObject task = (JSONObject) iterator.next();// Get user

            String title = (String) task.get(FIELD_TASK_TITLE);

            if (taskTitle.equals(title))
                return task;
        }

        return null;
    }

    /** Insert task to database */
    public void addTask(String nickname, Task task) throws IOException, ParseException {

        JSONObject teachers = (JSONObject) this.fileService.readData(this.fileService.getFileTeachersPath())
                .get(TABLE_TEACHERS);

        String teacher = nickname.replaceAll(" ", "_");

        JSONArray tasks = (JSONArray) teachers.get(teacher);

        // Create new task
        JSONObject jsonTask = new JSONObject();
        jsonTask.put(this.FIELD_TASK_TITLE, task.getTitle());
        jsonTask.put(this.FIELD_TASK_EXAMS, new JSONArray());

        // If no tasks
        if (tasks == null) {
            tasks = new JSONArray();
            tasks.add(jsonTask);
        } else {
            tasks.add(jsonTask);
        }

        teachers.put(teacher, tasks);

        // Main object
        JSONObject main = new JSONObject();
        main.put(TABLE_TEACHERS, teachers);

        // Write tasks to file
        this.fileService.writeData(this.fileService.getFileTeachersPath(), main);
    }

    /** Get teachers from database */
    public JSONArray getTeachers() throws FileNotFoundException, IOException, ParseException {
        JSONArray users = (JSONArray) this.fileService.readData(this.fileService.getFileUsersPath()).get(TABLE_USERS);

        JSONArray teachers = new JSONArray();

        Iterator iterator = users.iterator();

        while (iterator.hasNext()) {
            JSONObject user = (JSONObject) iterator.next();
            if (((String) user.get(DatabaseService.FIELD_USER_ROLE)).equals(DatabaseService.VALUE_ROLE_TEACHER)) {
                teachers.add(user);
            }
        }

        return teachers;
    }

    /** Find user from database */
    public User getUser(String nickname, String password) throws FileNotFoundException, IOException, ParseException {

        JSONArray users = (JSONArray) this.fileService.readData(this.fileService.getFileUsersPath()).get(TABLE_USERS);
        Iterator iterator = users.iterator();

        while (iterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) iterator.next();

            String nicknameDB = (String) jsonObject.get(FIELD_USER_NICKNAME);
            String passwordDB = (String) jsonObject.get(FIELD_USER_PASSWORD);
            String roleDB = (String) jsonObject.get(FIELD_USER_ROLE);

            if (nicknameDB.equals(nickname) && passwordDB.equals(password))
                return new User(nicknameDB, passwordDB, roleDB);
        }

        return null;
    }

    /** Insert user to database */
    public void addUser(String nickname, String password, String role) throws IOException, ParseException {

        JSONArray users = (JSONArray) this.fileService.readData(this.fileService.getFileUsersPath()).get(TABLE_USERS);

        // Create new user
        JSONObject user = new JSONObject();
        user.put(FIELD_USER_NICKNAME, nickname);
        user.put(FIELD_USER_PASSWORD, password);
        user.put(FIELD_USER_ROLE, role);
        users.add(user);

        // Main object
        JSONObject main = new JSONObject();
        main.put(TABLE_USERS, users);

        // Write users to file
        this.fileService.writeData(this.fileService.getFileUsersPath(), main);
    }

    /** Create default JSON object */
    public JSONObject createDefaulObject(String table, Object object) {
        JSONObject main = new JSONObject();
        main.put(table, object);
        return main;
    }

}
