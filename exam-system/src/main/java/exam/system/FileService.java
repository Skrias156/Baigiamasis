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
