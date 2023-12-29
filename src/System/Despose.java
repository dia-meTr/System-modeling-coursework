package System;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Despose extends Element {
    private final ArrayList<Double> requestsTimes;

    public Despose(String name) {
        super(name);
        requestsTimes = new ArrayList<>();
        tNext = Integer.MAX_VALUE;
    }

    @Override
    public void inAct(Request request) {
        request.setEndTime(tCurrent);
        requestsTimes.add(request.getTimeSpentInSystem());
    }

    @Override
    public  int getQuantity() {
        return requestsTimes.size();
    }

    public double getAverageTimeRequestInSystem() {
        double avgTime = 0;

        for (Double requestTime : requestsTimes) {
            avgTime += requestTime;
        }

        return avgTime / requestsTimes.size();
    }

    public void printTimes(){
        System.out.println("Розподіл часу в системі: ");

        int i = 1;
        DecimalFormat df = new DecimalFormat("0.0000000000");

        while (i <= requestsTimes.size()){
            System.out.print(df.format(requestsTimes.get(i-1)) + ", ");
            if (i % 10 == 0){
                System.out.println();
            }
            i += 1;
        }

    }
}
