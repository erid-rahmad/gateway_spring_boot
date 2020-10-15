package com.multipolar.sumsel.kasda.kasdagateway.converter;

import java.util.concurrent.atomic.AtomicLong;

public class Chronometer {
    private AtomicLong start;
    private AtomicLong lap;

    public Chronometer() {
        this.start = new AtomicLong(System.currentTimeMillis());
        this.lap = new AtomicLong(start.get());
    }

    public long elapsed() {
        return System.currentTimeMillis() - start.get();
    }

    public void reset () {
        start.set(System.currentTimeMillis());
    }

    public long lap() {
        long elapsed = System.currentTimeMillis() - lap.get();
        lap.set(System.currentTimeMillis());
        return elapsed;
    }

    @Override
    public String toString() {
        return "Chronometer{" +
          "elapsed=" + elapsed() +
          ", lap=" + (System.currentTimeMillis() - lap.get()) +
          '}';
    }
}
