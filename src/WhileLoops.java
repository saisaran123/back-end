import java.util.Scanner;
public class WhileLoops
{

    public void EvenNumbers()
    {
        int i=0;
        while (i<=20)
        {
            System.out.println(i);
            i+=2;
        }
    }

    public int SumOfDigits(int dig)
    {
        int sum=0;
        while (dig!=0)
        {
            int rem=dig%10;
            sum+=rem;
            dig=dig/10;
        }
        return sum;
    }



    public int Gcd(int num1,int num2)
    {
        int Gcd=1;
        int i =2;
        while (i<=Math.min(num1,num2))
        {
            if(num1%i==0 && num2%i==0)
            {
                Gcd=i;

            }
            i++;
        }
        return Gcd;
    }

    public int NumberOfDigits(int num)
    {
        int total=0;
        while (num!=0)
        {
            int rem=num%10;
            total++;
            num/=10;
        }
        return total;
    }

    public void MultiplicationTable()
    {
        Scanner Scan=new Scanner(System.in);
        System.out.println("Enter The Number I will print the Mtable  :");
        int Table=Scan.nextInt();
        int i=1;
        while (i<=12)
        {
            System.out.println(i+"x"+Table+"="+(i*Table));
            i++;
        }

    }


    public int SmallestDigit(int small)
    {
        int smallest=0;
        int number=9;
        while (small!=0)
        {
            int rem=small%10;
            if(rem<=number)
            {
                smallest=rem;
                number=rem;
            }
            small/=10;
        }
        return smallest;

    }

    public int SumOfSquarOfFirstNaturalNumber(int n) {
        int i = 0;
        int sum=0;
        while (i<=n)
        {
            sum+=i*i;
            i++;
        }
        return sum;
    }

    public static void main(CaseTransformer args[])
    {


        WhileLoops obj=new WhileLoops();
//        obj.EvenNumbers();
//        System.out.println(obj.SumOfDigits(1923));
//        System.out.println(obj.Gcd(2,3));
//        System.out.println(obj.NumberOfDigits(123));
        System.out.println(obj.SmallestDigit(7865));
//        obj.MultiplicationTable();
//          System.out.println(obj.SumOfSquarOfFirstNaturalNumber(5));

    }
}
