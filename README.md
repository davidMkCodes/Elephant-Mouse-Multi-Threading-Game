"# Elephant-Mouse-Multi-Threading-Game" 
Input (Integers): (number of rows) (number of columns) (striking distance) (number of elephants) (number of mice)
Example input: 100 100 6 4 7

This "game" was created to make use and gain a better understanding of multi-threading, locks, semaphores and other such concepts

User-inputs:
- x amount of mice
- x amount of elephants
- x by y board size
- mice striking distance

The rules of the game are as follows:
- Mice are trying to eat all elephants on the board
- Mice move 1 square in a random direction every turn
- Elephants move 1 square in a random direction every OTHER turn
- If TWO mice are within striking distance of eachother, the mice (can be one or both) that are ALSO in striking distance of an elephant will move towards the elephant 
  (no longer random)
- If only ONE mouse is within striking distance of an elephant, the mouse becomes frozen - waiting for another mice to to enter its own striking distance OR
  for the elephant to leave its striking distance OR an elephant randomly moves onto its square and launches it in a random direction
- If 2 mice are within striking distance of an elephant, the elephant becomes scared and freezes in place
- The striking distance is the number of squares a mice needs to be within an elephant to attack
- To eat an elephant (remove them from the game), 2 mice must occupy the same square as an elephant on the board. 
- If only 1 mice occupies the same square as an elephant, the mice gets launched x number of squares in a random direction (x = striking distance)


All mice and elephants move in real-time within a turn-based system using multithreading, locks, semaphores, etc
Although it is labeled a "game", it is not really a game as the mice always win 
The amount of time or turns it takes for them to win is based on the provided inputs, and some luck
For example, a larger board will lessen the chances of two mice encountering an elephant - considering their movement is initially random
