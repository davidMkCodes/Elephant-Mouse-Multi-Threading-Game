//Authors:
//DAVID MKRTCHYAN
//ADAM ALAAEDDINE
//=====================================================================================================================
public class Square {
    private int elephantCounter;
    private int miceCounter;

    public Square() {
        this.elephantCounter = 0;
        this.miceCounter = 0;
    }

    public void incrementElephantCounter() {
        elephantCounter++;
    }

    public void decrementElephantCounter() {
        elephantCounter--;
    }

    public int getElephantCounter() {
        return this.elephantCounter;
    }

    public void incrementMiceCounter() {
        miceCounter++;
    }

    public void decrementMiceCounter() {
        miceCounter--;
    }

    public int getMiceCounter() {
        return this.miceCounter;
    }


}


