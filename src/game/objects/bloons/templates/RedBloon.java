package game.objects.bloons.templates;

import game.objects.bloons.Bloon;
import game.roads.RoadTile;
import utils.config.ConfigReader;

/**
 * Weakest bloon type with a life of 1 and speed of 0.02 units/tick
 */
public class RedBloon extends Bloon {

	private static final float SPEED = ConfigReader.getFloat("red_bloon_speed");
	private static final int INITIAL_LIFE = ConfigReader.getInt("red_bloon_initial_life");
	@SuppressWarnings("unused")
	private static final String fishString = "🐠";

	/**
	 * Create a new red bloon
	 * @param t Tile to place bloon on
	 */
	public RedBloon(RoadTile t)
	{
		super(INITIAL_LIFE, SPEED, t);
	}
}
