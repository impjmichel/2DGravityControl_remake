package game.world.model.specialMaps;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GameMap;
import game.world.model.Messages;
import game.world.model.menu.KeyControlPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import levelEditor.model.EditorMapInformation;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.shapes.Box;

public class L1M03 extends GameMap
{
	private static final long	serialVersionUID	= 1L;
	private Body				door;
	private int					doorY;
	private Image				gSuit, gChange, gCatch;
	private int					gsX, gsY, gcX, gcY, catchX, catchY, frameCounter;
	private boolean				flying, changing;
	private String				str;

	public L1M03(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
	{
		super(world, frame, objects, position);
		doorY = 380;
		door = new StaticBody("extra door", new Box(30, 280));
		door.setPosition(800, doorY);
		world2D.add(door);
		try
		{
			gSuit = ImageIO.read(L1M03.class.getResource(Messages.sourceURI + "gravity suit200x200.png"));
			gChange = ImageIO.read(L1M03.class.getResource(Messages.sourceURI + "change sprite300x200.png"));
			gCatch = ImageIO.read(L1M03.class.getResource(Messages.sourceURI + "catching200x200.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		KeyControlPanel p = new KeyControlPanel(world, frame);
		if(interAct1 != null && interAct1.size() > 2)
		{
			str = p.getKeyString(frame.getSwitchKey());
			str = "Press " + str + " to use its functionality.";
			interAct1.add(3, str);
			interAct2.add(3, "");
		}
	}

	public void paintComponent(Graphics g)
	{
		world.setDead(true);
		super.paintComponent(g);
		world.setDead(false);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		fillBox(g2, door);
		if (!flying && !changing && !world.isGravitySuit())
		{
			BufferedImage subImg = ((BufferedImage) gSuit).getSubimage(gsX, gsY, 200, 200);
			g2.drawImage(subImg, 350, 320, 200, 200, null);
		}
		
		hero.drawHero(g2);
		if (flying && !changing && !world.isGravitySuit())
		{
			BufferedImage subImg = ((BufferedImage) gChange).getSubimage(gcX, gcY, 300, 200);
			g2.drawImage(subImg, (int) hero2D.getPosition().getX() - 150, 320, 300, 200, null);
		}
		if (changing && !world.isGravitySuit())
		{
			BufferedImage subImg = ((BufferedImage) gCatch).getSubimage(catchX, catchY, 200, 200);
			g2.drawImage(subImg, (int) hero2D.getPosition().getX() - 100, 320, 200, 200, null);
		}
	}

	@Override
	public void enter()
	{
		super.enter();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		world.setDead(false);
		super.actionPerformed(arg0);
		if (!world.isClosedL1M3())
		{
			if (doorY > 220)
			{
				doorY--;
				door.setPosition(800, doorY);
			}
		}

		if (interActMoved == interActMax)
		{
			world.setGravitySuit(true);
			world2D.remove(hero2D);
			hero.switchBody();
			hero2D = hero.getHeroBody();
			world2D.add(hero2D);
			world.setClosedL1M3(false);
		}

		if (interAct && !world.isGravitySuit())
			frameCounter++;

		if (gsX == 800 && gsY == 200)
		{
			flying = true;
			frameCounter = 0;
			gsX = 0;
		}

		if (catchX == 800 && catchY == 200)
			catchX = 0;

		if (frameCounter % 15 == 14)
		{
			if (!flying && !changing)
			{
				frameCounter += 2;
				gsX = (gsX + 200) % 1000;
				if (gsX == 0)
					gsY = 200;
			}
			else if (flying && !changing)
			{
				frameCounter += 9;
				gcY = (gcY + 200) % 1000;
				if (gcY == 0)
					gcX = (gcX + 300) % 900;
			}
			else if (changing)
			{
				catchX = (catchX + 200) % 1000;
				if (catchX == 0)
					catchY = 200;
			}
		}
		if (interActMoved == 8)
		{
			changing = true;
			frameCounter = 0;
		}
		
	}
}
