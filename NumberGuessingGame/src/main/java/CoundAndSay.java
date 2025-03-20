
import java.util.*;
public class CoundAndSay {
    public static void main(String[] args) {
        System.out.println(countNumber("33322514488"));
    }
    public static String countNumber(String num){
        Map<Character,Integer> map = new HashMap<>();
        List<Character> ref = new ArrayList<>();
        for (char i : num.toCharArray()) {
            if(map.containsKey(i)){
                map.put(i,map.get(i)+1);
            }
            else{
                ref.add(i);
                map.put(i,1);
            }
        }
        StringBuilder output = new StringBuilder();
        for (char i : ref){
            int count = map.get(i);
            output.append(count+""+i);
        }
        return output.toString();
    }
}
