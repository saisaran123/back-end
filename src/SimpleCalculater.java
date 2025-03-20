import java.lang.Math;
import java.util.Scanner;

public class SimpleCalculater
{
    Scanner scan=new Scanner(System.in);
    public int x;int y;

    public int add()
    {
        return x+y;
    }

    public int subtract()
    {
        return x-y;
    }

    public int multiply()
    {
        return x*y;
    }

    public int divide()
    {
        return x/y;
    }
    public double power()
    {
        return Math.pow(x,y);
    }

    public double sqrt()
    {
        return Math.sqrt(x);
    }

    public double hypotenuse()
    {
        return Math.hypot(x,y);
    }
    public double log()
    {
        return Math.log(x);
    }

    public void display()
    {
        System.out.println("\n\n\n");
        System.out.println("\t\t\t\t\t\t❤\uFE0F                                               ❤\uFE0F");
        System.out.println("\t\t\t\t\t\t"+"***************************************************");
        System.out.println("\t\t\t\t\t\t"+"*        press 1 for add two numbers              *");
        System.out.println("\t\t\t\t\t\t"+"*        press 2 for subtract two numbers         *");
        System.out.println("\t\t\t\t\t\t"+"*        press 3 for Multiply two numbers         *");
        System.out.println("\t\t\t\t\t\t"+"*        press 4 for Divide two numbers           *");
        System.out.println("\t\t\t\t\t\t"+"*        press 5 for sqrt a numbers               *");
        System.out.println("\t\t\t\t\t\t"+"*        press 6 for power two numbers            *");
        System.out.println("\t\t\t\t\t\t"+"*        press 7 for Hypotenuse two numbers       *");
        System.out.println("\t\t\t\t\t\t"+"*        press 8 for log a numbers                *");
        System.out.println("\t\t\t\t\t\t"+"*        press 9 for to Exit                      *");
        System.out.println("\t\t\t\t\t\t"+"***************************************************");
        System.out.println("\n\n\n");
    }

    public void getInput()
    {
        System.out.println("\n\n");
        System.out.println("\t\t\t\t\t\t****************************");
        System.out.print("\t\t\t\t\t\tEnter Number 1 : ");
        x=scan.nextInt();
        System.out.print("\t\t\t\t\t\tEnter Number 2 : ");
        y=scan.nextInt();
        System.out.println("\t\t\t\t\t\t****************************");

    }

    public void disp()
    {
        getInput();
        display();
        calc();
    }

    public void calc()
    {
        int cases;
        System.out.println("\n\n");
        System.out.println("\t\t\t\t\t\t****************************");
        System.out.print("\t\t\t\t\t\tEnter the operartion  :");
        cases=scan.nextInt();
        System.out.println("\t\t\t\t\t\t***************************");
        switch (cases)
        {
            case 1:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+add()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                disp();
                break;
            case 2:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+subtract()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                disp();
            case 3:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+multiply()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                disp();
                break;
            case 4:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+divide()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                disp();
                break;
            case 5:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+sqrt()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                disp();
                break;
            case 6:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+power()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                break;
            case 7:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+hypotenuse()+" ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t*******");
                disp();
                break;
            case 8:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*******");
                System.out.println("\t\t\t\t\t\t❤\uFE0F "+log()+" ❤\uFE0F ");
                System.out.println();
                System.out.println("\t\t\t\t\t\t*******");
                disp();
                break;
            case 9:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t*********************");
                System.out.println("\t\t\t\t\t\t❤\uFE0F THANKYOU!!! ❤\uFE0F ");
                System.out.println("\t\t\t\t\t\t**********************");
                break;

            default:
                System.out.println("\n\n\n");
                System.out.println("\t\t\t\t\t\t***********************");
                System.out.println("\t\t\t\t\t\t❤\uFE0F provide Valid INPUT ❤\uFE0F");
                System.out.println("\t\t\t\t\t\t***********************");
        }
    }


    public static void main(String[] args)
    {
        SimpleCalculater obj=new SimpleCalculater();
        obj.disp();
    }
}
