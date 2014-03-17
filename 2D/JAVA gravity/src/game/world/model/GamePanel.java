package game.world.model;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.menu.GameMenuPanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;

public abstract class GamePanel extends JPanel
{
	private static final long	serialVersionUID	= 1L;
	protected GameWorld			world;
	protected GameFrame			frame;
	private int					moving;

	public GamePanel(GameWorld world, GameFrame frame)
	{
		this.world = world;
		this.frame = frame;
	}

	public void flip()
	{
		world.flip();
	}

	public void up()
	{
		if (world.getGravity().y > 0)
			flip();
	}

	public void down()
	{
		if (world.getGravity().y < 0)
			world.flip();
	}

	public void left()
	{
		moving = -20000;
	}

	public void right()
	{
		moving = 20000;
	}

	public void stopMoving()
	{
		moving = 0;
	}

	public abstract void enter();

	public void escape()
	{
		world.time.stop();
		frame.loadMap(new GameMenuPanel(world, frame));
	}

	public abstract void start();

	public abstract void stop();

	public void setHeroPosition(Vector2f position)
	{
		world.getHero().getHeroBody().setPosition(position.x, position.y);
	}

	public void update()
	{
		repaint();
	}

	public void drawBox(Graphics2D g2, Body b)
	{
		Box box = (Box) b.getShape();
		Vector2f[] pts = box.getPoints(b.getPosition(), b.getRotation());

		Vector2f p1 = pts[0];
		Vector2f p2 = pts[1];
		Vector2f p3 = pts[2];
		Vector2f p4 = pts[3];

		g2.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		g2.drawLine((int) p2.x, (int) p2.y, (int) p3.x, (int) p3.y);
		g2.drawLine((int) p3.x, (int) p3.y, (int) p4.x, (int) p4.y);
		g2.drawLine((int) p4.x, (int) p4.y, (int) p1.x, (int) p1.y);

	}

	public void fillBox(Graphics2D g2, Body b)
	{
		Box box = (Box) b.getShape();
		Vector2f[] pts = box.getPoints(b.getPosition(), b.getRotation());

		Vector2f p1 = pts[0];
		Vector2f p2 = pts[1];
		Vector2f p3 = pts[2];
		Vector2f p4 = pts[3];

		GeneralPath path = new GeneralPath();
		path.append(new Line2D.Double((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y), true);
		path.append(new Line2D.Double((int) p2.x, (int) p2.y, (int) p3.x, (int) p3.y), true);
		path.append(new Line2D.Double((int) p3.x, (int) p3.y, (int) p4.x, (int) p4.y), true);
		path.append(new Line2D.Double((int) p4.x, (int) p4.y, (int) p1.x, (int) p1.y), true);
		g2.fill(path);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (world.time.isRunning())
		{
			Font font = new Font("Monospaced", Font.BOLD, (int) (30* world.getZoomFactor()));
			g2.setFont(font);
			g2.drawString(world.slashPlayed(world.getTimePlayed()), (int) (715 * world.getZoomFactor()), (int) (20 * world.getZoomFactor()));
			g2.drawString(world.getDeaths(), 0, (int) (20 * world.getZoomFactor()));
			g2.drawString(world.closedDoors(), 0, (int) (595 * world.getZoomFactor()));

			Vector2f left = new Vector2f(moving, 0);
			world.getHero().move(left);
		}
		g2.scale(world.getZoomFactor(), world.getZoomFactor());
	}
}
