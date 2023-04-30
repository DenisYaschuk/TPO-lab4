package lab4_1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TextHandler {
    private final static String regSpacePunct = "[\\s\\p{Punct}]+";
    public static List<String> getAllWords(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(s -> stringBuilder.append(s).append("\n"));
        }
        String content = stringBuilder.toString().trim();
        return Arrays.asList(content.split(regSpacePunct));
    }
}
