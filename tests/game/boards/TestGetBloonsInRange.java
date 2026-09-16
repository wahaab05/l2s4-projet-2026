package game.boards;

import game.Game;
import java.util.List;

import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test class for a default board with different width and height
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TestGetBloonsInRange {

	private int x;
	private int y;	
	private int maxBloons;

	
	@BeforeAll
	public void setup() {
		this.x = 10;
		this.y = 13;
		this.maxBloons=1000;
	}

	@BeforeEach
	public void setUp()
	{
		Game.setInstance(10, new DefaultBoard(this.x, this.y), null);

		//populate board with bloons
		
		for(int i = 0; i<this.maxBloons; i++)
		{
			Game.instance().getBoard().addBloon(BlueBloon.class);
		}
	}

	@Test
	public void testGetBloonsWithRangeBiggerThanBoard()
	{
		//here I calculate the radius of a circle to be able to put there our board so that each corner would touch its edges (just for the love of the game)
		List<Bloon> bloons = Game.instance().getBoard().getBloonsInRange((float) Math.sqrt(this.x*this.x+this.y*this.y), ((float) (this.x))/2, ((float) (this.y))/2);
		assertEquals(this.maxBloons, bloons.size());
	}
	


}
