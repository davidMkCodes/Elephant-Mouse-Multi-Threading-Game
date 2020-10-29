//Authors:
//DAVID MKRTCHYAN
//ADAM ALAAEDDINE
//=====================================================================================================================
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends Thread {
    public static int totalMice = 0;
    public static int turnCounter = 0;
    public static int doneMoving = 0;
    public static int waitingforElephant = 0;
    public static boolean gameDone = false;
    public static int atBarrier = 0;
    // ====================================
    private Elephant[] elephants;
    private Mouse[] mice;
    private Square[][] gameboard = null;
    int possibleDir[][] = {{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};
    double []distances = new double[8];
    // ====================================
    private String name = null;
    private int xPos, yPos;
    int newXPos = 0;
    int newYPos = 0;
    boolean frozen = false;
    boolean nearElephant = false;
    boolean nearMouse = false;

    public Mouse(String name, int xPos, int yPos, Square[][] gameboard, Elephant[] elephants, Mouse[] mice) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.gameboard = gameboard;
        this.elephants = elephants;
        this.mice = mice;
    }

    @Override
    public void run() {
        synchronized (gameboard) {
            atBarrier++;
            if (atBarrier == Hunt.numMice) {
                gameboard.notifyAll();
                atBarrier = 0;
            } else {
                boolean interrupted = true;
                while (interrupted) {
                    try {
                        gameboard.wait();
                        interrupted = false;
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        while (!gameDone) {
            moveRandom();
        }
    }

    public void moveRandom(){
        newXPos = 0;
        newYPos = 0;
        frozen = false;
        nearElephant = false;
        nearMouse = false;

        synchronized (gameboard) {
            for (Elephant elephant : elephants) {                         //check if near elephant
                if(elephant.isAlive()) {
                    if (distance(xPos, yPos, elephant.getXPos(), elephant.getYPos()) <= Hunt.strikingDistance) {
                        nearElephant = true;
                        break;
                    } else {
                        nearElephant = false;
                    }
                }
            }
            for (Mouse mouse : mice) {                                  //check if near mouse
                if (mouse != this) {
                    if (distance(xPos, yPos, mouse.getXPos(), mouse.getYPos()) <= 1.5 * Hunt.strikingDistance) {
                        nearMouse = true;
                        break;
                    }else{
                        nearMouse = false;
                    }
                }
            }

            if(nearElephant){
                if(nearMouse) {                                                //if near elephant AND near mouse
                    for (int i = 0; i < distances.length; i++) {                //move strategically (not random)
                        int tmpXPos, tmpYPos;

                        if (i == 0) {                       //top left
                            tmpXPos = xPos - 1;
                            tmpYPos = yPos + 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[0] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[0] = Integer.MAX_VALUE;
                            }
                        } else if (i == 1) {                      //top
                            tmpXPos = xPos;
                            tmpYPos = yPos + 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[1] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[1] = Integer.MAX_VALUE;
                            }
                        } else if (i == 2) {                    //top-right
                            tmpXPos = xPos + 1;
                            tmpYPos = yPos + 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[2] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[2] = Integer.MAX_VALUE;
                            }
                        } else if (i == 3) {                       //right
                            tmpXPos = xPos + 1;
                            tmpYPos = yPos;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[3] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[3] = Integer.MAX_VALUE;
                            }
                        } else if (i == 4) {                 //bottom right
                            tmpXPos = xPos + 1;
                            tmpYPos = yPos - 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[4] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[4] = Integer.MAX_VALUE;
                            }
                        } else if (i == 5) {                 //bottom
                            tmpXPos = xPos;
                            tmpYPos = yPos - 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[5] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[5] = Integer.MAX_VALUE;
                            }
                        } else if (i == 6) {                  //bottom left
                            tmpXPos = xPos - 1;
                            tmpYPos = yPos - 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[6] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[6] = Integer.MAX_VALUE;
                            }
                        } else {                       //left
                            tmpXPos = xPos - 1;
                            tmpYPos = yPos;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                distances[7] = closest(tmpXPos, tmpYPos);
                            } else {
                                distances[7] = Integer.MAX_VALUE;
                            }
                        }
                    }

                    int bestDir = -1;                                   //check for best direction to move to

                    for (int i = 0; i < distances.length; i++) {
                        if (bestDir == -1) bestDir = 0;

                        if (distances[i] < distances[bestDir]) {
                            bestDir = i;
                        }
                    }

                    if (bestDir == 0) {
                        newXPos = xPos - 1;
                        newYPos = yPos + 1;
                    } else if (bestDir == 1) {
                        newXPos = xPos;
                        newYPos = yPos + 1;
                    } else if (bestDir == 2) {
                        newXPos = xPos + 1;
                        newYPos = yPos + 1;
                    } else if (bestDir == 3) {
                        newXPos = xPos + 1;
                        newYPos = yPos;
                    } else if (bestDir == 4) {
                        newXPos = xPos + 1;
                        newYPos = yPos - 1;
                    } else if (bestDir == 5) {
                        newXPos = xPos;
                        newYPos = yPos - 1;
                    } else if (bestDir == 6) {
                        newXPos = xPos - 1;
                        newYPos = yPos - 1;
                    } else {
                        newXPos = xPos - 1;
                        newYPos = yPos;
                    }
                }
                else{                                           //if near elephant but NOT near mouse, become FROZEN
                    frozen = true;
                }
            }
            else{                                                                   //MOVE RANDOMLY
                int randDir = ThreadLocalRandom.current().nextInt(8);

                if(randDir == 0){
                    newXPos = xPos-1;
                    newYPos = yPos+1;
                }else if(randDir == 1){
                    newXPos = xPos;
                    newYPos = yPos+1;
                }else if(randDir == 2){
                    newXPos = xPos+1;
                    newYPos = yPos+1;
                }else if(randDir == 3){
                    newXPos = xPos+1;
                    newYPos = yPos;
                }else if(randDir == 4){
                    newXPos = xPos+1;
                    newYPos = yPos-1;
                }else if(randDir == 5){
                    newXPos = xPos;
                    newYPos = yPos-1;
                }else if(randDir == 6){
                    newXPos = xPos-1;
                    newYPos = yPos-1;
                }else if(randDir == 7){
                    newXPos = xPos-1;
                    newYPos = yPos;
                }

                if (newXPos >= gameboard.length - 1) {                       //CHECK BOUNDS
                    newXPos = gameboard.length-1;
                } else if (newXPos <= 0) {
                    newXPos = 0 ;
                }

                if (newYPos >= gameboard[0].length - 1) {
                    newYPos = gameboard[0].length-1;
                } else if (newYPos <= 0) {
                    newYPos = 0;
                }


            }

            atBarrier++;
            if (atBarrier == Hunt.totAnimalsAtBarriers) {                 //wait here for everyone to get next move
                printTurn();

                if (turnCounter % 2 == 0) Elephant.canMove = true;
                else Elephant.canMove = false;
                turnCounter++;
                atBarrier = 0;
                gameboard.notifyAll();

            }
            else {
                boolean interrupted = true;
                while (interrupted) {
                    try {
                        gameboard.wait();
                        interrupted = false;
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        if (!frozen) {                                                    //make move
            gameboard[newXPos][newYPos].incrementMiceCounter();
            gameboard[xPos][yPos].decrementMiceCounter();
            xPos = newXPos;
            yPos = newYPos;
        }
        else System.out.println(name + " IS FROZEN!");

        System.out.println(name + " to " + xPos + " " + yPos);

        synchronized (gameboard) {                                    //wait here for all animals to finish their moves
            doneMoving++;
            if (doneMoving == Hunt.totAnimalsAtBarriers) {
                gameboard.notifyAll();
                doneMoving = 0;
            } else {
                boolean interrupted = true;
                while (interrupted) {
                    try {
                        gameboard.wait();
                        interrupted = false;
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        synchronized (gameboard) {                                    //wait for elephants to finish doing checks
            waitingforElephant++;
            if (waitingforElephant == Hunt.totAnimalsAtBarriers) {
                if(Elephant.elephantEaten>0) {
                    Hunt.totAnimalsAtBarriers -= Elephant.elephantEaten;
                }
                gameboard.notifyAll();
                Mouse.waitingforElephant = 0;
            } else {
                boolean interrupted = true;
                while (interrupted) {
                    try {
                        gameboard.wait();
                        interrupted = false;
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }

    public double distance(int xPos1, int yPos1, int xPos2, int yPos2) {
        return Math.sqrt(Math.pow((xPos1 - xPos2), 2) + Math.pow((yPos1 - yPos2), 2));
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int newXPos) {
        xPos = newXPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int newYPos) {
        yPos = newYPos;
    }

    public String getMouseName() {
        return name;
    }

    private void printTurn(){
        if (turnCounter % 2 != 0) {
            System.out.println("\n=========================================================\n");
            System.out.println("TURN " + (Mouse.turnCounter) + " --- MICE ONLY");
            System.out.println("\n=========================================================\n");
        } else {
            System.out.println("\n=========================================================\n");
            System.out.println("TURN " + (Mouse.turnCounter));
            System.out.println("\n=========================================================\n");
        }
    }

    public boolean isInBounds(int x, int y){
        boolean inBounds = true;

        if (x >= gameboard.length - 1 || x <= 0) {
            inBounds = false;
        }

        if (y >= gameboard[0].length - 1 || y <= 0) {
            inBounds = false;
        }
        return inBounds;
    }

    public double closest(int x, int y){                                    //check for closest elephant
        double tmpDistance;
        double closest = -1;

        for (Elephant e : elephants) {
            if(e.isAlive()) {
                tmpDistance = distance(x, y, e.getXPos(), e.getYPos());

                if (closest == -1) closest = tmpDistance;

                if (tmpDistance < closest) closest = tmpDistance;
            }
        }

        return closest;
    }

}