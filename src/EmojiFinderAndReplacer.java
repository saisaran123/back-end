import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.SortedMap;

public class EmojiFinderAndReplacer
{
    Scanner scan=new Scanner(System.in);
    public String getInput() {
        System.out.println("\n Enter the paragraph here.. :(type ':wq' to finish):");


        String input = scan.nextLine();

        return input;
    }

    public void emojicounter(String input)
    {
        String distinctemoji="";

        for(int i=0;i<input.length();i++)
        {
            int output=0;
            int ival=input.codePointAt(i);

            if(ival>=0x1f600 && ival<=0x1f64f)
            {
                
               for(int j=0;j<input.length();j++)
               {
                   int jval=input.codePointAt(j);
                   if(ival==jval)
                   {
                       output++;
                   }
               }
                System.out.println(" " +"occures"+output+"Times");

            }

        }



    }

    public void emojiReplacer(String input)
    {
        String fin="";
        System.out.println("What emoji you wish to replace :");
        String repl=scan.nextLine();
        for(int i=0;i<input.length()-1;i++)
        {
            char c=input.charAt(i);
            if((c>=65 && c<=122) || c==' ')
            {
                fin+=c;
            }

            int val=input.codePointAt(i);
            if(val>=0x1f600 && val<=0x1f64f)
            {
                fin+=repl;
            }
        }
        System.out.println("the replaced emoji is :"+fin);

    }

    public static void main(String[] args)
    {
        EmojiFinderAndReplacer obj=new EmojiFinderAndReplacer();
        String input= obj.getInput();
        obj.emojicounter(input);
        obj.emojiReplacer(input);


    }
}
