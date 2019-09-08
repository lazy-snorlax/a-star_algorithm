package a_star_pathfind;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;


/**
 * This class holds a grid world map.
 * Each cell in the grid can hold an Integer value or null.
 */
public class GridWorld {
	private Integer map[][];

	public GridWorld(Integer[][] map) {
		assert(map!=null && map.length > 0 && map[0].length>0);
		this.map = map;
	}

	public GridWorld(int rows, int cols) {
		assert(rows>0 && cols > 0);
		this.map = new Integer[rows][cols];
	}

	public int getRows() {
		return map.length;
	}

	public int getColumns() {
		return map[0].length;
	}

	public Integer getValueAt(int row, int col) {
		return map[row][col];
	}

	public Integer getValueAt(Location loc) {
		return getValueAt(loc.getRow(),loc.getColumn());
	}

	public void setValueAt(int row, int col, Integer v) {
		map[row][col]=v;
	}

	public void setValueAt(Location loc, Integer v) {
		setValueAt(loc.getRow(),loc.getColumn(),v);
	}


	public static GridWorld fromString(String repr) {
		String[] rowData = repr.trim().split("\n");
		int rows = rowData.length;
		int cols = rowData[0].trim().split("\\p{javaWhitespace}+").length;
		GridWorld instance = new GridWorld(rows,cols);
		instance.initializeFromScanner(new Scanner(repr));
		return instance;
	}

	void initializeFromScanner(Scanner scanner) {
		for(int r=0; r<getRows(); r++) {
			for(int c=0; c<getColumns(); c++) {
				if (scanner.hasNextInt()) {
					map[r][c] = scanner.nextInt();
				}
				else {
					scanner.next("\\.");
					map[r][c] = null;
				}
			}
			if (scanner.hasNextLine()) scanner.nextLine();
		}
	}

	@Override
	public String toString() {
		StringBuffer repr = new StringBuffer();
		for(Integer[] row : map) {
			for(Integer v : row) {
				if (v!=null) repr.append(String.format("%2d",v));
				else repr.append(". ");
				repr.append(" ");
			}
			repr.append("\n");
		}
		return repr.toString();
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(map);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		GridWorld other = (GridWorld) obj;
		return Arrays.deepEquals(map, other.map);
	}

	public Iterable<Location> getNeighbours(Location loc) {
		List<Location> neighbours = new ArrayList<Location>();
		if (loc.getColumn() > 0) neighbours.add( new Location(loc.getRow(),loc.getColumn()-1) ); 
		if (loc.getColumn() < getColumns()-1) neighbours.add( new Location(loc.getRow(),loc.getColumn()+1) ); 
		if (loc.getRow() > 0) neighbours.add( new Location(loc.getRow()-1,loc.getColumn()) ); 
		if (loc.getRow() < getRows()-1) neighbours.add( new Location(loc.getRow()+1,loc.getColumn()) ); 
		return neighbours;
	}

	public Iterable<Location> getAllNeighbours(Location loc, int k) {
		@SuppressWarnings("unchecked")
		List<Location> neighbours = new ArrayList<Location>();
		neighbours.add(loc);
		int i = 0;
		while (i < k){
			if (loc.getColumn() > 0) {
				neighbours.add( new Location(loc.getRow(),loc.getColumn()-k) ); 
			}
			if (loc.getColumn() < getColumns()-k){
				neighbours.add( new Location(loc.getRow(),loc.getColumn()+k) ); 
			}
			if (loc.getRow() > 0) {
				neighbours.add( new Location(loc.getRow()-k,loc.getColumn()) ); 
			}
			if (loc.getRow() < getRows()-k){
				neighbours.add( new Location(loc.getRow()+k,loc.getColumn()) ); 
			}
			if (loc.getRow() < getRows()-k && loc.getColumn() > 0){
				neighbours.add( new Location(loc.getRow() + k,loc.getColumn() - k)); 
			}
			if (loc.getRow() > 0 && loc.getColumn() < getColumns() - k){
				neighbours.add( new Location(loc.getRow() - k,loc.getColumn() + k)); 
			}
			if (loc.getRow() < getRows()-k && loc.getColumn() < getColumns() - k){
				neighbours.add( new Location(loc.getRow() + k,loc.getColumn() + k)); 
			}
			if (loc.getRow() > 0 && loc.getColumn() > 0){
				neighbours.add( new Location(loc.getRow() - k,loc.getColumn() - k)); 
			}
			i = i + 1;	
		}
		return neighbours;
	}

	public int manhattenDistance(Location loc1, Location loc2){

		int h = Math.abs((loc1.getRow() - loc2.getRow())) + Math.abs((loc1.getColumn() - loc2.getColumn()));
		return h;
	}

}
