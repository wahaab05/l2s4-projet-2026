package game.boards;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import game.cells.Cell;
import game.cells.TurretCell;
import game.objects.bloons.Bloon;
import game.objects.turrets.Turret;
import game.roads.Road;
import game.roads.RoadTile;
import utils.datastructures.Tuple2;
import utils.maths.RandomUtils;
import utils.plane2d.Vector2f;
import utils.plane2d.coordsiterator.CoordsIterator;



/**
 * Game board containing roads and turrets
 */
public abstract class Board {
	/** 2d list of all cells of this board */
	protected Cell[][] cells;
	/** list of all roads present on this board */
	protected List<Road> roads;
	/** list of all turrets placed on this board */
	protected List<Turret> turrets;

	/**
	 * Create a new Board of a given size
	 * ! Warning: no fish will be added to the board
	 * @param x Horizontal size of board
	 * @param y Vertical size of board
	 */
	public Board(int x, int y)
	{
		if (x <= 1 || y <= 1)
			throw new IllegalArgumentException("Please choose board size above 1x1");

		this.cells = new Cell[y][x];
		this.roads = new ArrayList<>();
		this.turrets = new ArrayList<>();
	}




	/* ----------------	*\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */


	/**
	 * Board width accessor
	 * @return the board width (in number of cells)
	 */
	public int getWidth()
	{ return this.cells[0].length; }

	/**
	 * Board height accessor
	 * @return the board height (in number of cells)
	 */
	public int getHeight()
	{ return this.cells.length; }

	/**
	 * Board roads accessor
	 * @return the board roads
	 */
	public List<Road> getRoads()
	{ return this.roads; }

	/**
	 * Board cell accessor
	 * @param x x coordinate of the cell to return
	 * @param y y coordinate of the cell to return
	 * @return the corresponding cell
	 */
	public Cell getCell(int x, int y)
	{
		if (
			x < 0 || x >= this.getWidth() ||
			y < 0 || y >= this.getHeight()
		) {
			throw new IllegalArgumentException(
					"Arguments x:" + x + " y:" + y +
					" out of board's bounds: " +
					this.getWidth() + "x" + this.getHeight()
			);
		}
		// 🐟
		return cells[y][x];
	}




	/**
	 * @param range the matrix size in witch it should be calculated
	 * @param x coordinates of the matrix's center
	 * @param y coordinates of the matrix's center
	 * @return list of bloons in range sorted in decsending order(meaning the from the closest to exit bloon to the furthest from exit bloon)
	 */
	public List<Bloon> getBloonsInRange(float range, float x, float y)
	{
		Bloon temp;
		Vector2f vCircleCenter = new Vector2f(x, y);
		List<Bloon> bloonsInRange = new ArrayList<>();

		int x_debut = Math.max(0, (int)(x-range));
		float x_fin = Math.min(this.getWidth()-1, x+range);

		int y_debut_init =  Math.max(0, (int)(y-range));
		float y_fin = Math.min(this.getHeight()-1, (y+range));

		/*
		 * it checks for the boundaries cells without
		 * doing unnecessary itterations to calculate
		 * all the cells of the box
		 */
		/*
		 * I initialize the value at -1 to verify later
		 * if it has been reset to an actual value; as the
		 * distance can't be negative
		 */
		float minDistance=-1;

		for(; x_debut <= x_fin; x_debut++)
		{
			for(int y_debut = y_debut_init; y_debut <= y_fin; y_debut++)
			{
				Cell cell = this.getCell(x_debut, y_debut);

				for(Bloon b : cell.getBloons())
				{
					// here I check if the bloon is in the circle to the precision
					if(vCircleCenter.rangeCompare(b.getCoords(), range + Bloon.HIT_BOX_RADIUS) <= 0)
					{
						float currentDistance = b.getDistanceToTheEnd();

						if ((minDistance == -1) || (minDistance>currentDistance))
						{
							/*
							 * TODO : for Marko, to optimize algo by removing the
							 * first element of a list and puting on its stead
							 * the min element and the element I removed put in the end
							 */
							if(!bloonsInRange.isEmpty())
							{ 
								temp = bloonsInRange.get(0);
								bloonsInRange.add(temp);

							}
							else
							{
								bloonsInRange.add(b);
							}
							minDistance = currentDistance;
						}

						else
							bloonsInRange.add(b);
					}
				}
			}
		}


		return bloonsInRange;
	}




