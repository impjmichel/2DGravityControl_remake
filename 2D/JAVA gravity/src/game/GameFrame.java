package game;

import game.world.GameWorld;
import game.world.model.GameMap;
import game.world.model.GamePanel;
import game.world.model.Messages;
import game.world.model.menu.AudioControlPanel;
import game.world.model.menu.GameMenuPanel;
import game.world.model.menu.HiScorePanel;
import game.world.model.menu.KeyControlPanel;
import game.world.model.specialMaps.L1M01;
import game.world.model.specialMaps.L1M02;
import game.world.model.specialMaps.L1M03;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import levelEditor.model.EditorLevelInformation;
import levelEditor.model.EditorMapInformation;
import levelEditor.model.HiScoreEntry;
import net.phys2d.math.Vector2f;

public class GameFrame extends JFrame implements KeyListener
{
	private static final long						serialVersionUID		= 1L;
	private GamePanel								content;
	public GamePanel								previousMap;
	private int										leftKey, rightKey, switchKey, enterKey, selectedKey;
	private boolean									keybinding				= false;
	private GameWorld								world;
	private Map<String, EditorLevelInformation>		levels;
	private List<HiScoreEntry>						hiScore;
	private int										curLvl, curMap;
	private Map<String, Class<? extends GameMap>>	specialLevels;
	private Set<String>								dejavuSet;
	private Set<String>								activatedSet;
	private boolean									extraFact				= false;
	private boolean									tenDeathsSeen			= false;
	private boolean									twentyfiveDeathsSeen	= false;
	private boolean									thirtyfiveDeathsSeen	= false;
	private boolean									editorEnabled;
	private int										tutorial				= 0;

