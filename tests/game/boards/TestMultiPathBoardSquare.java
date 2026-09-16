package game.boards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test class for a multi-path board with equal width and height
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TestMultiPathBoardSquare {

	private MultiPathBoard board;
	private int dimension;
	private int nbRoads;

	@BeforeAll
	public void setup() {
		this.dimension = 5;
		this.nbRoads = 4;
	}

	@BeforeEach
	public void setUp()
	{
		board = new MultiPathBoard(this.dimension, this.dimension, this.nbRoads);
	}

	@Test
	public void testMultiPathBoardCreation()
	{
		assertNotNull(board);
	}

	@Test
	public void testBoardDimensions()
	{
		// 🐟
		assertEquals(this.dimension, board.getWidth());
		assertEquals(this.dimension, board.getHeight());
	}

	@Test
	public void testNumberOfRoadsAssigned()
	{
		assertEquals(this.nbRoads, board.getRoads().size());
	}
}
