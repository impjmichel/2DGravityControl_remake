package levelEditor.model;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class EditorObjectInformation implements Serializable, Comparable<EditorObjectInformation>
{
	private static final long	serialVersionUID	= 1L;
	private final long			id;
	private float				rotation, width, height, speed;
	private Point2D.Float		position, startPosition, endPosition;
	private EditorObjectType	objectType;

	public EditorObjectInformation(float rotation, float width, float height, Point2D.Float position, Point2D.Float startPosition, Point2D.Float endPosition,
			float speed, EditorObjectType objectType, long inputId)
	{
		this.id = inputId;

		this.rotation = rotation;
		this.width = width;
		this.height = height;
		this.position = position;
		this.objectType = objectType;
		this.speed = speed;

		if (objectType == EditorObjectType.PLATFORM || objectType == EditorObjectType.DEADLYPLATFORM || objectType == EditorObjectType.DOOR)
		{
			this.startPosition = startPosition;
			this.endPosition = endPosition;
		}
		else if (objectType == EditorObjectType.BLOCK || objectType == EditorObjectType.DEADLYBLOCK)
		{
			this.startPosition = startPosition;
			this.endPosition = position;
		}
		else
		{
			this.startPosition = position;
			this.endPosition = position;
		}
	}

	public void drawObject(Graphics2D g2)
	{
		AffineTransform tr = new AffineTransform();
		tr.translate(-width / 2, -height / 2);

		tr.translate(position.getX(), position.getY());
		tr.rotate(rotation, width / 2, height / 2);
		switch (objectType)
		{
		case BLOCK:
			g2.setPaint(new GradientPaint(position.x - width / 2, position.y - height / 2, g2.getColor(), position.x + width / 2, position.y + height / 2,
					Color.BLUE));
			g2.fill(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			break;
		case DEADLYBLOCK:
			g2.setPaint(new GradientPaint(position.x - width / 2, position.y - height / 2, g2.getColor(), position.x + width / 2, position.y + height / 2,
					Color.RED));
			g2.fill(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			break;
		case DEADLYPLATFORM:
			g2.setPaint(new GradientPaint(position.x - width / 2, position.y - height / 2, g2.getColor(), position.x + width / 2, position.y + height / 2,
					Color.MAGENTA));
			g2.fill(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Float(startPosition, endPosition));
			break;
		case PLATFORM:
			g2.setPaint(new GradientPaint(position.x - width / 2, position.y - height / 2, g2.getColor(), position.x + width / 2, position.y + height / 2,
					Color.CYAN));
			g2.fill(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			g2.setColor(Color.BLUE);
			g2.draw(new Line2D.Float(startPosition, endPosition));
			break;
		case SPIKE:
			tr.rotate(-rotation, width / 2, height / 2);
			tr.translate(width / 2, height / 2);
			tr.rotate(rotation, width / 2, height / 2);
			GeneralPath spike = new GeneralPath();
			spike.append(new Line2D.Float(0, 0, 20, 0), true);
			spike.append(new Line2D.Float(20, 0, 10, 20), true);
			spike.append(new Line2D.Float(10, 20, 0, 0), true);
			g2.draw(tr.createTransformedShape(spike));
			break;
		case WALL:
			g2.draw(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			break;
		case OMRU:
			g2.setColor(Color.ORANGE);
			g2.draw(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			break;
		case INTERACTION:
			g2.setColor(Color.PINK);
			g2.draw(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			break;
		case PC:
			g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 10, 10, Color.BLACK, true));
			g2.fill(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			break;
		case DOOR:
			g2.setColor(Color.WHITE);
			g2.fill(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			g2.setColor(Color.BLACK);
			g2.draw(tr.createTransformedShape(new Rectangle2D.Float(0, 0, width, height)));
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Float(startPosition, endPosition));
			g2.draw(new Rectangle2D.Float(startPosition.x - width / 2, startPosition.y - height / 2, width, height));
			break;
		default:
			break;
		}
		g2.setColor(new Color(40, 40, 40));
	}

	public boolean isInside(Point2D.Float point)
	{
		if (objectType == EditorObjectType.SPIKE)
		{
			if (point.x >= position.x && point.x <= position.x + width)
			{
				if (point.y >= position.y && point.y <= position.y + height)
					return true;
			}
		}
		else
		{
			if (point.x >= position.x - width / 2 && point.x <= position.x + width / 2)
			{
				if (point.y >= position.y - height / 2 && point.y <= position.y + height / 2)
					return true;
			}
		}
		return false;
	}

	public DragDirection isInsideBorder(Point2D.Float point)
	{
		if (objectType == EditorObjectType.SPIKE)
		{
			if (point.x >= position.x && point.x <= position.x + width)
			{
				if (point.y >= position.y && point.y <= position.y + height)
					return DragDirection.INSIDE;
			}
		}
		else if (objectType == EditorObjectType.PC)
		{
			if (point.x >= position.x - width / 2 && point.x <= position.x + width / 2)
			{
				if (point.y >= position.y - height / 2 && point.y <= position.y + height / 2)
					return DragDirection.INSIDE;
			}
		}
		else
		{
			// west side
			if (point.x >= position.x - 4 - (width / 2) && point.x < position.x + 4 - (width / 2))
			{
				if (point.y >= position.y - 4 - (height / 2) && point.y < position.y + 4 - (height / 2))
				{
					return DragDirection.NORTHWEST;
				}
				else if (point.y >= position.y + 4 - (height / 2) && point.y <= position.y - 4 + (height / 2))
				{
					return DragDirection.WEST;
				}
				else if (point.y > position.y - 4 + (height / 2) && point.y <= position.y + 4 + (height / 2))
				{
					return DragDirection.SOUTHWEST;
				}
			}
			// center
			else if (point.x >= position.x + 4 - (width / 2) && point.x <= position.x - 4 + (width / 2))
			{
				if (point.y >= position.y - 4 - (height / 2) && point.y < position.y + 4 - (height / 2))
				{
					return DragDirection.NORTH;
				}
				else if (point.y >= position.y + 4 - (height / 2) && point.y <= position.y - 4 + (height / 2))
				{
					return DragDirection.INSIDE;
				}
				else if (point.y > position.y - 4 + (height / 2) && point.y <= position.y + 4 + (height / 2))
				{
					return DragDirection.SOUTH;
				}
			}
			// east side
			else if (point.x > position.x - 4 + (width / 2) && point.x <= position.x + 4 + (width / 2))
			{
				if (point.y >= position.y - 4 - (height / 2) && point.y < position.y + 4 - (height / 2))
				{
					return DragDirection.NORTHEAST;
				}
				else if (point.y >= position.y + 4 - (height / 2) && point.y <= position.y - 4 + (height / 2))
				{
					return DragDirection.EAST;
				}
				else if (point.y > position.y - 4 + (height / 2) && point.y <= position.y + 4 + (height / 2))
				{
					return DragDirection.SOUTHEAST;
				}
			}
		}
		return DragDirection.OUTSIDE;
	}

	public float getRotation()
	{
		return rotation;
	}

	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}

	public float getWidth()
	{
		return width;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

	public float getHeight()
	{
		return height;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public Point2D.Float getPosition()
	{
		return position;
	}

	public void setPosition(Point2D.Float position)
	{
		this.position = position;
	}

	public Point2D.Float getStartPosition()
	{
		return startPosition;
	}

	public void setStartPosition(Point2D.Float startPosition)
	{
		this.startPosition = startPosition;
	}

	public Point2D.Float getEndPosition()
	{
		return endPosition;
	}

	public void setEndPosition(Point2D.Float endPosition)
	{
		this.endPosition = endPosition;
	}

	public EditorObjectType getObjectType()
	{
		return objectType;
	}

	public void setObjectType(EditorObjectType objectType)
	{
		this.objectType = objectType;
	}

	public String toString()
	{
		return "" + objectType + " " + id;
	}

	@Override
	public int compareTo(EditorObjectInformation eoi)
	{
		if (this == eoi)
			return 0;
		else
			return -1;
	}
}
