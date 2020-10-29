//Authors:
//DAVID MKRTCHYAN
//ADAM ALAAEDDINE
//=====================================================================================================================
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Elephant extends Thread {
    public static boolean canMove = true;
    public static int elephantEaten = 0;
    // ====================================
    private Square[][] gameboard = null;
    private Mouse[] mice = null;
    double[] distances = new double[9];
    private int possibleDir[][] = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
    // ====================================
    private int xPos, yPos, newXPos, newYPos;
    int miceInDistance = 0;
    private String name = null;
    private boolean isAlive = true;

    public Elephant(String name, int xPos, int yPos, Square[][] gameboard, Mouse[] mice) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.gameboard = gameboard;
        this.mice = mice;
    }

    @Override
    public void run() {
        synchronized (gameboard) {
            boolean interrupted = true;
            while (interrupted) {
                try {
                    gameboard.wait();
                    interrupted = false;
                } catch (InterruptedException ex) {
                }
            }
        }

        while (!Mouse.gameDone && isAlive) {
            moveRandom();
        }
    }

    public void moveRandom() {
        synchronized (gameboard) {
            elephantEaten = 0;
            miceInDistance = 0;

            for (Mouse m : mice) {             //check for amount of mice in striking distance
                if (distance(newXPos, newYPos, m.getXPos(), m.getYPos()) <= Hunt.strikingDistance) {
                    miceInDistance++;
                }
            }

            if (canMove) {
                if (miceInDistance >= 1) {
                    for (int i = 0; i < distances.length; i++) {        //strategically move (not random)
                        int tmpXPos, tmpYPos;

                        if (i == 0) {                   //top left
                            tmpXPos = xPos - 1;
                            tmpYPos = yPos + 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {         //check bounds
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)          //check for elephant
                                    distances[0] = -1;
                                distances[0] = shortestDistance(tmpXPos, tmpYPos);      //record closest mouse distance
                            } else {
                                distances[0] = -1;     //-1, b/c when sorting the array, it will end up at the end of it
                            }
                        } else if (i == 1) {              //top
                            tmpXPos = xPos;
                            tmpYPos = yPos + 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[1] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[1] = -1;
                            }
                        } else if (i == 2) {         //top-right
                            tmpXPos = xPos + 1;
                            tmpYPos = yPos + 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[2] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[2] = -1;
                            }
                        } else if (i == 3) {          //right
                            tmpXPos = xPos + 1;
                            tmpYPos = yPos;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[3] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[3] = -1;
                            }
                        } else if (i == 4) {         //bottom right
                            tmpXPos = xPos + 1;
                            tmpYPos = yPos - 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[4] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[4] = -1;
                            }
                        } else if (i == 5) {             //bottom
                            tmpXPos = xPos;
                            tmpYPos = yPos - 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[5] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[5] = -1;
                            }
                        } else if (i == 6) {        //bottom left
                            tmpXPos = xPos - 1;
                            tmpYPos = yPos - 1;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[6] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[6] = -1;
                            }
                        } else if (i == 7) {           //left
                            tmpXPos = xPos - 1;
                            tmpYPos = yPos;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[7] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[7] = -1;
                            }
                        } else {                        //current position
                            tmpXPos = xPos;
                            tmpYPos = yPos;

                            if (isInBounds(tmpXPos, tmpYPos)) {
                                if (gameboard[tmpXPos][tmpYPos].getElephantCounter() > 0)
                                    distances[0] = -1;
                                distances[8] = shortestDistance(tmpXPos, tmpYPos);
                            } else {
                                distances[8] = -1;
                            }
                        }
                    }
                    int bestDir = -1;

                    for (int i = 0; i < distances.length; i++) {     //choose best direction from all the distances
                        if (bestDir == -1) bestDir = 0;

                        if (distances[i] > distances[bestDir]) {
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
                    } else if (bestDir == 7) {
                        newXPos = xPos - 1;
                        newYPos = yPos;
                    } else {
                        newXPos = xPos;
                        newYPos = yPos;
                    }
                } else {                                                     //move randomly
                    boolean occupied = true;

                    while (occupied) {
                        int randDir = ThreadLocalRandom.current().nextInt(8);
                        int[] moveTo = possibleDir[randDir];

                        newXPos = xPos + moveTo[0];
                        newYPos = yPos + moveTo[1];

                        if (newXPos >= gameboard.length - 1) {
                            newXPos = gameboard.length - 1;
                        } else if (newXPos <= 0) {
                            newXPos = 0;
                        }

                        if (newYPos >= gameboard[0].length - 1) {
                            newYPos = gameboard[0].length - 1;
                        } else if (newYPos <= 0) {
                            newYPos = 0;
                        }

                        if (gameboard[newXPos][newYPos].getElephantCounter() == 0) {
                            occupied = false;                                 //if there's elephant, look for other spot
                        }
                    }


                }

                gameboard[newXPos][newYPos].incrementElephantCounter();        //update old and new square
                gameboard[xPos][yPos].decrementElephantCounter();

            }

            Mouse.atBarrier++;                                                 //wait for everyone to get moves
            if (Mouse.atBarrier == Hunt.totAnimalsAtBarriers) {
                printTurn();

                if (Mouse.turnCounter % 2 == 0) canMove = true;
                else canMove = false;

                Mouse.turnCounter++;
                Mouse.atBarrier = 0;
                gameboard.notifyAll();

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

        if (canMove == true) {                                                     //make move
            xPos = newXPos;
            yPos = newYPos;
            System.out.println(name + " to " + xPos + " " + yPos);
        }

        synchronized (gameboard) {                                            //wait for everyone to finish making move
            Mouse.doneMoving++;
            if (Mouse.doneMoving == Hunt.totAnimalsAtBarriers) {
                gameboard.notifyAll();
                Mouse.doneMoving = 0;
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
                                                                                     //elephant does checks
        if (gameboard[xPos][yPos].getMiceCounter() >= 2) {                      //if 2 >= mouse, elephant dies
            Hunt.numElephants--;
            isAlive = false;
            elephantEaten++;
            gameboard[xPos][yPos].decrementElephantCounter();
            System.out.println(name + " GOT EATEN!!!");

            if (Hunt.numElephants == 0) {
                Mouse.gameDone = true;
            }
        } else if (gameboard[xPos][yPos].getMiceCounter() == 1) {               //if 1 mouse, launch mouse
            for (Mouse m : mice) {
                if (m.getXPos() == xPos && m.getYPos() == yPos) {
                    newXPos = 0;
                    newYPos = 0;
                    int randDir = new Random().nextInt(8);
                    int[] moveTo = possibleDir[randDir];

                    newXPos = xPos + (moveTo[0] * Hunt.strikingDistance);
                    newYPos = yPos + (moveTo[1] * Hunt.strikingDistance);

                    if (newXPos >= gameboard.length - 1) {
                        newXPos = gameboard.length - 1;
                    } else if (newXPos <= 0) {
                        newXPos = 0;
                    }

                    if (newYPos >= gameboard[0].length - 1) {
                        newYPos = gameboard[0].length - 1;
                    } else if (newYPos <= 0) {
                        newYPos = 0;
                    }

                    gameboard[xPos][yPos].decrementMiceCounter();

                    m.setXPos(newXPos);
                    m.setYPos(newYPos);


                    System.out.println(m.getMouseName() + " LAUNCHED to " + m.getXPos() + " " + m.getYPos());

                    gameboard[newXPos][newYPos].incrementMiceCounter();
                    break;
                }
            }
        }

        synchronized (gameboard) {                                          //wait for elephants to finish doing checks
            Mouse.waitingforElephant++;
            if (Mouse.waitingforElephant == Hunt.totAnimalsAtBarriers) {
                if (elephantEaten > 0) {
                    Hunt.totAnimalsAtBarriers -= elephantEaten;
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

    //=======================================METHODS==============================================================

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

    public String getElephantName() {
        return name;
    }

    private void printTurn() {
        if (Mouse.turnCounter % 2 != 0) {
            System.out.println("\n=========================================================\n");
            System.out.println("TURN " + (Mouse.turnCounter) + " --- MICE ONLY");
            System.out.println("\n=========================================================\n");
        } else {
            System.out.println("\n=========================================================\n");
            System.out.println("TURN " + (Mouse.turnCounter));
            System.out.println("\n=========================================================\n");
        }
    }

    public double distance(int xPos1, int yPos1, int xPos2, int yPos2) {
        return Math.sqrt(Math.pow((xPos1 - xPos2), 2) + Math.pow((yPos1 - yPos2), 2));
    }

    public double shortestDistance(int x, int y) {                                  //finds mouse w/ the shortest
        double tmpDistance;                                                         //distance to current square
        double shortestDistance = -1;

        for (Mouse m : mice) {
            tmpDistance = distance(x, y, m.getXPos(), m.getYPos());

            if (shortestDistance == -1) shortestDistance = tmpDistance;

            if (tmpDistance < shortestDistance) shortestDistance = tmpDistance;
        }
        return shortestDistance;
    }


    public boolean isInBounds(int x, int y) {
        boolean inBounds = true;

        if ((x >= gameboard.length - 1 || x <= 0) || (y >= gameboard[0].length - 1 || y <= 0))
            inBounds = false;

        return inBounds;
    }
}