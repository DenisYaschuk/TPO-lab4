package lab4_3;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private final static int threadNum = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) {
        long time0 = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadNum);
        ForkJoinSame text1 = new ForkJoinSame(FileSystems.getDefault().getPath("src/lab4_3/text1.txt"));
        ForkJoinSame text2 = new ForkJoinSame(FileSystems.getDefault().getPath("src/lab4_3/text2.txt"));
        HashSet<String> words1 = forkJoinPool.submit(text1).join();
        HashSet<String> words2 = forkJoinPool.submit(text2).join();
        List<String> sameWordsList = new ArrayList<>();
        for (String word : words1) {
            if (words2.contains(word)) {
                sameWordsList.add(word);
            }
        }
        long time1 = System.nanoTime();
        System.out.printf("Algorithm time: %dms\n", (time1-time0)/1000000);
        System.out.printf("Same words in two texts: %d\n", sameWordsList.size());
        System.out.printf("Text1 words num: %d\n", words1.size());
        System.out.printf("Text2 words num: %d", words2.size());
    }
}
