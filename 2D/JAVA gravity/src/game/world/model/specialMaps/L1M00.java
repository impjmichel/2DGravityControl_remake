package game.world.model.specialMaps;

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
import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GameMap;
import game.world.model.menu.KeyControlPanel;

public class L1M00 extends GameMap
{
	private static final long	serialVersionUID	= 1L;
	private boolean				tutorial			= false;
	private boolean				tutorialEnd			= false;
	private boolean				tutorialMoved		= false;
	private int					tutorialX			= 920;
	private boolean				colorGain			= true;
	private int					colorN				= 30;
	private String[]			s;

	public L1M00(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
	{
		super(world, frame, objects, position);
		KeyControlPanel p = new KeyControlPanel(world, frame);
		String str = p.getKeyString(frame.getLeftKey());
		String str2 = p.getKeyString(frame.getRightKey());
		String str3 = p.getKeyString(frame.getEnterKey());
		String str4 = p.getKeyString(frame.getSwitchKey());
		s = new String[] { str, str2, str3, str4 };
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.BLACK);
		if (tutorial)
		{
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(0, 0, 0, 180));
			g2.fill(new Rectangle2D.Double(tutorialX, 200, 920, 200));
			g2.setColor(new Color(200, 200, 200, 255));
			Font font = new Font("Serif", Font.BOLD, 50);
			g2.setFont(font);
			FontRenderContext frc = g2.getFontRenderContext();
			GlyphVector gv = font.createGlyphVector(frc, s[0]);
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
		}
	}
	
	@Override
	public void left()
	{
		super.left();
		if (tutorialEnd && tutorialX < 20)
			tutorialMoved = true;
	}

	@Override
	public void right()
	{
		super.right();
		if (tutorialEnd && tutorialX < 20)
			tutorialMoved = true;
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
			tutorial = true;
			if (tutorialX > 0)
			{
				tutorialX -= 15;
				tutorialEnd = true;
			}
			if (tutorialMoved)
			{
				tutorialX -= 15;
				if (tutorialX < -930)
					tutorial = false;
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
