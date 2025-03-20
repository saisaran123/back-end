import java.util.Arrays;
import java.util.Scanner;

public class ChessMaker {
    Scanner scan = new Scanner(System.in);

    public String BLACK_BRIGHT_BG = "\033[100m";
    public String RED_BRIGHT_BG = "\033[101m";
    public String GREEN_BRIGHT_BG = "\033[102m";
    public String YELLOW_BRIGHT_BG = "\033[103m";
    public String BLUE_BRIGHT_BG = "\033[104m";
    public String WHITE_BRIGHT_BG = "\033[107m";
    public String RESET = "\033[0m";  // Reset color


    public String RED_TEXT = "\033[31m";
    public String YELLOW_TEXT = "\033[33m";
    public String GREEN_TEXT = "\033[32m";

    String first = BLACK_BRIGHT_BG;
    String second = WHITE_BRIGHT_BG;
    int xAxis = 0;
    int yAxis = 0;
    String smCircle = " ○ ";

    String[][] values = new String[8][8];

    public void chessMake() {
        for (String[] value : values) {
            Arrays.fill(value, "   ");
        }
    }

    int k = 0;

    public void displayChessBox() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (k % 2 == 0) {
                    System.out.print(first + values[i][j] + RESET);
                    k++;
                } else {
                    System.out.print(second + values[i][j] + RESET);
                    k++;
                }
            }
            k++;
            System.out.println();
        }
    }


    public void changeColor() {
        System.out.println(YELLOW_TEXT + "1. Black - Red" + RESET);
        System.out.println(YELLOW_TEXT + "2. Green - White" + RESET);
        System.out.println(YELLOW_TEXT + "3. Red - White" + RESET);
        System.out.println(YELLOW_TEXT + "4. Blue - White" + RESET);
        System.out.println(YELLOW_TEXT + "5. Green - Red" + RESET);
        System.out.println(YELLOW_TEXT + "6. Yellow - Blue" + RESET);
        System.out.print(YELLOW_TEXT + "Enter Your Choice: " + RESET);
        int choice = scan.nextInt();
        scan.nextLine();

        switch (choice) {
            case 1:
                first = BLACK_BRIGHT_BG;
                second = RED_BRIGHT_BG;

                break;
            case 2:
                first = GREEN_BRIGHT_BG;
                second = WHITE_BRIGHT_BG;

                break;
            case 3:
                first = RED_BRIGHT_BG;
                second = WHITE_BRIGHT_BG;
                break;
            case 4:
                first = BLUE_BRIGHT_BG;
                second = WHITE_BRIGHT_BG;
                break;
            case 5:
                first = GREEN_BRIGHT_BG;
                second = RED_BRIGHT_BG;
                break;
            case 6:
                first = YELLOW_BRIGHT_BG;
                second = BLUE_BRIGHT_BG;

                break;
            default:
                System.out.println(RED_TEXT + "Invalid input, try again." + RESET);
                changeColor();
        }
    }


    public void placeAllCoins()
    {

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if((i==0 && j==0) || (i==0 && j==7) || (i==7 && j==0) || (i==7 && j==7))
                {
                    values[i][j]=" ♜ ";
                }
                if((i==0 && j==1) || (i==0 && j==6) || (i==7 && j==1) || (i==7 && j==6))
                {
                    values[i][j]=" ♞ ";
                }
                if((i==0 && j==2) || (i==0 && j==5) || (i==7 && j==2) || (i==7 && j==5))
                {
                    values[i][j]=" ♝ ";
                }

                if((i==0 && j==3) || (i==7 && j==3) )
                {
                    values[i][j]=" ♛ ";
                }

                if((i==0 && j==4) || (i==7 && j==4) )
                {
                    values[i][j]=" ♚ ";
                }

                if((i==1 || i==6) )
                {
                    values[i][j]=" ♟ ";
                }
            }
        }
    }



    public void pawnMoves()
    {
        String pawn=" ♙ ";

            if(xAxis==6 && yAxis>=0)
            {
                values[xAxis][yAxis]=pawn;
                values[xAxis-1][yAxis]=smCircle;
                values[xAxis-2][yAxis]=smCircle;
            }
            if (xAxis==0)
            {
                values[xAxis][yAxis]=pawn;
            }
            else
            {
                values[xAxis][yAxis] = pawn;
                values[xAxis - 1][yAxis] = smCircle;
            }
    }


    public void rookMoves()
    {
        String rook=" ♖ ";
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(xAxis==i || yAxis==j)
                {
                    values[i][j]=smCircle;
                }
            }
        }
        values[xAxis][yAxis]=rook;
    }

    public void bishopMoves() {
        String bishop = " ♗ ";

        for (int i = xAxis , j = yAxis ; i >= 0 && j >= 0; i--, j--) {
            values[i][j] = smCircle;
        }

        for (int i = xAxis , j = yAxis; i >= 0 && j < 8; i--, j++) {
            values[i][j] = smCircle;
        }

        for (int i = xAxis, j = yAxis; i < 8 && j >= 0; i++, j--) {
            values[i][j] = smCircle;
        }

        for (int i = xAxis, j = yAxis; i < 8 && j < 8; i++, j++) {
            values[i][j] = smCircle;
        }

        values[xAxis][yAxis] = bishop;
    }

    public void queenMoves()
    {
        String queen = " ♕ ";
        rookMoves();
        bishopMoves();
        values[xAxis][yAxis]=queen;
    }

    public void kingMoves()
    {
        String king=" ♔ ";

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(xAxis!=0 && yAxis!=0 && xAxis!=7 && yAxis!=7)
                {
                    values[xAxis-1][yAxis]=smCircle;
                    values[xAxis-1][yAxis-1]=smCircle;
                    values[xAxis][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis]=smCircle;
                    values[xAxis+1][yAxis+1]=smCircle;
                    values[xAxis][yAxis+1]=smCircle;
                    values[xAxis-1][yAxis+1]=smCircle;
                }
                else if(yAxis==0 && xAxis==0)
                {
                    values[xAxis+1][yAxis]=smCircle;
                    values[xAxis+1][yAxis+1]=smCircle;
                    values[xAxis][yAxis+1]=smCircle;
                }

                else if(xAxis==0 && yAxis==7)
                {
                    values[xAxis][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis]=smCircle;
                }
                else if(xAxis==7 && yAxis==0)
                {
                    values[xAxis-1][yAxis]=smCircle;
                    values[xAxis+1][yAxis]=smCircle;
                    values[xAxis+1][yAxis+1]=smCircle;
                }
                else if(xAxis==7 && yAxis==7)
                {
                    values[xAxis-1][yAxis]=smCircle;
                    values[xAxis-1][yAxis-1]=smCircle;
                    values[xAxis][yAxis-1]=smCircle;
                }
                else if(xAxis==0 && (yAxis>=1 && yAxis<=6))
                {
                    values[xAxis][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis]=smCircle;
                    values[xAxis+1][yAxis+1]=smCircle;
                    values[xAxis][yAxis+1]=smCircle;
                }
                else if(yAxis == 0 && xAxis >= 1)
                {
                    values[xAxis-1][yAxis]=smCircle;
                    values[xAxis+1][yAxis]=smCircle;
                    values[xAxis+1][yAxis+1]=smCircle;
                    values[xAxis][yAxis+1]=smCircle;
                    values[xAxis-1][yAxis+1]=smCircle;
                }
                else if(xAxis==7 && (yAxis>=1 && yAxis<=6))
                {
                    values[xAxis-1][yAxis]=smCircle;
                    values[xAxis-1][yAxis-1]=smCircle;
                    values[xAxis][yAxis-1]=smCircle;
                    values[xAxis][yAxis+1]=smCircle;
                    values[xAxis-1][yAxis+1]=smCircle;
                }
                else
                {
                    values[xAxis-1][yAxis]=smCircle;
                    values[xAxis-1][yAxis-1]=smCircle;
                    values[xAxis][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis-1]=smCircle;
                    values[xAxis+1][yAxis]=smCircle;
                }


            }
        }
        values[xAxis][yAxis]=king;
    }

    public void knightMoves() {
        String knight = " ♘ ";
        int[][] moves = {
                { 2,  1}, { 2, -1},
                {-2,  1}, {-2, -1},
                { 1,  2}, { 1, -2},
                {-1,  2}, {-1, -2}
        };


        for (String[] row : values) {
            Arrays.fill(row, "   ");
        }


        values[xAxis][yAxis] = knight;


        for (int[] move : moves) {
            int newX = xAxis + move[0];
            int newY = yAxis + move[1];


            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                values[newX][newY] = smCircle;
            }
        }
    }

    public void specificPlace()
    {

        System.out.println(YELLOW_TEXT+"\n1. Pawn ♙");
        System.out.println("2. King ♔");
        System.out.println("3. Queen ♕");
        System.out.println("4. Rook ♖");
        System.out.println("5. Bishop ♗");
        System.out.println("6. Knight ♘ \n");
        System.out.print("Enter Your choice :");
        int choice= scan.nextInt();
        scan.nextLine();
        System.out.print("Enter The X-Axis :");
        xAxis= scan.nextInt()-1;
        scan.nextLine();
        System.out.print("Enter The Y-Axis :");
        yAxis= scan.nextInt()-1;
        scan.nextLine();

        if((xAxis>=0 && xAxis<=7) && (yAxis>=0 && yAxis<=7))
        {
            if(choice==1)
            {
                pawnMoves();

            }
            else if(choice==2)
            {
                kingMoves();
            }
            else if(choice==3)
            {
                queenMoves();
            }
            else if(choice==4)
            {
                rookMoves();
            }
            else if(choice==5)
            {
                bishopMoves();
            }
            else if(choice==6)
            {
                knightMoves();
            }
            else
            {
                System.out.println(RED_TEXT+"\t\t\t\t\t\t\t\tProvide Valid Input...");
                specificPlace();
            }
        }
        else {
            System.out.println(RED_TEXT+"\t\t\t\t\t\t\t\t Eigther X-axis or y-Axis Is wrong check again..");
            specificPlace();
        }


    }

    public void displayTheProject() {
        chessMake();
        displayChessBox();
        while (true) {
            System.out.println("\n\n ********************************************************|<   CHESS   >|*******************************************************************\n");
            String border = "*".repeat(70); // A border of 70 asterisks

            System.out.println("\n\t\t\t\t\t\t" +
                         border);
            System.out.println("\t\t\t\t\t\t*" + " ".repeat(68) + "*");
            System.out.println("\t\t\t\t\t\t*" + YELLOW_TEXT + "        1. Change The Color of The Board" + RESET + " ".repeat(28) + "*");
            System.out.println("\t\t\t\t\t\t*" + YELLOW_TEXT + "        2. Place All Coins" + RESET + " ".repeat(42) + "*");
            System.out.println("\t\t\t\t\t\t*" + YELLOW_TEXT + "        3. Place The specific piece and see the Possibilities" + RESET +" ".repeat(7
            )+ "*");
            System.out.println("\t\t\t\t\t\t*" + YELLOW_TEXT + "        of that coin's moves" + RESET + " ".repeat(34+9-3) + "*");
            System.out.println("\t\t\t\t\t\t*" + YELLOW_TEXT + "        4. Exit" + RESET + " ".repeat(53) + "*");
            System.out.println("\t\t\t\t\t\t*" + " ".repeat(68) + "*");
            System.out.println("\t\t\t\t\t\t"+border);
            System.out.print(YELLOW_TEXT + "\t\t\t\t\t\tEnter Your Choice: " + RESET);
            int choice = scan.nextInt();
            scan.nextLine();

            
            if (choice == 1) {
                chessMake();
                changeColor();
                displayChessBox();
            } else if (choice == 2) {
                chessMake();
                placeAllCoins();
                displayChessBox();
            } else if (choice == 3) {
                chessMake();
                specificPlace();
                displayChessBox();
            } else if (choice == 4) {
                System.out.println(GREEN_TEXT + "Thank you for playing!" + RESET);
                break;
            } else {
                System.out.println(RED_TEXT + "\t\t\t\t\t\t\t\t\t\t\tProvide valid input!" + RESET);
                displayTheProject();
            }
        }
    }

    // Add your other existing methods here...

    public static void main(String[] args) {
        ChessMaker obj = new ChessMaker();
        obj.displayTheProject();

    }
}

