import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
    public static void main(String[] args) throws Exception {
        final int numberOfPools = 1000;
        final Map<Integer, Integer> sizeToFreq = new HashMap<>();
        int counter = 0;
        Integer maxFrequency = 0;
        Integer reps = 0;
        Thread thread = null;

        //searching for max frequancy and corresponding key in HashMap

        thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Current maximum is " + Collections.max(sizeToFreq.values()));
                }
            }
        });
        thread.start();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfPools);
        for (int i = 0; i < numberOfPools; i++) {
            Future<Integer> future = executor.submit(new Robot());
            Integer frequency = future.get();
            synchronized (sizeToFreq) {
                if (!sizeToFreq.containsKey(frequency)) {
                    sizeToFreq.put(frequency, 1);
                    counter++;
                } else {
                    Integer value = sizeToFreq.get(frequency) + 1;
                    sizeToFreq.put(frequency, value);
                    counter++;
                }
                sizeToFreq.notify();
            }
            if (counter == numberOfPools) {
                thread.interrupt();
            }
        }
        executor.shutdown();
        maxFrequency = (Collections.max(sizeToFreq.values()));
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                reps = entry.getKey();
            }
        }
        // printing out results

        System.out.printf("The most frequent number of reps is %d (found %d times)\n", reps.intValue(), maxFrequency.intValue());
        System.out.println("Also found:");
        sizeToFreq.entrySet().forEach(entry -> {
            System.out.printf(" - %d (%d times)\n", entry.getKey().intValue(), entry.getValue().intValue());
        });
    }
}
