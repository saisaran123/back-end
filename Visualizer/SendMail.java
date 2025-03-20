package Visualizer;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


public class SendMail
{
    private String sendername;
    private String password;
    private String recepient;
    Properties properties=new Properties();
    Session session;
    Random random=new Random();
    private String otp="";

    public void configure(){
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendername,password);
            }
        });

    }

    public void sendMessage()  {
        Message message= prepareMessage(session,sendername,recepient);

        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Sent Successfully!! to "+recepient);
    }

    public void generateOTP(int n){
        String chars="0123456789";
        otp="";
        for (int i=0;i<n;i++){
            otp+=chars.charAt(random.nextInt(10));
        }

    }

    public  Message prepareMessage(Session session,String userName,String recepient){
        Message message1=new MimeMessage(session);
        try {
            message1.setFrom(new InternetAddress(userName));
            message1.setRecipient(Message.RecipientType.TO,new InternetAddress(recepient));
            message1.setSubject(" \uD83D\uDEA8 Your OTP is Escaping! Catch It Fast!");
            message1.setText(
                    "Hey Superhuman,\n\n" +
                            "Your One-Time Password is: " + otp + "\n" +
                            "But hurry! This OTP is like my ex—it won’t wait for you forever. In 10 minutes, it will vanish into thin air, leaving you sad, lonely, and OTP-less.\n\n" +
                            "If you fail:\n\n" +
                            "- It will turn into a pumpkin 🎃 and audition for *Cinderella 2: The Escape of the Code*.\n" +
                            "- It might join a secret ninja squad of expired OTPs.\n\n" +
                            "Instructions:\n\n" +
                            "1. Use the OTP.\n" +
                            "2. Save the day.\n" +
                            "3. Don’t let it join the circus.\n\n" +
                            "Stay Unreasonably Secure,\n\n" +
                            "[ZS Private Limited]"
            );

        }
        catch (Exception e){
            e.getMessage();
        }

        return message1;
    }


    public void setSendername(String sendername){
        this.sendername=sendername;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setRecepient(String recepient){
        this.recepient=recepient;
    }

    public String  getOTP(){
        return this.otp;
    }

    public void init(){
        configure();
        generateOTP(5);
        sendMessage();
    }


}

