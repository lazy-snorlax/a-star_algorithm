package a_star_pathfind;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jacop.core.*;
import org.jacop.constraints.*;
import org.jacop.search.*;


/**
 * This class infers where monsters must be on the map given the nearby monster indicators and the distance threshold.
 * The entry point for your code is in method inferMonsters().
 * The class can be run as a Java application. In this case it expects a file specifying the game parameters as argument.
 * The game parameters are provided in the 'spec' field.
 */
public class MapCompleter {

	private SearchProblem spec;			// the problem parameters
	private GridWorld monsters;			// map showing the inferred locations of monsters
	private GridWorld nearbyMonsters;   // map showing the number of nearby monsters

	private int k;
	IntVar vars[];
	Store store;
	IntVar monsterMap[][];
	IntVar nearbyMap[][];

	public MapCompleter(SearchProblem spec) {
		this.spec           = spec;
		this.monsters       = new GridWorld(spec.getRows(), spec.getColumns());
		this.nearbyMonsters = new GridWorld(spec.getRows(), spec.getColumns());
	}

	public GridWorld getMonsters() {
		return monsters;
	}

	public GridWorld getNearbyMonsters() {
		return nearbyMonsters;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java au.edu.unisa.comp2019_2015.assignment1.MapCompleter resources/worldXX_hints.txt");
			System.exit(1);
		}

