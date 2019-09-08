package a_star_pathfind;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;



/**
 * This class finds the best path from a start location to the goal locations given the locations of the monsters and the map of nearby monsters.
 * The result must be placed in the 'path' field. 
 * The entry point for your code is in method findPath().
 * 
 * The class can be run as a Java application. In this case it expects two files: 
 * the first specifies the size of the world, the locations of monsters, the start and goal locations; 
 * the second specifies the nearby monsters grid. The remaining parameters in the second file are ignored.
 * 
 */
public class PathFinder {
	
	private Location start;		// start location
	private Location goal;		// goal location
	private GridWorld monsters; // where the monsters are
	private GridWorld nearby;   // how many monsters nearby
	private Path path;			// the solution path
	private int explored = 0;	// number of locations expanded during the search
	
	HashSet<SearchNode>	exploredLocs = new HashSet<SearchNode>();
	PriorityQueue<SearchNode> frontier = new PriorityQueue<SearchNode>();
	
	public PathFinder(GridWorld monsters, GridWorld nearby, Location start, Location goal) {
		this.monsters = monsters;
		this.nearby   = nearby;
		this.start    = start;
		this.goal     = goal;
		this.path     = null;
	}
	
	public GridWorld getMonsters() {
		return monsters;
	}

	public GridWorld getNearby() {
		return nearby;
	}

	public Location getStart() {
		return start;
	}
	
	public Location getGoal() {
		return goal;
	}
	
	public Path getPath() {
		return path;
	}
	
	public int getExplored() {
		return explored;
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: a_star_pathfind resources/worldXX_monsters.txt resources/worldXX_near.txt");
			System.exit(1);
		}
		
		SearchProblem monsters = SearchProblem.fromFile(args[0]);
		SearchProblem nearby   = SearchProblem.fromFile(args[1]);
		PathFinder navigator   = new PathFinder(monsters.getWorld(), nearby.getWorld(), monsters.getStartLoc(), monsters.getGoalLoc());
		if (navigator.findPath() ){
			System.out.println("Path:");
			System.out.println(navigator.getPath());
		} else {
			System.out.println("No path exists");
		}
	}
	
	/* DO NOT CHANGE THE CODE ABOVE */
	
	public boolean findPath() {
		//TODO
		// Q1
		// Implement A* search that finds the "best" path from start to goal.
		// Construct a Path object and store it in "path"
		// Return true if a solution was found and false otherwise.
		// Refer to the assignment specification for details about the desired path.
		
		path = new Path(start);
		SearchNode startNode = new SearchNode(start, 0, nearby.manhattenDistance(start, goal));
		SearchNode goalNode = new SearchNode(goal, 0, 0);
		
		frontier.add(startNode);
		System.out.println("Start: " + startNode.toString());
		System.out.println("Goal: " + goalNode.toString());
		
		while(!frontier.isEmpty()){
			SearchNode temp = frontier.poll();
			//System.out.println("Expanding: " + temp.toString());
			
			if (temp.getLocation().equals(goal)){
				System.out.println("Path found!!!");
				//System.out.println("" + temp.toString());				
				explored = exploredLocs.size();
				System.out.println("Explored: " + explored);

				ArrayList<SearchNode> array = new ArrayList<SearchNode>();
				ArrayList<SearchNode> array2 = new ArrayList<SearchNode>();				
				array.add(temp);
				while (temp.getParent() != null){
					array.add(temp.getParent());
					temp = temp.getParent();
				}
				for (int k = array.size() -2; k >= 0; k --){
					array2.add(array.get(k));
				}
				for (SearchNode s : array2){
					path.moveTo(s.getLocation(), s.stepCost(nearby, s.getParent().getLocation()));
				}
				return true;
			}
			
			exploredLocs.add(temp);
			ArrayList<SearchNode> neighbours = (ArrayList<SearchNode>) temp.expand(nearby, goal);
			for (SearchNode s : neighbours){
				if (exploredLocs.contains(s)){
					continue;
				}
				
				else if (monsters.getValueAt(s.getLocation()) == 1){
					//exploredLocs.add(s);
					continue;
				}
				
				int stepCost = temp.stepCost(nearby, s.getLocation());
				int g = temp.getG() + stepCost;
				//System.out.println(temp.toString() + ": " + g + "  " + s.toString() + ": " + s.getG());
				if (!frontier.contains(s) || g < s.getG()){
					if (!frontier.contains(s)){
						frontier.add(s);
					}
				}
			}
		}
		System.out.println();
		return false;
	}	
}