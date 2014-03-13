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
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.Timer;

public class KeyControlPanel extends GamePanel implements ActionListener
{
	private static final long	serialVersionUID	= 1L;
	private String[]			s					= { "Game controls:", "+ UP/DOWN", "move left", "move right", "switch gravity", "interact (enter)", "Back",
			"+ ENTER"								};
	private int					select, maxSelect;
	private ArrayList<Shape>	shapes;
	private Timer				timer;
	private boolean				prevKeybinding;

	public KeyControlPanel(GameWorld world, GameFrame frame)
	{
		super(world, frame);
		select = 0;
		maxSelect = 5;
		shapes = new ArrayList<Shape>();
		Rectangle2D title = new Rectangle2D.Double(345, 100, 210, 40);
		shapes.add(title);
		Rectangle2D option1 = new Rectangle2D.Double(230, 200, 440, 40);
		shapes.add(option1);
		Rectangle2D option2 = new Rectangle2D.Double(230, 270, 440, 40);
		shapes.add(option2);
		Rectangle2D option3 = new Rectangle2D.Double(230, 340, 440, 40);
		shapes.add(option3);
		Rectangle2D option4 = new Rectangle2D.Double(230, 410, 440, 40);
		shapes.add(option4);
		Rectangle2D back = new Rectangle2D.Double(345, 480, 210, 40);
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
			if (select + 1 == id)
				p = new GradientPaint(300, 0, new Color(0xff, 0x5f, 0), 600, 0, new Color(0, 0x5f, 0xff));
			else
				p = new GradientPaint(200, 0, Color.LIGHT_GRAY, 700, 0, Color.DARK_GRAY);
			g2.setPaint(p);
			g2.fill(shapes.get(id));
			if (id > 0)
			{
				g2.setColor(Color.BLACK);
				g2.draw(shapes.get(id));
			}
		}
		Font font = new Font("Serif", Font.BOLD, 30);
		Font font2 = new Font("Serif", Font.BOLD, 20);
		g2.setFont(font);
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, s[0]);
		Shape glyph = gv.getOutline(350, 130);
		g2.draw(glyph);
		String[] str = getKeyStrings();
		for (int i = 0; i < 4; i++)
		{
			gv = font.createGlyphVector(frc, s[i + 2]);
			GlyphVector gv2 = font.createGlyphVector(frc, str[i]);
			glyph = gv.getOutline(250, 230 + i * 70);
			Shape glyph2 = gv2.getOutline(500, 230 + i * 70);
			g2.draw(glyph);
			g2.draw(glyph2);
			g2.setFont(font2);
			if (select == i)
			{
				if (frame.isKeybinding())
					gv = font2.createGlyphVector(frc, "please select a key");
				else
					gv = font2.createGlyphVector(frc, "to change press " + getKeyString(frame.getEnterKey()));
				glyph = gv.getOutline(230, 257 + i * 70);
				g2.draw(glyph);
			}
			if (select == 2)
			{
				gv = font2.createGlyphVector(frc, s[1]);
				glyph = gv.getOutline(555, 397);
				g2.draw(glyph);
			}
			if (select == 3)
			{
				gv = font2.createGlyphVector(frc, s[7]);
				glyph = gv.getOutline(585, 467);
				g2.draw(glyph);
			}
			g2.setFont(font);
		}
		gv = font.createGlyphVector(frc, s[6]);
		glyph = gv.getOutline(400, 510);
		g2.draw(glyph);
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
		select = (select + maxSelect - 1) % maxSelect;
	}

	@Override
	public void down()
	{
		select = (select + 1) % maxSelect;
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
		switch (select)
		{
		case 0:
			frame.setSelectedKey(1);
			frame.setKeybinding(true);
			break;
		case 1:
			frame.setSelectedKey(2);
			frame.setKeybinding(true);
			break;
		case 2:
			frame.setSelectedKey(3);
			frame.setKeybinding(true);
			break;
		case 3:
			frame.setSelectedKey(4);
			frame.setKeybinding(true);
			break;
		case 4:
			frame.loadMap(new GameMenuPanel(world, frame));
			break;
		}
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
		if (frame.isKeybinding() == false && prevKeybinding == true)
		{
			select++;
		}
		repaint();
		if (prevKeybinding != frame.isKeybinding())
			prevKeybinding = frame.isKeybinding();
	}

	public String getKeyString(int keyCode)
	{
		String str = "";
		if (keyCode == KeyEvent.VK_LEFT)
			str = "LEFT";
		else if (keyCode == KeyEvent.VK_RIGHT)
			str = "RIGHT";
		else if (keyCode == KeyEvent.VK_UP)
			str = "UP";
		else if (keyCode == KeyEvent.VK_DOWN)
			str = "DOWN";
		else if (keyCode == KeyEvent.VK_SPACE)
			str = "SPACEBAR";
		else if (keyCode == KeyEvent.VK_ENTER)
			str = "ENTER";
		else
		{
			char ch = (char) KeyEvent.getExtendedKeyCodeForChar(keyCode);
			str += ch;
		}
		return str;
	}

	public String[] getKeyStrings()
	{
		String left = getKeyString(frame.getLeftKey());
		String right = getKeyString(frame.getRightKey());
		String flip = getKeyString(frame.getSwitchKey());
		String enter = getKeyString(frame.getEnterKey());
		String[] str = { left, "" + right, "" + flip, "" + enter };
		return str;
	}
}
