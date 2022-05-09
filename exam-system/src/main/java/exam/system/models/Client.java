package exam.system.models;

public class Client {

    private String nickname;
    private String password;
    
    public Client(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
    }

    /**Get nickname */
    public String getNickname() {
        return nickname;
    }

    /**Get password */
    public String getPassword() {
        return password;
    }

}
