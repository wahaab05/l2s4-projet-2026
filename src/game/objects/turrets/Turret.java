package game.objects.turrets;

import game.Game;

import game.cells.Cell;
import game.cells.TurretCell;

import game.objects.GameObject;

import game.objects.bloons.Bloon;

import game.objects.projectiles.Projectile;

import game.upgrade.FloatUpgradeableAttribute;
import game.upgrade.IntUpgradeableAttribute;
import game.upgrade.ProjectileUpgradeableAttribute;
import game.upgrade.UpgradeableAttribute;
import utils.MyLogger;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;
import utils.plane2d.Vector2f;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Abstract class Turret
 *
 * Base class for all types of turrets in the game.
 *
 * Each turret has a cost, attack range, fire rate, and a projectile it shoots
 *
 * Concrete turret types (like DartTurret, BombTurret, etc.) should extend this class
 * and define their own shooting logic in the shoot() method.
 */
public class Turret extends GameObject implements Targetable
{
	protected TurretCell cell;						// Cell where the turret is located
	protected int cost;								// cost to buy the turret
	protected long nbTicksSinceLastShot;			// ticks since the last shot fired
	private static final float SELLING_RATE = 0.8f; // Multiplier to the actual tower cost when selling a tower

	protected FloatUpgradeableAttribute range;
	protected ProjectileUpgradeableAttribute projectileType;// Type of projectile to spawn
	protected IntUpgradeableAttribute shootingCooldown;

	private List<Projectile> projectiles;			// List of currently launched projectiles

	/**
	 * Constructor to create a turret
	 *
	 * @param c TurretCell instance to place this turret on
	 * @param cost Cost to buy the turret
	 * @param range Attack range, in Cell units
	 * @param shootingCooldownTicks Time between shots in ticks
	 * @param projectileClass Initial projectile type to use
	 */

	public Turret(TurretCell c, int cost, float range, int shootingCooldownTicks, Class<? extends Projectile> projectileClass)
	{
		super(c.getVectorCenter());
		this.cell = c;
		this.cost = cost;
		this.nbTicksSinceLastShot = shootingCooldownTicks+1; // Make turret able to shoot right away
		this.projectiles = new ArrayList<>();


		this.range = new FloatUpgradeableAttribute(
				range,
				1.25f,
				ConfigReader.getInt("turret_range_initial_upgrade_cost"),
				1.5f,
				4
		);

		this.projectileType = new ProjectileUpgradeableAttribute(
				projectileClass,
				ConfigReader.getInt("turret_projectile_initial_upgrade_cost"),
				1.25f
		);

		this.shootingCooldown = new IntUpgradeableAttribute(
				shootingCooldownTicks,
				0.8f,
				ConfigReader.getInt("turret_shooting_cooldown_initial_upgrade_cost"),
				1.25f,
				4
		);

		//c.addTurret(this);// We will add the turret to the cell after creating it, in the Board.addTurret() method, to avoid issues with the order of initialization of the turret and the cell.
	}


	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */

	/*
	 * Get the shooting cooldown of the turret, the minimum time between each shot (in ticks)
	 * @return shooting cooldown
	 */
	public int getShootingCooldown()
	{ return this.shootingCooldown.getValue(); }


	/**
	 * Get the upgradeable version of this shooting cooldown.
	 * @return UpgradeableAttribute of this shooting cooldown.
	 */
	public IntUpgradeableAttribute getUpgradeableShootingCooldown()
	{ return this.shootingCooldown; }


	/**
	 * Get the attack range of the turret
	 * @return attack range
	 */
	public float getRange()
	{ return this.range.getValue(); }

	/**
	 * Get the FloatUpgradeableAttribute attack range of this turret.
	 * @return Instance of UpgradeableAttribute.
	 */
	public FloatUpgradeableAttribute getUpgradeableRange()
	{ return this.range; }


