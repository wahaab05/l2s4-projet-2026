package game.cells;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Iterator;

import game.objects.GameObject;
import game.objects.turrets.Turret;
import utils.io.Color;




/**
 * Represents a cell where turrets can be placed
 * Turret cells cannot be crossed by bloons and may contain one or more turrets(in DefaultBoard)
 * Turret cells must contain strictly less than one(1) fish 𓆞
 */
public class TurretCell extends Cell  {

	/**
	 * List of turrets placed on this cell
	 *        ,-,
	 * 		 ('_)<
	 * 		  `-`
	 */
	private List<Turret> turrets;

	/**
	 * Constructs a TurretCell with given coordinates
	 * @param x Horizontal coordinate
	 * @param y Vertical coordinate
	 */
	public TurretCell(int x, int y)
	{
		super(x, y);
		this.col = Color.cyan;
		this.turrets = new ArrayList<>();
	}


	/**
	 * Get the list of turrets placed on this cell
	 * @return the list of turrets
	 */
	public List<Turret> getTurrets()
	{ return turrets; }

	/**
	 * Add a turret to this cell
	 * A fish may not be passed as argument, as it is not needed to add a turret
	 * @param t the turret to add
	 */
	public void addTurret(Turret t)
	{
		if (t != null)
			turrets.add(t);
	}

	/**
	 * @see game.cells.Cell#canPlace(java.lang.Class)
	 */
	@Override
	public boolean canPlace(Class<? extends GameObject> GC) {
		return Turret.class.isAssignableFrom(GC) || super.canPlace(GC);
	}


	/**
	 * Tries to remove the given turret from this turret cell.
	 *
	 * This method looks for the exact turret instance stored in the cell.
	 * If the turret is found, it is removed from the internal list and
	 * the method returns {@code true}.
	 *
	 * If the given turret is {@code null}, or if it is not present in
	 * this cell, nothing is removed and the method returns {@code false}.
	 *
	 * @param turret the turret to remove from this cell
	 * @return {@code true} if the turret was found and successfully removed,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean removeTurret(Turret turret)
	{
		if(turret == null)
			return false;

		Iterator<Turret> it = this.turrets.iterator();
		while(it.hasNext())
		{
			if(it.next() == turret)
			{
				it.remove();
				return true;
			}
		}

		return false;
	}

/* 	@Override
	public String toString()
	{
		return "T";
	}*/
	@Override
	public String toString()
	{
			return (turrets != null && !turrets.isEmpty()) ? "U" : "T";
	}
}
