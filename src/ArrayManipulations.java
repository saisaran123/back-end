import java.lang.reflect.Array;
import java.util.Arrays;
class ArrayManipulations {
    int[] sourceArr = new int[] {95,10, 45, 68,68, 94,2, 34,45};



    public int min()
    {
        int minimum=sourceArr[0];
        for(int i=0;i<sourceArr.length;i++)
        {
            if(sourceArr[i]<minimum)
            {
                minimum=sourceArr[i];
            }
        }
        return minimum;
    }



    public int max()
    {
        int maximum=sourceArr[0];
        for(int i=0;i<sourceArr.length;i++)
        {
            if(sourceArr[i]>maximum)
            {
                maximum=sourceArr[i];
            }
        }
        return maximum;
    }


    public int[] add(int[] destArr)
    {
        int[] resultArr=new int[sourceArr.length];
        if(sourceArr.length==destArr.length)
        {
            for(int i=0;i<sourceArr.length;i++)
            {
                resultArr[i]=sourceArr[i]+destArr[i];
            }
        }
        return resultArr;
    }


    public int[] sortAsc()
    {
        int[] sorted=sourceArr;
        for(int i=0;i<sourceArr.length;i++)
        {
            for(int j=i+1;j<sourceArr.length;j++)
            {
                if(sourceArr[i]>sourceArr[j])
                {
                    int temp=sourceArr[i];
                    sorted[i]=sourceArr[j];
                    sorted[j]=temp;
                }
            }
        }
        return sorted;
    }




    public int[] sortDec()
    {
        int[] sorted=sourceArr;
        for(int i=0;i<sourceArr.length;i++)
        {
            for(int j=i+1;j<sourceArr.length;j++)
            {
                if(sourceArr[i]<sourceArr[j])
                {
                    int temp=sorted[i];
                    sorted[i]=sorted[j];
                    sorted[j]=temp;
                }
            }
        }
        return sorted;
    }



    public int[] copyArray()
    {
        return sourceArr.clone();

    }


    public void findIndex(int element)
    {
        // Find the index of an array element.
        int j=0;
        for(int i=0;i<sourceArr.length;i++)
        {
            int eachval=sourceArr[i];
            if(eachval==element)
            {
                System.out.println("The Given Element Present in the index number "+i);
                j++;
            }
        }
        if(j==0)
        {
            System.out.println("The given Element does not exists");
        }

    }


    public boolean findElement(int element)
    {
        // Find if the specific element is present in the array.
        boolean isthere=false;
        for(int i:sourceArr)
        {
            if(i==element)
            {
                isthere=true;
                break;
            }
        }
        return isthere;
    }


    public int[] sub(int[] elementsToRemove)
    {

        int count = 0;
        for (int element : sourceArr) {
            boolean shouldRemove = false;
            for (int toRemove : elementsToRemove) {
                if (element == toRemove) {
                    shouldRemove = true;
                    break;
                }
            }
            if (!shouldRemove) {
                count++;
            }
        }

        int[] resultArray = new int[count];
        int index = 0;

        for (int element : sourceArr) {
            boolean shouldRemove = false;
            for (int toRemove : elementsToRemove) {
                if (element == toRemove) {
                    shouldRemove = true;
                    break;
                }
            }
            if (!shouldRemove) {
                resultArray[index++] = element;
            }
        }

        return resultArray;
    }

    public int countOccurrences(int element)
    {
//        // Counts the number of occurrences of a specific element in the source array.
            int count=0;
            for(int i:sourceArr)
            {
                if(i==element)
                {
                    count++;
                }
            }
            return count;
    }

    public int[] removeDuplicate()
    {
        int count = sourceArr.length;
        for(int i : sourceArr){
            int elementCount = countOccurrences(i) - 1;
            count = count - elementCount;
        }

        int[] result = new int[count];
        int index = 0;
        for(int i: sourceArr){
            boolean isUnqie = true;
            for(int j :result){
                if(i==j){
                    isUnqie = false;
                    break;
                }
            }

            if(isUnqie && count>0){
                result[index++] = i;
                count--;
            }
        }

        return result;
    }


    public static void main(String[] args) {

        ArrayManipulations obj=new ArrayManipulations();


    }
}
