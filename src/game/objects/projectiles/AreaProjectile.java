package game.objects.projectiles;


import game.Game;
import game.objects.bloons.Bloon;
import game.objects.turrets.Targetable;

import utils.plane2d.Vector2f;

import java.util.List;

/**
 * Represents a projectile applying area damage/effects to multiple bloons in range when fireing.
 */
public abstract class AreaProjectile extends Projectile
{
	/** How far from this projectile can a bloon be affected by the effect */
	protected float range;

	/**
	 * Class constructor
	 * @param name name of the projectile (interface use only)
	 * @param coords initial coords of the projectile
	 * @param target entity this projectile will move toward
	 * @param speed speed/tick of the projectile
	 * @param range how far can this projectile affect a bloon when fireing
	 */
	public AreaProjectile(String name, Vector2f coords, Targetable target, float speed, float range, int damage) {
		super(name, coords, target, speed, damage);
		this.range = range;
	}
	public AreaProjectile(String name, Vector2f coords, Targetable target, float speed, float range) {
		this(name, coords, target, speed, range, 0);
	}

	/**
	 * Get this projectile's range
	 * @return projectile range
	 */
	public float getRange() {
	return this.range;
	}


	/**
	 * sets bloons in range of this projectile
	 * @param bloonsInRange bloons in range to be set
	 */
	protected void setBloonsInRange(List<Bloon> bloonsInRange)
	{
		this.bloonsInRange = bloonsInRange;
	}

	@Override
	protected List<Bloon> computeBloonsInRange()
	{
		return Game.instance().getBoard().getBloonsInRange(this.getRange(), this.getCoords().x(), this.getCoords().y());
	}

	/**
	 * Generates an area projectile
	 * @param target target of the new projectile
	 * @return an area projectile
	 *
	 * @throws IllegalArgumentException If there is no such constructor
	 */
	// TODO refactor to take account of new constructor (when rewritten)
	// TODO INSPECT: not needed anymore ? (Projectile.create()) 🪼
	public static AreaProjectile create(Targetable target)
		throws IllegalArgumentException
	{
		try
		{
			return AreaProjectile.class.getDeclaredConstructor(Targetable.class).newInstance(target);
		}
		catch (ReflectiveOperationException e)
		{
			throw new IllegalArgumentException("Could not create new instance of AreaProjectile: " + e);
		}
	}

}
