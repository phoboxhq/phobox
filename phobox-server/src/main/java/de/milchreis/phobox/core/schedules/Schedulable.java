package de.milchreis.phobox.core.schedules;

public interface Schedulable {

    void start();

    void stop();

    boolean isReady();
}