	/**
	 * Get the cost of the turret, including price of applied upgrades.
	 * @return cost
	 */
	public int getCost()
	{
		int total = 0;
		total += cost;

		for(Tuple2<String, UpgradeableAttribute<?>> up : this.getAllUpgradeableAttributes())
			total += up.second().getTotalCost();

		return total;
	}

	/**
	 * Get the initial cost for buying this turret.
	 *
	 * @return the initial cost for this turret.
	 */
	public int getInitialCost()
	{ return this.cost; }

	/**
	 * Get the selling price of this tower, accounting for initial cost and upgrades.
	 * @return selling price
	 */
	public int getSellingPrice()
	{ return (int)(this.getCost() * Turret.SELLING_RATE); }

	/**
	 * Get the projectile class currently used by this turret.
	 * @return projectile class
	 */
	public Class<? extends Projectile> getProjectileClass()
	{ return this.projectileType.getValue(); }


	/**
	 * Get the upgradeable projectile class used by this turret.
	 * @return Upgradeable projectile class.
	 */
	public ProjectileUpgradeableAttribute getUpgradeableProjectile()
	{ return this.projectileType; }


	/**
	 * Get this tower's cell
	 * @return This tower's cell
	 */
	public Cell getCell()
	{
		return this.cell;
	}


	/**
	 * Get the currently active projectiles this turret has launched
	 * @return List of projectiles
	 */
	public List<Projectile> getLaunchedProjectiles()
	{
		return this.projectiles;
	}



	/**
	 * Get direction the projectile have to follow to reach this entity
	 * @see game.objects.turrets.Targetable#directionFrom
	 * @param p a projectile who wants to reach this entity
	 * @return direction to follow as a Vector2f
	 */
	public Vector2f directionFrom(Projectile p)
	{
		/** Direction = destination - currentPos
		 In this case 'this' (a turret) is the destination */
		return this.coords.diff(p.getCoords());
	}


	/**
	 * Get the list of tuples of all UpgradeableAttribute of this turret, coupled with their name
	 * as first value of the tuple.
	 * @return List of (Name, UpgradeableAttribute) tuples.
	 */
	public List<Tuple2<String, UpgradeableAttribute<?>>> getAllUpgradeableAttributes()
	{
		return List.of(
				new Tuple2<String, UpgradeableAttribute<?>>("RANGE", 			this.range),
				new Tuple2<String, UpgradeableAttribute<?>>("PROJECTILE_TYPE", 	this.projectileType),
				new Tuple2<String, UpgradeableAttribute<?>>("SHOOTING_COOLDOWN",	this.shootingCooldown)
		);
	}


	/**
	 * Get a list of tuples of upgradeable attributes which can be upgraded at least once more, coupled with their name.
	 *
	 * @return List of tuples (Name, UpgradeableAttribute)
	 */
	public List<Tuple2<String, UpgradeableAttribute<?>>> getAllUpgradeableAttributesWhichCanBeUpgradedAtLeastOnceMore()
	{
		List<Tuple2<String, UpgradeableAttribute<?>>> possibleUpAttrs = new ArrayList<>();

		for(Tuple2<String, UpgradeableAttribute<?>> upAttr : this.getAllUpgradeableAttributes())
			if(upAttr.second().canBeUpgraded())
				possibleUpAttrs.add(upAttr);

		return possibleUpAttrs;
	}

	/**
	 * Get a list of tuples of upgradeable attributes which can be downgraded at least once more, coupled with their name.
	 *
	 * @return List of tuples (Name, UpgradeableAttribute)
	 */
	public List<Tuple2<String, UpgradeableAttribute<?>>> getAllUpgradeableAttributesWhichCanBeDowngradedAtLeastOnceMore()
	{
		List<Tuple2<String, UpgradeableAttribute<?>>> possibleUpAttrs = new ArrayList<>();

		for(Tuple2<String, UpgradeableAttribute<?>> upAttr : this.getAllUpgradeableAttributes())
			if(upAttr.second().canBeDowngraded())
				possibleUpAttrs.add(upAttr);

		return possibleUpAttrs;
	}


	/* ---------------- *\
	 *					*
	 *		SETTERS		*
	 *					*
	\* ---------------- */


