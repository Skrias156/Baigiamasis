package exam.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileService {

    // Assets directory name
    private final String DIRECTORY_ASSETS = "Exam system";


    public FileService() {
    }

    /** Write data to file. */
    public void writeData(String fileName, String data, boolean dataAppend) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, dataAppend);
        fileWriter.write(data);
        fileWriter.close();
    }

    /** Write data to file. */
    public void writeData(String fileName, ArrayList<String> data) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);

        for (int index = 0; index < data.size(); index++) {
            fileWriter.write(data.get(index));
        }

        fileWriter.close();
    }

    /** Write data to file. */
    public void writeData(String fileName, JSONObject data) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(data.toJSONString());
        fileWriter.close();
    }

    /** Read data from file . */
    public JSONObject readData(String fileName) throws FileNotFoundException, IOException, ParseException {
        FileReader fileReader = new FileReader(fileName);
        JSONObject data = (JSONObject) new JSONParser().parse(fileReader);
        fileReader.close();
        return data;
    }

    /** Create assets */
    public void createAssets() throws IOException {
        File file = new File(this.getDirectoryAssetsPath());

        // Assets directory exist
        if (file.isDirectory())
            return;

        // Create assets
        file.mkdir();
        new File(this.getDirectoryDatabasePath()).mkdir();
        new File(this.getDirectoryExamPath()).mkdir();
        new File(this.getFileUsersPath()).createNewFile();
        new File(this.getFileTeachersPath()).createNewFile();
        new File(this.getFileResultsPath()).createNewFile();

        // Create default JSON objects
        this.writeData(this.getFileUsersPath(),
                new DatabaseService().createDefaulObject(DatabaseService.TABLE_USERS, new JSONArray()));
        this.writeData(this.getFileTeachersPath(),
                new DatabaseService().createDefaulObject(DatabaseService.TABLE_TEACHERS, new JSONObject()));
        this.writeData(this.getFileResultsPath(),
                new DatabaseService().createDefaulObject(DatabaseService.TABLE_EXAMS, new JSONObject()));

    }

    /** Get teachers file */
    public String getFileResultsPath() throws IOException {
        return getDirectoryCurrentPath() + "\\" + this.DIRECTORY_ASSETS + "\\egzaminai\\rezultatai.json";
    }

    /** Get teachers file */
    public String getFileTeachersPath() throws IOException {
        return getDirectoryCurrentPath() + "\\" + this.DIRECTORY_ASSETS + "\\database\\teachers.json";
    }

    /** Get database file */
    public String getFileUsersPath() throws IOException {
        return getDirectoryCurrentPath() + "\\" + this.DIRECTORY_ASSETS + "\\database\\users.json";
    }

    /** Get exam directory path */
    public String getDirectoryExamPath() throws IOException {
        return getDirectoryCurrentPath() + "\\" + this.DIRECTORY_ASSETS + "\\egzaminai";
    }

    /** Get database directory path */
    public String getDirectoryDatabasePath() throws IOException {
        return getDirectoryCurrentPath() + "\\" + this.DIRECTORY_ASSETS + "\\database";
    }

    /** Get program assets directory path */
    public String getDirectoryAssetsPath() throws IOException {
        return getDirectoryCurrentPath() + "\\" + this.DIRECTORY_ASSETS;
    }

    /** Get program current directory path */
    public String getDirectoryCurrentPath() throws IOException {
        return new File(".").getCanonicalFile().getAbsolutePath();
    }

}
