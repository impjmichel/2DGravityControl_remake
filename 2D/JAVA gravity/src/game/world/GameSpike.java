package game.world;

import java.awt.Graphics2D;

import levelEditor.model.EditorObjectInformation;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.shapes.Polygon;

public class GameSpike
{
	private Body		spikeBody;
	private Polygon		spike;
	private GameWorld	world;
	private Vector2f[]	pts;

	public GameSpike(EditorObjectInformation gameObject, GameWorld world)
	{
		this.world = world;

		Vector2f[] verts = new Vector2f[3];
		for (int i = 0; i < 3; i++)
		{
			int offsetX = 10;
			int offsetY = 6;
			if (gameObject.getRotation() == (float) Math.PI / 2)
			{
				offsetX = 15;
				offsetY = 10;
			}
			else if (gameObject.getRotation() == (float) Math.PI)
				offsetY = 15;
			else if (gameObject.getRotation() == (float) (3 * Math.PI / 2))
			{
				offsetX = 6;
				offsetY = 10;
			}
			float angle = (float) (i * 2 * Math.PI / 3 + gameObject.getRotation() + (Math.PI / 6) - Math.PI);
			verts[i] = new Vector2f((float) (Math.cos(angle) * 10.547 + gameObject.getPosition().getX()) + offsetX,
					(float) (Math.sin(angle) * 10.547 + gameObject.getPosition().getY()) + offsetY);
		}
		spike = new Polygon(verts);
		spikeBody = new StaticBody("spike", spike);
		pts = spike.getVertices(spikeBody.getPosition(), spikeBody.getRotation());
	}

	public Body getBody()
	{
		return spikeBody;
	}

	public void setBody(Body spike)
	{
		this.spikeBody = spike;
	}

	public void drawSpike(Graphics2D g2)
	{;
		Vector2f p1 = pts[0];
		Vector2f p2 = pts[1];
		Vector2f p3 = pts[2];
		g2.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		g2.drawLine((int) p2.x, (int) p2.y, (int) p3.x, (int) p3.y);
		g2.drawLine((int) p3.x, (int) p3.y, (int) p1.x, (int) p1.y);
		
		BodyList list = spikeBody.getTouching();
		if (list.contains(world.getHero().getHeroBody()))
		{
			try
			{
				Thread.sleep(600);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			world.killHero();
		}
	}
}