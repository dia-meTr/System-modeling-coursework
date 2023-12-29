package System;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) {

        verify();

        //run(10, 3, 2, 3, 18, 2);

        //experiment();
    }

    public static double run(double createMean, int createDeviation, double computerTime, int chanelTime, double responseMean, int responseDeviation) {
        //Initialization
        Element.resetNextIdField();

        Create creator = new Create("CREATOR");

        creator.setNormDistribution(createMean, createDeviation);

        Computer computer1 = new Computer("Computer 1");
        Computer computer2 = new Computer("Computer 2");

        computer1.setStatic(computerTime);
        computer2.setStatic(computerTime);

        Process channelTransmission = new Process("Channel Transmission");
        channelTransmission.setStatic(chanelTime);

        Process response1 = new Process("response for computer 1");
        response1.setNormDistribution(responseMean, responseDeviation);

        Process response2 = new Process("response for computer 2");
        response2.setNormDistribution(responseMean, responseDeviation);

        Despose despose = new Despose("DESPOSE");

        //Dependency injection
        creator.setNextElement(computer1);

        computer1.setNextElements(new ArrayList<>(){{add(channelTransmission); add(response1);}}, new ArrayList<>(){{add(0.5); add(0.5);}});
        computer2.setNextElements(new ArrayList<>(){{add(response2);}}, new ArrayList<>(){{add(1.0);}});

        channelTransmission.setNextElement(computer2);

        response1.setNextElement(despose);
        response2.setNextElement(despose);

        //Simulation
        Model model = new Model(new ArrayList<>(){{
            add(creator);
            add(computer1);
            add(computer2);
            add(channelTransmission);
            add(response1);
            add(response2);
            add(despose);
        }}, despose);

        int simulationTime = 18000;
        model.simulate(simulationTime);

        System.out.println("Середній час обробки запиту в інформаційній системі: " + df.format(despose.getAverageTimeRequestInSystem()));
        System.out.println("Завантаження 1 комп'ютера: " + df.format(computer1.getMeanLoad()/ simulationTime) );
        System.out.println("Завантаження 2 комп'ютера: " + df.format(computer2.getMeanLoad()/ simulationTime) );

        //System.out.println("Завантаження каналу: " + df.format(channelTransmission.getMeanLoad()/ simulationTime) );
        //System.out.println("Черга каналу: " + df.format(channelTransmission.getMeanQueue()/ simulationTime) );

        //System.out.println("Завантаження відповідь 1: " + df.format(response1.getMeanLoad()/ simulationTime) );
        //System.out.println("Черга відповідь 1" + df.format(response1.getMeanQueue()/ simulationTime) );

        //System.out.println("Завантаження відповідь 2: " + df.format(response2.getMeanLoad()/ simulationTime) );
        //System.out.println("Черга відповідь 2" + df.format(response2.getMeanQueue()/ simulationTime) );

        //despose.printTimes();
        return despose.getAverageTimeRequestInSystem();
    }

    public static void verify(){

        int[] createMean = {6, 8, 10, 12, 14, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        int[] computerTime = {2, 2, 2, 2, 2, 1, 3, 4, 6, 2, 2, 2, 2, 2, 2, 2, 2};
        int[] chanelTime = {3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 5, 7, 3, 3, 3, 3, 3};
        int[] responseMean = {18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 12, 14, 16, 20, 22};


        for(int x=0; x<17; x++){

            System.out.println("createMean " + createMean[x] + ", computerTime " + computerTime[x] + ", chanelTime " + chanelTime[x] + " responseMean, " + responseMean[x]);

            run(createMean[x], 3, computerTime[x], chanelTime[x], responseMean[x], 2);

            //System.out.println();
            System.out.println("---------------------------------------------------------------");
            //System.out.println();

        }
    }
    public static void experiment(){

        for(int x=0; x<40; x++) {
            for (double createMean : new double[]{9.5, 10}) {
                for (int computerTime : new int[]{2, 3}) {
                    for (double responseMean : new double[]{18, 18.5}) {
                        //System.out.println("createMean " + createMean + ", computerTime " + computerTime + ", responseMean " + responseMean);

                        double avg = 0;


                        avg = run(createMean, 3, computerTime, 3, responseMean, 2);
                        System.out.print(avg + "; ");
                    }
                }
            }
            System.out.println();
//            System.out.println();
        }



    }
}
