import java.util.Scanner;

public class LetterFrequencyCounter
{

    private String sentence;

    public LetterFrequencyCounter(String sentence)
    {
        this.sentence=sentence.toLowerCase();
    }


    public void charCount()
    {
        char[] alphabet={'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };
        System.out.println("Character \t Count \t Percentage");
        for(int i=0;i<alphabet.length;i++)
        {
            char eachiter=alphabet[i];

            int count=0;
            for(int j=0;j<sentence.length();j++)
            {
                char eachchar=sentence.charAt(j);

                if(eachchar==eachiter)
                {
                    count++;
                }
            }
            System.out.println("______________________________________");
            System.out.println( "\u001B[34m" +"\u001B[40m" + "| \t" +eachiter +"\t |\t\t"+ count +"\t |\t\t" + (count*100)/sentence.length() + "%" +"\t\t |" +"\u001B[40m" );
        }

    }



    public static void main(String[] args)
    {
        Scanner scan=new Scanner(System.in);
        String sentence=scan.nextLine();
        LetterFrequencyCounter obj=new LetterFrequencyCounter(sentence);
        obj.charCount();


    }
}
