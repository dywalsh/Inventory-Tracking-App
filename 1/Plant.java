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

    public boolean infect() {
        if (!isInfected) {
            isInfected = true;
            return true;
        } else return false;
    }

    public void addBug(RiceBug bug) {
        bugsOnPlant.add(bug);
        numBugsOnPlant++;

        if(bug.getStrength() > getStrongest()) {
            strongestOnPlant = bug;
        }
    }

    public void removeBug(RiceBug bug) {
        if (this.hasBugsOn()) {
            int bugIndex = 0;
            if (bugsOnPlant.size() > 1) {
                while (bugIndex < bugsOnPlant.size() && bugsOnPlant.get(bugIndex).getID() != bug.getID()) {
                    bugIndex++;
                }
            } else {
                bugIndex = 0;
            }

            bugsOnPlant.remove(bugIndex);

            if (strongestOnPlant == bug && bugsOnPlant.size() > 0) {
                strongestOnPlant = bugsOnPlant.get(0);
                for (int i = 1; i < bugsOnPlant.size(); i++) {
                    if (bugsOnPlant.get(i).getStrength() > strongestOnPlant.getStrength()) {
                        strongestOnPlant = bugsOnPlant.get(i);
                    }
                }
            } else {
                strongestOnPlant = null;
            }
            numBugsOnPlant = bugsOnPlant.size();
        }
    }

    public boolean hasBugsOn() {
        return numBugsOnPlant > 0;
    }

    public ArrayList<RiceBug> getBugsOnPlant() {
        return bugsOnPlant;
    }

    public int getStrongest() {
        if (strongestOnPlant != null) {
            return strongestOnPlant.getStrength();
        } else {
            return 0;
        }
    }
}
