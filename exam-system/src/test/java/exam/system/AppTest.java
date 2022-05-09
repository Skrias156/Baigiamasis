package exam.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * Unit test for Exam system.
 */
public class AppTest {

    @Test
    public void checkUsersTableFromDatabaseShouldNotNull()
            throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
        FileService fileService = new FileService();
        JSONObject users = fileService.readData(fileService.getFileUsersPath());
        assertNotNull("Should get users table from database", users);
    }

    @Test
    public void checkTeachersTableFromDatabaseShouldNotNull()
            throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
        FileService fileService = new FileService();
        JSONObject teachers = fileService.readData(fileService.getFileTeachersPath());
        assertNotNull("Should get teachers table from database", teachers);
    }

    @Test
    public void checkExamsTableFromDatabaseShouldNotNull()
            throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
        FileService fileService = new FileService();
        JSONObject exams = fileService.readData(fileService.getFileResultsPath());
        assertNotNull("Should get exams table from database", exams);
    }

    @Test
    public void compareEqualDatesShouldEqual() throws ParseException {
        int expected = 0;
        int actual = new DateTimeService().compare("2022-05-08 10:00:00", "2022-05-08 10:00:00", 0);
        assertEquals("Should equal dates", expected, actual);
    }

    @Test
    public void compareNotEqualDatesShouldNotEqual() throws ParseException {
        int expected = 0;
        int actual = new DateTimeService().compare("2022-05-09 10:00:00", "2022-05-08 10:00:00", 0);
        assertNotEquals("Should not equal dates", expected, actual);
    }

    @Test
    public void checkLithuanianTimeZoneShouldEuropeVilnius() {
        String expected = "Europe/Vilnius";
        String actual = DateTimeService.TIME_ZONE_EUROPE_VILNIUS;
        assertEquals("Should Lithuanian time zone - Europe/Vilnius", expected, actual);
    }

    @Test
    public void checkUsersFileExistShouldTrue() throws IOException {
        boolean actual = new FileService().getFileUsersPath().endsWith("users.json");
        assertTrue("Should users database file exist", actual);
    }

    @Test
    public void checkTeachersFileExistShouldTrue() throws IOException {
        boolean actual = new FileService().getFileTeachersPath().endsWith("teachers.json");
        assertTrue("Should teachers database file exist", actual);
    }

    @Test
    public void checkProgramDirectoryPathIsEmptyShouldFalse() throws IOException {
        Boolean actual = new FileService().getDirectoryCurrentPath().isEmpty();
        assertFalse("Should get a current program path", actual);
    }

    @Test
    public void checkAssetsDirectoryExistShouldTrue() throws IOException {
        boolean actual = new FileService().getDirectoryAssetsPath().endsWith("Exam system");
        assertTrue("Should assets directory name - Exam system", actual);
    }

}
