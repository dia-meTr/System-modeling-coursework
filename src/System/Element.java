package System;

public class Element {
    protected String name;
    protected double tNext, tCurrent;
    protected int quantity;
    protected Element nextElement;
    protected double delayMean, delayDeviation;
    protected int k;



    private String distribution;
    private static int nextId = 0;
    private int id;

    public Element(String name) {
        this.name = name;
        tNext = 0.0;
        tCurrent = tNext;
        nextElement = null;
        id = nextId;
        nextId++;
    }

    public static void resetNextIdField() {
        nextId = 0;
    }

    protected double getDelay(Request request) {
        switch (distribution) {
            case "exp":
                return FunRand.Exp(delayMean);
            case "norm":
                return FunRand.Norm(delayMean, delayDeviation);
            case "unif":
                return FunRand.Unif(delayMean, delayDeviation);
            case "erlang":
                return FunRand.Erlang(delayMean, k);
            case "static":
                return FunRand.Static(delayMean);
        }

        return delayMean;
    }

    public void setExpDistribution(double delayMean) {
        distribution = "exp";
        this.delayMean = delayMean;
    }
    public void setNormDistribution(double delayMean, double delayDeviation) {
        distribution = "norm";
        this.delayMean = delayMean;
        this.delayDeviation = delayDeviation;
    }
    public void setStatic(double delay) {
        distribution = "static";
        this.delayMean = delay;
    }
    public void setUnifDistribution(double delayMean, double delayDeviation) {
        distribution = "unif";
        this.delayMean = delayMean;
        this.delayDeviation = delayDeviation;
    }
    public void setErlangDistribution(double delayMean, int k) {
        distribution = "erlang";
        this.delayMean = delayMean;
        this.k = k;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setTCurrent(double tCurrent) {
        this.tCurrent = tCurrent;
    }
    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    public void inAct(Request request) {
    }
    public void outAct(){
        quantity++;
    }
    public double getTNext() {
        return tNext;
    }
    public int getId() {
        return id;
    }
    public void printInfo(){
        System.out.print(name + ": {" + " працюючих процесорів: " + 1 +
                "; кількість: " + quantity+
                "; tnext: " + tNext);

         if(name.contains("CREATOR")) {
             System.out.println(" }");
         }
    }
    public String getName() {
        return name;
    }

    public void doStatistics(double delta){
    }
}
