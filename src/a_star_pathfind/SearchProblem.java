package a_star_pathfind;
 

import java.io.FileReader;
import java.util.Scanner;

/**
 * This class holds the parameters of a game instance. 
 * Instances of this class are either created from the text file specifying a game or supplied by a JUnit test.
 * The class provides: the grid map holding some of the nearby monster indicators, the start and goal locations on the map,
 *                     and the distance threshold for "nearby" monsters.
 * The top left corner of the map is at (0,0).
 *
 */
public class SearchProblem {

	private GridWorld world;		// the sparse hints about nearby monsters
	private Location startLoc;		// start location
	private Location goalLoc;		// goal location
	private int sensorRange;		// the threshold for "nearby" monsters

	public SearchProblem(GridWorld world, Location startLoc, Location goalLoc, int sensorRange) {
		this.world = world;
		this.startLoc = startLoc;
		this.goalLoc = goalLoc;
		this.sensorRange = sensorRange;
	}

	public GridWorld getWorld() {
		return world;
	}
	
	public int getRows() {
		return world.getRows();
	}

	public int getColumns() {
		return world.getColumns();
	}

	public Location getStartLoc() {
		return startLoc;
	}

	public Location getGoalLoc() {
		return goalLoc;
	}

	public int getSensorRange() {
		return sensorRange;
	}


	public static SearchProblem fromFile(String fileName) {
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(fileName));
			while(scanner.hasNext("#")) {
				scanner.nextLine();
			}
			int rows = scanner.nextInt(); 
			int columns = scanner.nextInt(); 
			scanner.nextLine();
			int startRow = scanner.nextInt();
			int startCol = scanner.nextInt();
			scanner.nextLine();
			int goalRow = scanner.nextInt();
			int goalCol = scanner.nextInt();
			scanner.nextLine();
			int range = scanner.nextInt();
			scanner.nextLine();
			GridWorld world = new GridWorld(rows,columns);
			world.initializeFromScanner(scanner);			
			Location startLoc = new Location(startRow,startCol);
			Location goalLoc = new Location(goalRow,goalCol);
			return new SearchProblem(world,startLoc,goalLoc,range);
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		finally {
			if (scanner != null) scanner.close();
		}
		return null; // unreachable		
	}

}
