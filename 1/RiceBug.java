
public class RiceBug {
    private final char DIRECTION;
    private final int ID;
    private int strength;
    private int currentX;
    private int currentY;
    private int startTime;
    private boolean isAlive;

    public RiceBug(int ident, int initX, int initY, int startAt, char dir) {
        DIRECTION = dir;
        ID = ident;
        strength = 0;
        isAlive = false;
        currentX = initX;
        currentY = initY;
        startTime = startAt;
    }

    public void move(Plant[][] field) {
        field[currentX][currentY].removeBug(this);

        switch(DIRECTION) {
            case 'B':
                currentY++;
                break;
            case 'G':
                currentY--;
                break;
            case 'D':
                currentX--;
                break;
            case 'E':
                currentX++;
                break;
            case 'A':
                currentX--;
                currentY++;
                break;
            case 'C':
                currentX++;
                currentY++;
                break;
            case 'F':
                currentX--;
                currentY--;
                break;
            case 'H':
                currentX++;
                currentY--;
        }

        field[currentX][currentY].addBug(this);
    }

    public int getID() {
        return ID;
    }

    public int getStrength() {
        return strength;
    }

    public boolean alive() { return isAlive; }

    public void animate() { isAlive = true; }

    public void increaseStrength() {
        strength++;
    }

    public int getStartTime() { return startTime; }

    public String toString() {
        return "BUG ID: " + ID + "\nSTRENGTH: " + strength + "\nCURRENT POSITION: [" + currentX + "]" + "[" + currentY + "]" +
                "\nDIRECTION: " + DIRECTION + "\nBUG IS ALIVE: " + isAlive;
    }
}