	public GameFrame(GameWorld world, Map<String, EditorLevelInformation> levels, List<HiScoreEntry> hiScore,
			Map<String, Class<? extends GameMap>> specialLevels, boolean editorEnabled)
	{
		super("Gravity");
		this.world = world;
		this.levels = levels;
		this.hiScore = hiScore;
		this.specialLevels = specialLevels;
		this.editorEnabled = editorEnabled;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		content = new GameMenuPanel(this.world, this);
		setFocusable(true);
		addKeyListener(this);
		requestFocus();
		setContentPane(content);
		leftKey = KeyEvent.VK_LEFT;
		rightKey = KeyEvent.VK_RIGHT;
		switchKey = KeyEvent.VK_Z;
		enterKey = KeyEvent.VK_SPACE;
		dejavuSet = new TreeSet<String>();
		activatedSet = new TreeSet<String>();
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		if (!keybinding)
		{
			if (ke.getKeyCode() == switchKey)
				content.flip();
			else if (ke.getKeyCode() == leftKey || ke.getKeyCode() == KeyEvent.VK_LEFT)
				content.left();
			else if (ke.getKeyCode() == rightKey || ke.getKeyCode() == KeyEvent.VK_RIGHT)
				content.right();
			else if (ke.getKeyCode() == KeyEvent.VK_UP)
				content.up();
			else if (ke.getKeyCode() == KeyEvent.VK_DOWN)
				content.down();
		}
		else
			changeKey(ke.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent ke)
	{
		if (ke.getKeyCode() == KeyEvent.VK_ENTER)
			content.enter();
		else if (ke.getKeyCode() == enterKey)
			content.enter();
		else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
			content.escape();
		else if (ke.getKeyCode() == leftKey || ke.getKeyCode() == rightKey || ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_RIGHT)
			content.stopMoving();
	}

	@Override
	public void keyTyped(KeyEvent ke)
	{
	}

	public void loadMap(GamePanel map)
	{
		if (!content.getClass().equals(GameMenuPanel.class) && !content.getClass().equals(KeyControlPanel.class) &&
				!content.getClass().equals(AudioControlPanel.class) && !content.getClass().equals(HiScorePanel.class))
			previousMap = content;
		content.stop();
		content = map;
		setContentPane(content);
		content.start();
		validate();
		repaint();
	}

	public void loadMap(int level, int map, Vector2f position)
	{
		if (level == 0 || map == 0)
		{
			world.killHero();
			return;
		}
		EditorLevelInformation currentlevel = levels.get("level " + level);
		EditorMapInformation objectList = currentlevel.getMaps().get("map " + map);
		curLvl = level;
		curMap = map;
		GameMap panel = new GameMap(world, this, objectList, position);
		try
		{
			Class<? extends GameMap> cl = specialLevels.get("" + level + "-" + map);
			if (cl == specialLevels.get("1-1"))
				panel = new L1M01(world, this, objectList, position);
			if (cl == specialLevels.get("1-2"))
				panel = new L1M02(world, this, objectList, position);
			if (cl == specialLevels.get("1-3"))
				panel = new L1M03(world, this, objectList, position);
			// if (cl == specialLevels.get("1-20"))
			// panel = new L1M20(world, this, objectList, position);
			// TODO: add special map here
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (world.getDeathCount() >= 10 && !tenDeathsSeen)
			{
				panel.setSeen(false);
				panel.setShow(true);
				panel.setLine1(Messages.tenthDeath);
				panel.setLine2(Messages.tenthDeath2);
				tenDeathsSeen = true;
			}
			else if (world.getDeathCount() >= 25 && !twentyfiveDeathsSeen)
			{
				panel.setSeen(false);
				panel.setShow(true);
				panel.setLine1(Messages.twentyfifthDeath);
				panel.setLine2(Messages.twentyfifthDeath2);
				twentyfiveDeathsSeen = true;
			}
			else if (world.getDeathCount() >= 35 && !thirtyfiveDeathsSeen)
			{
				panel.setSeen(false);
				panel.setShow(true);
				panel.setLine1(Messages.thirtyfifthDeath);
				panel.setLine2(Messages.thirtyfifthDeath2);
				thirtyfiveDeathsSeen = true;
			}
			if (tutorial == 0)
			{
				panel.setSeen(false);
				panel.setShow(false);
				tutorial++;
			}
			else if (tutorial == 1)
			{
				panel.setSeen(false);
				panel.setShow(false);
				tutorial++;
			}
			world.setCurrentLevel(currentlevel);
			loadMap(panel);
		}

	}

	public List<HiScoreEntry> getHiScore()
	{
		return hiScore;
	}

	public void setHiScore(List<HiScoreEntry> hiScore)
	{
		this.hiScore = hiScore;
	}

	public int getLeftKey()
	{
		return leftKey;
	}

	public void setLeftKey(int leftKey)
	{
		this.leftKey = leftKey;
	}

	public int getRightKey()
	{
		return rightKey;
	}

	public void setRightKey(int rightKey)
	{
		this.rightKey = rightKey;
	}

	public int getSwitchKey()
	{
		return switchKey;
	}

	public void setSwitchKey(int switchKey)
	{
		this.switchKey = switchKey;
	}

	public int getEnterKey()
	{
		return enterKey;
	}

	public void setEnterKey(int enterKey)
	{
		this.enterKey = enterKey;
	}

	public boolean isKeybinding()
	{
		return keybinding;
	}

	public int getSelectedKey()
	{
		return selectedKey;
	}

	public void setSelectedKey(int selectedKey)
	{
		this.selectedKey = selectedKey;
	}

	public void setKeybinding(boolean keybinding)
	{
		this.keybinding = keybinding;
	}

	public int getCurLvl()
	{
		return curLvl;
	}

	public int getCurMap()
	{
		return curMap;
	}

	public boolean isEditorEnabled()
	{
		return editorEnabled;
	}

	public void setEditorEnabled(boolean editorEnabled)
	{
		this.editorEnabled = editorEnabled;
	}

	public void changeKey(int keyCode)
	{
		if (keyCode != KeyEvent.VK_ESCAPE)
		{
			switch (selectedKey)
			{
			case 0: // do nothing
				break;
			case 1: // left
				if (keyCode != KeyEvent.VK_RIGHT && keyCode != KeyEvent.VK_DOWN && keyCode != KeyEvent.VK_ENTER)
				{
					leftKey = keyCode;
					if (keyCode == rightKey)
						rightKey = KeyEvent.VK_RIGHT;
					if (keyCode == switchKey)
						switchKey = KeyEvent.VK_DOWN;
					if (keyCode == enterKey)
						enterKey = KeyEvent.VK_ENTER;
				}
				break;
			case 2: // right
				if (keyCode != KeyEvent.VK_LEFT && keyCode != KeyEvent.VK_DOWN && keyCode != KeyEvent.VK_ENTER)
				{
					rightKey = keyCode;
					if (keyCode == leftKey)
						leftKey = KeyEvent.VK_LEFT;
					if (keyCode == switchKey)
						switchKey = KeyEvent.VK_DOWN;
					if (keyCode == enterKey)
						enterKey = KeyEvent.VK_ENTER;
				}
				break;
			case 3: // switch
				if (keyCode != KeyEvent.VK_LEFT && keyCode != KeyEvent.VK_RIGHT && keyCode != KeyEvent.VK_ENTER)
				{
					switchKey = keyCode;
					if (keyCode == leftKey)
						leftKey = KeyEvent.VK_LEFT;
					if (keyCode == rightKey)
						rightKey = KeyEvent.VK_RIGHT;
					if (keyCode == enterKey)
						enterKey = KeyEvent.VK_ENTER;
				}
				break;
			case 4: // enter
				if (keyCode != KeyEvent.VK_LEFT && keyCode != KeyEvent.VK_RIGHT && keyCode != KeyEvent.VK_DOWN)
				{
					enterKey = keyCode;
					if (keyCode == leftKey)
						leftKey = KeyEvent.VK_LEFT;
					if (keyCode == rightKey)
						rightKey = KeyEvent.VK_RIGHT;
					if (keyCode == switchKey)
						switchKey = KeyEvent.VK_DOWN;
				}
				break;
			}
			selectedKey = 0;
			keybinding = false;
		}
	}

	public void getPreviousMap()
	{
		if (previousMap == null)
		{
			curLvl = 1;
			curMap = 1;
			loadMap(curLvl, curMap, new Vector2f(400f, 320));
		}
		else
			loadMap(previousMap);
	}

	public boolean dejavuCheck()
	{
		String str = "" + curLvl + "-" + curMap;
		if (str.equals("1-3") && !world.isGravitySuit())
		{
			dejavuSet.add(str);
			return false;
		}
		if (dejavuSet.contains(str) && !extraFact)
			return true;
		else
		{
			dejavuSet.add(str);
			return false;
		}
	}

	public boolean activatedCheck()
	{
		String str = "" + curLvl + "-" + curMap;
		if (activatedSet.contains(str))
			return true;
		else
		{
			activatedSet.add(str);
			return false;
		}
	}

	public Map<String, EditorLevelInformation> getLevels()
	{
		return levels;
	}

	public void setLevels(Map<String, EditorLevelInformation> levels)
	{
		this.levels = levels;
	}
}
