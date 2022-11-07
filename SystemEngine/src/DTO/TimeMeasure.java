package DTO;

import java.io.Serializable;

public class TimeMeasure implements Serializable {
    private long beginTime;
    private long endTime;
    public TimeMeasure() {
        resetTime();
    }
    public void startTime() {
        this.beginTime = System.nanoTime();
    }

    public void stopTime() {
        this.endTime = System.nanoTime();
    }
    public long getElapsedTime() {
        return (this.endTime - this.beginTime);
    }
    public void resetTime() {
        this.beginTime = 0;
        this.endTime = 0;
    }
}
