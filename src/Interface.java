


class Mobile {

    public void current() {
        System.out.println("takes low current");
    }
    public void circuites() {
        System.out.println("using IC's");
    }
}

class Generator {

    public void current() {
        System.out.println("use more current");
    }

    public void circuites() {
        System.out.println("more IC's were used");
    }
}

public class Interface
{

    public static void main(String[] args) {
        Generator generator=new Generator();
        generator.current();
    }
}

