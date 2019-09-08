package a_star_pathfind;
 

/**
 * Simple class represents a location on a grid map.
 * A location is a pair (row,column). 
 * The top left corner of the map is location (0,0);
 * The top right corner is at (0,w-1), where w is the number of columns in the grid.
 */
public class Location {
	private int row, column;
	
	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) return false;
		Location other = (Location) obj;
		return this == obj || other.column == column && other.row == row;
	}
	
	@Override
	public String toString() {
		return "(" + row + "," + column + ")";
	}

}
