package de.milchreis.phobox.helper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentHelper {

    public static void doItEvery(TimeUnit unit, int repeatDelay, int times, Callable<?> call) {

        Executors.newSingleThreadExecutor().execute(() -> {
            for(int i = 0 ; i < times ; i++) {

                try {
                    call.call();
                } catch (Exception e) {
                }

                try {
                    unit.sleep(repeatDelay);
                } catch (InterruptedException e) {
                }
            }
        });
    }

}
