package game.waves;

import java.util.LinkedHashMap;
import java.util.Map;

import game.boards.Board;
import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;

/**
 * Generates the waves used during the game.
 *
 * The main idea of this class is to keep all the wave-balancing logic
 * in one place instead of scattering it across the project.
 *
 * In practice, this class decides:
 * - how many bloons appear in a wave,
 * - which bloon types are used,
 * - how the difficulty increases over time.
 *
 * This makes the code easier to understand, easier to test,
 * and much easier to modify later if we want to rebalance the game.
 *
 * We also keep the generation deterministic for a given wave number.
 * This is useful during development because the same wave number
 * always produces the same kind of result, which helps for debugging
 * and for explaining the game behavior.
 */
public class WaveGenerator {

	/**
	 * Generates the description of one wave.
	 *
	 * We do not directly hardcode every wave by hand.
	 * Instead, we compute the wave from a few simple rules:
	 * - the wave number,
	 * - the board structure,
	 * - the progression of bloon types over time.
	 *
	 * The idea is to start with easier waves, mostly using weak bloons,
	 * then progressively introduce stronger bloons as the game advances.
	 * Every 5th wave is also treated as a slightly harder "elite" wave
	 * to create a more visible progression in difficulty.
	 *
	 * @param waveNumber the number of the wave to generate, starting from 1
	 * @param board the board on which the wave will later be spawned
	 * @return a {@code Wave} object describing the generated wave
	 * @throws IllegalArgumentException if the wave number is invalid
	 *         or if the board is {@code null}
	 */
	public Wave generate(int waveNumber, Board board)
	{
		if(waveNumber <= 0)
			throw new IllegalArgumentException("Wave number must be >= 1");
		if(board == null)
			throw new IllegalArgumentException("Board cannot be null");

		// Every 5th wave is considered stronger than a normal wave. :-))
		boolean eliteWave = (waveNumber % 5 == 0);

		// First, compute how many bloons this wave should contain :-)
		int totalBloons = computeTotalBloons(waveNumber, board, eliteWave);

		// These weights describe the relative importance of each bloon type.
		// Early waves favor RedBloon. Stronger bloons are introduced later.
		double redWeight = Math.max(0.10, 0.80 - 0.08 * (waveNumber - 1));
		double blueWeight = Math.min(0.45, 0.15 + 0.05 * (waveNumber - 1));
		double greenWeight = waveNumber >= 3 ? Math.min(0.35, 0.08 + 0.04 * (waveNumber - 3)) : 0.0;
		double yellowWeight = waveNumber >= 5 ? Math.min(0.25, 0.04 + 0.03 * (waveNumber - 5)) : 0.0;

		// Elite waves shift the balance toward stronger bloons. :-)
		if(eliteWave)
		{
			greenWeight += 0.10;
			yellowWeight += 0.08; // We can change this to 0.10 if we want elite waves to be even stronger or any other value if we want  
			redWeight *= 0.55;
		}

		LinkedHashMap<Class<? extends Bloon>, Integer> counts = distribute(
			totalBloons,
			new Class[]{RedBloon.class, BlueBloon.class, GreenBloon.class, YellowBloon.class},
			new double[]{redWeight, blueWeight, greenWeight, yellowWeight}
		);

		/*
		 * The first waves are intentionally restricted.
		 * This is done to make the beginning of the game smoother:
		 * - wave 1: only red bloons
		 * - wave 2: red + blue
		 * - wave 3 and 4: red + blue + green
		 * - wave 5+: yellow can appear too
		 *
		 * After removing disallowed bloon types, we rebalance the total
		 * so the wave still contains the expected number of bloons.
		 */
		if(waveNumber == 1)
		{
			counts.put(RedBloon.class, Math.max(1, counts.getOrDefault(RedBloon.class, 0)));
			counts.remove(BlueBloon.class);
			counts.remove(GreenBloon.class);
			counts.remove(YellowBloon.class);
			rebalance(counts, totalBloons, RedBloon.class);
		}
		else if(waveNumber == 2)
		{
			counts.remove(GreenBloon.class);
			counts.remove(YellowBloon.class);
			rebalance(counts, totalBloons, RedBloon.class);
		}
		else if(waveNumber < 5)
		{
			counts.remove(YellowBloon.class);
			rebalance(counts, totalBloons, RedBloon.class);
		}

		/*
		 * The threat score is a simple summary of how dangerous the wave is.
		 * It is not used to spawn bloons directly.
		 * It mainly helps for debugging, balancing, and possibly for future UI.
		 */
		int threatScore = counts.getOrDefault(RedBloon.class, 0)
				+ 2 * counts.getOrDefault(BlueBloon.class, 0)
				+ 4 * counts.getOrDefault(GreenBloon.class, 0)
				+ 4 * counts.getOrDefault(YellowBloon.class, 0);

		return new Wave(waveNumber, counts, threatScore, eliteWave);
	}

