package game.world.model.specialMaps;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GameMap;
import game.world.model.Messages;
import game.world.model.menu.KeyControlPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
	private boolean				tutorial			= false;
	private boolean				tutorialEnd			= false;
	private boolean				tutorialMoved		= false;
	private int					tutorialX			= 920;
	private boolean				colorGain			= true;
	private int					colorN				= 30;
	private String				s;

	public L1M03(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
	{
		super(world, frame, objects, position);
//		doorY = 380;
//		door = new StaticBody("extra door", new Box(30, 280));
//		door.setPosition(800, doorY);
//		world2D.add(door);
//		try
//		{
//			gSuit = ImageIO.read(L1M03.class.getResource(Messages.sourceURI + "gravity suit200x200.png"));
//			gChange = ImageIO.read(L1M03.class.getResource(Messages.sourceURI + "change sprite300x200.png"));
//			gCatch = ImageIO.read(L1M03.class.getResource(Messages.sourceURI + "catching200x200.png"));
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		KeyControlPanel p = new KeyControlPanel(world, frame);
//		if(interAct1 != null && interAct1.size() > 2)
//		{
//			str = p.getKeyString(frame.getSwitchKey());
//			str = "Press " + str + " to use its functionality.";
//			interAct1.add(3, str);
//			interAct2.add(3, "");
//		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		KeyControlPanel p = new KeyControlPanel(world, frame);
		String str = p.getKeyString(frame.getSwitchKey());
		s = "Change the gravity by pressing " + str + ".";
		if (tutorial)
		{
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(0, 0, 0, 180));
			g2.fill(new Rectangle2D.Double(tutorialX, 200, 920, 200));
			g2.setColor(new Color(200, 200, 200, 255));
			Font font = new Font("Serif", Font.BOLD, 40);
			g2.setFont(font);
			FontRenderContext frc = g2.getFontRenderContext();
			GlyphVector gv = null;
			gv = font.createGlyphVector(frc, s);
			Shape glyph = gv.getOutline(tutorialX + 30, 315);
			g2.setColor(new Color(220, 220, 220, 255));
			g2.fill(glyph);
			g2.setColor(new Color(0, 0, 0, 255));
			g2.draw(glyph);
		}
		if (!tutorialMoved && tutorialX <= 0)
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

	@Override
	public void enter()
	{
		if (tutorialEnd && tutorialX < 20)
		{
			tutorialMoved = true;
		}
		super.enter();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (colorGain)
			colorN += 3;
		else
			colorN -= 5;
		if (colorN >= 200)
			colorGain = false;
		else if (colorN <= 30)
			colorGain = true;
		if (!seen)
		{
			tutorial = true;
			hero.setPaused(true);
			if (tutorialX > 0)
			{
				tutorialX -= 15;
				tutorialEnd = true;
			}
			if (tutorialMoved)
			{
				tutorialX -= 15;
				if (tutorialX < -930)
				{
					tutorial = false;
					hero.setPaused(false);
				}
			}
		}
		if (!tutorial)
		{
			setRunning(true);
		}
		else
		{
			setRunning(false);
		}
		super.actionPerformed(arg0);
	}
}
