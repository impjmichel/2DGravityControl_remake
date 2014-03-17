package game.world.model.menu;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GamePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.Timer;

import levelEditor.EditorControl;

public class GameMenuPanel extends GamePanel implements ActionListener
{
	private static final long	serialVersionUID	= 1L;
	private ArrayList<Shape>	shapes;
	private static String[]		s					= { "Start", "Controls", "Audio", "HiScore", "Exit" };
	private static String		editor				= "Level Editor";
	private int					select, maxSelect;
	private Timer				timer;

	public GameMenuPanel(GameWorld world, GameFrame frame)
	{
		super(world, frame);
		setPreferredSize(new Dimension((int)(890 * world.getZoomFactor()), (int)(590*world.getZoomFactor())));
		select = 0;
		maxSelect = 5;
		if (frame.isEditorEnabled())
			maxSelect++;
		shapes = new ArrayList<Shape>();
		for (int i = 0; i < 5; i++)
		{
			Rectangle2D option = new Rectangle2D.Double(0, 0, 300, 60);
			shapes.add(option);
		}
		timer = new Timer(1000 / 60, this);
		timer.start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(world.getZoomFactor(), world.getZoomFactor());
		g2.translate(300, 150);
		Font font = new Font("Serif", Font.BOLD, 60);
		g2.setFont(font);
		FontRenderContext frc = g2.getFontRenderContext();
		for (int id = 0; id < s.length; id++)
		{
			if (select == id)
			{
				GradientPaint p = new GradientPaint(0, 0, new Color(0xff, 0x5f, 0), 300, 0, new Color(0, 0x5f, 0xff));
				g2.setPaint(p);
			}
			else
			{
				GradientPaint p = new GradientPaint(0, 0, Color.LIGHT_GRAY, 300, 0, Color.DARK_GRAY);
				g2.setPaint(p);
			}
			g2.fill(shapes.get(id));
			g2.setColor(Color.BLACK);
			g2.draw(shapes.get(id));
			g2.translate(20, 50);
			GlyphVector gv = null;
			if (id == 0 && frame.previousMap != null)
				gv = font.createGlyphVector(frc, "Continue");
			else
				gv = font.createGlyphVector(frc, s[id]);
			Shape glyph = gv.getOutline(0, 0);

			g2.draw(glyph);
			g2.translate(-20, 30);
		}
		if (frame.isEditorEnabled())
		{
			Rectangle2D option = new Rectangle2D.Double(0, -480, 300, 60);
			if (select == 5)
			{
				GradientPaint p = new GradientPaint(0, 0, new Color(0xff, 0x5f, 0), 300, 0, new Color(0, 0x5f, 0xff));
				g2.setPaint(p);
			}
			else
			{
				GradientPaint p = new GradientPaint(0, 0, Color.LIGHT_GRAY, 300, 0, Color.DARK_GRAY);
				g2.setPaint(p);
			}
			g2.fill(option);
			g2.setColor(Color.BLACK);
			g2.draw(option);
			font = new Font("Serif", Font.BOLD, 50);
			GlyphVector gv2 = font.createGlyphVector(frc, editor);
			Shape glyph = gv2.getOutline(20, -435);
			g2.draw(glyph);
		}
	}

	public void flip()
	{
	}

	public void up()
	{
		select = (select + maxSelect - 1) % maxSelect;
	}

	public void down()
	{
		select = (select + 1) % maxSelect;
	}

	public void enter()
	{
		switch (select)
		{
		case 0:
			world.time.start();
			frame.getPreviousMap();
			break;
		case 1:
			frame.loadMap(new KeyControlPanel(world, frame));
			break;
		case 2:
			frame.loadMap(new AudioControlPanel(world, frame));
			break;
		case 3:
			frame.loadMap(new HiScorePanel(world, frame));
			break;
		case 4:
			System.exit(0);
			break;
		case 5:
			new EditorControl(frame.getLevels(), frame.getHiScore(), frame.isEditorEnabled());
			break;
		}
	}

	public void escape()
	{
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		update();
	}

	public void update()
	{
		maxSelect = 5;
		if (frame.isEditorEnabled())
			maxSelect++;
		repaint();
	}

	@Override
	public void left()
	{
	}

	@Override
	public void right()
	{
	}

	public void start()
	{
		timer.start();
	}

	public void stop()
	{
		timer.stop();
	}
}
