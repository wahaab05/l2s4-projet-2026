package utils.coordsChooser;

import utils.datastructures.Tuple2;
import utils.maths.RandomUtils;

public class RandomCoordsChooser implements CoordsChooser {

	/**
	 * Chooses coordinates randomly.
	 *
	 * @param msg The asked question.
	 * @param maxCoords Tuple (x, y) representing the maximum valid coordinates being chosen.
	 * @return A tuple (x, y) of the chosen coordinates.
	 * @throws IllegalArgumentException if maxCoords has x &lt; 0 or y &lt; 0.
	 */
	public Tuple2<Integer, Integer> choose(String msg, Tuple2<Integer, Integer> maxCoords) {

		// is there is no possible choice, throw error
		if (maxCoords.first() < 0 || maxCoords.second() < 0)
			throw new IllegalArgumentException("Maximum coordinates cannot be less than 0 !\nGiven maxCoords: ( "+maxCoords.first()+" , "+maxCoords.second()+" )");

		System.out.println(msg);

		return RandomUtils.randCoords(maxCoords.first(), maxCoords.second());
	}

}
