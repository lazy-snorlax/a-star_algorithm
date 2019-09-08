package a_star_pathfind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchNode implements Comparable<Object>{

	private SearchNode parent;
	private Location location;
	private int f, g, h;

	/**
	 * @param location
	 * @param f
	 * @param g
	 * @param h
	 */
	public SearchNode(Location location, int g, int h) {
		this.location = location;
		this.g = g;
		this.h = h;
		this.f = g + h;
	}
	/**
	 * @param parent
	 * @param location
	 * @param f
	 * @param g
	 * @param h
	 */
	public SearchNode(SearchNode parent, Location location, int g, int h) {
		this(location,g,h);
		this.parent = parent;
	}


	/**
	 * @return the parent
	 */
	public SearchNode getParent() {
		return parent;
	}
	public void setParent(SearchNode parent) {
		this.parent = parent;
	}
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @return the f
	 */
	public int getF() {
		return f;
	}
	/**
	 * @return the g
	 */
	public int getG() {
		return g;
	}
	/**
	 * @return the h
	 */
	public int getH() {
		return h;
	}

	public Collection<SearchNode> expand(GridWorld map, Location goal){
		Collection<SearchNode> list = new ArrayList<SearchNode>();
		List<Location> neighbours = (List<Location>) map.getNeighbours(this.getLocation());

		for (Location l : neighbours){
			list.add(new SearchNode(this, l, stepCost(map, l) + this.getG(), map.manhattenDistance(goal, l)));
		}

		return list;
	}

	@Override
	public String toString() {
		return nodesOnPath() + "[f=" + f + ",g=" + g + ",h=" + h + "]";
	}
	
	public List<Location> getLocationsOnPath() {
		List<Location> locations = new ArrayList<>();
		locations.add(location);
		if (parent == null) return locations;
		locations = parent.getLocationsOnPath();
		locations.add(location);
		return locations;		
	}
	
	public String nodesOnPath(){
		StringBuffer str = new StringBuffer();
		for (Location loc : getLocationsOnPath()){
			str.append("(" + loc.getRow() + "," + loc.getColumn() + ")");
		}
		return str.toString();
	}

		

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}
	
	@Override
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) return false;
		SearchNode other = (SearchNode) obj;
		return this == obj || other.location.equals(location);
	}
	
	@Override
	public int compareTo(Object o) {
		int otherF = ((SearchNode)o).f;
		return f - otherF; // prefer lower F value
	}

	public int stepCost(GridWorld map, Location loc){
			float s = (map.getValueAt(this.location) + map.getValueAt(loc)) + 1;
			s = s/2;
			s = Math.round(s);
			//System.out.println("StepCost of " + this.getLocation().toString() + " and " + node.getLocation().toString() + " = " + s);
			return (int)s;
	}
}
