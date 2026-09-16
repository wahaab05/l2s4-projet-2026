package game.objects.bloons.templates;

import game.objects.bloons.Bloon;
import game.roads.RoadTile;
import utils.config.ConfigReader;

/**
 * Bloon with a life of 4 and speed of 0.05 units/tick
 */
public class GreenBloon extends Bloon {

	private static final float SPEED = ConfigReader.getFloat("green_bloon_speed");
	private static final int INITIAL_LIFE = ConfigReader.getInt("green_bloon_initial_life");

	/**
	 * Create a new green bloon 🦈
	 * @param t Tile to place bloon on
	 */
	public GreenBloon(RoadTile t)
	{
		super(INITIAL_LIFE, SPEED, t);
	}
}
