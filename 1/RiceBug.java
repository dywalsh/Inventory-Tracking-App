
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
        isAlive = true;
        currentX = initX;
        currentY = initY;
        startTime = startAt;
    }

    public void move(Plant[][] field) {
        try {
            field[currentX][currentY].removeBug(this);


            switch (DIRECTION) {
                case 'B':
                    currentX--;
                    break;
                case 'G':
                    currentX++;
                    break;
                case 'D':
                    currentY--;
                    break;
                case 'E':
                    currentY++;
                    break;
                case 'A':
                    currentY--;
                    currentX--;
                    break;
                case 'C':
                    currentY++;
                    currentX--;
                    break;
                case 'F':
                    currentY--;
                    currentX++;
                    break;
                case 'H':
                    currentY++;
                    currentX++;
                    break;
                default:
                    currentY = -1;
                    currentX = -1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            currentX = -1;
            currentY = -1;
        }
    }

    public void kill() {
        isAlive = false;
        currentX = 0;
        currentY = 0;
    }

    public int getID() {
        return ID;
    }

    public int getStrength() {
        return strength;
    }

    public boolean alive() { return isAlive; }

    public void increaseStrength() {
        strength++;
    }

    public int getStartTime() { return startTime; }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public String toString() {
        return "BUG ID: " + ID + "\nSTRENGTH: " + strength + "\nCURRENT POSITION: [" + currentX + "]" + "[" + currentY + "]" +
                "\nDIRECTION: " + DIRECTION + "\nBUG IS ALIVE: " + isAlive;
    }
}
