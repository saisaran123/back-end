public class Main
{

    public int CalculateTotal(int m1,int m2,int m3)
    {
        int total=m1+m2+m3;
        return total;
    }

    public double CalculateAvg(double total)
    {
        double average=total/3;
        return average;
    }

    public void determineGrade(double average)
    {
        if(average>=60)
        {
            System.out.println("You Passed ");
        }
        else
        {
            System.out.println("You Failed");
        }
    }



    public static void main(CaseTransformer[] args)
    {
        System.out.println("Hello world!");
        Main obj=new Main();
        double sum=obj.CalculateTotal(91,92,93);
        double avg=obj.CalculateAvg(sum);
        obj.determineGrade(avg);
    }
}