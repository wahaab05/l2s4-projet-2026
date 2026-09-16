package game.waves;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import game.boards.Board;
import game.objects.bloons.Bloon;



/**
 * Represents a generated wave of bloons.
 *
 * A wave stores its number, the number of bloons of each type,
 * the total number of bloons, a global threat score, and whether
 * it is considered an elite wave.
 *
 * This class is immutable: once a wave is created, its content
 * cannot be modified from the outside. This makes the wave easier
 * to use safely in the game logic, in tests, and in future displays.
 */
public class Wave {

	private final int number;
	private final Map<Class<? extends Bloon>, Integer> bloonCounts;
	private final int totalBloons;
	private final int threatScore;
	private final boolean eliteWave;

	/**
	 * Creates a new wave description.
	 *
	 * The given map is copied and wrapped in an unmodifiable view so that
	 * the internal state of the wave remains stable after construction.
	 *
	 * @param number the number of the wave
	 * @param bloonCounts the number of bloons for each bloon type
	 * @param threatScore a global score representing the danger of this wave
	 * @param eliteWave {@code true} if this wave is an elite wave,
	 *        {@code false} otherwise
	 */
	public Wave(int number, Map<Class<? extends Bloon>, Integer> bloonCounts, int threatScore, boolean eliteWave)
	{
		this.number = number;
		this.bloonCounts = Collections.unmodifiableMap(new LinkedHashMap<>(bloonCounts));
		this.totalBloons = this.bloonCounts.values().stream().mapToInt(Integer::intValue).sum();
		this.threatScore = threatScore;
		this.eliteWave = eliteWave;
	}




	/**
	 * Returns the number of this wave.
	 *
	 * @return the wave number
	 */
	public int getNumber()
	{ return this.number; }





	/**
	 * Returns the bloon composition of this wave.
	 *
	 * The returned map associates each bloon class with the number
	 * of bloons of that type in the wave.
	 *
	 * @return an unmodifiable map describing the bloon composition
	 */
	public Map<Class<? extends Bloon>, Integer> getBloonCounts()
	{ return this.bloonCounts; }




	/**
	 * Returns the total number of bloons in this wave.
	 *
	 * @return the total number of bloons
	 */
	public int getTotalBloons()
	{ return this.totalBloons; }




	/**
	 * Returns the threat score of this wave.
	 *
	 * This score is used to give a global idea of the difficulty
	 * of the wave.
	 *
	 * @return the threat score of the wave
	 */
	public int getThreatScore()
	{ return this.threatScore; }




	/**
	 * Tells whether this wave is an elite wave.
	 *
	 * @return {@code true} if the wave is elite, {@code false} otherwise
	 */
	public boolean isEliteWave()
	{ return this.eliteWave; }

	/**
	 * Spawns all bloons of this wave on the given board.
	 *
	 * For each bloon type stored in the wave, this method adds
	 * the corresponding number of bloons to the board.
	 *
	 * @param board the board on which the bloons must be spawned
	 */
	public void spawnOn(Board board)
	{
		for(Map.Entry<Class<? extends Bloon>, Integer> e : this.bloonCounts.entrySet())
		{
			for(int i = 0; i < e.getValue(); i++)
				board.addBloon(e.getKey());
		}
	}




	/**
	 * Returns a readable textual description of the wave.
	 *
	 * The returned string contains the wave number, total bloon count,
	 * threat score, elite status, and bloon composition.
	 *
	 * @return a string representation of the wave
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Wave ").append(number)
			.append(" [total=").append(totalBloons)
			.append(", threat=").append(threatScore);
		if(eliteWave)
			sb.append(", elite");
		sb.append("] ");

		boolean first = true;
		for(Map.Entry<Class<? extends Bloon>, Integer> e : bloonCounts.entrySet())
		{
			if(!first)
				sb.append(", ");
			first = false;
			sb.append(e.getKey().getSimpleName()).append(" x").append(e.getValue());
		}
		return sb.toString();
	}
}













































