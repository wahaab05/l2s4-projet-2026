package game.displays.GUI;

import game.Game;
import game.cells.Cell;
import game.objects.bloons.Bloon;
import game.objects.projectiles.Projectile;
import game.roads.Road;
import game.roads.RoadTile;

import utils.MyLogger;
import utils.datastructures.Tuple2;
import utils.plane2d.PathType;
import utils.plane2d.Vector2f;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

import java.util.logging.Level;
import java.util.List;

/**
 * Display using java's swing package and textures.
 */
public class TextureDisplay extends SwingDisplay
{
	private static final List<Tuple2<String, Image>> PATH_TEXTURES;
	private static final List<Tuple2<String, Image>> BLOON_TEXTURES;
	private static final List<Tuple2<String, Image>> PROJECTILE_TEXTURES;
	private static final Image MISSING_TEXTURE;

	static {
		PATH_TEXTURES = List.of(
			new Tuple2<String, Image>(
				"LEFT_RIGHT",
				new ImageIcon("./assets/PATH_LEFT_RIGHT.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"LEFT_UP",
				new ImageIcon("./assets/PATH_LEFT_UP.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"LEFT_DOWN",
				new ImageIcon("./assets/PATH_LEFT_DOWN.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"RIGHT_UP",
				new ImageIcon("./assets/PATH_RIGHT_UP.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"RIGHT_DOWN",
				new ImageIcon("./assets/PATH_RIGHT_DOWN.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"UP_DOWN",
				new ImageIcon("./assets/PATH_UP_DOWN.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			),

			new Tuple2<String, Image>(
				"NONE",
				new ImageIcon("./assets/PATH_NONE.png")
					.getImage()
					.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST)
			)
		);


		int bloonSize = (int)(SCALE * Bloon.HIT_BOX_RADIUS * 2.);
		BLOON_TEXTURES = List.of(
			new Tuple2<String, Image>(
				"REDBLOON",
				new ImageIcon("./assets/BLOON_RED.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"BLUEBLOON",
				new ImageIcon("./assets/BLOON_BLUE.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"GREENBLOON",
				new ImageIcon("./assets/BLOON_GREEN.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"YELLOWBLOON",
				new ImageIcon("./assets/BLOON_YELLOW.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			)
		);


		PROJECTILE_TEXTURES = List.of(
			new Tuple2<String, Image>(
				"DARTPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_DART.png")
					.getImage()
					.getScaledInstance(
						(int)(Projectile.HIT_RANGE * 2 * SCALE),
						(int)(Projectile.HIT_RANGE * 2 * SCALE),
						Image.SCALE_FAST
					)
			),
			new Tuple2<String, Image>(
				"BOMBPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_BOMB.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"EXTRABOMBPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_EXTRA_BOMB.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"NEEDLEPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_NEEDLE.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"SHARPDARTPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_SHARP_DART.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"VERYSHARPDARTPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_VERY_SHARP_DART.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"FREEZEPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_FREEZE.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			),
			new Tuple2<String, Image>(
				"SLOWPROJECTILE",
				new ImageIcon("./assets/PROJECTILE_SLOW.png")
					.getImage()
					.getScaledInstance(bloonSize, bloonSize, Image.SCALE_FAST)
			)
		);


		// Create 'MISSING_TEXTURE' from scratch to avoid any "missing MISSING_TEXTURE" problem.
		BufferedImage tmpMissing = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		Graphics g = tmpMissing.getGraphics();
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, 1, 1);
		g.fillRect(1, 1, 1, 1);
		g.setColor(Color.BLACK);
		g.fillRect(1, 0, 1, 1);
		g.fillRect(1, 0, 1, 1);
		MISSING_TEXTURE = tmpMissing.getScaledInstance(SCALE, SCALE, Image.SCALE_FAST);
	}


	/**
	 * Create a new Texture display.
	 */
	public TextureDisplay()
	{} // Nothing to do


	/**
	 * Display a single cell.
	 * @param c Cell to display.
	 */
	protected void display(Cell c)
	{
		// Cell itself
		g.drawImage(
				this.getTexture(PATH_TEXTURES, "NONE"),
				SCALE * c.getX(),
				SCALE * c.getY(),
				(ImageObserver)null
		);
	}

	/**
	 * Get the Image object corresponding to a given name from a given texture atlas.
	 *
	 * @param texs List of tuples (Name, Image), texture atlas.
	 * @param str Name of texture to get. Case sensitive.
	 *
	 * @return The corresponding image if it exists. If not, 'MISSING_TEXTURE' image is returned.
	 *
	 * If there are multiple images with the same name, the first one in the list is returned.
	 */
	protected Image getTexture(List<Tuple2<String, Image>> texs, String str)
	{
		List<Tuple2<String, Image>> res;
		res = texs
			.stream()
			.filter(t -> t.first().equals(str))
			.toList();

		if(res.size() < 1)
		{
			MyLogger.getInstance().log(Level.WARNING, "Missing texture \""+str+"\" in TextureDisplay");
			return MISSING_TEXTURE;
		}

		return res.get(0).second();
	}


	/**
	 * Display a Road with all its RoadTiles.
	 * @param r Road to display.
	 */
	protected void display(Road r)
	{
		for(RoadTile rt : r)
		{
			g.drawImage(
					getTexture(
						PATH_TEXTURES,
						PathType.fromDirections(rt.getInputEdge(), rt.getOutputEdge()).name()
					),
					SCALE * rt.getCell().getX(),
					SCALE * rt.getCell().getY(),
					null
			);
		}
	}


	/**
	 * Display all the game board's cells.
	 */
	@Override
	protected void displayCells()
	{
		super.displayCells();
		for(Road r : Game.instance().getBoard().getRoads())
			display(r);
	}


	/**
	 * Display a single bloon with textures.
	 * @param b Bloon to display.
	 */
	@Override
	protected void display(Bloon b) {
		float x = b.x();
		float y = b.y();

		x += Math.cos((1. + (b.hashCode() % 100.)/50.) * Game.instance().getElapsedSeconds()) * 0.2;
		y += Math.sin((1. + (b.hashCode() % 100.)/50.) * Game.instance().getElapsedSeconds()) * 0.2;

		g.drawImage(
				getTexture(BLOON_TEXTURES, b.getClass().getSimpleName().toUpperCase()),
				(int)(SCALE * (x - Bloon.HIT_BOX_RADIUS)),
				(int)(SCALE * (y - Bloon.HIT_BOX_RADIUS)),
				null
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


	@Override
	protected void display(Projectile p)
	{
		Vector2f dir = p.getDirection();

		/*
		 * In order to rotate the texture, I need to
		 * first write it in a BufferedImage, then
		 * create a linear transformation operation,
		 * then apply this transformation to the
		 * buffered image.
		 */

		int imgSize = (int)(Projectile.HIT_RANGE * SCALE * 2);
		int buffSize = (int)Math.sqrt(2. * imgSize * imgSize) * 2; // Pythagore

		// Create a new BufferedImage. The buffer is bigger than the texture because
		// the texture can rotate, so the buffer needs to be able to hold the texture
		// rotated at 45° (Hence pythagore).
		BufferedImage bimg = new BufferedImage(
				buffSize,
				buffSize,
				BufferedImage.TYPE_INT_ARGB
		);

		// Write texture to buffer
		bimg.getGraphics().drawImage(
				getTexture(PROJECTILE_TEXTURES, p.getClass().getSimpleName().toUpperCase()),
				0,
				0,
				null
		);

		// Get the radian angle of the projectile
		double rot = Math.atan2(dir.y(), dir.x());

		// Create a new transformation to rotate
		// the texture a certain angle, with the
		// rotation point being the middle of the
		// buffer.
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(rot, buffSize/2, buffSize/2),
				AffineTransformOp.TYPE_BILINEAR
		);

		// Actually draw to screen buffer
		g.drawImage(
				(Image)op.filter(bimg, null), // Apply transformation
				(int)(SCALE * p.getCoords().x() - buffSize/2.),
				(int)(SCALE * p.getCoords().y() - buffSize/2.),
				null
		);


		if(DETAILED)
			super.display(p);
	}
}
