import java.io.*;

public class MergeFile {


    public  void mergeFiles(String file1, String file2, String outputFile) {
        try (
                BufferedReader r1 = new BufferedReader(new FileReader(file1));
                BufferedReader r2 = new BufferedReader(new FileReader(file2));

                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
        ) {
            String line;

            while ((line = r1.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            while ((line = r2.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Files merged successfully into " + outputFile);

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    MergeFile obj=new MergeFile();
    obj.mergeFiles("/home/dines-zstch1528/IdeaProjects/Assignmets/src/QuizApp/Source/UserInfo.txt","/home/dines-zstch1528/IdeaProjects/Assignmets/src/QuizApp/Source/UserInfo2.txt","newFile.txt");

    }
}
