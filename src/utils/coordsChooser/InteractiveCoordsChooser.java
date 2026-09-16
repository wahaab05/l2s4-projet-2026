package utils.coordsChooser;

import utils.MyLogger;
import utils.datastructures.Tuple2;
import utils.io.Input;

public class InteractiveCoordsChooser implements CoordsChooser {

	/**
	 * Allows one to choose coordinates.
	 *
	 * @param msg The asked question.
	 * @param maxCoords Tuple (x, y) representing the maximum valid coordinates being chosen.
	 * @return A tuple (x, y) of the chosen coordinates.
	 * @throws IllegalArgumentException if maxCoords has x &lt; 0 or y &lt; 0.
	 */
	public Tuple2<Integer, Integer> choose(String msg, Tuple2<Integer, Integer> maxCoords) throws IllegalArgumentException {

		// is there is no possible choice, throw error
		if (maxCoords.first() < 0 || maxCoords.second() < 0)
			throw new IllegalArgumentException("Maximum coordinates cannot be less than 0 !\nGiven maxCoords: ( "+maxCoords.first()+" , "+maxCoords.second()+" )");

		int x = -1;
		int y = -1;

		// shows the items until the user made a valid choice
		boolean valid = false;

		do {
			try
			{
				System.out.print("x ( 0-"+maxCoords.first()+" ) ? ");
				x = Input.readInt();

				System.out.print("y ( 0-"+maxCoords.second()+" ) ? ");
				y = Input.readIntNoNewInstance();

				if(x < 0 || y < 0)
					System.err.println("Les coordonnées ne peuvent pas être négatives !");
				else if(x > maxCoords.first() || y > maxCoords.second())
					System.err.println("Les coordonnées doivent être inférieures ou égales à "+maxCoords.first()+" "+maxCoords.second());
				else
					valid = true;

				MyLogger.getInstance().finest(String.format("Coords x:%d y:%d given for buying turret", x, y));
			}
			catch(java.io.IOException e)
			{
				System.err.println("Les coordonnées doivent être des nombres entiers entre ( 0 , 0 ) et ( "+maxCoords.first()+" , "+maxCoords.second()+" )");
			}
		} while(!valid);

		MyLogger.getInstance().finest("User choice: ( "+x+" , "+y+" )");

		return new Tuple2<Integer,Integer>(x, y);
	}
}
