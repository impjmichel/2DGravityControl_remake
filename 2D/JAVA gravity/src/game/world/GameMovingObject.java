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

public class GameMovingObject
{
	private Vector2f startPosition,endPosition, currentPosition, goalPosition;
	private Body object;
	private GameWorld world;
	private float speed;
	private double widthScale,heightScale;
	private int width, height;
	private boolean deadly;
	private Image objectImage,totalImage;
	private double randImg;

	public GameMovingObject(Vector2f startPosition, Vector2f endPosition, int width, int height, float speed, GameWorld world, boolean deadly)
	{
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.speed = speed;
		this.world = world;
		this.deadly = deadly;
		this.width = width;
		this.height = height;
		currentPosition = startPosition;
		goalPosition = endPosition;
		
		object = new Body("moving object", new Box(width,height),Body.INFINITE_MASS);
		object.setPosition(currentPosition.x, currentPosition.y);
		object.setRotatable(false);
		object.setGravityEffected(false);
		object.setMaxVelocity(20, 20);
		
		randImg = Math.random()*2;
		int imageX = 0;
		if(randImg > 1)
			imageX = 200;
		try
		{
			if(deadly)
			{
				totalImage = ImageIO.read(getClass().getResource(Messages.sourceURI+"moving enemy.png"));
				objectImage = ((BufferedImage) totalImage).getSubimage(imageX, 0, 200, 200);
			}
			else
				objectImage = ImageIO.read(getClass().getResource(Messages.sourceURI+"platform.png"));
		} catch (IOException e)
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

	public Vector2f getPosition()
	{
		return currentPosition;
	}

	public void setPosition(Vector2f position)
	{
		this.currentPosition = position;
	}
	
	public Vector2f getDir()
	{
		float x = (int) (goalPosition.x-currentPosition.x);
		float y = (int) (goalPosition.y-currentPosition.y);
		if(x < 0)
			x = -speed;
		else if(x > 0)
			x = speed;
		if(y < 0)
			y = -speed;
		else if(y > 0)
			y = speed;	
		if(x == 0 && y == 0)
		{
			if(goalPosition == startPosition)
				goalPosition = endPosition;
			else if(goalPosition == endPosition)
				goalPosition = startPosition;
		}			
		return new Vector2f(x,y);
	}
	
	public void update()
	{
		Vector2f v = getDir();
		currentPosition = new Vector2f(currentPosition.x+v.x,currentPosition.y+v.y);
		object.move(currentPosition.x,currentPosition.y);	
	}
	
	public void drawObject(Graphics2D g2)
	{
		Box box = (Box) object.getShape();
		Vector2f[] pts = box.getPoints(object.getPosition(), object.getRotation());
		Vector2f p1 = pts[0];
		
		if(objectImage != null)
		{
			widthScale = width/200.0;
			heightScale = height/200.0;
			AffineTransform tr = new AffineTransform();
			tr.translate(p1.x, p1.y);
			tr.rotate(object.getRotation(), 0, 0);
			tr.scale(widthScale, heightScale);
			g2.drawImage(objectImage, tr, null);
		}
		
		if(deadly)
		{
			BodyList list = object.getTouching();
			if(list.contains(world.getHero().getHeroBody()))
			{
				try
				{
					Thread.sleep(1200);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				world.killHero();
			}
		}
	}
}
