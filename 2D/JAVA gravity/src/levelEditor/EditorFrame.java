package levelEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import levelEditor.model.EditorLevelInformation;
import levelEditor.model.EditorMainPanel;
import levelEditor.model.EditorMapInformation;
import levelEditor.model.EditorObjectInformation;
import levelEditor.model.EditorToolPanel;
import levelEditor.model.SelectedTool;

public class EditorFrame extends JFrame implements KeyListener, ActionListener
{
	private static final long					serialVersionUID	= 1L;
	private Map<String, EditorLevelInformation>	levels;
	private EditorControl						control;
	private int									currentMap, currentLevel, maxMap, maxLevel;
	private JPanel								content;
	private SelectedTool						selectedTool;
	public EditorToolPanel						toolPanel;
	public EditorMainPanel						mainPanel2;

	public EditorFrame(Map<String, EditorLevelInformation> levels, EditorControl control, boolean exitOnClose)
	{
		super("Level Editor - Gravity");
		this.levels = levels;
		this.control = control;

		if (exitOnClose)
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setFocusable(true);

		addKeyListener(this);
		selectedTool = SelectedTool.CURSOR;
		createMenu();
		content = new JPanel(new BorderLayout());
		if (levels.size() < 1)
		{
			currentLevel = 0;
			currentMap = 0;
			maxLevel = 0;
			maxMap = 0;
			addNewLevel();
		}
		else
		{
			currentLevel = 1;
			currentMap = 0;
			maxLevel = levels.size();
			maxMap = levels.get("level " + currentLevel).getMaps().size();
		}
		mainPanel2 = new EditorMainPanel(this, this.levels.get("level " + currentLevel).getMaps().get("map " + currentMap));
		mainPanel2.setPreferredSize(new Dimension(1001, 700));
		toolPanel = new EditorToolPanel(this);
		content.add(toolPanel, BorderLayout.EAST);
		content.add(mainPanel2, BorderLayout.CENTER);
		setContentPane(content);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		Timer timer = new Timer(1000 / 60, this);
		timer.start();
	}

