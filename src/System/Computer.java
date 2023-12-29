package System;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Computer extends Element {
    private int working;
    protected LinkedList<Request> queue;
    protected double meanQueue, meanLoad;
    protected ArrayList<Request> tNextList;
    private ArrayList<Process> nextElements = new ArrayList<>();
    private ArrayList<Double> nextElementsProbabilities = new ArrayList<>();

    public Computer(String name) {
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
        Request processedRequest = getRequestWithMinTNext();
        //System.out.println("Queue = " + queue.size());
        tNextList.remove(processedRequest);
        working = 0;

        if (!queue.isEmpty()) {
            super.outAct();
            working = 1;

            Request request = getRequestFromQueue();
            request.setTNext(tCurrent + getDelay(request));
            tNextList.add(request);
        }

        Process nextProcess = this.findNextProcessInChain();
        if(nextProcess != null) {
            nextProcess.inAct(processedRequest);
        }
    }

    public int getQueue(){
        return queue.size();
    }

    private Process findNextProcessInChain() {
        if(!this.nextElements.isEmpty()) {
            double rand = new Random().nextDouble();
            double cumulativeProbability = 0.0;

            for (int i = 0; i < this.nextElements.size(); i++) {
                cumulativeProbability += this.nextElementsProbabilities.get(i);
                if(rand < cumulativeProbability) {
                    return this.nextElements.get(i);
                }
            }

            return this.nextElements.get(this.nextElements.size() - 1);
        }

        return null;
    }

    @Override
    public double getTNext() {
        Request request = getRequestWithMinTNext();
        return request != null ? request.getTNext() : Integer.MAX_VALUE;
    }
    @Override
    public void doStatistics(double delta) {
        this.meanQueue = meanQueue + queue.size() * delta;
        this.meanLoad = meanLoad + working * delta;
    }

    public double getMeanLoad() {
        return meanLoad;
    }

    protected Request getRequestFromQueue() {
        return queue.poll();
    }

    public double getMeanQueue() {
        return  meanQueue;
    }

    private Request getRequestWithMinTNext() {
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

    public void setNextElements(ArrayList<Process> processes, ArrayList<Double> probabilities) {
        if(probabilities.stream().mapToDouble(a -> a).sum() != 1.0) {
            throw new RuntimeException("The probabilities must sum into 1");
        }

        this.nextElements = processes;
        this.nextElementsProbabilities = probabilities;
    }
}