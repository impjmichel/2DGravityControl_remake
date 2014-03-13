package levelEditor.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import levelEditor.EditorFrame;

public class EditorToolPanel extends JPanel
{
	private static final long	serialVersionUID	= 1L;
	private EditorFrame			frame;
	private JPanel				infoPanel, selectionPanel;
	private int					selectedLevel, selectedMap;

	public EditorToolPanel(EditorFrame frame)
	{
		super(new BorderLayout());
		this.frame = frame;
		selectedLevel = 1;
		selectedMap = 1;
		setPreferredSize(new Dimension(200, 400));
		add(createToolBar(), BorderLayout.NORTH);
		selectionPanel = createSelectionPanel();
		add(selectionPanel, BorderLayout.WEST);
		infoPanel = creatInfoPanel();
		add(infoPanel, BorderLayout.SOUTH);
	}

	public JPanel createToolBar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.setPreferredSize(new Dimension(190, 230));
		JToolBar toolBar = new JToolBar(null, JToolBar.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		JButton button = new JButton("Cursor");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.CURSOR);
			}
		});
		toolBar.add(button);

		toolBar.addSeparator();

		button = new JButton("Wall");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.WALL);
			}
		});
		toolBar.add(button);

		button = new JButton("Platform");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.PLATFORM);
			}
		});
		toolBar.add(button);
		panel.add(toolBar);

		button = new JButton("Block");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.BLOCK);
			}
		});
		toolBar.add(button);

		button = new JButton("OMRU");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.OMRU);
			}
		});
		toolBar.add(button);
		
		toolBar.addSeparator();
		button = new JButton("PC");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.PC);
			}
		});
		toolBar.add(button);
		
		toolBar.addSeparator();
		button = new JButton("Next Level");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.NEXTLEVEL);
			}
		});
		toolBar.add(button);

		toolBar = new JToolBar(null, JToolBar.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		button = new JButton("Delete");
		button.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.DELETE);
			}
		});
		toolBar.add(button);

		toolBar.addSeparator();

		button = new JButton("Spike");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.SPIKE);
			}
		});
		toolBar.add(button);

		button = new JButton("Deadly Platform");
		button.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.DEADLYPLATFORM);
			}
		});
		toolBar.add(button);

		button = new JButton("Deadly Block");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.DEADLYBLOCK);
			}
		});
		toolBar.add(button);

		button = new JButton("Interaction");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.INTERACTION);
			}
		});
		toolBar.add(button);
		
		toolBar.addSeparator();
		button = new JButton("Door");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.DOOR);
			}
		});
		toolBar.add(button);
		
		toolBar.addSeparator();
		button = new JButton("Line");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setSelectedTool(SelectedTool.LINE);
			}
		});
		toolBar.add(button);

		panel.add(toolBar);
		panel.doLayout();
		return panel;
	}

	@SuppressWarnings("rawtypes")
	private JPanel createSelectionPanel()
	{
		JPanel masterPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new FlowLayout());

		ArrayList<String> s = new ArrayList<String>();
		Map<String, EditorLevelInformation> tempLevels = frame.getLevels();
		if (tempLevels.size() > 0)
		{
			for (int i = 0; i < tempLevels.size(); i++)
			{
				s.add("Level " + (i + 1));
			}

			JComboBox<String[]> levelBox = new JComboBox(s.toArray());
			levelBox.setSelectedIndex(selectedLevel - 1);
			levelBox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JComboBox<String> cb = (JComboBox<String>) e.getSource();
					selectedLevel = cb.getSelectedIndex() + 1;
					selectedMap = 1;
					frame.loadLevelAndMap(selectedLevel, 1);
				}
			});
			panel.add(levelBox);
		}

		s = new ArrayList<String>();
		EditorLevelInformation tempMaps = frame.getLevels().get("level " + selectedLevel);
		if (tempMaps.getMaps().size() > 0)
		{
			for (int i = 0; i < tempMaps.getMaps().size(); i++)
			{
				s.add("Map " + (i + 1));
			}
			JComboBox<String> mapBox = new JComboBox(s.toArray());
			mapBox.setSelectedIndex(selectedMap - 1);
			mapBox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JComboBox cb = (JComboBox) e.getSource();
					selectedMap = cb.getSelectedIndex() + 1;
					frame.loadLevelAndMap(selectedLevel, selectedMap);
				}
			});
			panel.add(mapBox);
		}
		masterPanel.add(panel, BorderLayout.NORTH);

		panel = new JPanel(new GridLayout(4, 1));
		EditorMapInformation currentMap = frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap);
		JLabel label = new JLabel("       Fact String 1 :");
		panel.add(label);
		JTextField field = new JTextField(6);
		field.setText(currentMap.getLine1());
		field.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				EditorMapInformation currentMap = frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap);
				String txt = ((JTextField) arg0.getSource()).getText();
				currentMap.setLine1(txt);
			}
		});
		panel.add(field);
		label = new JLabel("       Fact String 2 :");
		panel.add(label);
		field = new JTextField(6);
		field.setText(currentMap.getLine2());
		field.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				EditorMapInformation currentMap = frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap);
				String txt = ((JTextField) arg0.getSource()).getText();
				currentMap.setLine2(txt);
			}
		});
		panel.add(field);
		masterPanel.add(panel, BorderLayout.SOUTH);

		return masterPanel;
	}

	private JPanel creatInfoPanel()
	{
		JPanel informationPanel = new JPanel(new BorderLayout());

		JPanel panel = null;
		if (frame.mainPanel2.getSelectedObject() != null)
		{
			if (frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.BLOCK ||
					frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.DEADLYBLOCK)
			{
				panel = new JPanel(new GridLayout(7, 2));
			}
			else if (frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.PLATFORM ||
					frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.DEADLYPLATFORM)
			{
				panel = new JPanel(new GridLayout(8, 2));
			}
			else
			{
				panel = new JPanel(new GridLayout(5, 2));
			}

			JLabel label = new JLabel(frame.mainPanel2.getSelectedObject().toString());
			informationPanel.add(label, BorderLayout.NORTH);

			label = new JLabel("       Width :");
			panel.add(label);
			JTextField field = new JTextField(5);
			field.setText("" + (int) frame.mainPanel2.getSelectedObject().getWidth());
			field.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String txt = ((JTextField) arg0.getSource()).getText();
					try
					{
						int newWidth = Integer.parseInt(txt);
						frame.mainPanel2.getSelectedObject().setWidth(newWidth);
					}
					catch (NumberFormatException e)
					{
						((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getWidth());
					}
				}
			});
			panel.add(field);

			label = new JLabel("       Height :");
			panel.add(label);
			field = new JTextField(5);
			field.setText("" + (int) frame.mainPanel2.getSelectedObject().getHeight());
			field.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String txt = ((JTextField) arg0.getSource()).getText();
					try
					{
						int newHeight = Integer.parseInt(txt);
						frame.mainPanel2.getSelectedObject().setHeight(newHeight);
					}
					catch (NumberFormatException e)
					{
						((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getHeight());
					}
				}
			});
			panel.add(field);

			label = new JLabel("       Rotation :");
			panel.add(label);
			field = new JTextField(5);
			field.setText("" + (int) Math.toDegrees(frame.mainPanel2.getSelectedObject().getRotation()));
			field.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String txt = ((JTextField) arg0.getSource()).getText();
					try
					{
						int rotation = Integer.parseInt(txt);
						frame.mainPanel2.getSelectedObject().setRotation((float) Math.toRadians(rotation));
					}
					catch (NumberFormatException e)
					{
						((JTextField) arg0.getSource()).setText("" + (int) Math.toDegrees(frame.mainPanel2.getSelectedObject().getRotation()));
					}
				}
			});
			panel.add(field);

			label = new JLabel("       X :");
			panel.add(label);
			field = new JTextField(5);
			field.setText("" + (int) frame.mainPanel2.getSelectedObject().getPosition().x);
			field.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String txt = ((JTextField) arg0.getSource()).getText();
					try
					{
						int pX = Integer.parseInt(txt);
						int pY = (int) frame.mainPanel2.getSelectedObject().getPosition().y;
						frame.mainPanel2.getSelectedObject().setPosition(new Point2D.Float(pX, pY));
						frame.mainPanel2.getSelectedObject().setEndPosition(new Point2D.Float(pX, pY));
					}
					catch (NumberFormatException e)
					{
						((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getPosition().x);
					}
				}
			});
			panel.add(field);

			label = new JLabel("       Y :");
			panel.add(label);
			field = new JTextField(5);
			field.setText("" + (int) frame.mainPanel2.getSelectedObject().getPosition().y);
			field.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String txt = ((JTextField) arg0.getSource()).getText();
					try
					{
						int pY = Integer.parseInt(txt);
						int pX = (int) frame.mainPanel2.getSelectedObject().getPosition().x;
						frame.mainPanel2.getSelectedObject().setPosition(new Point2D.Float(pX, pY));
						frame.mainPanel2.getSelectedObject().setEndPosition(new Point2D.Float(pX, pY));
					}
					catch (NumberFormatException e)
					{
						((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getPosition().y);
					}
				}
			});
			panel.add(field);

			if (frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.PLATFORM ||
					frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.DEADLYPLATFORM)
			{
				label = new JLabel("       Starting X :");
				panel.add(label);
				field = new JTextField(5);
				field.setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().x);
				field.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						String txt = ((JTextField) arg0.getSource()).getText();
						try
						{
							int sX = Integer.parseInt(txt);
							float sY = frame.mainPanel2.getSelectedObject().getStartPosition().y;
							frame.mainPanel2.getSelectedObject().setStartPosition(new Point2D.Float(sX, sY));
						}
						catch (NumberFormatException e)
						{
							((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().x);
						}
					}
				});
				panel.add(field);

				label = new JLabel("       Starting Y :");
				panel.add(label);
				field = new JTextField(5);
				field.setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().y);
				field.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						String txt = ((JTextField) arg0.getSource()).getText();
						try
						{
							int sY = Integer.parseInt(txt);
							float sX = frame.mainPanel2.getSelectedObject().getStartPosition().x;
							frame.mainPanel2.getSelectedObject().setStartPosition(new Point2D.Float(sX, sY));
						}
						catch (NumberFormatException e)
						{
							((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().y);
						}
					}
				});
				panel.add(field);

				label = new JLabel("       Speed % :");
				panel.add(label);
				field = new JTextField(5);
				field.setText("" + (int) (frame.mainPanel2.getSelectedObject().getSpeed() * 100));
				field.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						String txt = ((JTextField) arg0.getSource()).getText();
						try
						{
							int speed = Integer.parseInt(txt);
							frame.mainPanel2.getSelectedObject().setSpeed((float) (speed / 100.0));
						}
						catch (NumberFormatException e)
						{
							((JTextField) arg0.getSource()).setText("" + (int) (frame.mainPanel2.getSelectedObject().getSpeed() * 100));
						}
					}
				});
				panel.add(field);
			}
			if (frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.BLOCK ||
					frame.mainPanel2.getSelectedObject().getObjectType() == EditorObjectType.DEADLYBLOCK)
			{
				label = new JLabel("       Max X Velocity:");
				panel.add(label);
				field = new JTextField(5);
				field.setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().x);
				field.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						String txt = ((JTextField) arg0.getSource()).getText();
						try
						{
							int sX = Integer.parseInt(txt);
							float sY = frame.mainPanel2.getSelectedObject().getStartPosition().y;
							frame.mainPanel2.getSelectedObject().setStartPosition(new Point2D.Float(sX, sY));
						}
						catch (NumberFormatException e)
						{
							((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().x);
						}
					}
				});
				panel.add(field);

				label = new JLabel("       Max Y Velocity:");
				panel.add(label);
				field = new JTextField(5);
				field.setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().y);
				field.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						String txt = ((JTextField) arg0.getSource()).getText();
						try
						{
							int sY = Integer.parseInt(txt);
							float sX = frame.mainPanel2.getSelectedObject().getStartPosition().x;
							frame.mainPanel2.getSelectedObject().setStartPosition(new Point2D.Float(sX, sY));
						}
						catch (NumberFormatException e)
						{
							((JTextField) arg0.getSource()).setText("" + (int) frame.mainPanel2.getSelectedObject().getStartPosition().y);
						}
					}
				});
				panel.add(field);
			}
			informationPanel.add(panel, BorderLayout.CENTER);
		}

		panel = new JPanel(new GridLayout(6, 2));

		JLabel label = new JLabel("");
		panel.add(label);
		label = new JLabel("");
		panel.add(label);

		label = new JLabel("       Next Map");
		panel.add(label);
		label = new JLabel("");
		panel.add(label);

		label = new JLabel("                 North:");
		panel.add(label);
		JTextField field = new JTextField(3);
		field.setText("" + frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(0));
		field.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String txt = ((JTextField) arg0.getSource()).getText();
				try
				{
					int nextMap = Integer.parseInt(txt);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().remove(0);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().add(0, nextMap);
				}
				catch (NumberFormatException e)
				{
					((JTextField) arg0.getSource()).setText("" +
							frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(0));
				}
			}
		});
		panel.add(field);

		label = new JLabel("                 East:");
		panel.add(label);
		field = new JTextField(3);
		field.setText("" + frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(1));
		field.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String txt = ((JTextField) arg0.getSource()).getText();
				try
				{
					int nextMap = Integer.parseInt(txt);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().remove(1);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().add(1, nextMap);
				}
				catch (NumberFormatException e)
				{
					((JTextField) arg0.getSource()).setText("" +
							frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(1));
				}
			}
		});
		panel.add(field);

		label = new JLabel("                 South:");
		panel.add(label);
		field = new JTextField(3);
		field.setText("" + frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(2));
		field.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String txt = ((JTextField) arg0.getSource()).getText();
				try
				{
					int nextMap = Integer.parseInt(txt);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().remove(2);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().add(2, nextMap);
				}
				catch (NumberFormatException e)
				{
					((JTextField) arg0.getSource()).setText("" +
							frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(2));
				}
			}
		});
		panel.add(field);

		label = new JLabel("                 West:");
		panel.add(label);
		field = new JTextField(3);
		field.setText("" + frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(3));
		field.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String txt = ((JTextField) arg0.getSource()).getText();
				try
				{
					int nextMap = Integer.parseInt(txt);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().remove(3);
					frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().add(3, nextMap);
				}
				catch (NumberFormatException e)
				{
					((JTextField) arg0.getSource()).setText("" +
							frame.getLevels().get("level " + selectedLevel).getMaps().get("map " + selectedMap).getNextMapInt().get(3));
				}
			}
		});
		panel.add(field);

		informationPanel.add(panel, BorderLayout.SOUTH);
		return informationPanel;
	}

	public void update()
	{
		remove(selectionPanel);
		selectionPanel = createSelectionPanel();
		add(selectionPanel, BorderLayout.WEST);

		remove(infoPanel);
		infoPanel = creatInfoPanel();
		add(infoPanel, BorderLayout.SOUTH);
		validate();
	}

	public void setSelectedLevel(int selectedLevel)
	{
		this.selectedLevel = selectedLevel;
	}

	public void setSelectedMap(int selectedMap)
	{
		this.selectedMap = selectedMap;
	}
}
