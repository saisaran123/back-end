package Visualizer;

import java.util.Scanner;
import java.util.Random;

public class captchaMaker {
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();
    String characterSet = "abcdefghijklmnopqrstuvwxyz1234567890";
    int length = 5;
    String color = "\033[38;5;18m";
    String backgroundColor = "\033[48;5;250m";
    String defaul = "\033[0m";
    String captcha;

    public void setLength(int length) {
        this.length = length;
    }
    captchaMaker(){
        int length = (rand.nextInt(6)+5);
        setLength(length);
        captcha = captchaGenerater();
    }

    public String getCaptcha() {
        return captcha;
    }

    public String captchaGenerater(){
        int num;
        String capcha="  ";
        for (int i=0;i<this.length;i++){
            num = rand.nextInt(characterSet.length());
            capcha += characterSet.charAt(num);
        }
        return capcha+"  ";
    }
    public void display(){
        System.out.println(backgroundColor+repeat(" ",captcha.length())+defaul);
        System.out.println(backgroundColor+color+captcha+defaul);
        System.out.println(backgroundColor+repeat(" ",captcha.length())+defaul);
    }
    public String repeat(String e,int count){
        String output = "";
        for (int i = 0; i < count; i++) {
            output+=e;
        }
        return output;
    }
}
