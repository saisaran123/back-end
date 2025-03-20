import java.util.Stack;

public class History
{
    Stack<Site> historyOfSites=new Stack<>();

    public void showEntireHistory(){
        for(Site site : historyOfSites){
            System.out.println(site);
        }
    }

    public void goBackToPrevWebsite(){
        historyOfSites.pop();
        if(!historyOfSites.isEmpty()){
            System.out.println(historyOfSites.peek());
        }
        else {
            System.out.println("Nothing is there!");
        }

    }
    public static void main(String[] args) {
        History stack=new History();
        stack.historyOfSites.add(new Site("one.zoho.com","Zoho"));
        stack.historyOfSites.add(new Site("one.google.com","Google"));

        stack.goBackToPrevWebsite();
        stack.goBackToPrevWebsite();



    }

}

class Site{
    private String urlName;
    private String siteName;

    Site(String urlName,String siteName){
        this.siteName=siteName;
        this.urlName=urlName;
    }


    public String toString() {
        return "URL Name :"+urlName+"\nSITE Name :"+siteName+"\n";
    }
}
