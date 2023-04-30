package lab4_1;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private final static int threadNum = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) throws IOException {
        List<String> allWords = TextHandler.getAllWords("src/lab4_1/text1.txt");

        NormalCounter normalCounter = new NormalCounter(allWords);
        long normalTime1 = System.nanoTime();
        HashMap<String, Integer> normalResult = normalCounter.createDictionary();
        long normalTime2 = System.nanoTime();
        long normalTime = (normalTime2 - normalTime1)/1000;
        System.out.printf("Normal counting time: %dmсs\n", normalTime);

        ForkJoinPool forkJoinPool = new ForkJoinPool(threadNum);
        long forkJoinTime1 = System.nanoTime();
        HashMap<String, Integer> forkJoinResult = forkJoinPool.submit(new ForkJoinCounter(allWords)).join();
        long forkJoinTime2 = System.nanoTime();
        long forkJoinTime = (forkJoinTime2 - forkJoinTime1)/1000;
        System.out.printf("Fork Join counting time: %dmсs\n", forkJoinTime);

        System.out.printf("Algorithms difference: %.2f\n", ((double) normalTime / forkJoinTime));
        System.out.println("Words in text: " + allWords.size());
        int countRez = forkJoinResult.size();
        System.out.println("Unique words: " + countRez);
        int minRez = Collections.min(forkJoinResult.values());
        int maxRez = Collections.max(forkJoinResult.values());
        System.out.println("Min: " + minRez + "\tMax: " + maxRez + "\tRange: " + (maxRez-minRez));
        double sumRez = forkJoinResult.values().stream().mapToDouble(x -> x).sum();
        double avgRez = sumRez / countRez;
        System.out.printf("Expected value (average): %.2f\n", avgRez);
        double avgX2 = forkJoinResult.values().stream().mapToDouble(x -> Math.pow(x, 2)).sum() / countRez;
        double varianceRez = avgX2 - Math.pow(avgRez, 2);
        System.out.printf("Variance: %.2f\n", varianceRez);
        System.out.printf("Deviation: %.2f\n", Math.sqrt(varianceRez));
    }
}
