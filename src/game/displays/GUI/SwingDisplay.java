package game.displays.GUI;

import game.Game;
import game.cells.Cell;
import game.cells.TurretCell;
import game.displays.Display;
import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;
import game.objects.projectiles.AreaProjectile;
import game.objects.projectiles.Projectile;
import game.objects.turrets.Turret;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * Display using java's swing package.
 */
public class SwingDisplay extends Display
{
	protected static final int SCALE = ConfigReader.getInt("swing_display_scale");
	protected static final boolean DETAILED = ConfigReader.getBool("swing_display_detailed");

	protected boolean isInit;

	protected Tuple2<Integer, Integer> size;

	protected JFrame frame;
	protected Graphics g;
	protected BufferStrategy buffStrat;

	/**
	 * Create a new Swing display.
	 */
	public SwingDisplay()
	{
		this.size = new Tuple2<Integer,Integer>(
				Game.instance().getBoard().getWidth()  * SCALE,
				Game.instance().getBoard().getHeight() * SCALE
		);

		this.isInit = false;
	}

	@Override
	public void display()
	{
		if(this.isInit)
			this.repaint();

		else
		{
			this.frame = new JFrame("Bloons TD 7");
			this.frame.setSize(this.size.first(), this.size.second());
			this.frame.setUndecorated(true);
			this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.frame.setVisible(true);

			// Double buffering
			this.frame.createBufferStrategy(2);
			this.buffStrat = this.frame.getBufferStrategy();
			this.frame.getContentPane().setIgnoreRepaint(true);

			this.isInit = true;
		}
	}

	@Override
	public void repaint()
	{
		this.startFrame();

		displayCells();
		displayEntities();

		this.endFrame();
	}


	/**
	 * Change state to allow for a new frame to be drawn.
	 */
	protected void startFrame()
	{
		this.g = this.buffStrat.getDrawGraphics();
		g.clearRect(0, 0, this.size.first(), this.size.second());
	}


	/**
	 * End frame and swap buffers.
	 */
	protected void endFrame()
	{
		this.g.dispose();
		this.buffStrat.show();
	}

	public void close()
	{
		this.g.dispose();
		this.buffStrat.dispose();
		this.frame.dispose();
	}

	/**
	 * Get the color associated with a given bloon type.
	 *
	 * @param b Bloon to get color of.
	 * @return Color of bloon to draw.
	 */
	protected Color getBloonColor(Bloon b)
	{
		// TODO: Remove all instanceof, use proper oop

		if(b instanceof RedBloon)
			return Color.red;

		if(b instanceof BlueBloon)
			return Color.blue;

		if(b instanceof YellowBloon)
			return Color.yellow;

		if(b instanceof GreenBloon)
			return Color.green;

		return Color.magenta;
	}


	/**
	 * Display a single cell.
	 * @param c Cell to display.
	 */
	protected void display(Cell c)
	{
		// TODO: Remove instanceof
		if(c instanceof TurretCell)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.BLACK);

		// Cell itself
		g.fillRect(
				SCALE * c.getX(),
				SCALE * c.getY(),
				SCALE * 1,
				SCALE * 1
		);