	private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new FlowLayout());

		JMenuItem item = new JMenuItem("new Map");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.setToolTipText("Ctrl + N");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				EditorLevelInformation currentFrameLevel = levels.get("level " + currentLevel);
				addNewMap(currentFrameLevel);
			}
		});
		menuBar.add(item);

		item = new JMenuItem("new Level");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				addNewLevel();
			}
		});
		menuBar.add(item);

		// SAVE
		item = new JMenuItem("Save...");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.setToolTipText("Ctrl + Shift + S");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				saveAs();
			}
		});
		menuBar.add(item);

		item = new JMenuItem("Save data.glf");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.setToolTipText("Ctrl + S");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				saveGravityLevels();
			}
		});
		menuBar.add(item);

		// OPEN
		item = new JMenuItem("Open...");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.setToolTipText("Ctrl + Shift + O");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				openAs();
			}
		});
		menuBar.add(item);

		item = new JMenuItem("Open data.glf");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.setToolTipText("Ctrl + O");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				openGravityLevels();
			}
		});
		menuBar.add(item);

		item = new JMenuItem("About...");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Level Editor version 0.4", "About Level Editor", JOptionPane.QUESTION_MESSAGE);
			}
		});
		menuBar.add(item);

		// QUIT
		item = new JMenuItem("Quit");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		menuBar.add(item);

		setJMenuBar(menuBar);
	}

	private void addNewLevel()
	{
		maxMap = -1;
		maxLevel++;
		EditorLevelInformation value = new EditorLevelInformation(new TreeMap<String, EditorMapInformation>(),
				new TreeMap<String, List<EditorObjectInformation>>(), new TreeMap<String, Boolean>());
		addNewMap(value);
		levels.put("level " + maxLevel, value);
		currentLevel = maxLevel;
		loadLevelAndMap(currentLevel, currentMap);
	}

	private void addNewMap(EditorLevelInformation value)
	{
		maxMap++;
		EditorMapInformation map = new EditorMapInformation(new ArrayList<EditorObjectInformation>(), new ArrayList<Integer>(), "", "", null, null,
				new ArrayList<Line2D.Float>(), null);
		value.getMaps().put("map " + maxMap, map);
		currentMap = maxMap;
		loadLevelAndMap(currentLevel, maxMap);
	}

	public void loadLevelAndMap(int levelID, int mapID)
	{
		if (mainPanel2 != null)
		{
			EditorLevelInformation level = levels.get("level " + levelID);
			maxMap = level.getMaps().size() - 1;
			EditorMapInformation map = level.getMaps().get("map " + mapID);
			content.remove(mainPanel2);
			mainPanel2 = new EditorMainPanel(this, map);
			mainPanel2.setPreferredSize(new Dimension(1001, 700));
			content.add(mainPanel2, BorderLayout.CENTER);
			currentLevel = levelID;
			currentMap = mapID;
			toolPanel.setSelectedLevel(currentLevel);
			toolPanel.setSelectedMap(currentMap);
			toolPanel.update();
			validate();
		}
	}

	public SelectedTool getSelectedTool()
	{
		return selectedTool;
	}

	public void setSelectedTool(SelectedTool selectedTool)
	{
		this.selectedTool = selectedTool;
	}

	public Map<String, EditorLevelInformation> getLevels()
	{
		return levels;
	}

	public void setLevels(Map<String, EditorLevelInformation> levels)
	{
		this.levels = levels;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		repaint();
		System.out.println("mapID : " + currentMap);
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		if (ke.isControlDown() && ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_S)
			saveAs();
		else if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_S)
			saveGravityLevels();

		if (ke.isControlDown() && ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_O)
			openAs();
		else if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_O)
			openGravityLevels();

		if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_N)
		{
			EditorLevelInformation currentFrameLevel = levels.get("level " + currentLevel);
			addNewMap(currentFrameLevel);
		}
		if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_L)
			addNewLevel();
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	private void openAs()
	{
		final JFileChooser openFile = new JFileChooser();

		openFile.setFileFilter(new FileNameExtensionFilter("Gravity files (*.glf)", "glf"));

		if (openFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = openFile.getSelectedFile();
			String strFileName = file.getAbsolutePath();

			GravityIO gIO = new GravityIO(control);
			try
			{
				gIO.load(strFileName);
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			maxLevel = levels.size();
			loadLevelAndMap(1, 1);
		}
	}

	private void openGravityLevels()
	{
		GravityIO gIO = new GravityIO(control);
		try
		{
			gIO.load("src/data.glf");
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxLevel = levels.size();
		loadLevelAndMap(maxLevel, 0);
	}

	private void saveAs()
	{
		JFileChooser saveFile = new JFileChooser();
		saveFile.setFileFilter(new FileNameExtensionFilter("Gravity files (*.glf)", "glf"));

		if (saveFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String strFileName = saveFile.getSelectedFile().getAbsolutePath();
			boolean save = false;
			if (saveFile.getSelectedFile().exists())
			{
				if (JOptionPane.showConfirmDialog(this, "Wilt u dit bestand overschrijven?") == JOptionPane.YES_OPTION)
				{
					save = true;
				}
			}
			else
			{
				save = true;
			}
			if (save)
			{
				GravityIO gIO = new GravityIO(control);
				if (!strFileName.toLowerCase().endsWith(".glf"))
				{
					strFileName = strFileName.concat(".glf");
				}
				try
				{
					gIO.save(strFileName);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Could not be saved!", "error", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e)
				{

				}
			}
		}
	}

	private void saveGravityLevels()
	{
		GravityIO gIO = new GravityIO(control);
		try
		{
			gIO.save("src/data.glf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getCurrentLevel()
	{
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel)
	{
		this.currentLevel = currentLevel;
	}

	public EditorControl getControl()
	{
		return control;
	}
}
