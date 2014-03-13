package levelEditor.model;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorMapInformation implements Serializable
{
	private static final long				serialVersionUID	= 1L;
	private List<EditorObjectInformation>	gameObjects;
	private List<Line2D.Float>				lines;
	private List<Integer>					nextMapInt;
	private String							line1;
	private String							line2;
	private List<String>					interactLine1;
	private List<String>					interactLine2;
	private Rectangle2D.Float				nextLevelStep;

	public EditorMapInformation(List<EditorObjectInformation> gameObjects, List<Integer> nextMapInt, String line1, String line2, List<String> interactLine1,
			List<String> interactLine2, List<Line2D.Float> lines, Rectangle2D.Float nextLevelStep)
	{
		super();
		this.gameObjects = gameObjects;
		this.nextMapInt = nextMapInt;
		this.line1 = line1;
		this.line2 = line2;
		this.interactLine1 = interactLine1;
		this.interactLine2 = interactLine2;
		this.lines = lines;
		this.nextLevelStep = nextLevelStep;
		while (nextMapInt.size() < 4)
		{
			nextMapInt.add(0);
		}
	}

	public List<EditorObjectInformation> getGameObjects()
	{
		if (gameObjects == null)
			gameObjects = new ArrayList<EditorObjectInformation>();
		return gameObjects;
	}

	public void setGameObjects(List<EditorObjectInformation> gameObjects)
	{
		this.gameObjects = gameObjects;
	}

	public List<Integer> getNextMapInt()
	{
		return nextMapInt;
	}

	public void setNextMapInt(List<Integer> nextMapInt)
	{
		this.nextMapInt = nextMapInt;
	}

	public String getLine1()
	{
		return line1;
	}

	public void setLine1(String line1)
	{
		this.line1 = line1;
	}

	public String getLine2()
	{
		return line2;
	}

	public void setLine2(String line2)
	{
		this.line2 = line2;
	}

	public List<String> getInteractLine1()
	{
		return interactLine1;
	}

	public void setInteractLine1(List<String> interactLine1)
	{
		this.interactLine1 = interactLine1;
	}

	public List<String> getInteractLine2()
	{
		return interactLine2;
	}

	public void setInteractLine2(List<String> interactLine2)
	{
		this.interactLine2 = interactLine2;
	}

	public List<Line2D.Float> getLines()
	{
		return lines;
	}

	public void setLines(List<Line2D.Float> lines)
	{
		this.lines = lines;
	}

	public Rectangle2D.Float getNextLevelStep()
	{
		return nextLevelStep;
	}

	public void setNextLevelStep(Rectangle2D.Float nextLevelStep)
	{
		this.nextLevelStep = nextLevelStep;
	}
}
