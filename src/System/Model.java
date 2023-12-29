package System;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Model {
    private final DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<Element> elements;
    public double tNext, tCurrent;
    public int event;
    private Despose despose;

    public Model(ArrayList<Element> elements, Despose despose) {
        this.elements = elements;
        tNext = 0.0;
        tCurrent = tNext;
        event = 0;
        this.despose = despose;
    }

    public void simulate(double time) {
        int nextThreshold = 1000;

        while (tCurrent < time) {
            tNext = Double.MAX_VALUE;
            for (Element e : elements) {
                if (e.getTNext() < tNext) {
                    tNext = e.getTNext();
                    event = e.getId();
                }
            }

            //System.out.println("It's time for event in " + elements.get(event).getName() + ", time = " + tNext);
            for (Element e : elements) {
                e.doStatistics(tNext - tCurrent);
            }

            //nextThreshold = printCurrent(nextThreshold);

            tCurrent = tNext;
            for (Element e : elements) {
                e.setTCurrent(tCurrent);
            }

            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTNext() == tCurrent) {
                    e.outAct();
                }
            }
        }

        //printResult();
    }

    private int printCurrent(int nextThreshold){
        if (tCurrent >= nextThreshold) {
            System.out.println(df.format(despose.getAverageTimeRequestInSystem() ));
            nextThreshold += 1000;
        }
        return nextThreshold;
    }

    public void printResult() {
        System.out.println("\n-----------------------РЕЗУЛЬТАТ-----------------------");

        for (Element e : elements) {
            System.out.println(e.getName() + ": {");

            System.out.println("\tкількість: " + e.getQuantity() + ";");
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("\tсередня довжина черги: " + df.format(p.getMeanQueue() / tCurrent) + ";");
                System.out.println("\tсередня навантаження каналу: " + df.format(p.getMeanLoad() / tCurrent) + ";");
            }
            System.out.println("}\n");
        }
    }
}
