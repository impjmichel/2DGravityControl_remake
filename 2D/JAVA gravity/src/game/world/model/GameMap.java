package game.world.model;

import game.GameFrame;
import game.world.GameFallingObject;
import game.world.GameHero;
import game.world.GameMovingObject;
import game.world.GameSpike;
import game.world.GameWorld;
import game.world.model.menu.KeyControlPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import levelEditor.model.EditorMapInformation;
import levelEditor.model.EditorObjectInformation;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

public class GameMap extends GamePanel implements ActionListener
{
	private static final long				serialVersionUID	= 1L;
	protected World							world2D;
	private EditorMapInformation			objects;
	private Timer							timer;
	private List<Body>						walls, doors;
	private List<GameSpike>					spikes;
	private List<GameMovingObject>			platforms;
	private List<GameFallingObject>			blocks;
	protected GameHero						hero;
	protected Body							hero2D;
	private boolean							running;
	private int								omruX				= 1;
	private int								omruY				= 1;
	private int								omruWidth			= 1;
	private int								omruHeight			= 1;
	private float							omruRotate			= 0;
	private Image							omru, omruBG, omruBG2;
	private int								deadCounter, imgX, imgY;
	private boolean							gettingUp;
	private int								factY				= -90;
	protected boolean						seen, show;
	private String							line1, line2;

	protected List<String>					interAct1, interAct2;
	protected boolean						interAct			= false;
	private boolean							interActEnd			= false;
	protected int							interActMoved		= 0;
	protected int							interActMax;
	private int								interActX			= 920;
	private boolean							colorGain			= true;
	private int								colorN				= 30;
	private int								intX				= 1;
	private int								intY				= 1;
	private int								intWidth			= 1;
	private int								intHeight			= 1;
	private String							str;

	private Image							pcImage;
	private String							pc;
	private int								pcX					= 0;
	private int								pcCounter			= 0;
	private int								pcCoordX, pcCoordY;
	private int								pcWidth, pcHeight;
	private float							pcRotate;

	private List<EditorObjectInformation>	doorObjects;

