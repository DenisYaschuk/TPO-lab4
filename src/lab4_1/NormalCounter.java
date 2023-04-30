package lab4_1;

import java.util.HashMap;
import java.util.List;

public class NormalCounter {
    private final HashMap<String, Integer> dictionary;
    private final List<String> words;

    public NormalCounter(List<String> wordList){
        this.dictionary = new HashMap<>();
        this.words = wordList;
    }
    public HashMap<String, Integer> createDictionary() {
        for (String word : words) {
            dictionary.putIfAbsent(word, word.length());
        }
        return dictionary;
    }
}
