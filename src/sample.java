import java.util.Scanner;
public class sample
{
    public void splitSentence(String input, char delimiter) {
        for(int i=0;i<input.length();i++)
        {
            char c=input.charAt(i);
            System.out.print(c);
            if(c==delimiter)
            {
                System.out.println("\n");
            }

        }


    }

    public static void main(String args[])
    {
//        sample obj=new sample();
//        obj.splitSentence("hello folks",'e');

        Scanner scan = new Scanner(System.in);
        int e = scan.nextInt();

        System.out.println(e);
        System.out.println(scan.nextInt());




    }

}
