package game.world.model.specialMaps;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GameMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import levelEditor.model.EditorMapInformation;
import net.phys2d.math.Vector2f;

public class L1M00 extends GameMap
{
	private static final long	serialVersionUID	= 1L;
	private int					tutorial			= 0;
	private boolean				tutorialEnd			= false;
	private boolean				tutorialMoved		= false;
	private int					tutorialX			= 920;
	private boolean				colorGain			= true;
	private int					colorN				= 30;
	private String[]			s					= { "", "Welcome back.", "", 
														"You might wonder", "   'why is it so dark?'",
														"I've been saving energy lately.","You know...",
														"For the sake of resurrecting you again.", "And again, and again, and again",
														"It has been quite some time since", "our last meeting.", 
														"Summarizing your life thus far seems", "the right thing to do.",
														"", "   *ahemm*",
														"You learned to walk.","", 
														"You learned to interact with objects.","", 
														"You received the gravity suit.","", 
														"You failed.","", 
														"The end.","", 
														"The end.", "                        -your life-"};

	public L1M00(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
	{
		super(world, frame, objects, position);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 255));
		g2.fill(new Rectangle2D.Double(0, 0, 920, 600));
		if (tutorial > 0)
		{
			if (s.length > tutorial)
			{
				g2.setStroke(new BasicStroke(1));
				g2.setColor(new Color(0, 0, 0, 180));
				g2.fill(new Rectangle2D.Double(tutorialX, 200, 920, 200));
				g2.setColor(new Color(200, 200, 200, 255));
				Font font = new Font("Serif", Font.BOLD, 50);
				g2.setFont(font);
				FontRenderContext frc = g2.getFontRenderContext();
				GlyphVector gv = font.createGlyphVector(frc, s[tutorial]);
				Shape glyph = gv.getOutline(tutorialX + 30, 280);
				GlyphVector gv2 = font.createGlyphVector(frc, s[tutorial+1]);
				Shape glyph2 = gv2.getOutline(tutorialX + 30, 340);
				g2.setColor(new Color(220, 220, 220, 255));
				g2.fill(glyph);
				g2.fill(glyph2);
				g2.setColor(new Color(0, 0, 0, 255));
				g2.draw(glyph);
				g2.draw(glyph2);
			}
		}
		if (!tutorialMoved && tutorialX <= 0)
		{
			g2.setColor(new Color(colorN, colorN, colorN, 100));
			GeneralPath triangle = new GeneralPath();
			triangle.append(new Line2D.Double(850, 365, 825, 380), true);
			triangle.append(new Line2D.Double(825, 380, 850, 395), true);
			triangle.append(new Line2D.Double(850, 395, 850, 365), true);
			g2.fill(triangle);
			g2.setColor(Color.BLACK);
			g2.draw(triangle);
			g2.setColor(new Color(colorN, colorN, colorN, 100));
			triangle = new GeneralPath();
			triangle.append(new Line2D.Double(870, 365, 895, 380), true);
			triangle.append(new Line2D.Double(895, 380, 870, 395), true);
			triangle.append(new Line2D.Double(870, 395, 870, 365), true);
			g2.fill(triangle);
			g2.setColor(Color.BLACK);
			g2.draw(triangle);
			// TODO: draw a enter thingy.
		}
	}

	@Override
	public void enter()
	{
		if (tutorialEnd && tutorialX < 20)
		{
			tutorialMoved = true;
		}
		else if (tutorialX < 20)
		{
			tutorial += 2;
		}
		super.enter();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (colorGain)
			colorN += 5;
		else
			colorN -= 5;
		if (colorN >= 200)
			colorGain = false;
		else if (colorN <= 30)
			colorGain = true;

		if (!seen)
		{
			if (tutorial == 0)
				tutorial = 1;
			if (tutorialX > 0)
			{
				tutorialX -= 15;
			}
			if (tutorialMoved)
			{
				tutorialX -= 15;
				if (tutorialX < -930)
					tutorial = 255;
			}
		}
		if (tutorial >= s.length -1)
		{
			tutorialEnd = true;
		}
		if (tutorial == 255)
		{
			setRunning(true);
			world.setGravitySuit(true);
			world2D.remove(hero2D);
			hero.switchBody();
			hero2D = hero.getHeroBody();
			world2D.add(hero2D);
			frame.loadMap(1, 1, new Vector2f(400f, 320));
		}
		else
		{
			setRunning(false);
		}
		super.actionPerformed(arg0);
	}
}
