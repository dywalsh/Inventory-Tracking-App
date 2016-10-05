import java.util.ArrayList;

public class Plant {
    private int numBugsOnPlant;
    private ArrayList<RiceBug> bugsOnPlant;
    private RiceBug strongestOnPlant;
    private boolean isInfected;

    public Plant() {
        numBugsOnPlant = 0;
        strongestOnPlant = null;
        isInfected = false;
        bugsOnPlant = new ArrayList<RiceBug>();
    }

    public Plant(int initialBugs) {
        numBugsOnPlant = initialBugs;
        strongestOnPlant = null;
        isInfected = numBugsOnPlant > 0? true : false;
    }

    public boolean infect() {
        if(!isInfected) {
            isInfected = true;
            return true;
        } else return false;
    }

    public RiceBug getStrongestOnPlant() {
        return strongestOnPlant;
    }

    public void addBug(RiceBug bug) {
        bugsOnPlant.add(bug);
        if(!bug.alive()) bug.animate();

        if(bug.getStrength() > strongestOnPlant.getStrength()) {
            strongestOnPlant = bug;
        }
    }

    public void removeBug(RiceBug bug) {
        int bugIndex = 0;

        while(bugsOnPlant.get(bugIndex).getID() != bug.getID()) {
            bugIndex++;
        }

        bugsOnPlant.remove(bugIndex);

        if (strongestOnPlant == bug) {
            strongestOnPlant = bugsOnPlant.get(0);
            for(int i = 1; i < bugsOnPlant.size(); i++) {
                if(bugsOnPlant.get(i).getStrength() > strongestOnPlant.getStrength()) {
                    strongestOnPlant = bugsOnPlant.get(i);
                }
            }
        }
        numBugsOnPlant = bugsOnPlant.size();
    }

    public boolean hasBugsOn() {
        return numBugsOnPlant > 0;
    }
}
