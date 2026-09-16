package game.roads;

import java.util.Iterator;

/**
 * Iterator class for Roads
 */
public class RoadIterator implements Iterator<RoadTile> {

	private RoadTile current;

	/**
	 * Create a new RoadIterator instance.
	 * The provided {@link RoadTile} instance is returned at the first call of {@link next()}
	 *  __v_
	 * (____\/{
	 * @param start Start road tile to iterate on
	 */
	public RoadIterator(RoadTile start)
	{
		this.current = new RoadTile(null, 0, start != null ? start.getRoad() : null);
		this.current.setNext(start);
	}

	/**
	 * Check if the iterator has a next road tile
	 * @return True if it has a next tile
	 */
	@Override
	public boolean hasNext()
	{ return current.hasNext(); }

	/**
	 * Check if the iterator has a previous road tile
	 * @return True if is has a previous tile
	 */
	public boolean hasPrevious()
	{ return current.hasPrevious(); }

	/**
	 * Step the iterator forward in the list
	 * @return The road tile the iterator is currently positionned on
	 */
	@Override
	public RoadTile next()
	{
		this.current = current.next();
		return this.current;
	}

	/**
	 * Step the iterator backwards in the list
	 * @return The road tile the iterator is currently positionned on
	 */
	public RoadTile previous()
	{
		this.current = current.previous();
		return this.current;
	}

}
