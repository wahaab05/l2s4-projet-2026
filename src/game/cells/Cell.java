package game.cells;

import java.util.ArrayList;
import java.util.List;

import game.objects.GameObject;
import game.objects.bloons.Bloon;
import game.objects.turrets.Turret;
import utils.io.Color;
import utils.io.ConsoleUtils;
import utils.plane2d.Vector2f;


/**
* Abstract class representing a cell on the game board.
* A cell has coordinates (x, y) and may contain bloons.
*/
public abstract class Cell {

	/** list of all bloons present on this cell */
	protected List<Bloon> bloons;
	/** x coord of the cell */
	protected int x;
	/** y coord of the cell */
	protected int y;
	/** cli display color */
	protected Color col;

	/**
	 * Constructs a Cell with given coordinates
	 *
	 * @param x the x-coordinate of the cell
	 * @param y the y-coordinate of the cell
	 */
	public Cell (int x, int y)
	{
		this.x = x;
		this.y = y;
		 this.bloons = new ArrayList<>();
	}



	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */

	/**
	 * Get the x coordinate of this cell
	 * @return the x coordinate
	 */
	public int getX()
	{ return this.x; }

	/**
	 * Get the y coordinate of this cell
	 * @return the y coordinate
	 */
	public int getY()
	{ return this.y; }

	/**
	 * Get the cell's coordinates as a Vector2f
	 * @return cell's coordinates as a floating vector
	 */
	public Vector2f getCoords()
	{ return new Vector2f(x, y); }

	/**
	 * Returns the list of bloons present in this cell
	 * @return the list of bloons
	*/
	public List<Bloon> getBloons()
	{ return this.bloons; }

	/**
	 * Get the color of this cell for display
	 * @return The cell's color
	 */
	public Color getColor()
	{ return this.col; }

	/**
	 * Get this cell's center coordinates as a Vector2f
	 * @return vector2f representing this cell's center
	 */
	public Vector2f getVectorCenter()
	{
		return new Vector2f((float)(
				(2.0*this.x+1)/2.0
			), (float)(
				(2.0*this.y+1)/2.0)
			);
	}

	/**
	 * Get this cell's output border coords as a Vector2f
	 * @param dir the output direction
	 * @return the output border coordinates (only relevant coord)
	 */
	public Vector2f getExitBorderCoords(Vector2f dir)
	{
		if (dir.compSum() == -1) { // moves in positive direction (LEFT/UP)
			return dir.mult(-1)/* back to positive */.mult(new Vector2f(this.x, this.y));
		} else {
			return dir.mult(new Vector2f(this.x+1, this.y+1));
		}
	}



	/* ---------------- *\
	 *					*
	 *	  	SETTERS		*
	 *					*
	\* ---------------- */


	/**
	 * Add a bloon to bloons set
	 * @param b bloon to add
	 */
	public void addBloon(Bloon b) {
		this.bloons.add(b);
	}



	/* ------------------------ *\
	 *							*
	 *			METHODS			*
	 * 		(for fishing 🎣)	*
	 * .            ,			*
	 * 			 .:/			*
	 * 	.      ,,///;,   ,;/	*
	 * 	 .   o:::::::;;///		*
	 * 		>::::::::;;\\\		*
	 * 		  ''\\\\\'" ';\		*
	 * 			 ';\			*
	 *							*
	\* ------------------------ */


	/**
	 * Removes a bloon the bloons set
	 * @param b bloon to remove
	 */
	public void removeBloon(Bloon b)
	{ this.bloons.remove(b); }



	/**
	 * Tries to remove the given turret from this cell.
	 *
	 * In the base Cell class, a cell is not supposed to store turrets.
	 * For that reason, this default implementation does not remove anything
	 * and simply returns {@code false}.
	 *
	 * This method is mainly here to let specialized cells redefine the behavior.
	 * For example, a {@code TurretCell} can override this method and actually
	 * search for the turret in its internal collection, remove it, and return
	 * {@code true} if the removal was successful.
	 *
	 * @param turret the turret that should be removed from this cell
	 * @return {@code true} if the turret was found and removed from the cell;
	 *         {@code false} if this cell does not manage turrets or if the
	 *         turret was not present
	 */
	public boolean removeTurret(Turret turret)
	{
		return false; /* :-) */
	}




	/**
	 * Remove a fish from the cell
	 */
	public void removeFish()
	{
		System.out.println("Don't you dare remove this cute fish: 🐠!!"); /*personne n'a encore eu le cœur de le faire :-) */
	}




	/**
	 * Return true if there are bloons on this cell
	 * @return are there bloons on this cell
	 */
	public boolean hasBloons()
	{ return (!this.getBloons().isEmpty()); }




	/**
	 * Ask this cell if a certain game object type can be placed on it
	 * @param GC Class to check
	 * @return true if the object type can be placed, false otherwise
	 */
	public boolean canPlace(Class<? extends GameObject> GC)
	{
		return Bloon.class.isAssignableFrom(GC);
	}





	/**
	* Convenience overload for tests / callers that provide an instance instead of a Class.
	* This method simply delegates to {@link #canPlace(Class)} using the runtime class of {@code go}.
	*
	* @param go a GameObject instance (may be null)
	* @return true if an object of this instance's type can be placed on this cell, false otherwise
	*/
	public boolean canPlace(GameObject go)
	{
		// If the caller passes an instance, reuse the existing "Class-based" placement rule.
		return (go != null) && canPlace(go.getClass());
	}



	/**
	 * Compares this cell with another object
	 * Two cells are equal if they have the same coordinates.
	 *
	 * @param o the object to compare with
	 * @return true if the objects are equal, false otherwise
	 */
	public boolean equals(Object o)
	{
		if (! (o instanceof Cell))
		{
			return false;
		}
		Cell other = (Cell)o;
		return this.x == other.x && this.y == other.y;
	}




	/**
	 * Return the string representation of this cell's coordinates
	 * @return string representation of cell's coords
	 */
	public String coordsToString()
	{ return new Vector2f(x, y).toString(); }




	/**
	 * Get the string representation of this cell
	 * @return The string representation
	 */
	public String toString()
	{
		return "C";
	}




	/**
	 * Get the string representation of this cell, fitted with ansi code for displaying color in the terminal.
	 * @return The string representation
	 */
	public String toColoredString()
	{
		return ConsoleUtils.colorString(this.toString(), this.col);
	}





	/**
	 * Generates a cell of the given class
	 * @param cellClass an instanciable subclass of Cell
	 * @param <T> type of cell to use
	 * @param x x coord of the new cell @see game.cells
	 * @param y y coord of the new cell @see game.cells
	 * @return a cell object of the given class
	 *
	 * @throws IllegalArgumentException If there is no such constructor {@code public cellClass(int x, int y)}.
	 */
	public static <T extends Cell> T createCell(Class<T> cellClass, int x, int y)
		throws IllegalArgumentException
	{
		try
		{
			return cellClass.getDeclaredConstructor(int.class, int.class).newInstance(x, y);
		}
		catch (ReflectiveOperationException e)
		{
			throw new IllegalArgumentException("Could not create new instance of Cell: " + e);
		}
	}
}
