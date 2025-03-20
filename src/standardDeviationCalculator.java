import java.lang.Math;
import java.util.Arrays;
import java.text.DecimalFormat;

public class standardDeviationCalculator
{


    public int count(int arr[])
    {
        int count=0;
        for(int i=0;i<arr.length;i++)
        {
            count++;
        }
        return count;
    }

    public int sum(int arr[])
    {
        {

            int sum=0;
            for(int i=0;i<arr.length;i++)
            {
                sum+=arr[i];
            }
            return sum;
        }
    }



    public double mean(int arr[])
    {
        double counting=count(arr);
        double avg=sum(arr)/counting;
        return avg;
    }

    public float round(double num){
        return (float) (Math.round(num * 100) / 100.0);
    }

    public float[] differnce(int arr[])
    {

        double myavg=mean(arr);
        float[] result=new float[arr.length];
        for (int i=0;i< arr.length;i++)
        {


            result[i]=round((arr[i]-myavg));


        }
        return result;
    }

    public float[] differnceSquare(int arr[])
    {

        float[] result=new float[arr.length];
        float[] mydiff=differnce(arr);
        for (int i=0;i< arr.length;i++)
        {
            result[i]=round(mydiff[i]*mydiff[i]);
        }

        return result;

    }

    public float sumOfDifferncesSquare(int arr[])
    {
        float sum=0;
        float[] mysquares=differnceSquare(arr);
        for (int i=0;i< arr.length;i++)
        {
            sum+=mysquares[i];
        }
        return sum;

    }


    public float variance(int arr[])
    {
        float result =sumOfDifferncesSquare(arr)/count(arr);
        return result;
    }

    public float standardDeviation(int arr[])
    {
        float myvarience=variance(arr);
        double result=Math.pow(myvarience,0.5);
        return (float)result;

    }


    public void showMenu()
    {
        int[] lst = {-5,1,8,7,2};
        System.out.println("                    _______________________________________________________________");
        System.out.println("                    |  Count - " + count(lst)                                    );
        System.out.println("                    |  Sum - " + sum(lst)                                        );
        System.out.println("                    |  Mean - " + mean(lst)                                      );
        System.out.println("                    |  Differences - " + Arrays.toString(differnce(lst))         );
        System.out.println("                    |  Differences^2 - " + Arrays.toString(differnceSquare(lst)) );
        System.out.println("                    |  Sum of Diffeences^2 - " + sumOfDifferncesSquare(lst)      );
        System.out.println("                    |  Variance - " + variance(lst)                              );
        System.out.println("                    |  Standard Deviation - " + standardDeviation(lst)           );
        System.out.println("                    ---------------------------------------------------------------");


    }




    public static void main(String[] args)
    {
        standardDeviationCalculator obj=new standardDeviationCalculator();
        obj.showMenu();
    }

}

