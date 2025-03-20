import java.util.Scanner;

public class LatinSquare {
    Scanner scan=new Scanner(System.in);
    int order;
    private int[][] arr;


    public boolean latinSquare(){


//        2 3 1 4     1 2
//        4 1 3 2     2 1
//        3 4 2 1
//        1 2 4 3

        boolean isTrue=true;

            for(int i=0;i<order;i++){

                for(int j=0;j<order;j++){

                    if(!checker(arr[i][j],i,j)){
                        isTrue=false;
                    }

                }

            }

        return  isTrue;
    }

    public boolean checker(int n,int i,int j){
        System.out.println("\n\n");
        for(int k=0;k<order;k++)
        {
            if( (arr[i][k] == n && k != j) || (arr[k][j] == n && k!=i)){
                return false;
            }
        }
        return true;
    }


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
    public void display()
    {
        System.out.println("The Given Board :\n");
        for(int i=0;i<order;i++){
            for(int j=0;j<order;j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
        if(latinSquare()){

            System.out.println("******************************");
            System.out.println("|  Yes This is latin square  |");
            System.out.println("******************************");

        }
        else{
            System.out.println("**********************************");
            System.out.println("|  No This is Not a latin square  |");
            System.out.println("**********************************");
        }
    }


    public static void main(String[] args) {
        LatinSquare obj=new LatinSquare();
        obj.userInput();
        obj.display();
    }
}