	/**
	 * Get the turrets this board has.
	 * @return List of turrets
	 */
	public List<Turret> getTurrets()
	{ return this.turrets; }



	/* ----------------	*\
	 *					*
	 *		SETTERS		*
	 *					*
	\* ---------------- */


	/**
	 * Board cell setter
	 * Only add a cell if the location is available (location is null)
	 * @param x x coordinate of the cell to set
	 * @param y y coordinate of the cell to set
	 * @param cell the cell to set
	 */
	public void setCell(int x, int y, Cell cell)
	{
		if (this.getCell(x, y) == null)
		{
			this.cells[y][x] = cell;
		}
	}


	/**
	 * Create and add a bloon to this board on a random road
	 * @param bloon Bloon to add
	 * @return The new bloon created
	 */
	public Bloon addBloon(Class<? extends Bloon> bloon)
	{
		int max = this.roads.size();
		int i = RandomUtils.randInt(0, max);

		Road r = this.roads.get(i);
		RoadTile rt = r.getHead();
		Bloon b = null;

		try {
			Constructor<? extends Bloon> BC = bloon.getConstructor(RoadTile.class);
			b = BC.newInstance(rt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
				"Could not create a new Bloon instance from " + bloon + ": " + e
			);
		}

		return b;
	}

	


	/**
	 * Create a new Turret of a given class and place it on a cell.
	 * @param TClass Turret class to instantiate
	 * @param coords Position to place turret on
	 * @param <T> Type of turret to instantiate
	 * @return Newly created turret
	 * @throws IllegalArgumentException If coordinates are invalid, or if cell cannot accept turret, or if Turret class cannot be instantiated.
	 *
	 * {@code TClass} must have a public constructor which takes a single {@link TurretCell} instance as argument.
	 */
	public <T extends Turret> T addTurret(Class<T> TClass, Tuple2<Integer, Integer> coords) throws IllegalArgumentException
	{
		int x = coords.first();
		int y = coords.second();

		if(		x < 0 || x >= this.getWidth() ||
				y < 0 || y >= this.getHeight() )
			throw new IllegalArgumentException( String.format("Coordinates (%d, %d) invalid for board of size (%d, %d)", x, y) );

		Cell c = this.getCell(x, y);
		if(!c.canPlace(TClass))
			throw new IllegalArgumentException(String.format("Cannot place Turrent on Cell (%d, %d) of type %s", x, y, TClass));

		TurretCell tcell = (TurretCell)c;

		T t = null;

		try {
			Constructor<T> tconstr = TClass.getConstructor(TurretCell.class);
			t = tconstr.newInstance(tcell);
		} catch(Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not instantiate Turret of class "+TClass+" : "+e);
		}

		tcell.addTurret(t);
		this.turrets.add(t);

		return t;
	}


	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */

	/**
	 * Generates the path for the board
	 * @param ci CoordsIterator to use to find the next road tile to construct starting from the end
	 * @param cellClass the instanciable class of cell of with the path will be composed
	 * @return road that has been constructed
	 *
	 * The cell is instanciated using {@code cellClass(int x, int y)}, so there must exist a such constructor.
	 */
	protected Road generatePath(CoordsIterator ci, Class<? extends Cell> cellClass)
	{
		Road r = new Road(ci.getDirection());

		do {
			int x = ci.getX();
			int y = ci.getY();

			Cell c = this.getCell(x, y);

			// Check if cell already exists in board
			if(c == null)
			{
				c = Cell.createCell(cellClass, x, y);
				this.setCell(x, y, c);
			}

			r.addCell(c);

			ci.previous();

		} while (ci.hasPrevious());

		int x = ci.getX();
		int y = ci.getY();
		Cell c = this.getCell(x, y);
		if(c == null)
		{
			c = Cell.createCell(cellClass, x, y);
			this.setCell(x, y, c);
		}

		r.addCell(c);

		return r;
	}


	/**
	 * Fills the gap in the board generation with turret cells
	 */
	protected void fillBoard()
	{
		for(int j = 0; j < this.getHeight(); j++)
		{
			for(int i = 0; i < this.getWidth(); i++)
			{
				if(getCell(i, j) == null)
				{
					Cell c = new TurretCell(i, j);
					setCell(i, j, c);
				}
			}
		}
	}


	/**
	 * Check if this board still has bloons left in it.
	 * @return True is there are bloons left.
	 */
	public boolean hasBloonsLeft()
	{
		for(Road r : this.roads)
			if(!r.getBloons().isEmpty())
				return true;

		return false;
	}


}
