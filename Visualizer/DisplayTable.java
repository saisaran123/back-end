package Visualizer;

import java.util.ArrayList;

public class DisplayTable {
    ArrayList<String> heading;
    ArrayList<ArrayList<String>> content;
    ArrayList<Integer> maxColWidths;
    String tableName;

    public int sum(){
        int sum=0;
        for(int i:maxColWidths){
            sum+=i;
        }
        return  sum+2*maxColWidths.size()+1;
    }

    public void line(int n){
        for(int i=0;i<n;i++){
            System.out.print("-");
        }

    }

    public void display(){
        int sum=sum()+2*maxColWidths.size();
        for(int ini=0;ini<(sum/2)-(tableName.length()/2)-4;ini++){
            System.out.print(" ");
        }
        System.out.print("*");
        System.out.print("*");
        System.out.print("* ");
        System.out.print(tableName);
        System.out.print(" *");
        System.out.print("*");
        System.out.print("*");

        System.out.println();
        line(sum);
        System.out.println();
        System.out.print("|");

        for(int i=0;i<heading.size();i++){
            String  name=heading.get(i);
            System.out.print(" ");
            System.out.print(" ");
            System.out.print(name);
            System.out.print(" ");

            for(int j=0;j<maxColWidths.get(i)-name.length();j++){
                System.out.print(" ");
            }
            System.out.print("|");
        }
        System.out.println();
        line(sum);

        for(int k=0;k< content.size();k++){
            System.out.println();

            System.out.print("|");
            for(int l=0;l<content.get(k).size();l++){
                String name=content.get(k).get(l);
                System.out.print(" ");
                System.out.print(" ");
                System.out.print(name);
                System.out.print(" ");


                for(int s=0;s<maxColWidths.get(l)-name.length();s++){
                    System.out.print(" ");
                }
                System.out.print("|");
            }
        }
        System.out.println();
        line(sum);

    }

    public DisplayTable(ArrayList<ArrayList<String>> content,ArrayList<String> heading,ArrayList<Integer> maxColWidths,String tableName){
        this.heading=heading;
        this.content=content;
        this.maxColWidths=maxColWidths;
        this.tableName=tableName;
        this.display();

    }
}
