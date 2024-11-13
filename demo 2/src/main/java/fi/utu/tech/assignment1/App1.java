package fi.utu.tech.assignment1;

import java.util.List;
import java.util.stream.Stream;

public class App1 {

    public static void main(String[] args) {
        Count sharedCount = new Count();

        // Luodaan ja käynnistetään threadCount verran laskijasäikeitä
        int threadCount = 100;
        List<Counter> counters = Stream.generate(() -> new Counter(sharedCount)).limit(threadCount).toList();
        counters.forEach(c -> c.start());
        counters.forEach(c -> {
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
         
        System.out.printf("Got %d, expected %d%n", sharedCount.getCount(), threadCount);
    }
}

class Count {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

class Counter extends Thread {

    Count count;

    public Counter(Count c) {
        this.count = c;
    }

    @Override
    public void run() {
        /**
         * This thread's purpose in life is to 
         * increase the value of the shared count by one
         */
        int oldCount = count.getCount();
        // Alla oleva sleep ei ole pakollinen ongelman ilmenemiselle,
        // mutta se lisää esiintymisen todennäköisyyttä
        try {
			Thread.sleep((long) (100 * Math.random()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        count.setCount(oldCount + 1);
    }
}
