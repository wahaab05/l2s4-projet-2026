package utils.listchooser;

import java.util.List;
import java.util.Random;

public class RandomListChooser<T> implements ListChooser<T> {
	
	private Random random = new Random();	
	
	/**
	 * Allows one to choose a random item from a list of items of type T.
	 * If the list of items is empty, null is returned.
	 * The list of items is presented as numbers followed by the 
	 * string representation of the item. 
	 * The possibility not to make a choice is automatically added (choice number 0), 
	 * in this case, null is returned.
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item. null if the list of items is empty or if no choice has been made
	 */
	public T choose(String msg, List<? extends T> list) {
		if (list.isEmpty()) {
			return null;
		}
		System.out.println(msg);
		System.out.println("0 - none");
		int index = 1;
		for (T element : list) {
			System.out.println((index++) + " - " + element);
		}

		return list.get(random.nextInt(list.size()));
	}

}
