import java.util.Scanner;
public class Counter
{
    String input;
    Scanner scan=new Scanner(System.in);
    public String getInput() {
        System.out.println("Enter the paragraph (type ':wq' to finish):");
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


    public void vowelCounter(String input)
    {
        int vowelcounter=0;

        input=input.toLowerCase();
        for(int i=0;i<input.length();i++)
        {
            char c=input.charAt(i);

            if(c=='a'||c=='e'||c=='i'||c=='o'||c=='u')
            {
                vowelcounter+=1;
            }


        }
        System.out.println("Vowel Count :"+vowelcounter);


    }

    public void consonantCount(String input) {
        int consonantcount = 0;
        input = input.toLowerCase();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (!(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') && (c >= 'a' && c <= 'z'))
            {
                consonantcount += 1;
            }

        }
        System.out.println("Consonant Counter : " +consonantcount );
    }


    public int wordCounter(String input)
    {
        int words=1;
        if(input.isBlank())
        {
            return 0;
        }
        for(int i=0;i<input.length()-1;i++)
        {
            if((input.charAt(i+1)==' ' && input.charAt(i+2)!=' ' && input.charAt(i+2)!='\n' || input.charAt(i)=='\n')  )
            {
                words++;
            }
        }
        return words;
    }

    public int sentenceCounter(String input )
    {
        int count=0;
        if(input.trim().isEmpty())
        {
            count=0;
        }
        else
        {
            count=1;
        }
        for (int i = 0; i < input.length(); i++)
        {
            char c=input.charAt(i);
            if(c=='.')
            {
                count+=1;
            }

        }
        return count;
    }

    public int paraCounter(String input)
    {
        int para=0;
        for(int i=0;i<input.length()-1;i++)
        {
            char c=input.charAt(i);
            if(c=='\n' && input.charAt(i+1)=='\n')
            {
                para+=1;
            }
        }
        return para;
    }

    public void showMenu()
    {
        System.out.println("press 1 for vowel count");
        System.out.println("press 2 for consonant count");
        System.out.println("press 3 for word count");
        System.out.println("press 4 for sentence count");
        System.out.println("press 5 for paragrah count");
        System.out.println("press 6 for exit ");
    }

    public void execution()
    {
        String input = getInput();
        while (true)
        {
            showMenu();
            int value=scan.nextInt();
            scan.nextLine();
            switch (value)
            {
                case 1:
                    vowelCounter(input);
                    System.out.println("\n");
                    break;
                case 2:
                    consonantCount(input);
                    System.out.println("\n");
                    break;
                case 3:
                    System.out.println("Word Count : "+wordCounter(input));
                    System.out.println("\n");
                    break;
                case 4:
                    System.out.println("Sentence Count: "+sentenceCounter(input));
                    System.out.println("\n");
                    break;
                case 5:
                    System.out.println("Para Count: "+paraCounter(input));
                    System.out.println("\n");
                    break;
                case 6:
                    System.out.println("Thankyou!!");
                    break;

            }
            if(value==6)
            {
                break;
            }
        }
    }
    public static void main(String[] args)
    {
        Counter obj=new Counter();
        obj.execution();


    }
}
