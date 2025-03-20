//import java.io.File;
//import java.util.ArrayList;
//
//public class FileTree
//{
//    String directoryPath = "/home/dines-zstch1528/Desktop/python";
//    File directory= new File(directoryPath);
//
//    public int totalNumberOfFiles(){
//        if (directory.exists() && directory.isDirectory()) {
//            File[] files = directory.listFiles();
//            int fileCount = 0;
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isFile()) {
//                        fileCount++;
//                    }
//                }
//            }
//
//            return fileCount;
//        } else {
//            return -1;
//            }
//    }
//
//    public void whatFilesAreThey(){
//        ArrayList<ArrayList<String>> allFileNames=
//
//    }
//
//
//    public static void main(String[] args) {
//        FileTree obj=new FileTree();
//    }
//}
