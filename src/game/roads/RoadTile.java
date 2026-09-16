package game.roads;

import game.cells.Cell;
import utils.datastructures.Node;
import utils.plane2d.Direction;
import utils.plane2d.PathType;

/**
 * Represents a node in a road linked list
 */
public class RoadTile extends Node<Cell> {

	private int distToEnd;
	// TODO INSPECT: road is not used in this class, it may be a residue of previous bad organization of classes (pre game-singleton)
	private Road road; 

	/** in/out directions */
	private Direction inputDirection;
	private Direction outputDirection;

	/**
	 * Class constructor
	 * @param cell cell to store in this node
	 * @param distToEnd how many cells are separating the referenced cell to the end of the road
	 * @param road the road storing this tile
	 */
	public RoadTile(Cell cell, int distToEnd, Road road)
	{
		super(cell, road != null ? road.list : null);
		this.distToEnd = distToEnd;
		this.road = road;

		this.inputDirection = null;
		this.outputDirection = null;
	}


	/* ---------------------------- *\
	 *								*
	 * 			GETTERS				*
	 * 								*
	 * 	          /"*._         _	*
	 * 		  .-*'`    `*-.._.-'/	*
	 * 		< * ))     ,       (	*
	 * 		  `*-._`._(__.--*"`.\	*
	\* ---------------------------- */

	/**
	 * The cell contained in this road tile
	 * @return The contained cell
	 */
	public Cell getCell()
	{ return this.value; }

	/**
	 * Get the distance of this road tile to the end of the path
	 * @return Distance of this roadtile to the end
	 */
	public int getDistToEnd()
	{ return this.distToEnd; }

	/**
	 * Check if this roadTile has a next neighbor or not
	 * @return True if it has a next neighbor
	 */
	public boolean hasNext()
	{ return this.next != null; }

	/**
	 * Check if this roadTile has a previous neighbor or not
	 * @return True if it has a previous neighbor
	 */
	public boolean hasPrevious()
	{ return this.prev != null; }

	/**
	 * Get the next road tile in the path
	 * @return the next node
	 */
	public RoadTile next()
	{ return (RoadTile) this.next; }

	/**
	 * Previous node accessor
	 * @return the previous node
	 */
	public RoadTile previous()
	{ return (RoadTile) this.prev; }

	/**
	 * Get this tile's road
	 * @return the road "hosting" this tile
	 */
	public Road getRoad()
	{ return this.road; }




	/* ---------------- *\
	 *					*
	 * 		METHODS		*
	 * 					*
	\* ---------------- */

	/**
	 * Get the direction in which bloons go when entering this cell.
	 * Vector between the previous tile's center and this tile's center.
	 * If there is no previous tile, return the road's direction.
	 * @return Input direction
	 */
	public Direction getInputDirection()
	{
		if (this.inputDirection == null)
		{
			RoadTile p = this.previous();

			Cell curr = this.getCell();
			Cell prev = null;

			if(p != null && (prev = p.getCell()) != null)
				this.inputDirection = Direction.fromCoords(
						curr.getX() - prev.getX(),
						curr.getY() - prev.getY()
				);
			else
				this.inputDirection = this.getRoad().getDir();
		}

		return this.inputDirection;
	}

	/**
	 * Get the direction in which bloons go when leaving this cell.
	 * Vector between this tile's center and the next tile's center.
	 * If there is no next tile, return the road's direction.
	 * @return Output direction
	 */
	public Direction getOutputDirection()
	{
		if (this.outputDirection == null)
		{
			RoadTile n = this.next();

			Cell curr = this.getCell();
			Cell next = null;

			if(n != null && (next = n.getCell()) != null)
				this.outputDirection = Direction.fromCoords(
						next.getX() - curr.getX(),
						next.getY() - curr.getY()
				);
			else
				this.outputDirection = this.getRoad().getDir();
		}
		return this.outputDirection;
	}

	/**
	 * Get the edge connected to this road's previous tile, from which bloons enter this tile.
	 * If there is no previous tile, return the opposite of the path's direction.
	 * @return Input edge
	 */
	public Direction getInputEdge()
	{ return this.getInputDirection().opposite(); }

	/**
	 * Get the edge connected to this road's next tile, from which bloons leave this tile.
	 * If there is no next tile, return the path's direction.
	 * @return Ouput edge
	 */
	public Direction getOutputEdge()
	{ return this.getOutputDirection(); }

	/**
	 * Get this tile's path type from the previous and next tiles.
	 * If there is no previous or next tile, the road's direction is used instead.
	 * @return The tile's path type
	 */
	public PathType getPathType()
	 {
		Direction d1 = this.getInputEdge();
		Direction d2 = this.getOutputEdge();

		return PathType.fromDirections(d1, d2);
	}

	/**
	 * Return true if given RoadTile is equal to this one
	 * Two RoadTiles are considered equals if their stored cell are equals
	 * @param o another RoadTile as an Object
	 * @return are the two RoadTiles equals
	 */
	public boolean equals( Object o  )
	{
		if (! (o instanceof RoadTile))
		{
			return false;
		}
		RoadTile r = (RoadTile) o;
		return this.getCell().equals(r.getCell());
	}
}
