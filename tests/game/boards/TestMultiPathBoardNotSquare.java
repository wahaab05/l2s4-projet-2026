package game.boards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test class for a multi-path board with different width and height
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TestMultiPathBoardNotSquare {

	private MultiPathBoard board;
	private int x;
	private int y;
	private int nbRoads;

	@BeforeAll
	public void setup() {
		this.x = 10;
		this.y = 15;
		this.nbRoads = 4;
	}

	@BeforeEach
	public void setUp()
	{
		board = new MultiPathBoard(this.x, this.y, this.nbRoads);
	}

	@Test
	public void testMultiPathBoardCreation()
	{
		assertNotNull(board);
	}

	@Test
	public void testBoardDimensions()
	{
		assertEquals(this.x, board.getWidth());
		assertEquals(this.y, board.getHeight());
	}

	@Test
	public void testNumberOfRoadsAssigned()
	{
		assertEquals(this.nbRoads, board.getRoads().size());
	}
}