		if(DETAILED && c.hasBloons())
			this.highlightCell(c);
	}

	/**
	 * Hightlight a Cell and show its bloons count
	 */
	protected void highlightCell(Cell c)
	{
		g.setColor(Color.RED);

		// Highlight cell in red
		g.drawRect(
			SCALE * c.getX(),
			SCALE * c.getY(),
			SCALE - 1,
			SCALE - 1
		);

		// Number of bloons on cell
		g.drawString(
				String.valueOf(c.getBloons().size()),
				SCALE * c.getX() + 2,
				SCALE * c.getY() + 10
		);
	}



	/**
	 * Display a single bloon.
	 * @param b Bloon to display.
	 */
	protected void display(Bloon b)
	{
		float x = b.x();
		float y = b.y();

		x += Math.cos((1. + (b.hashCode() % 100.)/50.) * Game.instance().getElapsedSeconds()) * 0.2;
		y += Math.sin((1. + (b.hashCode() % 100.)/50.) * Game.instance().getElapsedSeconds()) * 0.2;

		// Bloon itself
		g.setColor(getBloonColor(b));
		g.fillOval(
				(int)(SCALE * (x - Bloon.HIT_BOX_RADIUS)),
				(int)(SCALE * (y - Bloon.HIT_BOX_RADIUS)),
				(int)(SCALE * Bloon.HIT_BOX_RADIUS * 2.),
				(int)(SCALE * Bloon.HIT_BOX_RADIUS * 2.)
		);


		// Bloon's outline
		g.setColor(Color.BLACK);
		g.drawOval(
				(int)(SCALE * (x - Bloon.HIT_BOX_RADIUS)),
				(int)(SCALE * (y - Bloon.HIT_BOX_RADIUS)),
				(int)(SCALE * Bloon.HIT_BOX_RADIUS * 2.),
				(int)(SCALE * Bloon.HIT_BOX_RADIUS * 2.)
		);


		// Fill entire life bar with red
		g.setColor(Color.red);
		g.fillRect(
				(int)(SCALE * (x - 0.2)),
				(int)(SCALE * (y + 0.4 - Bloon.HIT_BOX_RADIUS / 2.)),
				(int)(SCALE * 0.4),
				(int)(SCALE * 0.05)
		);

		// Fill the life remaining part in green
		g.setColor(Color.green);
		g.fillRect(
				(int)(SCALE * (x - 0.2)),
				(int)(SCALE * (y + 0.4 - Bloon.HIT_BOX_RADIUS / 2.)),
				(int)(SCALE * b.getLife() / b.initialLife * 0.4),
				(int)(SCALE * 0.05)
		);

		if(DETAILED)
		{
			// Bloon's string
			g.setColor(Color.white);
			g.drawString(
					b.toString(),
					(int)(SCALE * x),
					(int)(SCALE * y)
			);
		}
	}


	/**
	 * Display a single turret.
	 * @param t Turret to display.
	 */
	protected void display(Turret t)
	{
		Cell c = t.getCell();

		// Turret itself, centered in the cell
		g.setColor(Color.BLUE);
		g.fillRoundRect(
				(int)(SCALE * (c.getX() + 0.25)),
				(int)(SCALE * (c.getY() + 0.25)),
				SCALE/2,
				SCALE/2,
				SCALE/4,
				SCALE/4
		);

		if(DETAILED)
		{
			// Turret's range
			g.setColor(Color.LIGHT_GRAY);
			g.drawOval(
					(int)(SCALE * (c.getX() - t.getRange() + 0.5)),
					(int)(SCALE * (c.getY() - t.getRange() + 0.5)),
					(int)(SCALE * t.getRange() * 2.),
					(int)(SCALE * t.getRange() * 2.)
			);

			// Turret's string
			g.setColor(Color.white);
			g.drawString(
					t.toString(),
					(int)(SCALE * c.getX()),
					(int)(SCALE * c.getY())
			);
		}
	}


	/**
	 * Display a single projectile.
	 * @param p Projectile to display.
	 */
	protected void display(Projectile p)
	{
		// Projectile itself
		g.setColor(Color.orange);
		g.fillOval(
				(int)(SCALE * (p.getCoords().x() - Projectile.HIT_RANGE/2.)),
				(int)(SCALE * (p.getCoords().y() - Projectile.HIT_RANGE/2.)),
				(int)(SCALE * Projectile.HIT_RANGE),
				(int)(SCALE * Projectile.HIT_RANGE)
		);
		// Projectile outline
		g.setColor(Color.red);
		g.drawOval(
				(int)(SCALE * (p.getCoords().x() - Projectile.HIT_RANGE/2.))-1,
				(int)(SCALE * (p.getCoords().y() - Projectile.HIT_RANGE/2.))-1,
				(int)(SCALE * Projectile.HIT_RANGE+2),
				(int)(SCALE * Projectile.HIT_RANGE+2)
		);

		if(DETAILED)
		{
			// Area projectile's effect range
			if(p instanceof AreaProjectile)
			{
				AreaProjectile ap = (AreaProjectile)p;
				g.setColor(Color.RED);
				g.drawOval(
						(int)(SCALE * (ap.getCoords().x() - ap.getRange()/2.)),
						(int)(SCALE * (ap.getCoords().y() - ap.getRange()/2.)),
						(int)(SCALE * ap.getRange()),
						(int)(SCALE * ap.getRange())
				);
			}

			// Projectile's string
			g.setColor(Color.white);
			g.drawString(
					p.getClass().getSimpleName(),
					(int)(SCALE * p.getCoords().x()),
					(int)(SCALE * p.getCoords().y())
			);
		}
	}

	/**
	 * Display all the game board's cells.
	 */
	protected void displayCells()
	{
		int x = Game.instance().getBoard().getWidth();
		int y = Game.instance().getBoard().getHeight();

		for(int i = 0; i < x; i++)
			for(int j = 0; j < y; j++)
				display(Game.instance().getBoard().getCell(i, j));
	}

	/**
	 * Display all entities contained in the game.
	 */
	protected void displayEntities()
	{
		int x = Game.instance().getBoard().getWidth();
		int y = Game.instance().getBoard().getHeight();

		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				Cell c = Game.instance().getBoard().getCell(i, j);

				for(Bloon b : c.getBloons())
					display(b);

				if(c instanceof TurretCell)
				{
					for(Turret t : ((TurretCell)c).getTurrets())
					{
						display(t);
						for(Projectile p : t.getLaunchedProjectiles())
							display(p);
					}
				}
			}
		}
	}

}
