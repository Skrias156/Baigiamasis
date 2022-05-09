package exam.system.models;

public class User extends Client {

    private String role;
    
    public User(String nickname, String password, String role){
        super(nickname, password);
        this.role = role;
    }


    /**Get role */
    public String getRole() {
        return role;
    }


}
