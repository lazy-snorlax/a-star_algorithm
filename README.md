# a-star_algorithm
A-Star Pathfinding Algorithm
 An A* Pathfinding Algorithm I created a few years back.
 
 It takes an 8x8 grid and from a provided starting point finds the optimal path to a provided end point.
 As I recall, in context it was built as a game. You needed to find the optimal path from A to B without running into any monsters. Each square of the grid contained a number presenting the number of nearby monsters in an adjacent square.
 
 #Example:
 
 World: number of nearby monsters
 
8 8 # map size (rows columns)

7 0 # start location (row column)

0 7 # end location (row column)

1   # nearby threshold

1  1  2  1  2  1  1  0

2  3  5  3  3  2  2  1 

3  5  7  4  3  2  2  1 

3  6  9  7  5  3  3  2 

3  6  7  6  4  3  4  3 

2  4  4  5  4  5  5  4 

2  4  3  3  3  6  6  4 

1  2  2  2  3  5  4  2


Which is based on:

0  0  0  1  0  1  0  0 

0  1  0  0  0  0  0  0 

0  1  1  1  0  0  1  0 

0  1  1  1  0  1  0  0 

0  1  1  1  1  0  0  1 

1  0  1  0  0  0  1  1 

0  0  0  0  1  1  1  0 

0  1  1  0  0  1  1  0
