package lab4_4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindFile {
    public static List<File> getListOfFilesToSearch(String pathToDir) {
        List<File> files = new ArrayList<>();
        String directoryPath = pathToDir;
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            searchFiles(directory, files);
        }
        return files;
    }

    private static void searchFiles(File directory, List<File> files) {
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    searchFiles(file, files);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        files.add(file);
                    }
                }
            }
        }
    }
}
