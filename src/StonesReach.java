import java.util.Scanner;
public class StonesReach
{
    public void ReachingStones(int number)
    {
        for(int i=0;i<=number;i++)
        {
            System.out.println(("You Reached the "+i+" Stone\n ").repeat(i));
        }
    }

    public static void main(String args[])
    {
        System.out.print("Number of Stones  ?  :");
        StonesReach obj=new StonesReach();
        Scanner sc=new Scanner(System.in);
        int total=sc.nextInt();
        obj.ReachingStones(total);


    }
}
