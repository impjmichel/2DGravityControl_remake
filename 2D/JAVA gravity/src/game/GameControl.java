package game;

import game.world.GameWorld;
import game.world.model.GameMap;
import game.world.model.specialMaps.L1M00;
import game.world.model.specialMaps.L1M01;
import game.world.model.specialMaps.L1M02;
import game.world.model.specialMaps.L1M03;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import levelEditor.EditorControl;
import levelEditor.model.EditorLevelInformation;
import levelEditor.model.HiScoreEntry;

public class GameControl
{
	private GameWorld								world;
	private GameFrame								frame;
	private Map<String, EditorLevelInformation>		levels;
	private Map<String, Class<? extends GameMap>>	specialLevels;
	private List<HiScoreEntry>						hiScore;
	private boolean									editorEnabled;

	public static void main(String[] args)
	{
		new GameControl();
	}
	
	public GameControl()
	{
		setupSpecialMaps();
		world = new GameWorld();
		load("src/data.glf");
		frame = new GameFrame(world, levels, hiScore, specialLevels, editorEnabled);
		world.setFrame(frame);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void setupSpecialMaps()
	{
		specialLevels = new TreeMap<String, Class<? extends GameMap>>();
		specialLevels.put("1-0", L1M00.class);
		specialLevels.put("1-1", L1M01.class);
		specialLevels.put("1-2", L1M02.class);
		specialLevels.put("1-3", L1M03.class);
		//TODO add special maps here
	}

	public void load(String filename)
	{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			Object read = in.readObject();
			if (read instanceof EditorControl)
			{
				hiScore = new ArrayList<HiScoreEntry>();
				hiScore.addAll(((EditorControl) read).getHiScore());
				levels = new TreeMap<String, EditorLevelInformation>();
				levels.putAll(((EditorControl) read).getLevels());
				editorEnabled = ((EditorControl) read).isEditorEnabled();
			}
			in.close();
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Could not find data.glf", "Error 2404", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		catch (ClassNotFoundException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Could not read data.glf", "Error 2405", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Could not read data.glf", "Error 2406", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}
}
