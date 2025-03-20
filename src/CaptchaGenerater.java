import java.util.Random;
import java.util.Scanner;

public class CaptchaGenerater
{
    Scanner scan=new Scanner(System.in);
    String text;
    int length;

    public int getLength() {
        return length;
    }

    public void setLength() {
        System.out.println("Enter the Length: ");
        length=scan.nextInt();
        scan.nextLine();
    }

    public String getText()
    {
        return text;
    }

    public void setText()
    {
        System.out.println("Enter The Text: ");
        text=scan.nextLine();
    }
    public boolean choose()
    {
        System.out.println("1. Random Captcha Generator \n2. Custom Captcha Generator  press(1/2)");
        int val=scan.nextInt();
        scan.nextLine();
        boolean retval=false;
        if(val==1)
        {
            retval=true;
        }
        return retval;

    }


    public static void main(String[] args)
    {

        CaptchaGenerater obj=new CaptchaGenerater();


        if(obj.choose()){
            Captcha obj1=new Captcha();
        }
        else {
            obj.setText();
            obj.setLength();
            Captcha obj2=new Captcha(obj.text, obj.length);
        }
    }
}

class Captcha{
    Scanner scan=new Scanner(System.in);
    private String text="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int length =5;

    Random random=new Random();
    Captcha()
    {

        StringBuilder randomChars = new StringBuilder();
        for(int i = 0; i< length; i++)
        {
            int randomIndex=random.nextInt(0,text.length());

            randomChars.append("\u001B[31m"+"\u001B[44m"+text.charAt(randomIndex)+"\u001B[0m");

        }
        System.out.println("\n\n");
        System.out.println("\t\t\t\t\t\t\t\t"+randomChars);

        System.out.println("\n\n");
    }


    public void showColor()
    {
        System.out.println("Enter 1 for RED");
        System.out.println("Enter 2 for GREEN");
        System.out.println("Enter 3 for BLUE");
        System.out.println("Enter 4 for CYAN");
    }
    Captcha(String text,int length)
    {
        this.text=text;
        this.length =length;
        StringBuilder randomChars = new StringBuilder();
        showColor();
        int color=scan.nextInt();
        String bgColor= "\u001B[47m";
        scan.nextLine();
        String textColor="";
        switch (color)
        {
            case 1:
                textColor="\u001B[31m";
                break;
            case 2:
                textColor= "\u001B[32m";
                break;
            case 3:
                textColor="\u001B[34m";
                break;
            case 4:
                textColor="\u001B[36m";
                break;
            default:
                System.out.println("Provide Valid Number...");
        }
        for(int i = 0; i< this.length; i++)
        {
            int randomIndex=random.nextInt(0,text.length());

            randomChars.append(bgColor).append(textColor)
                    .append(text.charAt(randomIndex))
                    .append("\u001B[0m");

        }
        System.out.println("\n\n");
        System.out.println("\t\t\t\t\t\t\t\t"+randomChars);
        System.out.println("\n\n");

    }

}
