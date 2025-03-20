import java.util.Scanner;

public class CaeserCipher
{


    Scanner scan=new Scanner(System.in);
    public String getInput() {
        System.out.println("\n Enter the paragraph here.. :(type ':wq' to finish):");


        String input = "";
        while (true) {
            String line = scan.nextLine();
            if (line.equals(":wq"))
            {
                break;
            } else
            {
                input+=(line+"\n");
            }
        }
        return input;
    }
    public int getKey()
    {
        System.out.print("Enter the key :");
        int key= scan.nextInt();
        return key;
    }

    public String encrption(String input,int key)
    {

        key=key%26;

        String encrypted="";
        for(int i=0;i<input.length();i++)
        {
            char c=input.charAt(i);
            if(c>='a' && c<='z')
            {
                if((int)c+key>122)
                {
                    encrypted+=(char)(c-26+key);
                }
                else {
                    encrypted = encrypted + (char)(c+key);
                }

            }
            else if(c>='A' && c<='Z')
            {
                if((int)c+key>90)
                {
                    encrypted+=(char)(c-26+key);
                }
                else {
                    encrypted = encrypted + (char)(c+key);
                }
            }
            else {
                encrypted+=c;
            }


        }
        return encrypted;
    }


    public String decryption(String input,int key)
    {

        key=key%26;
        String decrypted="";
        for(int i=0;i<input.length();i++)
        {

            char c=input.charAt(i);
            if(c>='a' && c<='z')
            {
                if((int)c-key<97)
                {
                    decrypted+=(char)(c+26-key);
                }
                else {
                    decrypted = decrypted + (char)(c-key);
                }

            }
            else if(c>='A' && c<='Z')
            {
                if((int)c-key<65)
                {
                    decrypted+=(char)(c+26-key);
                }
                else {
                    decrypted = decrypted + (char)(c-key);
                }
            }
            else {
                decrypted+=c;
            }


        }
        return decrypted;

    }

    public void execution()
    {
        System.out.println("\n\n");
        System.out.println("<<-----------------------------------------------Hi welcome this is Encrypting and Decrypting tool---------------------------------------->>");
        while(true)
        {

            System.out.println("Press 1 for Encrypting the Message");
            System.out.println("Press 2 for Decrypting the Message");
            System.out.println("Press 3 for exit the tool");
            int value= scan.nextInt();
            scan.nextLine();
            switch (value)
            {
                case 1:
                    System.out.println(encrption(getInput(),getKey()));
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println(decryption(getInput(),getKey()));
                    System.out.println("\n");
                    break;
                case 3:
                    System.out.println("----------------------------------------");
                    System.out.println("Thankyou For using my code!! visit again");
                    System.out.println("----------------------------------------");
                    System.out.println("\n");
                    break;
                default:
                    System.out.println("Please provide the valid input");

            }
            if(value==3)
            {
                break;
            }
        }
    }


    public static void main(String[] args)
    {
        CaeserCipher obj=new CaeserCipher();
        obj.execution();
    }
}

