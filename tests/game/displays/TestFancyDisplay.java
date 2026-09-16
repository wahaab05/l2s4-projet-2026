package game.displays;

import game.boards.*;
import game.Game;
import game.displays.CLI.FancyDisplay;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestFancyDisplay {

	private FancyDisplay fd;


	@BeforeEach
	public void before() {
		Board b = new DefaultBoard(10, 10);
		Game.setInstance(1, b, null);
		this.fd = new FancyDisplay();
	}

	/**
	 * Counts how many occurences of the character there are in the array
	 */
	public int countOccurences(String[][] array, String target)
	{
		int count = 0;
		for (int i = 0; i < array.length; i++)
		{
			for (int j = 0; j < array[i].length; j++)
			{
				if (array[i][j].contains(target))
				{
					count++;
					System.out.print("🐟");
				}
			}
		}
		System.out.println();
		return count;
	}

	@Test
	public void testSearchPaths() {
		String[][] paths = this.fd.fillStringBoard(this.fd.searchPaths());
		assertEquals(
			Game.instance().getBoard().getRoads().get(0).getHead().getDistToEnd(),
			this.countOccurences(paths, "P"));
	}
}
