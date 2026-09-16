package game.roads;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import game.cells.Cell;
import game.objects.bloons.Bloon;
import utils.datastructures.DoublyLinkedList;
import utils.plane2d.Direction;
import utils.plane2d.Vector2f;

/**
 * Represents a road as a linked list
 */
public class Road implements Iterable<RoadTile> {

	protected DoublyLinkedList<Cell> list;
	protected Direction dir;
	protected List<Bloon> exitedBloons;

	/**
	 * Class constructor
	 * @param direction this road's direction
	 */
	public Road(Direction direction)
	{
		this.list = new DoublyLinkedList<>();
		this.dir = direction;
		this.exitedBloons = new ArrayList<>();
	}

	/**
	 * Create a new road, with a default direction of RIGHT.
	 *
	 * 	|\    \ \ \ \ \ \ \      __
	 * 	|  \    \ \ \ \ \ \ \   | O~-_
	 * 	|   >----|-|-|-|-|-|-|--|  __/
	 * 	|  /    / / / / / / /   |__\
	 * 	|/     / / / / / / /
	 */
	public Road()
	{ this(Direction.RIGHT); }




	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */


	/**
	 * Get the list of bloons travelling on this road.
	 * @return list of bloons
	 */
	public List<Bloon> getBloons()
	{
		List<Bloon> blns = new ArrayList<>();

		for(RoadTile rt : this)
			blns.addAll(rt.getCell().getBloons());

		return blns;
	}

	/**
	 * Get list of bloons which exited the last tile on this road.
	 * Every bloon in this list doesn't have a tile.
	 *
	 * @return List of bloons on this road exiting the board.
	 */
	public List<Bloon> getExitedBloons()
	{ return this.exitedBloons; }


	/**
	 * Return true if the given cell is part of this road
	 * @param cell the cell to test
	 * @return is this cell part of the road 🐡
	 */
	public boolean contains(Cell cell)
	{
		for(RoadTile rt : this)
			if(rt.getCell() == cell)
				return true;

		return false;
	}


	/**
	 * Return the first tile of this road
	 * @return the first tile of this road (null if none)
	 */
	public RoadTile getHead()
	{ return (RoadTile) this.list.getHead(); }


	/**
	 * Return the last tile of this road
	 * @return the last tile of this road (null if none)
	 */
	public RoadTile getTail()
	{ return (RoadTile) this.list.getTail(); }


	/**
	 * Get this road's direction
	 * @return this road's direction
	 */
	public Direction getDir()
	{ return this.dir; }


	/**
	 * Returns the distance to end from the first node
	 * @return distance to end from the first cell (size of the list)
	 */
	public int getDistToEnd()
	{
		if (this.list.isEmpty())
			return 0;

		RoadTile currentHead = (RoadTile) this.list.getHead();
		return currentHead.getDistToEnd();
	}





	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */

	/**
	 * Get a RoadIterator instance on this list
	 * @return RoadIterator instance
	 */
	@Override
	public Iterator<RoadTile> iterator() {
		return new RoadIterator(this.getHead());
	}


	/**
	 * Add a cell in first position in the list
	 * (therefore the road is set from end to start)
	 * @param cell the cell to add
	 * @throws IllegalArgumentException If given cell isn't neighbor to current head
	 */
	public void addCell(Cell cell) throws IllegalArgumentException
	{
		RoadTile ht = this.getHead();

		// Check if given cell is neighbor
		if(ht != null)
		{
			Cell h = ht.getCell();

			Vector2f nc = cell.getVectorCenter();
			Vector2f pc = h.getVectorCenter();

			if(Math.abs(nc.x() - pc.x()) > 1.01f ^ Math.abs(nc.y() - pc.y()) > 1.01f)
				throw new IllegalArgumentException(
						"Cell given is not neighbor of current head:\n" +
					   "Head: "+nc + " ; New cell: " + pc);
		}

		this.list.insertHead(new RoadTile(cell, this.getDistToEnd()+1, this));
	}


	/**
	 * Add a bloon to the start of this road.
	 * The bloon's coordinates are updated so it lies
	 * on the first tile.
	 *
	 * @param b Bloon to add
	 */
	public void addBloon(Bloon b)
	{
		this.getHead().getCell().addBloon(b);
	}

	/**
	 * Add a bloon to the list of exited bloons.
	 * @param b Bloon to add
	 */
	public void addBloonToExitedList(Bloon b)
	{ this.exitedBloons.add(b); }


	/**
	 * String representation of the road
	 * @return the road meta-infos as string
	 */
	public String toString()
	{
		return this.hashCode() + " " + this.getClass().getSimpleName();
	}
}
