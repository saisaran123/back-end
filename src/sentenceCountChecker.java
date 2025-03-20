import java.util.Scanner;

public class sentenceCountChecker
{
    private String sentence;
    private int count;

    public sentenceCountChecker(String sentence,int count)
    {
        this.sentence=sentence;
        this.count=count;
    }

    public String charChecker()
    {
        String updatedText="";
        for(int i=0;i<sentence.length();i++)
        {
            if(i>=count)
            {
                updatedText+="\u001B[31m"+sentence.charAt(i);
            }
            else{
                updatedText+=sentence.charAt(i);
            }
        }
        return updatedText;
    }

    public static void main(String[] args)
    {
        Scanner scan=new Scanner(System.in);
        String sentence=scan.nextLine();
        sentenceCountChecker obj=new sentenceCountChecker(sentence,10);
        System.out.println(obj.charChecker());
    }
}
