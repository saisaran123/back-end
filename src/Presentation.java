import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Presentation
{
    Scanner scan=new Scanner(System.in);
    LinkedList<Slider> slides=new LinkedList();
    public static void main(String[] args) {
        Presentation obj=new Presentation();

        obj.slides.add(new Slider("Dinesh",4));
        obj.slides.add(new Slider("Dinesh karthick",3));
        ListIterator<Slider> itr= obj.slides.listIterator();
        while (true){
            System.out.print("press 1 to mo.ve forward 2 to move previous :");
            String ch=obj.scan.nextLine();
            if(ch.equals("1")){
                if(itr.hasNext()){
                    System.out.println(itr.next());
                }
                else{
                    System.out.println("No Next Elem");
                }
            }
            else if(ch.equals("2")){
                if(itr.hasPrevious()){
                    System.out.println(itr.previous());
                }
                else{
                    System.out.println("No previous Elem");
                }
            }



        }

    }

}


class Slider{
    private String title;
    private int num;

    public Slider(String title,int num){
        this.title=title;
        this.num=num;
    }

    public String toString() {
        return "Title Number :"+num+"Title :"+title;
    }
}