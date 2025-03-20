    import java.util. Scanner;

    public class StringUtilities {
        Scanner scan = new Scanner(System.in);

        public String getInput() {
            System.out.println("Enter the paragraph (type ':wq' to finish):");
            StringBuilder input = new StringBuilder();
            while (true) {
                String line = scan.nextLine();
                if (line.equals(":wq"))
                {
                    break;
                } else
                {
                    input.append(line).append("\n");
                }
            }
            return input.toString();
        }


        public void showMenu() {
            System.out.println("Press 1 for String info");
            System.out.println("Press 2 for convert to uppercase");
            System.out.println("Press 3 for convert to lowercase");
            System.out.println("Press 4 to search any text");
            System.out.println("Press 5 to print the reverse");
            System.out.println("Press 6 for for split the sentence ");
            System.out.println("Press 7 for exit");

        }

        public void inputDetails(String input) {
            System.out.println("Total Number Of Words  :"+wordCounter(input));
            System.out.println("Total Number Of Character  :"+charCount(input));
            System.out.println("Total Number Of Lines  :"+lineCount(input));

        }

        public int wordCounter(String input)
        {
            int words=1;
            if(input.isBlank())
            {
                return 0;
            }
            for(int i=0;i<input.length()-1;i++)
            {
                if((input.charAt(i+1)==' ' && input.charAt(i+2)!=' ' && input.charAt(i+2)!='\n' || input.charAt(i)=='\n')  )
                {
                    words++;
                }
            }
            return words;
        }

        public int charCount(String input)
        {
            int linecount=0;
            for(int i=0;i<input.length();i++)
            {
                linecount++;
            }
            return linecount-1;
        }

        public int lineCount(String input)
        {int lines=1;
            for(int i=0;i<input.length();i++)
            {

                char c=input.charAt(i);
                if(c=='\n')
                {
                    lines++;
                }
            }
            return lines-1;
        }

        public void contains(String input, String search) {
            boolean flag=false;
            StringBuilder temp= new StringBuilder();
            int s=0;
            for(int i=0;i<input.length();i++)
            {

                if(temp.toString().equals(search))
                {
                    flag=true;
                    break;
                }
//                char c=input.charAt(i);
                if(input.charAt(i)==search.charAt(s))
                {
                    temp.append(search.charAt(s));
                    s++;

                }
                else {
                    s=0;
                    temp = new StringBuilder();
                }




            }
            if(flag)
            {
                System.out.println("The word found!!");
            }
            else {
                System.out.println("The given word not found!!");
            }

        }


        public String toLowerCase(String input) {
            StringBuilder fin= new StringBuilder();
            for(int i=0;i<input.length();i++)
            {
                char c=input.charAt(i);
                if(c>='A' && c<='Z')
                {
                    fin.append((char) (c + 32));
                }
                else {
                    fin.append(c);
                }

            }
            return fin.toString();

        }

        public String toUpperCase(String input) {
            StringBuilder fin= new StringBuilder();
            for(int i=0;i<input.length();i++)
            {
                char c=input.charAt(i);
                if(c>='a' && c<='z')
                {
                    fin.append((char) (c - 32));
                }
                else {
                    fin.append(c);
                }

            }
            return fin.toString();
        }

        public String reverse(String input) {
            StringBuilder rev= new StringBuilder();
            for(int i=input.length()-1;i>=0;i--)
            {

                char c=input.charAt(i);
                rev.append(c);
            }
            return rev.toString();

        }

        public void splitSentence(String input, char delimiter) {
            for(int i=0;i<input.length();i++)
            {
                char c=input.charAt(i);
                System.out.print(c);
                if(c==delimiter)
                {
                    System.out.print("\n");
                }

            }
            System.out.println("\n");


        }

//        public void substringCount


        public void execution(String input) {
            while (true) {
            showMenu();
            int value = scan.nextInt();
            scan.nextLine();
                switch (value) {
                    case 1:
                        inputDetails(input);
                        System.out.println("\n");
                        break;
                    case 2:
                        System.out.println(toUpperCase(input));
                        System.out.println("\n");
                        break;
                    case 3:
                        System.out.println(toLowerCase(input));
                        System.out.println("\n");
                        break;
                    case 4:
                        System.out.print("what do you want to search :");
                        String search = scan.nextLine();
                        contains(input, search);
                        System.out.println("\n");
                        break;

                    case 5:
                        System.out.println(reverse(input));
                        System.out.println("\n");
                        break;
                    case 6:
                        System.out.print("How do you want to split  :");
                        String delimiter = scan.next();
                        splitSentence(input, delimiter.charAt(0));
                        System.out.println("\n");
                        break;
                    case 7:
                        System.out.println("Thank you visit again");
                        break;
                    default:
                        System.out.println("Please provide valid input....");
                        System.out.println("\n");


                }
                if (value==7)
                {

                    break;
                }

            }
        }





        public static void main(String[] args   )
        {
            StringUtilities obj =new StringUtilities();
            String para= obj.getInput();
            obj.execution(para);
        }
    }
