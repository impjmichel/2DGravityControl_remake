package game.world.model.menu;

import game.GameFrame;
import game.world.GameWorld;
import game.world.model.GamePanel;

import java.awt.Color;
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
import java.util.List;

import javax.swing.Timer;

import levelEditor.model.HiScoreEntry;

public class HiScorePanel extends GamePanel implements ActionListener
{
	private static final long	serialVersionUID	= 1L;
	private String[]			s					= { "Back", "Name", "  Time", "Deaths" };
	private ArrayList<Shape>	shapes;
	private Timer				timer;

	public HiScorePanel(GameWorld world, GameFrame frame)
	{
		super(world, frame);
		shapes = new ArrayList<Shape>();
		Rectangle2D back = new Rectangle2D.Double(345, 530, 210, 40);
		shapes.add(back);
		timer = new Timer(1000 / 60, this);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int id = 0; id < shapes.size(); id++)
		{
			GradientPaint p = null;
			p = new GradientPaint(350, 0, new Color(0xff, 0x5f, 0), 550, 0, new Color(0, 0x5f, 0xff));
			g2.setPaint(p);
			g2.fill(shapes.get(id));
			g2.setColor(Color.BLACK);
			g2.draw(shapes.get(id));
		}
		Font font = new Font("Serif", Font.BOLD, 30);
		g2.setFont(font);
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, s[0]);
		Shape glyph = gv.getOutline(370, 560);
		g2.draw(glyph);

		gv = font.createGlyphVector(frc, s[1]);
		glyph = gv.getOutline(200, 70);
		g2.draw(glyph);
		gv = font.createGlyphVector(frc, s[2]);
		glyph = gv.getOutline(400, 70);
		g2.draw(glyph);
		gv = font.createGlyphVector(frc, s[3]);
		glyph = gv.getOutline(600, 70);
		g2.draw(glyph);
		List<HiScoreEntry> score = frame.getHiScore();
		for (int index = 0; index < score.size(); index++)
		{
			font = new Font("Serif", Font.BOLD, 20);
			HiScoreEntry entry = score.get(index);
			gv = font.createGlyphVector(frc, entry.getName());
			glyph = gv.getOutline(200, 100 + 30 * index);
			g2.draw(glyph);
			gv = font.createGlyphVector(frc, world.slashPlayed(entry.getTime()));
			glyph = gv.getOutline(400, 100 + 30 * index);
			g2.draw(glyph);
			gv = font.createGlyphVector(frc, "" + entry.getDeaths());
			glyph = gv.getOutline(600, 100 + 30 * index);
			g2.draw(glyph);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		update();
	}

	@Override
	public void flip()
	{
	}

	@Override
	public void up()
	{
	}

	@Override
	public void down()
	{
	}

	@Override
	public void left()
	{
	}

	@Override
	public void right()
	{
	}

	@Override
	public void enter()
	{
		frame.loadMap(new GameMenuPanel(world, frame));
	}

	public void escape()
	{
		frame.loadMap(new GameMenuPanel(world, frame));
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

	public void update()
	{
		repaint();
	}
}
