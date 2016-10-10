import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private Plant[][] field;
    private final int X_SIZE;
    private final int Y_SIZE;
    private int period;
    private final int NUM_TOTAL_BUGS;
    private int numUninfectedPlants;
    private int numAliveBugs;
    private static String result;
    private RiceBug[] bugs;

    public Simulation(int xdimen, int ydimen, int duration, int numBugs, RiceBug[] riceBugs) {
        X_SIZE = xdimen;
        Y_SIZE = ydimen;
        field = new Plant[X_SIZE][Y_SIZE];

        for(int i = 0; i < X_SIZE; i++) {
            for(int j = 0; j < Y_SIZE; j++) {
                field[i][j] = new Plant();
            }
        }

        period = duration;
        NUM_TOTAL_BUGS = numBugs;
        bugs = riceBugs;
        numUninfectedPlants = X_SIZE * Y_SIZE;
        numAliveBugs = NUM_TOTAL_BUGS;

        runSimulation();
    }

    public void runSimulation() {
        for(int time = 0; time < period; time++) {
            /*
             * Place all active bugs on their respective plants for this instant in time, first
             * ensuring their position on the field is within the bounds of the field.
             */
            for(int i = 0; i < bugs.length; i++) {
                if(bugs[i].alive() && time >= bugs[i].getStartTime()) {
                    if (bugs[i].getCurrentY() < Y_SIZE && bugs[i].getCurrentX() < X_SIZE
                            && bugs[i].getCurrentX() >= 0 && bugs[i].getCurrentY() >= 0) {

                        field[bugs[i].getCurrentX()][bugs[i].getCurrentY()].addBug(bugs[i]);
                    } else {
                        bugs[i].kill();
                        numAliveBugs--;
                    }
                }
            }

            /*
             * Loops through plants in the field, first killing all weaker bugs, then infecting
             * the plant if possible.
             */
            for(int i = 0; i < X_SIZE; i++) {
                for(int j = 0; j < Y_SIZE; j++) {

                    Plant currentPlant = field[i][j];

                    if(currentPlant.hasBugsOn()) {
                        ArrayList<RiceBug> bugsOnCurrent = currentPlant.getBugsOnPlant();
                        for(int k = 0; k < bugsOnCurrent.size(); k++) {
                            RiceBug b = bugsOnCurrent.get(k);
                            if(b.getStrength() < currentPlant.getStrongest()) {
                                b.kill();
                                currentPlant.removeBug(b);
                                numAliveBugs--;
                            }
                        }

                        if(currentPlant.infect()) {
                            ArrayList<RiceBug> currentBugs = currentPlant.getBugsOnPlant();
                            for(int k = 0; k < currentBugs.size(); k++) {
                                currentBugs.get(k).increaseStrength();
                            }
                            numUninfectedPlants--;
                        }
                    }
                }
            }

            //Move bugs in their individual direction for the next iteration in time
            for(int i = 0; i < bugs.length; i++) {
                if(time >= bugs[i].getStartTime() && bugs[i].alive())
                bugs[i].move(field);
            }
        }

        result = numUninfectedPlants + " " + numAliveBugs + "\n";
    }

    public static void main(String[] args) {

        //Processing of input. Assumed perfect input (no error handling).
        Scanner in = new Scanner(System.in);
        int xdimen = in.nextInt();
        int ydimen = in.nextInt();
        int duration = in.nextInt();
        int numOfBugs = in.nextInt();
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
        System.out.println(result);
    }
}