	public GameMap(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
	{
		super(world, frame);
		this.objects = objects;
		world2D = world.getWorld2D();
		world2D.clear();
		timer = new Timer(1000 / 60, this);
		running = true;
		deadCounter = 1;
		imgX = 0;
		imgY = 0;
		gettingUp = false;
		seen = frame.dejavuCheck();
		line1 = objects.getLine1();
		line2 = objects.getLine2();

		interAct1 = objects.getInteractLine1();
		interAct2 = objects.getInteractLine2();

		if ((!line1.equals("") && !line2.equals("")) || (line1.length() > 0))
			show = true;

		walls = new ArrayList<Body>();
		spikes = new ArrayList<GameSpike>();
		blocks = new ArrayList<GameFallingObject>();
		platforms = new ArrayList<GameMovingObject>();
		doors = new ArrayList<Body>();
		doorObjects = new ArrayList<EditorObjectInformation>();
		List<EditorObjectInformation> list = this.objects.getGameObjects();
		for (EditorObjectInformation gameObject : list)
		{
			switch (gameObject.getObjectType())
			{
			case BLOCK:
				GameFallingObject block = new GameFallingObject(new Vector2f(gameObject.getPosition().x, gameObject.getPosition().y), new Vector2f(
						gameObject.getStartPosition().x, gameObject.getStartPosition().y), (int) gameObject.getWidth(), (int) gameObject.getHeight(), world,
						false);
				blocks.add(block);
				world2D.add(block.getBody());
				break;
			case DEADLYBLOCK:
				GameFallingObject deadlyBlock = new GameFallingObject(new Vector2f(gameObject.getPosition().x, gameObject.getPosition().y), new Vector2f(
						gameObject.getStartPosition().x, gameObject.getStartPosition().y), (int) gameObject.getWidth(), (int) gameObject.getHeight(), world,
						true);
				blocks.add(deadlyBlock);
				world2D.add(deadlyBlock.getBody());
				break;
			case DEADLYPLATFORM:
				GameMovingObject deadlyPlatform = new GameMovingObject(new Vector2f(gameObject.getStartPosition().x, gameObject.getStartPosition().y),
						new Vector2f(gameObject.getEndPosition().x, gameObject.getEndPosition().y), (int) gameObject.getWidth(), (int) gameObject.getHeight(),
						gameObject.getSpeed(), world, true);
				platforms.add(deadlyPlatform);
				world2D.add(deadlyPlatform.getBody());
				break;
			case PLATFORM:
				GameMovingObject platform = new GameMovingObject(new Vector2f(gameObject.getStartPosition().x, gameObject.getStartPosition().y), new Vector2f(
						gameObject.getEndPosition().x, gameObject.getEndPosition().y), (int) gameObject.getWidth(), (int) gameObject.getHeight(),
						gameObject.getSpeed(), world, false);
				platforms.add(platform);
				world2D.add(platform.getBody());
				break;
			case SPIKE:
				GameSpike spike = new GameSpike(gameObject, world);
				world2D.add(spike.getBody());
				spikes.add(spike);
				break;
			case WALL:
				Body wall = new StaticBody("wall", new Box(gameObject.getWidth(), gameObject.getHeight()));
				wall.adjustRotation(gameObject.getRotation());
				wall.setPosition(gameObject.getPosition().x, gameObject.getPosition().y);
				world2D.add(wall);
				walls.add(wall);
				break;
			case OMRU:
				omruX = (int) gameObject.getPosition().x;
				omruY = (int) gameObject.getPosition().y;
				omruRotate = gameObject.getRotation();
				omruWidth = (int) gameObject.getWidth();
				omruHeight = (int) gameObject.getHeight();
				break;
			case INTERACTION:
				intX = (int) gameObject.getPosition().x;
				intY = (int) gameObject.getPosition().y;
				intWidth = (int) gameObject.getWidth();
				intHeight = (int) gameObject.getHeight();
				interActMax = interAct1.size();
				break;
			case PC:
				pc = gameObject.toString();
				pcCoordX = (int) gameObject.getPosition().x;
				pcCoordY = (int) gameObject.getPosition().y;
				pcRotate = gameObject.getRotation();
				pcWidth = (int) gameObject.getWidth();
				pcHeight = (int) gameObject.getHeight();
				break;
			case DOOR:
				doorObjects.add(gameObject);
				Body door = new StaticBody(gameObject.toString(), new Box(gameObject.getWidth(), gameObject.getHeight()));
				door.adjustRotation(gameObject.getRotation());
				door.setPosition(gameObject.getStartPosition().x, gameObject.getStartPosition().y);
				world2D.add(door);
				doors.add(door);
				break;
			default:
				break;
			}
		}
		hero = world.getHero();
		hero2D = hero.getHeroBody();
		hero2D.setPosition(position.x, position.y);
		world2D.add(hero2D);

		if (omruWidth != 1)
		{
			try
			{
				omru = ImageIO.read(getClass().getResource(Messages.sourceURI + "portal160x165.png"));
				omruBG = ImageIO.read(getClass().getResource(Messages.sourceURI + "portal bg.png"));
				omruBG2 = ImageIO.read(getClass().getResource(Messages.sourceURI + "portal bg2.png"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (intWidth != 1)
		{
			KeyControlPanel p = new KeyControlPanel(world, frame);
			str = p.getKeyString(frame.getEnterKey());
		}
		if (pcWidth > 1)
		{
			try
			{
				pcImage = ImageIO.read(getClass().getResource(Messages.sourceURI + "pc.png"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void enter()
	{
		if (interActEnd && hero.isPaused())
		{
			if (interActX > -10)
				interActMoved++;
		}
		float x = hero2D.getPosition().getX();
		float y = hero2D.getPosition().getY();
		if (x > omruX - omruWidth / 2 && x < omruX + omruWidth / 2)
		{
			if (y > omruY - omruHeight / 2 && y < omruY + omruHeight / 2)
				world.setSaveSpot(new Vector2f(x, y));
		}
		if (x > intX - intWidth / 2 && x < intX + intWidth / 2)
		{
			if (y > intY - intHeight / 2 && y < intY + intHeight / 2)
			{
				if (!frame.activatedCheck())
					interAct = true;
			}
		}

		if (x > pcCoordX - pcWidth / 2 && x < pcCoordX + pcWidth / 2)
		{
			if (y > pcCoordY - pcHeight / 2 && y < pcCoordY + pcHeight / 2)
			{
				if (world.isClosed(pc))
					world.setClosed(pc, false);
				else
					world.setClosed(pc, true);
			}
		}
	}

	@Override
	public void start()
	{
		timer.start();
	}

	@Override
	public void stop()
	{
		timer.stop();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		g2.setColor(new Color(40, 40, 40));

		for (Body body : walls)
		{
			drawBox(g2, body);
		}

		for (Body body : doors)
		{
			fillBox(g2, body);
		}

		for (GameSpike spike : spikes)
		{
			spike.drawSpike(g2);
		}

		for (GameFallingObject object : blocks)
		{
			object.drawObject(g2);
		}

		for (GameMovingObject object : platforms)
		{
			object.drawObject(g2);
		}

		AffineTransform tr = new AffineTransform();
		tr.translate(omruX - omruWidth / 2, omruY - omruHeight / 2);
		tr.rotate(omruRotate, omruWidth / 2, omruHeight / 2);
		tr.scale(omruWidth / 160.0, omruHeight / 165.0);
		if (omruWidth != 1)
		{
			if (world.getSaveMap() == frame.getCurMap())
				g2.drawImage(omruBG, tr, null);
			else
				g2.drawImage(omruBG2, tr, null);

			if (world.isDead())
			{
				Image subImg = ((BufferedImage) omru).getSubimage(160 * imgX, 165 * imgY, 160, 165);
				g2.drawImage(subImg, tr, null);
			}
			if (gettingUp)
			{
				Image subImg = ((BufferedImage) omru).getSubimage(160 * imgX, 165 * imgY, 160, 165);
				g2.drawImage(subImg, tr, null);
			}
		}

		if (pcImage != null)
		{
			tr = new AffineTransform();
			tr.translate(pcCoordX - pcWidth / 2, pcCoordY - pcHeight / 2);
			tr.rotate(pcRotate, pcWidth / 2, pcHeight / 2);
			tr.scale(pcWidth / 80.0, pcHeight / 80.0);
			BufferedImage subImg = ((BufferedImage) pcImage).getSubimage(pcX * 80, 0, 80, 80);
			g2.drawImage(subImg, tr, null);
		}

		if (!world.isDead())
			hero.drawHero(g2);

		if (interAct)
		{
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(0, 0, 0, 180));
			g2.fill(new Rectangle2D.Double(interActX, 200, 920, 200));
			g2.setColor(new Color(200, 200, 200, 255));
			Font font = new Font("Monospaced", Font.BOLD, 30);
			g2.setFont(font);
			FontRenderContext frc = g2.getFontRenderContext();
			GlyphVector gv = font.createGlyphVector(frc, "");
			GlyphVector gv2 = font.createGlyphVector(frc, "");
			if (interActMoved <= interActMax && interActMoved > 0)
			{
				gv = font.createGlyphVector(frc, interAct1.get(interActMoved - 1));
				gv2 = font.createGlyphVector(frc, interAct2.get(interActMoved - 1));
			}
			Shape glyph = gv.getOutline(35, 310);
			Shape glyph2 = gv2.getOutline(35, 340);
			g2.setColor(new Color(220, 220, 220, 255));
			g2.fill(glyph);
			g2.fill(glyph2);
			g2.setColor(new Color(0, 0, 0, 255));
			g2.draw(glyph);
			g2.draw(glyph2);

			if (interActMoved <= interActMax && interActX <= 0)
			{
				g2.setColor(new Color(colorN, colorN, colorN, 100));
				g2.fill(new Ellipse2D.Double(875 - str.length() * 5, 365, 25 + str.length() * 5, 30));
				g2.setColor(Color.BLACK);
				g2.draw(new Ellipse2D.Double(875 - str.length() * 5, 365, 25 + str.length() * 5, 30));
				g2.setColor(new Color(30, 30, 30, 150));

				Font font2 = new Font("Serif", Font.BOLD, 14);
				g2.setFont(font2);
				g2.drawString(str, (int) (884 - str.length() * 6), 385);
			}
		}
		else if (!seen && show)
		{
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(0, 0, 0, 180));
			g2.fill(new Rectangle2D.Double(0, factY, 920, 85));
			g2.setColor(new Color(200, 200, 200, 255));
			Font font = new Font("Monospaced", Font.BOLD, 30);
			g2.setFont(font);
			FontRenderContext frc = g2.getFontRenderContext();
			GlyphVector gv = font.createGlyphVector(frc, line1);
			GlyphVector gv2 = font.createGlyphVector(frc, line2);
			Shape glyph = gv.getOutline(30, factY + 35);
			Shape glyph2 = gv2.getOutline(30, factY + 65);
			g2.setColor(new Color(220, 220, 220, 255));
			g2.fill(glyph);
			g2.fill(glyph2);
			g2.setColor(new Color(0, 0, 0, 255));
			g2.draw(glyph);
			g2.draw(glyph2);
		}
		g2.setStroke(new BasicStroke(2));
		g2.setColor(new Color(40, 40, 40));
		for (Line2D.Float line : objects.getLines())
			g2.draw(line);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (doors.size() > 0)
		{
			for (int i = 0; i < doors.size(); i++)
			{
				Body door = doors.get(i);
				EditorObjectInformation baseDoor = doorObjects.get(i);
				if (world.isClosedDoor(baseDoor.toString()))
				{
					int diffX = (int) (door.getPosition().getX() - baseDoor.getStartPosition().x);
					int diffY = (int) (door.getPosition().getY() - baseDoor.getStartPosition().y);
					if (diffX != 0)
					{
						if (diffX > 0)
							diffX--;
						else
							diffX++;
					}
					if (diffY != 0)
					{
						if (diffY > 0)
							diffY--;
						else
							diffY++;
					}
					door.setPosition(baseDoor.getStartPosition().x + diffX, baseDoor.getStartPosition().y + diffY);
				}
				else
				{
					int diffX = (int) (door.getPosition().getX() - baseDoor.getEndPosition().x);
					int diffY = (int) (door.getPosition().getY() - baseDoor.getEndPosition().y);
					if (diffX != 0)
					{
						if (diffX > 0)
							diffX--;
						else
							diffX++;
					}
					if (diffY != 0)
					{
						if (diffY > 0)
							diffY--;
						else
							diffY++;
					}
					door.setPosition(baseDoor.getEndPosition().x + diffX, baseDoor.getEndPosition().y + diffY);
				}
			}
		}

		if (pcImage != null)
		{
			pcCounter = (pcCounter + 1) % 5;
			if (pcCounter == 4 && world.isClosed(pc))
				pcX = (pcX + 1) % 4;
		}
		if (colorGain)
			colorN += 3;
		else
			colorN -= 5;
		if (colorN >= 200)
			colorGain = false;
		else if (colorN <= 30)
			colorGain = true;

		if (hero2D.getPosition().getY() > 280)
			factY = 0;
		else
			factY = 520;
		if (world.isDead() || gettingUp)
			deadCounter++;
		if (running && !world.isDead())
		{
			for (int i = 0; i < 4; i++)
			{
				world2D.step();
				validate();
			}
		}
		if (world.isDead())
		{
			if (deadCounter % 4 == 0)
			{
				imgX = (imgX + 1) % 5;
				if (imgX == 0)
					imgY = (imgY + 1) % 3;
			}
			if (imgY == 2 && imgX == 4)
			{
				deadCounter = 0;
				gettingUp = true;
				imgX = 0;
			}
		}
		if (gettingUp)
		{
			imgY = 3;
			if (deadCounter % 20 == 19)
				imgX++;
			if (imgX == 2)
				world.setDead(false);
			if (imgX == 4)
				gettingUp = false;
		}
		if (!interAct)
		{
			setRunning(true);
		}
		else
		{
			setRunning(false);
			hero.setPaused(true);
			if (interActX > 0)
			{
				interActX -= 25;
				interActEnd = true;
			}
			if (interActMoved > 0)
				interActEnd = true;
			if (interActMoved == interActMax + 1)
			{
				interActX -= 25;
				if (interActX < -930)
				{
					interAct = false;
					hero.setPaused(false);
				}
			}
		}

		update();
		for (GameMovingObject object : platforms)
			object.update();

		int x = (int) hero2D.getPosition().getX();
		int y = (int) hero2D.getPosition().getY();
		if (y < 0)
			frame.loadMap(frame.getCurLvl(), objects.getNextMapInt().get(0), new Vector2f(x, 590));
		if (x > 900)
			frame.loadMap(frame.getCurLvl(), objects.getNextMapInt().get(1), new Vector2f(10, y));
		if (y > 600)
			frame.loadMap(frame.getCurLvl(), objects.getNextMapInt().get(2), new Vector2f(x, 10));
		if (x < 0)
			frame.loadMap(frame.getCurLvl(), objects.getNextMapInt().get(3), new Vector2f(890, y));

		if (objects.getNextLevelStep() != null)
		{
			if (x > objects.getNextLevelStep().x && x < objects.getNextLevelStep().x + objects.getNextLevelStep().width)
			{
				if (y > objects.getNextLevelStep().y && y < objects.getNextLevelStep().y + objects.getNextLevelStep().height)
					frame.loadMap(frame.getCurLvl() + 1, 1, new Vector2f(450, 300));
			}
		}
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public boolean isShow()
	{
		return show;
	}

	public void setShow(boolean show)
	{
		this.show = show;
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

	public boolean isSeen()
	{
		return seen;
	}

	public void setSeen(boolean seen)
	{
		this.seen = seen;
	}
}