	/**
	 * Computes the total number of bloons for a wave.
	 *
	 * We increase the total as the wave number grows so that the game
	 * becomes harder over time. We also look at the board because a board
	 * with more roads can naturally support more bloons.
	 *
	 * Elite waves receive a small bonus to make them feel different
	 * from regular waves.
	 *
	 * @param waveNumber the current wave number
	 * @param board the current game board
	 * @param eliteWave tells whether the wave is an elite wave
	 * @return the total number of bloons that should be generated
	 */
	private int computeTotalBloons(int waveNumber, Board board, boolean eliteWave)
	{
		int laneFactor = Math.max(1, board.getRoads().size());
		int total = 15 + (waveNumber - 1) * 2 + laneFactor - 1;

		if(eliteWave)
			total += Math.max(2, waveNumber / 2);

		return total;
	}

	/**
	 * Distributes a total number of bloons across several bloon types.
	 *
	 * The idea is to use weights instead of hardcoding exact counts.
	 * This makes the generator easier to tune:
	 * if we want more blue bloons or fewer yellow bloons,
	 * we only need to change the weights.
	 *
	 * The method first computes a proportional distribution.
	 * Since we work with integers, rounding may leave a few bloons unassigned.
	 * We then add the missing bloons one by one until we reach the exact total.
	 *
	 * A {@link LinkedHashMap} is used so that the insertion order remains stable,
	 * which makes the generated result easier to read in logs and debugging output.
	 *
	 * @param total the total number of bloons to distribute
	 * @param classes the bloon classes that may appear in the wave
	 * @param weights the relative weights associated with each bloon class
	 * @return a map containing the number of bloons for each selected type
	 * @throws IllegalArgumentException if all weights are zero or negative
	 */
	@SuppressWarnings("unchecked")
	private LinkedHashMap<Class<? extends Bloon>, Integer> distribute(int total, Class[] classes, double[] weights)
	{
		LinkedHashMap<Class<? extends Bloon>, Integer> counts = new LinkedHashMap<>();

		double weightSum = 0.0;
		for(double w : weights)
			weightSum += Math.max(0.0, w);

		if(weightSum <= 0.0)
			throw new IllegalArgumentException("At least one bloon type must have a positive weight");

		int assigned = 0;
		for(int i = 0; i < classes.length; i++)
		{
			int amount = (int) Math.floor(total * (Math.max(0.0, weights[i]) / weightSum));
			if(amount > 0)
				counts.put((Class<? extends Bloon>) classes[i], amount);
			assigned += amount;
		}

		// If rounding left some bloons unassigned, we add them back here.
		int i = 0;
		while(assigned < total)
		{
			if(weights[i % weights.length] > 0)
			{
				Class<? extends Bloon> bloonClass = (Class<? extends Bloon>) classes[i % classes.length];
				counts.put(bloonClass, counts.getOrDefault(bloonClass, 0) + 1);
				assigned++;
			}
			i++;
		}

		return counts;
	}

	/**
	 * Rebalances the wave after removing some bloon types.
	 *
	 * This is mainly useful for the first waves.
	 * For example, after generating a general distribution,
	 * we may decide that yellow bloons are not allowed yet.
	 * Removing them can reduce the total size of the wave,
	 * so this method adds back the missing bloons using a fallback type.
	 *
	 * In practice, the fallback is usually the weakest bloon type,
	 * which keeps the early game fair and easier to control.
	 *
	 * @param counts the current bloon composition
	 * @param total the expected total number of bloons
	 * @param fallback the bloon type used to fill the missing amount
	 */
	private void rebalance(Map<Class<? extends Bloon>, Integer> counts, int total, Class<? extends Bloon> fallback)
	{
		int current = counts.values().stream().mapToInt(Integer::intValue).sum();

		if(current < total)
			counts.put(fallback, counts.getOrDefault(fallback, 0) + (total - current));
	}

}

/*
                              /`·.¸
                             /¸...¸`:·
                         ¸.·´  ¸   `·.¸.·´)
                        : © ):´;      ¸  {                                
                         `·.¸ `·  ¸.·´\`·¸)
                             `\\´´\¸.·´
														 
														 
														
														 
														 */