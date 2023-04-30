package lab4_4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentSearcher extends RecursiveTask<List<File>> {

    private List<File> files;
    private String[] keywords;
    private final int limit = 2;

    public DocumentSearcher(List<File> files, String[] keywords) {
        this.files = files;
        this.keywords = keywords;
    }

    private Collection<DocumentSearcher> createSubtasks() {
        int size = files.size();
        int middle = size / 2;
        List<DocumentSearcher> subtasks = new ArrayList<>();
        DocumentSearcher left = new DocumentSearcher(files.subList(0, middle), keywords);
        DocumentSearcher right = new DocumentSearcher(files.subList(middle, size), keywords);
        subtasks.add(left);
        subtasks.add(right);
        return subtasks;
    }

    @Override
    protected List<File> compute() {
        int size = files.size();
        if (size > limit) {
            List<File> result = new ArrayList<>();
            Collection<DocumentSearcher> subTasks = createSubtasks();
            invokeAll(subTasks);
            for (DocumentSearcher subTask : subTasks) {
                result.addAll(subTask.join());
            }
            return result;
        } else {
            List<File> result = new ArrayList<>();
            Pattern pattern = Pattern.compile(String.join("|", keywords));
            for (File file : files) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            result.add(file);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}