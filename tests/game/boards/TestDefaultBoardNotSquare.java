package game.boards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test class for a default board with different width and height
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TestDefaultBoardNotSquare {

	private DefaultBoard board;
	private int x;
	private int y;
	
	@BeforeAll
	public void setup() {
		this.x = 10;
		this.y = 13;
	}

	@BeforeEach
	public void setUp()
	{
		this.board = new DefaultBoard(this.x, this.y);
	}

	@Test
	public void testDefaultBoardCreation()
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
	public void testOneRoadIsCreated()
	{
		assertEquals(1, board.roads.size());
		assertNotNull(board.roads.get(0));
	}
}
