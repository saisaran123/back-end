import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ArrayCreator
{
    Scanner scan=new Scanner(System.in);
    public void chooseOption()
    {

        System.out.println("Enter One For Linear Array");
        System.out.println("Enter Two For Random Array");
        int option =scan.nextInt();
        if(option==1)
        {
            System.out.println(Arrays.toString(linearArray()));
        }
        if(option==2)
        {
            System.out.println(Arrays.toString(randomArray()));
        }
        else
        {
            System.out.println("provide Valid input");
        }

    }




    public int getStartValue()
    {
        System.out.print("Enter The start Value :");
        int start=scan.nextInt();
        return start;
    }

    public int getEndValue()
    {
        System.out.print("Enter The End Value :");
        int end=scan.nextInt();
        return end;
    }

    public int getStepValue()
    {
        System.out.print("Enter The Step Value :");
        int step=scan.nextInt();
        return step;
    }

    public int getLengthOfArray()
    {
        System.out.print("Enter The Length of the Array :");
        int end=scan.nextInt();
        return end;
    }


    public char getLeftArraySymbol()
    {
        System.out.print("Enter The Left Array Symbol :");
        char symbol=scan.next().charAt(0);
        return symbol;
    }

    public char getRighttArraySymbol()
    {
        System.out.print("Enter The Right Array Symbol :");
        char symbol=scan.next().charAt(0);
        return symbol;
    }

    public char getArraySeperator()
    {
        System.out.print("Enter The Right Array Symbol :");
        char sep=scan.next().charAt(0);
        return sep;
    }

    public int[] linearArray()
    {

        int start=getStartValue();
        int step=getStepValue();
        int length=getLengthOfArray();
        int[] result=new int[length];
        result[0]=start;
        for(int i=1;i<result.length;i++)
        {
            result[i]=result[i-1]+step;
        }
        return result;

    }

    public int[] randomArray()
    {
        int start=getStartValue();
        int end=getEndValue();
        int length=getLengthOfArray();
        Random var=new Random();
        int[] result=new int[length];
        for(int i=0;i<result.length;i++)
        {
            result[i]=var.nextInt(start,end);
        }
        return result;
    }



    public static void main(String[] args)
    {
        ArrayCreator obj=new ArrayCreator();
        obj.chooseOption();
    }
}
