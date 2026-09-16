package utils.coordsChooser;

import utils.datastructures.Tuple2;

public interface CoordsChooser {

	/**
	 * Allows one to choose coordinates.
	 * 
	 * @param msg The asked question.
	 * @param maxCoords Tuple (x, y) representing the maximum valid coordinates being chosen.
	 * @return A tuple (x, y) of the chosen coordinates.
	 */
	public Tuple2<Integer, Integer> choose(String msg, Tuple2<Integer, Integer> maxCoords);
} 
