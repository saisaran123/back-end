import java.util.Scanner;

public class TextTransformer {
    public String input;
    Scanner scan=new Scanner(System.in);
    public String setInput() {

        // get the input from user and set it
        System.out.println("Enter the paragraph (type ':wq' to finish):");
         input = "";
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


//    public String getInput() {
//        // return the input
//
//        return
//    }

    public void caseTransform() {
        // get the options from user and call all the methods
        setInput();
        while (true)
        {
            System.out.println("press 1 for toLowecase  ");
            System.out.println("press 2 for toUppercase");
            System.out.println("press 3 for toCapitalizecase");
            System.out.println("press 4 for toAlternatecase");
            System.out.println("press 5 for toInversecase");
            System.out.println("press 6 for Exit ");
            int value=scan.nextInt();
            scan.nextLine();

            switch (value)
            {
                case 1:
                    System.out.println(toLowerCase());
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println(toUpperCase());
                    System.out.println("\n");
                    break;
                case 3:
                    System.out.println(capitalizeCase());
                    System.out.println("\n");
                    break;
                case 4:
                    System.out.println(alternativeCase());
                    System.out.println("\n");
                    break;
                case 5:
                    System.out.println(inverseCase());
                    System.out.println("\n");
                    break;
                case 6:
                    System.out.println("Thankyou!!");
                    System.out.println("\n");
                    break;
                default:
                    System.out.println("please provide valid input....");
                    break;

            }
            if(value==6)
            {
                break;
            }
        }

    }

public String toLowerCase()
    {
        String fin="";
        for(int i=0;i<input.length();i++)
        {
            char c=input.charAt(i);
            if(c>='A' && c<='Z')
            {
                fin+=(char) (c + 32);
            }
            else {
                fin+=(c);
            }
        }
        return fin;
    }




    public String toUpperCase() {
        String fin = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'z') {
                fin += ((char) (c - 32));
            } else {
                fin += (c);
            }

        }
        return fin;
    }

    public String capitalizeCase() {
        String capital = "";

        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (i==0)
            {
                capital+=input.toUpperCase().charAt(0);


            }

            else if(c==' ' && (input.charAt(i+1))!=' ')
            {
                capital+=' ';
                capital+=input.toUpperCase().charAt(i+1);
                i++;
            }

            else 
            {
                capital+=c;
            }


        }
        return capital;
    }

    public String alternativeCase() {
        // change the input to alternative case
        String alternate="";
        for (int i = 0; i < input.length(); i++)
        {
            char c=input.charAt(i);
            if(i%2==0)
            {
                alternate+=input.toLowerCase().charAt(i);
            }
            else
            {

                alternate+=input.toUpperCase().charAt(i);
            }
        }
        return alternate;

    }

    public String inverseCase() {
        // change the input into inverse case
        String inverse="";
        for (int i = 0; i < input.length(); i++)
        {
            char c=input.charAt(i);
            if(c>='a' && c<='z')
            {
                inverse+=input.toUpperCase().charAt(i);
            }
            else if (c>='A' && c<='Z') {
                inverse+=input.toLowerCase().charAt(i);

            }
            else
            {
                inverse+=c;
            }
        }
        return inverse;

    }

    public static void main(String[] args) {
        TextTransformer obj = new TextTransformer();
        obj.caseTransform();
    }
}

