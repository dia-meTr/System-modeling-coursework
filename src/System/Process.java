package System;

import System.Element;
import System.Request;

import java.util.*;

public class Process extends Element {
    private int working;
    protected LinkedList<Request> queue;

//    protected int workersAmount, busyWorkersAmount;
    protected ArrayList<Request> tNextList;
    protected double meanQueue, meanLoad;

    public Process(String name) {
        super(name);
        working = 0;
        queue = new LinkedList<>();
        tNextList = new ArrayList<>();
        meanQueue = 0.0;
        meanLoad = 0.0;
    }



    @Override
    public void inAct(Request request) {
        if (working == 0) {
            super.outAct();
            working = 1;

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
        working = 0;

        if (!queue.isEmpty()) {
            super.outAct();
            working = 1;

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
        this.meanLoad = meanLoad + working * delta;
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