		SearchProblem problem = SearchProblem.fromFile(args[0]);
		MapCompleter completer = new MapCompleter(problem);
		if (completer.inferMonsters()) {
			System.out.println("Locations of monsters:");
			System.out.println(completer.getMonsters());
			System.out.println("Nearby monsters:");
			System.out.println(completer.getNearbyMonsters());
		} else {
			System.out.println("No solution exists");
		}
	}

	/* DO NOT CHANGE THE CODE ABOVE */

	public boolean inferMonsters() {
		//TODO
		// Q3
		// Implement constraint-based monster inference using JaCOP
		// Place the result in the 'monsters' and 'nearbyMonsters' fields
		// Return true if a solution was found and false otherwise.
		// Refer to the assignment specification for details about this method.

		k = spec.getSensorRange();
		int b = (int) Math.pow((1 + 2*k), 2);
		store = new Store();

		
		monsterMap = new IntVar[spec.getColumns()][spec.getRows()];
		nearbyMap = new IntVar[spec.getColumns()][spec.getRows()];
		for (int i=0; i < monsterMap.length; i ++){
			for (int j=0; j < monsterMap.length; j ++){
				monsterMap[i][j] = new IntVar(store, 0, 1);
				nearbyMap[i][j] = new IntVar (store, 0, b);
			}
		}

		vars = new IntVar[spec.getColumns() * spec.getRows()];
		int index = 0;
		for (int i=0; i < monsterMap.length; i ++){
			for (int j=0; j < monsterMap[i].length; j ++){
				if (index < vars.length){
					vars[index] = monsterMap[i][j];
					index++;
				}
			}
		}

		ArrayList<Location> neighbours = new ArrayList<Location>();
		for (int x = 0; x < monsters.getColumns(); x ++){
			for (int y = 0; y < monsters.getRows(); y ++){

				neighbours = (ArrayList<Location>) spec.getWorld().getAllNeighbours(new Location(x,y), k);
//				neighbours = missingNeighbours(neighbours);
				neighbours = removeNegNeighbours(neighbours);
//				System.out.println(neighbours.toString() + " Size:" + neighbours.size());
				ArrayList<IntVar> intVarNeighbours = locsToIntVars(neighbours);
				IntVar s = nearbyMap[x][y];
				IntVar neigh[] = new IntVar[intVarNeighbours.size()];
				neigh = intVarNeighbours.toArray(neigh);

				if (spec.getWorld().getValueAt(x, y) != null){
					int sum = spec.getWorld().getValueAt(x, y);
					Constraint c1 = new XeqC(s, sum); 
					store.impose(c1);
				}
				Constraint c = new Sum(neigh, s);
				store.impose(c);
			}
		}

		// implement depth first search				
		boolean result = search();
		if (result) {
			System.out.println("Solution:");

			// Put the solved values from the locations array to the monster map
			for (int x = 0; x < monsters.getColumns(); x ++){
				for (int y = 0; y < monsters.getRows(); y ++){
					monsters.setValueAt(x, y, monsterMap[x][y].value());
				}
			}

			// monster map to nearby map
			monsterToNearby(monsters);
			return true;
		}
		else {
			System.out.println("No solution found");
			return false;
		}
	}

	private ArrayList<IntVar> locsToIntVars(ArrayList<Location> locs){
		ArrayList<IntVar> a = new ArrayList<IntVar>();
		System.out.println(locs.toString());
		for (Location l : locs){
			a.add(monsterMap[l.getRow()][l.getColumn()]);
		}
		return a;
	}
	
	private ArrayList<Location> removeNegNeighbours(ArrayList<Location> locs){		
		locs.removeIf(l -> l.getRow() < 0 || l.getColumn() < 0);
		locs.removeIf(l -> l.getRow() > spec.getRows() || l.getColumn() > spec.getColumns());
		return locs;
	}
	
	private ArrayList<Location> missingNeighbours(ArrayList<Location> locs){
		Location temp = locs.get(0);
		int f = 1;
		while (f <= k){
			Location l1 = new Location(temp.getColumn() - k, temp.getRow() - f);
			Location l2 = new Location(temp.getColumn() - k, temp.getRow() + f);
			Location l3 = new Location(temp.getColumn() + k, temp.getRow() - f);
			Location l4 = new Location(temp.getColumn() + k, temp.getRow() + f);
			Location l5 = new Location(temp.getColumn() - f, temp.getRow() + k);
			Location l6 = new Location(temp.getColumn() + f, temp.getRow() + k);
			Location l7 = new Location(temp.getColumn() - f, temp.getRow() - k);
			Location l8 = new Location(temp.getColumn() + f, temp.getRow() - k);
//			System.out.println("" + l1 + l2 + l3 + l4 + l5 + l6 + l7 + l8);
			locs.add(l1);
			locs.add(l2);
			locs.add(l3);
			locs.add(l4);
			locs.add(l5);
			locs.add(l6);
			locs.add(l7);
			locs.add(l8);
			f++;
		}
		return locs;
	}

	public boolean search() {
		Search<IntVar> search = new DepthFirstSearch<IntVar>(); 
		search.setPrintInfo(false); // don't print store
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(vars, new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());                                
		return search.labeling(store, select); 
	}

	private void monsterToNearby(GridWorld monsters) {
		int grid = 0;

		while (k > 0){
			for (int x = 0; x < monsters.getColumns(); x ++){
				for (int y = 0; y < monsters.getRows(); y ++){
					if (monsters.getValueAt(x, y) != null){
						grid += monsters.getValueAt(x, y);

						if (x < monsters.getColumns() - k)
							grid += monsters.getValueAt(x + k, y);
						if (y < monsters.getRows() - k)
							grid += monsters.getValueAt(x, y + k);

						if (x > 0)
							grid += monsters.getValueAt(x - k, y);

						if (y > 0)
							grid += monsters.getValueAt(x, y - k);

						if (x < monsters.getColumns() - k && y < monsters.getRows() - k)
							grid += monsters.getValueAt(x + k, y + k);

						if (x > 0 && y < monsters.getRows() - 1)
							grid += monsters.getValueAt(x - k, y + k);

						if (x < monsters.getColumns() - 1 && y > 0)
							grid += monsters.getValueAt(x + k, y - k);

						if (x > 0 && y > 0)
							grid += monsters.getValueAt(x - k, y - k);
					}
					if (x >= 0 && y >= 0){
						nearbyMonsters.setValueAt(x, y, grid);
					}
					grid = 0;
				}
			}
			k--;
		}




	}

}
