package game.world.model.specialMaps;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GameMap;
import game.world.model.menu.KeyControlPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import levelEditor.model.EditorMapInformation;
import net.phys2d.math.Vector2f;

public class L1M04 extends GameMap
{
	private static final long	serialVersionUID	= 1L;
	private boolean				tutorial			= false;
	private boolean				tutorialEnd			= false;
	private boolean				tutorialMoved		= false;
	private int					tutorialX			= 920;
	private boolean				colorGain			= true;
	private int					colorN				= 30;
	private String[]			s 					= {"That counter doesn't seem quite fair...", "Wait, let me reset it."};

	public L1M04(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
	{
		super(world, frame, objects, position);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		KeyControlPanel p = new KeyControlPanel(world, frame);
		String str = p.getKeyString(frame.getEnterKey());
		
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
			gv = font.createGlyphVector(frc, s[0]);
			Shape glyph = gv.getOutline(tutorialX + 30, 280);
			GlyphVector gv2 = font.createGlyphVector(frc, s[1]);
			Shape glyph2 = gv2.getOutline(tutorialX + 30, 340);
			g2.setColor(new Color(220, 220, 220, 255));
			g2.fill(glyph);
			g2.fill(glyph2);
			g2.setColor(new Color(0, 0, 0, 255));
			g2.draw(glyph);
			g2.draw(glyph2);
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
			world.setTimePlayed(0);
			world.setDeathCount(0);
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
