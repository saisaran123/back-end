import java.util.Calendar;
import java.util.Scanner;

public class DateValidater {
    public static void main(String[] args) {
        DateValidater obj=new DateValidater();
        String[] temp=obj.getEachValue();
        int Year=Integer.parseInt(temp[0]);
        int Month=Integer.parseInt(temp[1]);
        int Day=Integer.parseInt(temp[2]);
        isValid validator = new isValid(Year,Month-1,Day);
        System.out.println(validator);
    }

    public String getInput()
    {
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter The Date in the format of yyyy-mm-dd");
        String input=scan.nextLine();
        return input;
    }

    public String[]  getEachValue() {
        String input = getInput();

        String[] temp = null;
        if (input.contains("-")) {

            temp = input.split("-");


        }
        return temp;
    }
}


class isValid {
    private int Year;
    private int Month;
    private int Day;
    private boolean isValidDate;

    public isValid(int Year, int Month, int Day) {
        this.Year = Year;
        this.Month = Month;
        this.Day = Day;

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        try {
            calendar.set(Year, Month, Day);
            calendar.getTime();
            this.isValidDate = true;
        } catch (Exception e) {
            this.isValidDate = false;
        }
    }


    public String toString() {
        String val = "Not Valid";
        if (isValidDate) {
            val = "Valid";
        }
        return val;
    }
}
