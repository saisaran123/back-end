import java.util.Scanner;
public class CaseTransformer {
    public boolean isValid(String input)
    {
        if(input.isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean isAlphabet(String input)
    {
        boolean returnval=false;
        for(int i=0;i<=input.length()-1;i++)
        {
            int ischar=input.charAt(i);
            if((ischar >= 65 && ischar <= 90) || (ischar >= 97 && ischar <= 122))
            {
              return true;
            }

        }
        return returnval;
    }

    public String  converter(String input)
    {
        input=input.toLowerCase().trim();
        String output="";
        boolean flag = true;
        for(int i=0;i<input.length();i++)
        {
            char Travers=input.charAt(i);
            if (Travers==' '){
                flag=true;
                continue;
            }

            else{
                if(flag)
                {
                    char next=input.charAt(i);
                    output+=(char)(next-32);
                    flag = false;
                }
                else
                {
                    output+=Travers;
                }
            }
        }
        return output;
    }



    //converting to pascalcase

    public String  TransformToPascalCase(String input)
    {


        if(!(isValid(input)&& isAlphabet(input)))
        {
            System.out.println("Invalid input.....");
        }


        return converter(input);
    }




//converting the given string into camalcase

    public String  toCamelCase(String input)
    {

        String fin =converter(input);
        String first=fin.substring(1);
        String second=fin.substring(0,1).toLowerCase();

        String converted=second+first;

        if(!(isValid(input)&& isAlphabet(input)))
        {
            System.out.println("Invalid input.....");
        }
    return converted;
    }





    //converting the given string into swap case
    public String toswapcase(String input)
    {
        String output="";
        if(!(isValid(input)&& isAlphabet(input)))
        {
            System.out.println("Invalid Syntax...");
        }
        else
        {
            for(int i=0;i<input.length();i++)
            {
                char travers=input.charAt(i);
                if(travers>=65 && travers<=90)
                {
                    output+=(char)(travers+32);
                } else if (travers>=97 && travers<=122)
                {
                    output+=(char)(travers-32);
                }
                else{
                    output+= travers;
                }

            }
        }
        return output;
    }



    // converting the given string into snake_case
    public String toSnakeCaseConverter(String input)
    {
        String output="";
        if(!(isValid(input)&& isAlphabet(input)))
        {
            System.out.println("Invalid Syntax...");
        }
        else
        {
            for(int i=0;i<input.length();i++)
            {
                char travese=input.charAt(i);
                if(travese==' ')
                {
                    travese='_';
                    output+=travese;
                }
                else
                {
                    output+=travese;
                }
            }
        }
        return output;
    }






    public static void main(String args[])
    {
        CaseTransformer obj =new CaseTransformer();
        System.out.print("Enter the String  :");
        Scanner sc=new Scanner(System.in);
        String input=sc.nextLine();
        System.out.println("PascalCase :"+obj.TransformToPascalCase(input));
        System.out.println("camalCase :"+obj.toCamelCase(input));
        System.out.println("SwAp CaSe :"+obj.toswapcase(input));
        System.out.println("snake_case :"+obj.toSnakeCaseConverter(input));

    }


}
