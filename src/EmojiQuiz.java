import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.*;

class Question {
    public String emoji;
    public String answer;
    public String clue;

    public Question(String emoji, String answer, String clue) {
        this.emoji = emoji;
        this.answer = answer;
        this.clue = clue;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getAnswer() {
        return answer;
    }

    public String getClue() {
        return clue;
    }
}

class QuizGame {
    public Question[] questions;
    public int points = 0;
    public int currentQuestionIndex = 0;
    public Scanner scan = new Scanner(System.in);

    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";

    public QuizGame() {
        questions = new Question[]{
                new Question("\t\t\t\t\t\t\uD83E\uDDD1\u200D\uD83E\uDD1D\u200D\uD83E\uDDD1", "people", "\t\t\t\t\t\tplural of person"),
                new Question("\t\t\t\t\t\t\uD83D\uDCDA", "books", "\t\t\t\t\t\twe take those things in our school days"),
                new Question("\t\t\t\t\t\t\uD83D\uDC68\u200D\uD83D\uDCBB", "code", "\t\t\t\t\t\twhat we do here"),
                new Question("\t\t\t\t\t\t\uD83D\uDD0D", "lens", "\t\t\t\t\t\twe used that in our childhood to burn the paper with the sun"),
                new Question("\t\t\t\t\t\t\uD83D\uDCC5", "calendar", "\t\t\t\t\t\tit has all the dates and months")
        };
    }

    public int getPoints() {
        return points;
    }

    public void displayQuestion(SoundPlayer soundPlayer) {
        Question currentQuestion = questions[currentQuestionIndex];
        System.out.println(YELLOW + "\t\t\t\t\t\tFind The Zoho product by this Emoji!!" + RESET);
        System.out.println(currentQuestion.getEmoji());
        System.out.print(YELLOW + "\t\t\t\t\t\tType Here: " + RESET);
        String guess = scan.nextLine().toLowerCase();

        if (guess.equals(currentQuestion.getAnswer())) {
            soundPlayer.playSound("/home/dines-zstch1528/Music/victory.wav");
            points++;
            System.out.println(GREEN + "\t\t\t\t\t\tHurray! You are Right!!" + RESET);
        } else {
            ifWrong(soundPlayer, currentQuestion);
        }
        currentQuestionIndex++;
    }

    public void ifWrong(SoundPlayer soundPlayer, Question currentQuestion) {
        soundPlayer.playSound("/home/dines-zstch1528/Music/defeat.wav");

        System.out.println(RED + "\t\t\t\t\t\tSorry.. Wrong!!" + RESET);
        System.out.print(YELLOW + "\t\t\t\t\t\tDo you need a clue? (yes/no) :" + RESET);
        String wantClue = scan.nextLine().toLowerCase();

        if (wantClue.equals("yes")) {
            System.out.println(YELLOW + currentQuestion.getClue() + RESET);
            System.out.print(YELLOW + "\t\t\t\t\t\tType here: ");
            String secondChance = scan.nextLine().toLowerCase();
            if (secondChance.equals(currentQuestion.getAnswer())) {
                soundPlayer.playSound("/home/dines-zstch1528/Music/victory.wav");
                System.out.println(GREEN + "\t\t\t\t\t\tCorrect! But no points this time." + RESET);
            } else {
                soundPlayer.playSound("/home/dines-zstch1528/Music/defeat.wav");
                System.out.println(RED + "\t\t\t\t\t\tWrong again. The correct answer was: " + currentQuestion.getAnswer() + RESET);
            }
        } else {
            System.out.println(RED + "\t\t\t\t\t\tThe correct answer was: " + currentQuestion.getAnswer() + RESET);
        }
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.length;
    }
}


class SoundPlayer {
    public void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                System.out.println("Sound file not found: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
}


public class EmojiQuiz {
    static int maxScore = 0;
    static String maxScorer = "";
    static long lowestTimeTaken = 1000;
    long timeTaken = 0;
    String name = "";
    QuizGame quizGame;

    public static void main(String[] args) {
        EmojiQuiz quiz = new EmojiQuiz();
        quiz.startGame();
    }

    public void startGame() {
        Scanner scan = new Scanner(System.in);
        SoundPlayer soundPlayer = new SoundPlayer();
        QuizGame game = new QuizGame();
        boolean keepPlaying;

        do {
            System.out.print(QuizGame.YELLOW + "\t\t\t\t\t\tEnter Your Name: " + QuizGame.RESET);
            name = scan.nextLine();

            long initialTime = System.currentTimeMillis();
            while (game.hasNextQuestion()) {
                game.displayQuestion(soundPlayer);
            }
            long finalTime = System.currentTimeMillis();
            timeTaken = (finalTime - initialTime) / 1000;

            updateMaxScore(game.getPoints());
            displayProfile(game.getPoints());

            System.out.print(QuizGame.YELLOW + "\t\t\t\t\t\tDo you want to play again? (yes/no): " + QuizGame.RESET);
            String playAgain = scan.nextLine().toLowerCase();
            keepPlaying = playAgain.equals("yes");
            if(keepPlaying)
            {
                game = new QuizGame();

            }

        } while (keepPlaying);
    }

    public void updateMaxScore(int points) {
        if (points > maxScore || (points == maxScore && timeTaken < lowestTimeTaken)) {
            maxScore = points;
            maxScorer = name;
            lowestTimeTaken = timeTaken;
        }
    }

    public void displayProfile(int points) {
        int borderLength = 50;
        String border = "*".repeat(borderLength);

        System.out.println(QuizGame.YELLOW + "\n\n\t\t\t\t\t\t" + border);
        System.out.println("\t\t\t\t\t\t*  Name: " + name + " ".repeat(borderLength - ("  Name: " + name).length() - 2) + "*");
        System.out.println("\t\t\t\t\t\t*  Score: " + points + " ".repeat(borderLength - ("  Score: " + points).length() - 2) + "*");
        System.out.println("\t\t\t\t\t\t*  Time Taken: " + timeTaken + " sec" + " ".repeat(borderLength - ("  Time Taken: " + timeTaken + " sec").length() - 2) + "*");
        System.out.println("\t\t\t\t\t\t*  Max Scorer: " + maxScorer + " ".repeat(borderLength - ("  Max Scorer: " + maxScorer).length() - 2) + "*");
        System.out.println("\t\t\t\t\t\t*  Max Score: " + maxScore + " ".repeat(borderLength - ("  Max Score: " + maxScore).length() - 2) + "*");
        System.out.println("\t\t\t\t\t\t" + border + QuizGame.RESET + "\n\n");
    }

}
