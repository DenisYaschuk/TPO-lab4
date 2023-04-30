package lab4_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinCounter extends RecursiveTask<HashMap<String, Integer>> {
    private final HashMap<String, Integer> dictionary;
    private final List<String> words;
    private final int limit = 100000;

    public ForkJoinCounter(List<String> wordList)  {
        this.dictionary = new HashMap<>();
        this.words = wordList;
    }

    private Collection<ForkJoinCounter> createSubtasks() {
        List<ForkJoinCounter> subtasks = new ArrayList<>();
        int midpoint = words.size() / 2;
        List<String> leftHalf = words.subList(0, midpoint);
        List<String> rightHalf = words.subList(midpoint, words.size());
        subtasks.add(new ForkJoinCounter(leftHalf));
        subtasks.add(new ForkJoinCounter(rightHalf));
        return subtasks;
    }

    @Override
    protected HashMap<String, Integer> compute() {
        if (words.size() > limit) {
            Collection<ForkJoinCounter> subTasks = createSubtasks();
            ForkJoinTask.invokeAll(subTasks);

            for (ForkJoinCounter subTask : subTasks) {
                HashMap<String, Integer> subResult = subTask.join();
                for (String key : subResult.keySet()) {
                    dictionary.putIfAbsent(key, subResult.get(key));
                }
            }
            return dictionary;
        } else {
            for (String word : words) {
                dictionary.putIfAbsent(word, word.length());
            }
            return dictionary;
        }
    }
}
