package exam.system;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import exam.system.models.Client;
import exam.system.models.User;

public class ClientService {

    private DatabaseService databaseService;
    private ScannerService scannerService;


    public ClientService(ScannerService scannerService) {
        this.scannerService = scannerService;
        this.databaseService = new DatabaseService();
    }


    /** Login */
    public void login(String nickname, String password) {
        User user = null;
        while (user == null) {
            try {
                //Find user
                user = this.databaseService.getUser(nickname, password);
            } catch (IOException | ParseException e) {
                System.err.println("Prisijungti nepavyko.");
                return;
            }

            // Bad login
            if (user == null) {
                System.out.println("Neteisingas slapyvardis arba slaptazodis.");
                Client client = this.scanData();
                nickname = client.getNickname();
                password = client.getPassword();
            }
        }

        //User role
        String role = user.getRole();

        // Teacher
        if (role.equals(DatabaseService.VALUE_ROLE_TEACHER)) {
            try {
                new TeacherService(user, this.scannerService).runContent();
            } catch (IOException | ParseException e) {
                System.err.println("Programos klaida.");
                return;
            }
        }

        // Student
        if (role.equals(DatabaseService.VALUE_ROLE_STUDENT)) {
                try {
                    new StudentService(this.scannerService).runContent();
                } catch (IOException | ParseException | java.text.ParseException e) {
                    System.err.println("Programos klaida.");
                }
        }
    }


    /** Register */
    public void register(String nickname, String password) {

        User user;
        try {
            //Find user
            user = databaseService.getUser(nickname, password);

            while (user != null) {
                // Check if exist user
                user = databaseService.getUser(nickname, password);
                if (user != null) {
                    System.out.println("Vartotojas egzistuoja.");
                    nickname = scannerService.scanText("Slapyvardis: ");
                }
            }

        } catch (IOException | ParseException e1) {
            System.err.println("Sukurti vartotojo nepavyko.");
            return;
        }

        //Scan user role
        int roleNumber = scannerService.scanNumber("Studentas - 1, Destytojas - 2. Nurodykite skaiciu: ", 2);

        String role = "";
        if (roleNumber == 1)
            role = DatabaseService.VALUE_ROLE_STUDENT;
        if (roleNumber == 2)
            role = DatabaseService.VALUE_ROLE_TEACHER;

        //Create new teacher
        try {
            databaseService.addUser(nickname, password, role);
        } catch (IOException | ParseException e) {
            System.err.println("Sukurti vartotojo nepavyko.");
            return;
        }

        System.out.println("Vartotojas sukurtas sekmingai.");
    }


    /**Scan user nickaname and password */
    public Client scanData(){
        System.out.println("\nPrasome nurodyti vartotojo duomenys.");
        String nickname = scannerService.scanText("Slapyvardis: ");
        String password = scannerService.scanText("Slaptazodis: ");//scanPassword("Slaptažodis: ");
        return new Client(nickname, password);
    }


    /**Scan action - login, register ant ect.*/
    public int scanAction(){
        System.out.println("\nPrasome pasirinkti pageidaujama veiksma:");
        int action = scannerService.scanNumber("Prisijungti - 1, Registruotis - 2, Baigti - 3. Nurodykite skaiciu: ", 3);
        return action;
    }
    
}
