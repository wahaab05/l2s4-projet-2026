package game.shops;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.Game;
import game.boards.Board;
import game.cells.Cell;
import game.cells.TurretCell;
import game.objects.turrets.Turret;
import game.objects.turrets.templates.CanonMonkey;
import game.objects.turrets.templates.DartMonkey;
import game.objects.turrets.templates.FreezeMonkey;
import game.objects.turrets.templates.JunkyMonkey;
import game.objects.turrets.templates.NeedleMonkey;
import game.objects.turrets.templates.SlowMonkey;
import game.objects.turrets.templates.SniperMonkey;
import game.upgrade.UpgradeableAttribute;
import utils.datastructures.Tuple2;

/**
 * Economic layer used to buy, upgrade and sell turrets.
 *
 * This class is intentionally lightweight so it can be integrated into the
 * current project without forcing a redesign of the rest of the architecture.
 * It tracks the available credits and exposes helper methods to interact with a
 * {@link Board} using the turret classes already present in the project.
 *
 * It also allows to buy a beautiful fish:
 * 		  /`·.¸
 * 		 /¸...¸`:·
 * 	 ¸.·´  ¸   `·.¸.·´)
 * 	: © ):´;      ¸  {
 * 	`·.¸ `·  ¸.·´\`·¸)
 * 		`\\´´\¸.·´
 */
public class Shop {

	/** Default amount of credits available at the beginning of a game. */
	public static final int DEFAULT_INITIAL_CREDITS = 2500;

	private final List<Class<? extends Turret>> catalog;
	private int credits;

	/**
	 * Create a shop with the default initial amount of credits.
	 */
	public Shop()
	{
		this(DEFAULT_INITIAL_CREDITS);
	}

	/**
	 * Create a shop with a custom initial amount of credits.
	 *
	 * @param initialCredits number of credits initially available
	 */
	public Shop(int initialCredits)
	{
		if(initialCredits < 0)
			throw new IllegalArgumentException("Initial credits cannot be negative");

		this.credits = initialCredits;
		this.catalog = new ArrayList<>();
		this.catalog.add(DartMonkey.class);
		this.catalog.add(NeedleMonkey.class);
		this.catalog.add(SniperMonkey.class);
		this.catalog.add(CanonMonkey.class);
		this.catalog.add(JunkyMonkey.class);
		this.catalog.add(FreezeMonkey.class);
		this.catalog.add(SlowMonkey.class);
	}

	/**
	 * Get the current amount of credits.
	 *
	 * @return remaining credits
	 */
	public int getCredits()
	{ return this.credits; }

	/**
	 * Get the list of turret classes sold by this shop.
	 *
	 * @return immutable catalog of turret classes
	 */
	public List<Class<? extends Turret>> getCatalog()
	{ return Collections.unmodifiableList(this.catalog); }

	/**
	 * Add credits to the shop.
	 *
	 * @param amount amount of credits to add
	 */
	public void addCredits(int amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Cannot add a negative amount of credits");

		this.credits += amount;
	}

	/**
	 * Return the cost of a turret class by instantiating a temporary sample.
	 *
	 * @param turretClass turret class whose price is requested
	 * @return purchase cost of the turret
	 */
	public int getTurretCost(Class<? extends Turret> turretClass)
	{
		try {
			Constructor<? extends Turret> c = turretClass.getConstructor(TurretCell.class);
			Turret sample = c.newInstance(new TurretCell(0, 0));
			return sample.getInitialCost();
		} catch (ReflectiveOperationException e) {
			throw new IllegalArgumentException("Could not read turret cost for class " + turretClass, e);
		}
	}
	/*
	 * 	|\   \\\\__     o
	 * 	| \_/    o \    o
	 * 	> _   (( <_  oo
	 * 	| / \__+___/
	 * 	|/     |/
	 */


	/**
	 * Check whether the shop can currently afford a given turret.
	 *
	 * @param turretClass turret class to test
	 * @return true if the turret is sold and affordable
	 */
	public boolean canBuy(Class<? extends Turret> turretClass)
	{
		return this.catalog.contains(turretClass) && this.credits >= this.getTurretCost(turretClass);
	}

	/**
	 * Check whether the shop can currently afford a given upgrade.
	 *
	 * @param upgrade Upgrade to check.
	 * @return true if the upgrade is affordable
	 */
	public boolean canUpgrade(UpgradeableAttribute<?> upgrade)
	{
		return this.credits >= upgrade.getCost() && upgrade.canBeUpgraded();
	}

