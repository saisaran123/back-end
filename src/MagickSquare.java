import java.util.Scanner;

public class MagickSquare
{
    Scanner scan=new Scanner(System.in);
    int order;
    private int[][] arr;


    public void userInput(){
        System.out.print("Enter the order (example 3) :");
        order= scan.nextInt();
        arr=new int[order][order];
        scan.nextLine();
        for(int i=0;i<order;i++){
            for(int j=0;j<order;j++){
                System.out.println("enter value for ("+i+","+j+") :");
                arr[i][j]=scan.nextInt();
                scan.nextLine();
            }

        }

    }


    public boolean geteach(){
        boolean isTrue=true;
        for(int i=0;i<order;i++){
            for(int j=0;j<order;j++){
                if(!(isUniq(arr[i][j],i,j))){
                     isTrue=false;
                     break;
                }
            }
        }
        return isTrue;
    }


    public boolean isUniq(int n,int k,int l)
    {
        for(int i=0;i<order;i++){
            for(int j=0;j<order;j++){

                if( i ==k && l==j){
                    continue;
                }
                if( (arr[i][j] == n )){
                    return false;
                }
            }
        }
        return true;
    }

    public void disp()
    {
        if(geteach())
        {
            System.out.println("Yes");
        }
        else{
            System.out.println("No");
        }
    }


    public static void main(String[] args) {
        MagickSquare obj=new MagickSquare();
        obj.userInput();
        obj.disp();
    }
}
