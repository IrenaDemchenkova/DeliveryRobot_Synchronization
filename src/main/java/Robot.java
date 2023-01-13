import java.util.Random;
import java.util.concurrent.Callable;

public class Robot implements Callable {
    private final String letters = "RLRFR";
    private final int length = 100;
    private final char symbol = 'R';

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static Integer freq(String string, char symbol) {
        Integer freq = (int) string.chars().filter(ch -> ch == symbol).count();
        return freq;
    }

    @Override
    public Integer call() throws Exception {
        String result = generateRoute(letters, length);
        int freq = freq(result, symbol);
        return freq;
    }
}
