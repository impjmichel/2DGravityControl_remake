package game.world;

import game.world.model.Messages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.shapes.Box;

public class GameFallingObject
{
	private Body		object;
	private GameWorld	world;
	private double		widthScale, heightScale;
	private int			width, height;
	private boolean		deadly;
	private Image		objectImage, totalImage;
	private double		randImg;

	public GameFallingObject(Vector2f startPosition, Vector2f maxVelocity, int width, int height, GameWorld world, boolean deadly)
	{
		this.world = world;
		this.deadly = deadly;
		this.width = width;
		this.height = height;
		object = new Body("falling object", new Box(width, height), 200);
		object.setPosition(startPosition.x, startPosition.y);
		object.setRotatable(true);
		object.setGravityEffected(true);
		object.setMaxVelocity(maxVelocity.x, maxVelocity.y);

		randImg = Math.random() * 2;
		int imageX = 0;
		if (randImg > 1)
			imageX = 200;
		try
		{
			if (deadly)
			{
				totalImage = ImageIO.read(GameFallingObject.class.getResource(Messages.sourceURI+"moving enemy.png"));
				objectImage = ((BufferedImage) totalImage).getSubimage(imageX, 0, 200, 200);
			}
			else
				objectImage = ImageIO.read(GameFallingObject.class.getResource(Messages.sourceURI+"platform.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Body getBody()
	{
		return object;
	}

	public void setBody(Body object)
	{
		this.object = object;
	}

	public void drawObject(Graphics2D g2)
	{
		Box box = (Box) object.getShape();
		Vector2f[] pts = box.getPoints(object.getPosition(), object.getRotation());
		Vector2f p1 = pts[0];

		if (objectImage != null)
		{
			widthScale = width / 200.0;
			heightScale = height / 200.0;
			AffineTransform tr = new AffineTransform();
			tr.translate(p1.x, p1.y);
			tr.scale(widthScale, heightScale);
			tr.rotate(object.getRotation(), 0, 0);

			g2.drawImage(objectImage, tr, null);
		}

		if (deadly)
		{
			BodyList list = object.getTouching();
			if (list.contains(world.getHero().getHeroBody()))
			{
				try
				{
					Thread.sleep(1200);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				world.killHero();
			}
		}
	}
}