	/**
	 * Place this tower in the center of a given cell
	 * @param c Cell to place tower on
	 */
	public void setCell(TurretCell c)
	{
		this.cell = c;
		this.coords = c.getVectorCenter();
	}




	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */


	/**
	 * Check if this turret can shoot
	 * @return True if it can shoot
	 */
	public boolean canShoot()
	{
		return this.nbTicksSinceLastShot >= this.shootingCooldown.getValue();
	}

	/**
	 * Return true if this turret has at least one upgradeable attribute that can be upgraded right now
	 * @return does this turret have at least one upgrade available
	 */
	public boolean canBeUpgraded()
	{
		return this.getAllUpgradeableAttributesWhichCanBeUpgradedAtLeastOnceMore().size() > 0;
	}

	/**
	 * Return true if this turret has at least one upgradeable attribute that can be downgraded right now
	 * @return does this turret have at least one upgrade available for downgrade
	 */
	public boolean canBeDowngraded()
	{
		return this.getAllUpgradeableAttributesWhichCanBeDowngradedAtLeastOnceMore().size() > 0;
	}

	/**
	 * Make this turret play its turn.
	 * Makes this turret's projectiles play for one tick.
	 */
	public void tick()
	{
		Logger logger = MyLogger.getInstance();
		logger.finer("Ticking turret " + this.toString());
		if(this.canShoot())
			this.shoot();

		Iterator<Projectile> pIt = this.projectiles.iterator();

		logger.finer("About to start ticking projectiles of current turret");
		Projectile p;
		while(pIt.hasNext())
		{
			p = pIt.next();
			p.tick();

			if(p.isDead())
			{
				logger.finer("Projectile dead");
				pIt.remove();
			}
		}

		this.nbTicksSinceLastShot++;
	}



	public List<Targetable> chooseTarget()
	{
		List<Targetable> targets = new ArrayList<Targetable>();


		List<Bloon> bloons = Game.instance().getBoard().getBloonsInRange(
				this.range.getValue(),
				this.cell.getVectorCenter().x(),
				this.cell.getVectorCenter().y()
		);

		if(!bloons.isEmpty())
			targets.add(bloons.get(0));

		return targets;
	}


	/**
	 * Spawn the needed projectiles
	 *	a list of all newly spawned projectiles
	 */
	public List<Projectile> shoot()
	{
		Logger logger = MyLogger.getInstance();

		List<Targetable> targets = this.chooseTarget();
		List<Projectile> recentlyAddedProjectiles = new ArrayList<Projectile>();

		// Exit early if no target
		if(targets.isEmpty())
			return List.of();

		logger.fine("Turret "+this+" shoots");
		this.nbTicksSinceLastShot = 0;

		for(Targetable target : targets)
		{
			Projectile p = Projectile.create(this.projectileType.getValue(), this.coords, target);
			recentlyAddedProjectiles.add(p);
		}
		this.projectiles.addAll(recentlyAddedProjectiles);

		return recentlyAddedProjectiles;
	}



	@Override
	public boolean isInHitRangeOf(Projectile p) {
		return this.coords.rangeCompare(p.getCoords(), Projectile.HIT_RANGE) <= 0;
	}


	@Override
	public boolean isCollidingWith(Projectile p) {
		/*
		 * We consider that a projectile is looking to
		 * apply its effect when it is colliding with its
		 * target. If the projectile is in the range of
		 * this turret, it applies effect and dispawns.
		*/
		return this.isInHitRangeOf(p);
	}

	/**
	 * String representation of the turret
	 * @return the turret meta-infos as string
	 */
	public String toString()
	{
		return
			"[ " +
				//this.hashCode() + " " +// Unique id
				this.getClass().getSimpleName() +
				"(" +
					"shootingCooldown: " + this.shootingCooldown.getValue() + ", " +
					"nbTicksSinceLastShot:" + this.nbTicksSinceLastShot + ", " +
					"range: " + this.range.getValue() +
				")" +
			" ]";
	}
}

