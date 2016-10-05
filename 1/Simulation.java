//1:46 hours in
import java.util.Scanner;

public class Simulation {
    private Plant[][] field;
    private final int X_SIZE;
    private final int Y_SIZE;
    private int period;
    private final int NUM_TOTAL_BUGS;
    private int numInfectedPlants;
    private int numAliveBugs;
    private RiceBug[] bugs;

    public Simulation(int xdimen, int ydimen, int duration, int numBugs, RiceBug[] riceBugs) {
        X_SIZE = xdimen;
        Y_SIZE = ydimen;
        field = new Plant[X_SIZE][Y_SIZE];
        period = duration;
        NUM_TOTAL_BUGS = numBugs;
        bugs = riceBugs;
        numInfectedPlants = 0;
        numAliveBugs = NUM_TOTAL_BUGS;

        runSimulation();
    }

    public void runSimulation() {
        for(int currentTime = 0; currentTime < period; currentTime++) {
            for(int j = 0; j < bugs.length; j++) {
                RiceBug currentBug = bugs[j];
                if(currentBug.getStartTime() == currentTime) {
                    field[currentBug.x?][]
                }
            }
        }
    }

    public static void main(String[] args) {

        //Processing of input. Assumed perfect input (no error handling).
        Scanner in = new Scanner(System.in);
        int xdimen = Integer.parseInt(in.next());
        int ydimen = Integer.parseInt(in.next());
        int duration = Integer.parseInt(in.next());
        int numOfBugs = Integer.parseInt(in.next());
        RiceBug[] riceBugs = new RiceBug[numOfBugs];

        for(int i = 0; i < numOfBugs; i++) {
            int posX = Integer.parseInt(in.next());
            int posY = Integer.parseInt(in.next());
            int startTime = Integer.parseInt(in.next());
            char dir = in.next().charAt(0);

            RiceBug newBug = new RiceBug(i, posX, posY, startTime, dir);
            riceBugs[i] = newBug;
        }

        new Simulation(xdimen, ydimen, duration, numOfBugs, riceBugs);
    }
}
