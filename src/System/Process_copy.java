package System;

import java.util.ArrayList;
import java.util.LinkedList;

public class Process_copy extends Element {
    protected int workersAmount, busyWorkersAmount;
    protected ArrayList<Request> tNextList;
    protected LinkedList<Request> queue;
    protected double meanQueue, meanLoad;

    public Process_copy(String name) {
        super(name);
        workersAmount = Integer.MAX_VALUE;
        busyWorkersAmount = 0;
        queue = new LinkedList<>();
        tNextList = new ArrayList<>();
        meanQueue = 0.0;
        meanLoad = 0.0;
    }

    public Process_copy(String name, int workersAmount) {
        this(name);
        this.workersAmount = workersAmount;
    }

    @Override
    public void inAct(Request request) {
        if (busyWorkersAmount < workersAmount) {
            super.outAct();
            busyWorkersAmount++;

            request.setTNext(tCurrent + getDelay(request));
            tNextList.add(request);
        } else {
           queue.add(request);
        }
    }

    @Override
    public void outAct() {
        Request processedRequest = getPatientWithMinTNext();
        tNextList.remove(processedRequest);
        busyWorkersAmount--;

        if (!queue.isEmpty()) {
            super.outAct();
            busyWorkersAmount++;

            Request request = getPatientFromQueue();
            request.setTNext(tCurrent + getDelay(request));
            tNextList.add(request);
        }

        goToNextElement(processedRequest);
    }

    @Override
    public double getTNext() {
        Request request = getPatientWithMinTNext();
        return request != null ? request.getTNext() : Integer.MAX_VALUE;
    }
    @Override
    public void doStatistics(double delta) {
        this.meanQueue = meanQueue + queue.size() * delta;
        this.meanLoad = meanLoad + busyWorkersAmount * delta;
    }

    protected Request getPatientFromQueue() {
        return queue.poll();
    }
    protected void goToNextElement(Request request) {
        if(nextElement != null) {
            nextElement.inAct(request);
        }
    }

    public double getMeanLoad() {
        return meanLoad;
    }
    public double getMeanQueue() {
        return  meanQueue;
    }

    private Request getPatientWithMinTNext() {
        if (tNextList.isEmpty()) {
            return null;
        }

        Request minTNextRequest = tNextList.get(0);

        for (Request request : tNextList) {
            if (request.getTNext() < minTNextRequest.getTNext()) {
                minTNextRequest = request;
            }
        }

        return minTNextRequest;
    }
}