public class StringPerformance {
    public static void main(String[] args) {
        String originalPassword = "mypassword";
        StringBuilder sb = new StringBuilder("Hello");
        User u2 = new User(originalPassword);
        User u1 = new User(sb);

    }
}

class User{
    private String password;
    private StringBuilder password1;

    public User(String password){
        this.password = password + " Immutable";
        System.out.println(password);
    }

    public User(StringBuilder password1){
        this.password1 = password1;
        this.password1.append(" Mutable");
        System.out.println(password1);
    }
}