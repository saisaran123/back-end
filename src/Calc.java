public class Calc
{
    public void PrintSum(int a,int b)
    {
        int sum=a+b;
        System.out.println("Sum :"+sum);
    }

    public void PrintDifference(int a,int b)
    {
        int sum=a-b;
        System.out.println("Difference :"+sum);
    }

    public void PrintProduct(int a,int b)
    {
        int sum=a*b;
        System.out.println("Product :"+sum);
    }

    public static void main(String args[])
    {
        Calc obj =new Calc();
        obj.PrintSum(10,20);
        obj.PrintDifference(20,10);
        obj.PrintProduct(10,20);
    }
}
