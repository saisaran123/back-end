package Visualizer;

import java.sql.*;
import java.util.Scanner;

public class login {
    String url = "jdbc:mysql://localhost:3306/dbvis";
    String dbuser = "sai";
    String pass = "erenxnaruto";
    Scanner sc = new Scanner(System.in);
    CaesarCipher enc = new CaesarCipher();
    public void loginUser() {
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        try (Connection connect = DriverManager.getConnection(url, dbuser, pass);
             PreparedStatement stat = connect.prepareStatement("SELECT role FROM login WHERE username = ? AND password = ?")) {
            stat.setString(1, username);
            stat.setString(2, enc.encrypt(password));
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("Login successful! Role: " + role);
                if ("admin".equals(role)) {
                    System.out.println("Welcome, Admin!");
                } else {
                    System.out.println("Welcome, User!");
                }
                Executer on = new Executer(role,connect,username);
                on.start();
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void signInUser() {
        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        String password;
        while (true) {
            System.out.print("Enter your password (8+ characters, including at least one number and one special character @$#): ");
            password = sc.nextLine();

            if (password.matches("^(?=.*[0-9])(?=.*[@$#]).{8,}$")) {
                break;
            } else {
                System.out.println("Invalid password. Ensure it is 8+ characters, contains at least one number, and one special character @$#.");
            }
        }

        try (Connection connect = DriverManager.getConnection(url, dbuser, pass);
             PreparedStatement stat = connect.prepareStatement("INSERT INTO login (username, password, role) VALUES (?, ?, 'user');")) {
            stat.setString(1, username);
            stat.setString(2, enc.encrypt(password));

            int rowsAffected = stat.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sign-up successful! You can now log in.");
            } else {
                System.out.println("Sign-up failed. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void menu() {
        while (true) {
            System.out.print("\n1. Login\n2. Sign up\n3. Exit\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                loginUser();
            } else if (choice == 2) {
                signInUser();
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid input! Please try again.");
            }
        }
    }
    public void captcha(){
        captchaMaker obj = new captchaMaker();
        String captcha = obj.getCaptcha().trim();
        obj.display();
        System.out.print("To verify that you're human , kindly enter what is writen in the captcha above \n Enter : ");
        String output = sc.nextLine();
        if(captcha.equals(output)){
            menu();
        }
        else{
            System.out.println("invalid captcha!.");
        }
    }

    public static void main(String[] args) {login lob = new login();lob.captcha();}
}