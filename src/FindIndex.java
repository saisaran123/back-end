import java.util.Arrays;

public class FindIndex
{
    int[] arr={10,20,2,20,76,80,54,10,20};

    public int findFirstIndex(int element)
    {
        int output=-1;
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]==element)
            {
                output=i;
                break;

            }
        }
        return output;
    }

    public int findLastIndex(int element)
    {
        int output=-1;
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]==element)
            {
                output=i;


            }
        }
        return output;
    }

    public int[] findAllIndices(int element)
    {
        int totalelem=0;
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]==element)
            {
                totalelem++;
            }
        }

        int[] result=new int[totalelem];
        int k=0;
        for(int j=0;j<arr.length;j++)
        {
            if(arr[j]==element)
            {
                result[k]=j;
                k++;
            }
        }
        return result;
    }





    public static void main(String[] args)
    {
        FindIndex obj=new FindIndex();
        System.out.println(obj.findFirstIndex(20));
        System.out.println(obj.findLastIndex(20));
        System.out.println(Arrays.toString(obj.findAllIndices(20)));
    }
}
