/*
COMP 333 - PROJECT 1
//Authors:
//DAVID MKRTCHYAN
//ADAM ALAAEDDINE
====================================================================================================================
Speed-tests  (ARGUMENTS (300 200 6 4 7):

RUN #1:
         NUMBER OF TURNS TO COMPLETE: 1,261,767
         TIME ELAPSED:                4:42
         NOTES: Ran on high-end PC
RUN #2:
         NUMBER OF TURNS TO COMPLETE: 1,928,159
         TIME ELAPSED:                7:08
         NOTES: Ran on high-end PC
RUN #3:
          NUMBER OF TURNS TO COMPLETE: N/A
          TIME ELAPSED:                N/A
          NOTES: Ran on 2015 iMac. IDE crashed after 6-7m (code/program did not crash, only the IDE)
RUN #4:
         NUMBER OF TURNS TO COMPLETE: 1,740,741
         TIME ELAPSED:                6:14
         NOTES: Ran on high-end PC
RUN #5:
         NUMBER OF TURNS TO COMPLETE: 1,354,599
         TIME ELAPSED:                5:51
         NOTES: Ran on high-end PC
RUN #6:
         NUMBER OF TURNS TO COMPLETE: 150,775
         TIME ELAPSED:                1:15
         NOTES:  Ran on 2015 iMac, but on a 100x100 board.

====================================================================================================================
*/

import java.util.Random;
import java.util.Scanner;

public class Hunt {
    public static int numMice;
    public static int numElephants;
    public static int totAnimalsAtBarriers = 0;
    public static int strikingDistance;
    static boolean gameDone = false;

    public static void main(String[] args) {
        Random rand = new Random();
        Scanner myObj = new Scanner(System.in);

        int numRows = Integer.parseInt(args[0]);
        int numCol = Integer.parseInt(args[1]);
        strikingDistance = Integer.parseInt(args[2]);
        numElephants = Integer.parseInt(args[3]);
        numMice = Integer.parseInt(args[4]);

        while (numRows <= 0) {                                                                    //check inputs
            System.out.println("Please enter a number of rows greater than zero!");
            numRows = myObj.nextInt();
        }
        while (numCol <= 0) {
            System.out.println("Please enter a number of columns greater than zero!");
            numCol = myObj.nextInt();
        }
        while (strikingDistance <= 0) {
            System.out.println("Please enter a striking distance greater than zero!");
            strikingDistance = myObj.nextInt();
        }
        while (numElephants <= 0) {
            System.out.println("Please enter a number of elephants greater than zero!");
            numElephants = myObj.nextInt();
        }
        while (numMice <= 0 || numElephants >= numMice) {
            if (numMice <= 0) {
                System.out.println("Please enter a number of mice greater than zero!");
            } else {
                System.out.println("Please enter a number of mice greater than the number of elephants!");
            }
            numMice = myObj.nextInt();
        }

        totAnimalsAtBarriers = numElephants + numMice;
        Square[][] gameboard = new Square[numRows][numCol];
        Elephant[] elephants = new Elephant[numElephants];
        Mouse[] mice = new Mouse[numMice];

        //=============================================================================================================
        //============================Initialize & run ================================================================
        //=============================================================================================================

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCol; j++) {
                gameboard[i][j] = new Square();
            }
        }

        for (int i = 0; i < numElephants; i++) {                                                                           //start elephants
            boolean notOccupied = false;
            while (!notOccupied) {
                int chosenRow = rand.nextInt(numRows);
                int chosenColumn = rand.nextInt(numCol);

                if (gameboard[chosenRow][chosenColumn].getElephantCounter() == 0) {
                    gameboard[chosenRow][chosenColumn].incrementElephantCounter();
                    elephants[i] = new Elephant("Elephant " + (i + 1), chosenRow, chosenColumn, gameboard, mice);
                    notOccupied = true;
                }
            }

            System.out.println(elephants[i].getElephantName() + " spawned at " + elephants[i].getXPos() + " " + elephants[i].getYPos());
            elephants[i].start();
        }

        for (int i = 0; i < numMice; i++) {                                                                                 //start mice
            boolean notOccupied = false;
            while (!notOccupied) {
                int chosenRow = rand.nextInt(numRows);
                int chosenColumn = rand.nextInt(numCol);

                if (gameboard[chosenRow][chosenColumn].getElephantCounter() == 0) {
                    gameboard[chosenRow][chosenColumn].incrementMiceCounter();
                    mice[i] = new Mouse("Mouse " + (i + 1), chosenRow, chosenColumn, gameboard, elephants, mice);
                    notOccupied = true;
                }
            }
            System.out.println(mice[i].getMouseName() + " spawned at " + mice[i].getXPos() + " " + mice[i].getYPos());
            mice[i].start();
        }

        //=============================================================================================================
        //=================================Join threads after done=====================================================
        //=============================================================================================================

        for (Mouse mouse : mice) {
            boolean joined = false;
            while (!joined) {
                try {
                    mouse.join();
                    joined = true;
                } catch (InterruptedException ex) {
                }
            }
        }

        for (Elephant elephant : elephants) {
            boolean joined = false;
            while (!joined) {
                try {
                    elephant.join();
                    joined = true;
                } catch (InterruptedException ex) {
                }
            }
        }
        System.out.println("THE GAME IS DONE!");
    }
}