	/**
	 * Buy a turret and place it on the board.
	 *
	 * @param board board on which the turret must be placed
	 * @param turretClass type of turret to instantiate
	 * @param coords target cell coordinates
	 * @param <T> concrete turret type
	 * @return the newly created turret
	 */
	public <T extends Turret> T buyTurret(Board board, Class<T> turretClass, Tuple2<Integer, Integer> coords)
	{
		if(board == null)
			throw new IllegalArgumentException("Board cannot be null");
		if(coords == null)
			throw new IllegalArgumentException("Coordinates cannot be null");
		if(!this.catalog.contains(turretClass))
			throw new IllegalArgumentException("Turret class not sold by this shop: " + turretClass);

		int cost = this.getTurretCost(turretClass);
		if(this.credits < cost)
			throw new IllegalStateException("Not enough credits to buy " + turretClass.getSimpleName());

		this.credits -= cost;

		try {
    	return board.addTurret(turretClass, coords);
		} catch(RuntimeException e) {
			this.credits += cost;
			throw e;
		}
	}

	/**
	 * Buy and apply an upgrade on a turret.
	 *
	 * @param upgrade Upgrade to buy
	 * @return the price paid for the upgrade (or the price of this fish? 🐟)
	 */
	public int buyUpgrade(UpgradeableAttribute<?> upgrade)
	{
		if(!this.canUpgrade(upgrade))
			throw new IllegalStateException("Not enough credits to buy upgrade " + upgrade);

		int cost = upgrade.getCost();

		this.credits -= cost;
		upgrade.upgrade();

		return cost;
	}



	/**
	 * Sells a turret and gives its selling price back to the player.
	 *
	 * This method first checks that the turret is valid and really placed on a cell.
	 * It then removes the turret from that cell using {@code removeTurret}, which
	 * avoids using {@code instanceof} and keeps the code cleaner.
	 *
	 * The turret is also removed from the board's global list so that it completely
	 * disappears from the game logic. This prevents a sold turret from still being
	 * considered active by the engine.
	 *
	 * @param turret the turret to sell
	 * @return the number of credits refunded to the player
	 * @throws IllegalArgumentException if the turret is {@code null}, not placed
	 *         on a cell, or not found in its cell
	 */
	public int sellTurret(Turret turret)
	{
		if(turret == null)
			throw new IllegalArgumentException("Turret cannot be null");

		Cell cell = turret.getCell();
		if(cell == null)
			throw new IllegalArgumentException("Turret is not placed on any cell");

		boolean removed = cell.removeTurret(turret);
		if(!removed)
			throw new IllegalArgumentException("Turret not found in its cell");

		if(Game.instance() != null && Game.instance().getBoard() != null)
			Game.instance().getBoard().getTurrets().removeIf(t -> t == turret);

		int refund = turret.getSellingPrice();
		this.credits += refund;
		return refund;
	}

/* 

	/**
	 * Remove duplicated references of the same turret from a cell.
	 *
	 * The current board implementation adds a newly created turret both from
	 * the turret constructor and from {@code Board.addTurret(Class, Tuple2)}.
	 * This method keeps only a single reference in the cell to make the behavior
	 * easier to reason about
	 *
	 * @param cell cell to clean
	 * @param turret turret whose references must be deduplicated
	 */
	/*private void removeDuplicateTurretReferences(TurretCell cell, Turret turret)
	{
		boolean found = false;
		Iterator<Turret> it = cell.getTurrets().iterator();
		while(it.hasNext())
		{
			Turret current = it.next();
			if(current == turret)
			{
				if(found)
					it.remove();
				else
					found = true;
			}
		}
	}*/

	/**
	 * Sell an upgrade and get the resell value of that upgrade.
	 *
	 * @param upgrade Upgrade to sell.
	 * @return Selling price of upgrade.
	 *
	 * @throws IllegalArgumentException If the upgrade cannot be downgraded.
	 */
	public int sellUpgrade(UpgradeableAttribute<?> upgrade) throws IllegalArgumentException
	{
		if(!upgrade.canBeDowngraded())
			throw new IllegalArgumentException("Cannot downgrade UpgradeableAttribute instance "+upgrade);

		upgrade.downgrade();
		int refund = (int)(upgrade.getCost() * 0.8);
		this.credits += refund;
		return refund;
	}
}
