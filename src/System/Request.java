package System;

public class Request {
    private double startTime, endTime;
    private double tNext;

    public Request(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    public void setTNext(double tNext) {
        this.tNext = tNext;
    }
    public double getTimeSpentInSystem() {
        return endTime - startTime;
    }
    public double getTNext() {
        return tNext;
    }
}

