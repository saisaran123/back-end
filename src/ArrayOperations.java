public class ArrayOperations
{

    public int evenCount(int a[]){
        int count = 0;
        for(int i : a){
            if(i%2==0){

                count++;
            }
        }
        return count;
    }

    public int oddCount(int a[]){
        int count = 0;
        for(int i : a){
            if(i%2==1){
                count++;
            }
        }
        return count;
    }



    public static void main(String args[])
    {

    }
}
