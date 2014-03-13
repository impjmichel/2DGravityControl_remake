package levelEditor.model;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import levelEditor.EditorFrame;

public class EditorMainPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long				serialVersionUID	= 1L;
	private final int						offset				= 50;
	private EditorFrame						frame;
	private EditorMapInformation			map;
	private List<EditorObjectInformation>	objects;
	private EditorObjectInformation			selectedObject, tempEOI;
	private DragDirection					dragDirection;
	private boolean							secondClickLine, secondClickNextLevel;
	private Point2D.Float					firstPointLine, firstPointNextLevel;
	private Point2D.Float					positionMouse;

	public EditorMainPanel(EditorFrame frame, EditorMapInformation map)
	{
		this.frame = frame;
		this.map = map;
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		objects = map.getGameObjects();
		tempEOI = null;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(offset, offset);

		Font font = new Font("Monospaced", Font.BOLD, 30);
		g2.setFont(font);
		g2.drawString(frame.getSelectedTool().toString(), 600, -10);
		// grid
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i < 31; i++)
			g2.drawLine(0, i * 20, 900, i * 20);
		for (int i = 0; i < 46; i++)
			g2.drawLine(i * 20, 0, i * 20, 600);

		// objects
		g2.setColor(new Color(200, 10, 10, 150));
		if (map.getNextLevelStep() != null)
			g2.draw(map.getNextLevelStep());
		if(firstPointNextLevel != null)
			g2.draw(getRectangle(firstPointNextLevel, positionMouse));
		
		if (firstPointLine != null)
			g2.draw(new Line2D.Float(firstPointLine, positionMouse));
		g2.setColor(new Color(200, 10, 10, 255));
		g2.setStroke(new BasicStroke(4));
		for (Line2D.Float line : map.getLines())
			g2.draw(line);

		g2.setStroke(new BasicStroke(2));
		g2.setColor(new Color(40, 40, 40));
		if (objects != null)
		{
			for (EditorObjectInformation object : objects)
				object.drawObject(g2);
			drawSelectedObject(g2);
		}
	}

	private void drawSelectedObject(Graphics2D g2)
	{
		if (selectedObject != null)
		{
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.GREEN);
			selectedObject.drawObject(g2);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (frame.getSelectedTool() == SelectedTool.CURSOR)
		{
			Point2D.Float position = new Point2D.Float(e.getPoint().x - offset, e.getPoint().y - offset);
			Point2D.Float gridPosition = getGridPoint(position);
			if (dragDirection != null)
			{
				switch (dragDirection)
				{
				case INSIDE:
					selectedObject.setPosition(gridPosition);
					selectedObject.setEndPosition(gridPosition);
					break;
				case EAST:
					if (gridPosition.x >= selectedObject.getPosition().x)
					{
						float xOffset = gridPosition.x - selectedObject.getPosition().x;
						float oldWidth = selectedObject.getWidth();
						selectedObject.setWidth(selectedObject.getWidth() / 2 + xOffset);
						float addedWidth = selectedObject.getWidth() - oldWidth;
						selectedObject.setPosition(new Point2D.Float(selectedObject.getPosition().x + addedWidth / 2, selectedObject.getPosition().y));
					}
					break;
				case NORTH:
					if (gridPosition.y <= selectedObject.getPosition().y)
					{
						float yOffset = selectedObject.getPosition().y - gridPosition.y;
						float oldHeight = selectedObject.getHeight();
						selectedObject.setHeight(selectedObject.getHeight() / 2 + yOffset);
						float addedHeight = selectedObject.getHeight() - oldHeight;
						selectedObject.setPosition(new Point2D.Float(selectedObject.getPosition().x, selectedObject.getPosition().y - addedHeight / 2));
					}
					break;
				case NORTHEAST:
					break;
				case NORTHWEST:
					break;
				case SOUTH:
					if (gridPosition.y >= selectedObject.getPosition().y)
					{
						float yOffset = gridPosition.y - selectedObject.getPosition().y;
						float oldHeight = selectedObject.getHeight();
						selectedObject.setHeight(selectedObject.getHeight() / 2 + yOffset);
						float addedHeight = selectedObject.getHeight() - oldHeight;
						selectedObject.setPosition(new Point2D.Float(selectedObject.getPosition().x, selectedObject.getPosition().y + addedHeight / 2));
					}
					break;
				case SOUTHEAST:
					break;
				case SOUTHWEST:
					break;
				case WEST:
					if (gridPosition.x <= selectedObject.getPosition().x)
					{
						float xOffset = selectedObject.getPosition().x - gridPosition.x;
						float oldWidth = selectedObject.getWidth();
						selectedObject.setWidth(selectedObject.getWidth() / 2 + xOffset);
						float addedWidth = selectedObject.getWidth() - oldWidth;
						selectedObject.setPosition(new Point2D.Float(selectedObject.getPosition().x - addedWidth / 2, selectedObject.getPosition().y));
					}
					break;
				default:
					break;
				}
			}
			frame.toolPanel.update();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		positionMouse = new Point2D.Float(e.getX() - offset, e.getY() - offset);
		if (selectedObject != null)
		{
			DragDirection cursorDir = selectedObject.isInsideBorder(positionMouse);
			if (cursorDir == DragDirection.EAST)
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			}
			else if (cursorDir == DragDirection.WEST)
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			}
			else if (cursorDir == DragDirection.NORTH)
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			}
			else if (cursorDir == DragDirection.SOUTH)
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			}
			else if (cursorDir == DragDirection.SOUTHEAST || cursorDir == DragDirection.NORTHWEST)
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			}
			else if (cursorDir == DragDirection.SOUTHWEST || cursorDir == DragDirection.NORTHEAST)
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			}
			else
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		Point2D.Float position = new Point2D.Float(arg0.getX() - offset, arg0.getY() - offset);
		EditorObjectInformation object = null;
		switch (frame.getSelectedTool())
		{
		case CURSOR:
			boolean insideBorder = false;
			for (EditorObjectInformation object2 : map.getGameObjects())
			{
				if (object2.isInsideBorder(position) != DragDirection.OUTSIDE)
					insideBorder = true;
				if (object2.isInside(position))
				{
					selectedObject = object2;
					frame.toolPanel.update();
					break;
				}
			}
			if (!insideBorder)
			{
				selectedObject = null;
				frame.toolPanel.update();
			}
			break;
		case WALL:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 40, 40, position, null, null, 0, EditorObjectType.WALL, frame.getControl().getNext(EditorObjectType.WALL));
			map.getGameObjects().add(object);
			selectedObject = object;
			break;
		case BLOCK:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 40, 40, position, new Point2D.Float(40, 40), null, 0, EditorObjectType.BLOCK, frame.getControl().getNext(
					EditorObjectType.BLOCK));
			map.getGameObjects().add(object);
			selectedObject = object;
			break;
		case DEADLYBLOCK:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 40, 40, position, new Point2D.Float(40, 40), null, 0, EditorObjectType.DEADLYBLOCK, frame.getControl()
					.getNext(EditorObjectType.DEADLYBLOCK));
			map.getGameObjects().add(object);
			selectedObject = object;
			break;
		case SPIKE:
			if (arg0.isControlDown() && arg0.getButton() == MouseEvent.BUTTON1)
			{
				position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
				object = new EditorObjectInformation((float) (Math.PI / 2 * 3), 20, 20, position, null, null, 0, EditorObjectType.SPIKE, frame.getControl()
						.getNext(EditorObjectType.SPIKE));
				map.getGameObjects().add(object);
			}
			else if (arg0.getButton() == MouseEvent.BUTTON1)
			{
				position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
				object = new EditorObjectInformation(0, 20, 20, position, null, null, 0, EditorObjectType.SPIKE, frame.getControl().getNext(
						EditorObjectType.SPIKE));
				map.getGameObjects().add(object);
			}
			if (arg0.isControlDown() && arg0.getButton() == MouseEvent.BUTTON3)
			{
				position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
				object = new EditorObjectInformation((float) (Math.PI / 2), 20, 20, position, null, null, 0, EditorObjectType.SPIKE, frame.getControl()
						.getNext(EditorObjectType.SPIKE));
				map.getGameObjects().add(object);
			}
			else if (arg0.getButton() == MouseEvent.BUTTON3)
			{
				position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
				object = new EditorObjectInformation((float) Math.PI, 20, 20, position, null, null, 0, EditorObjectType.SPIKE, frame.getControl().getNext(
						EditorObjectType.SPIKE));
				map.getGameObjects().add(object);
			}
			selectedObject = object;
			break;
		case PLATFORM:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 40, 40, position, position, position, 1, EditorObjectType.PLATFORM, frame.getControl().getNext(
					EditorObjectType.PLATFORM));
			map.getGameObjects().add(object);
			selectedObject = object;
			break;
		case DEADLYPLATFORM:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 40, 40, position, position, position, 1, EditorObjectType.DEADLYPLATFORM, frame.getControl().getNext(
					EditorObjectType.DEADLYPLATFORM));
			map.getGameObjects().add(object);
			selectedObject = object;
			break;
		case DELETE:
			selectedObject = null;
			Iterator<EditorObjectInformation> it = map.getGameObjects().iterator();
			while (it.hasNext())
			{
				object = it.next();
				if (object.isInside(position))
				{
					if (object.getObjectType() == EditorObjectType.INTERACTION)
					{
						map.setInteractLine1(null);
						map.setInteractLine2(null);
					}
					else if (object.getObjectType() == EditorObjectType.PC)
					{
						frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().get(object.toString()).clear();
						frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().remove(object.toString());
					}
					it.remove();
					break;
				}
			}
			break;
		case OMRU:
			Iterator<EditorObjectInformation> it2 = map.getGameObjects().iterator();
			while (it2.hasNext())
			{
				object = it2.next();
				if (object.getObjectType() == EditorObjectType.OMRU)
				{
					it2.remove();
					break;
				}
			}
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 160, 160, position, null, null, 0, EditorObjectType.OMRU, frame.getControl().getNext(EditorObjectType.OMRU));
			map.getGameObjects().add(object);
			selectedObject = object;
			frame.setSelectedTool(SelectedTool.CURSOR);
			break;
		case INTERACTION:
			Iterator<EditorObjectInformation> it3 = map.getGameObjects().iterator();
			while (it3.hasNext())
			{
				object = it3.next();
				if (object.getObjectType() == EditorObjectType.INTERACTION)
				{
					it3.remove();
					map.setInteractLine1(null);
					map.setInteractLine2(null);
					break;
				}
			}
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 100, 100, position, null, null, 0, EditorObjectType.INTERACTION, frame.getControl().getNext(
					EditorObjectType.INTERACTION));
			map.getGameObjects().add(object);
			selectedObject = object;
			frame.setSelectedTool(SelectedTool.CURSOR);
			new LinesDialog(frame, map);
			break;
		case PC:
			Iterator<EditorObjectInformation> it4 = map.getGameObjects().iterator();
			while (it4.hasNext())
			{
				object = it4.next();
				if (object.getObjectType() == EditorObjectType.PC)
				{
					frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().get(object.toString()).clear();
					frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().remove(object.toString());
					it4.remove();
					break;
				}
			}
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			object = new EditorObjectInformation(0, 80, 80, position, null, null, 0, EditorObjectType.PC, frame.getControl().getNext(EditorObjectType.PC));
			map.getGameObjects().add(object);
			selectedObject = object;
			frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().put(object.toString(), new ArrayList<EditorObjectInformation>());
			frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().get(object.toString()).add(object);
			frame.getLevels().get("level " + frame.getCurrentLevel()).getActivatedComps().put(object.toString(), true);
			break;
		case DOOR:
			if (frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().isEmpty())
			{
				JOptionPane.showMessageDialog(new JFrame(), "No PC created to link this door.", "Error 2407", JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				tempEOI = null;
				position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
				object = new EditorObjectInformation(0, 20, 200, position, position, position, 1, EditorObjectType.DOOR, frame.getControl().getNext(
						EditorObjectType.DOOR));
				EditorObjectInformation selectedPC = getPcSelection();
				if (tempEOI != null)
				{
					frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().get(selectedPC.toString()).add(object);
					map.getGameObjects().add(object);
				}
				else
					JOptionPane.showMessageDialog(new JFrame(), "No PC selected.", "Error 2408", JOptionPane.WARNING_MESSAGE);
			}
			break;
		case LINE:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			if (secondClickLine)
			{
				Line2D.Float newLine = new Line2D.Float(firstPointLine, position);
				map.getLines().add(newLine);
				secondClickLine = false;
				firstPointLine = null;
			}
			else
			{
				secondClickLine = true;
				firstPointLine = position;
			}
			break;
		case NEXTLEVEL:
			position = getGridPoint(new Point2D.Float(arg0.getPoint().x - offset, arg0.getPoint().y - offset));
			if (secondClickNextLevel)
			{
				map.setNextLevelStep(getRectangle(firstPointNextLevel, position));
				secondClickNextLevel = false;
				firstPointNextLevel = null;
			}
			else
			{
				secondClickNextLevel = true;
				firstPointNextLevel = position;
			}
			break;
		default:
			break;
		}
	}

	private EditorObjectInformation getPcSelection()
	{
		new PcSelectionDialog(frame, this);

		return tempEOI;
	}

	public Point2D.Float getGridPoint(Point2D.Float point)
	{
		float xOffset = point.x % 20;
		float yOffset = point.y % 20;
		float x = point.x - xOffset;
		float y = point.y - yOffset;
		return new Point2D.Float(x, y);
	}
	
	public Rectangle2D.Float getRectangle(Point2D.Float first, Point2D.Float last)
	{
		float firstX;
		float secondX;
		float firstY;
		float secondY;
		if (first.x < last.x)
		{
			firstX = first.x;
			secondX = last.x;
		}
		else
		{
			firstX = last.x;
			secondX = first.x;
		}
		if (first.y < last.y)
		{
			firstY = first.y;
			secondY = last.y;
		}
		else
		{
			firstY = last.y;
			secondY = first.y;
		}
		return new Rectangle2D.Float(firstX, firstY, secondX - firstX, secondY - firstY);
	}

	public EditorObjectInformation getSelectedObject()
	{
		return selectedObject;
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Point2D.Float position = new Point2D.Float(e.getPoint().x - offset, e.getPoint().y - offset);
		if (selectedObject != null)
			dragDirection = selectedObject.isInsideBorder(position);
		frame.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		dragDirection = DragDirection.OUTSIDE;
	}

	public EditorObjectInformation getTempEOI()
	{
		return tempEOI;
	}

	public void setTempEOI(EditorObjectInformation tempEOI)
	{
		this.tempEOI = tempEOI;
	}

	private class PcSelectionDialog extends JDialog implements ActionListener, PropertyChangeListener
	{
		private static final long	serialVersionUID	= 1L;
		private String[]			s					= { "OK", "Cancel" };
		private JOptionPane			optionPane;
		private JPanel				panel;
		private JComboBox<String[]>	pcBox;
		private EditorMainPanel		emp;

		public PcSelectionDialog(Window parent, EditorMainPanel emp)
		{
			super(parent);
			this.emp = emp;
			setModal(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setTitle("PC selection");
			createPanel();
			setLocationRelativeTo(parent);
			setVisible(true);
		}

		@SuppressWarnings("unchecked")
		private void createPanel()
		{
			Object[] options = { s[0], s[1] };
			panel = new JPanel(new BorderLayout());

			List<String> s = new ArrayList<String>();
			Map<String, List<EditorObjectInformation>> tempLevel = frame.getLevels().get("level " + frame.getCurrentLevel()).getComps();
			for (String obj : tempLevel.keySet())
			{
				s.add(obj);
			}
			pcBox = new JComboBox(s.toArray());
			panel.add(pcBox);
			optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			optionPane.addPropertyChangeListener(this);

			setContentPane(optionPane);
			pack();
		}

		public void actionPerformed(ActionEvent e)
		{
			optionPane.setValue(s[0]);
		}

		@Override
		public void propertyChange(PropertyChangeEvent pce)
		{
			String prop = pce.getPropertyName();

			if ((pce.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop)))
			{
				Object value = optionPane.getValue();
				if (value == JOptionPane.UNINITIALIZED_VALUE)
					return;

				optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
				if (s[0].equals(value))
				{
					emp.setTempEOI(frame.getLevels().get("level " + frame.getCurrentLevel()).getComps().get(pcBox.getSelectedItem()).get(0));
					dispose();
				}
				else
					dispose();
			}
		}
	}

	private class LinesDialog extends JDialog implements ActionListener, PropertyChangeListener
	{
		private static final long		serialVersionUID	= 1L;
		private int						numberOfEnters		= 1;
		private JTextField				intLines;
		private JButton					button;
		private JPanel					panel, linePanel;
		private JOptionPane				optionPane;
		private String[]				s					= { "OK", "Cancel" };
		private List<JTextField>		fields, fields2;
		private EditorMapInformation	map;

		public LinesDialog(Window parent, EditorMapInformation map)
		{
			super(parent);
			this.map = map;
			setModal(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setTitle("Interaction Lines");
			fields = new ArrayList<JTextField>();
			fields2 = new ArrayList<JTextField>();
			createPanel();
			setLocationRelativeTo(parent);
			setVisible(true);
		}

		private void createPanel()
		{
			panel = new JPanel(new BorderLayout());
			linePanel = new JPanel();
			panel.add(linePanel, BorderLayout.NORTH);
			JPanel mainPanel = new JPanel(new FlowLayout());
			Object[] options = { s[0], s[1] };

			createLines();

			intLines = new JTextFieldLimit(2, 3, false);
			mainPanel.add(intLines);
			button = new JButton("create lines");
			button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String str = intLines.getText();
					try
					{
						numberOfEnters = Integer.parseInt(str);
						createLines();
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog((JButton) arg0.getSource(), "Is not an elligible number", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			mainPanel.add(button);

			panel.add(mainPanel, BorderLayout.CENTER);
			optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);

			optionPane.addPropertyChangeListener(this);

			setContentPane(optionPane);
			pack();
		}

		public void createLines()
		{
			fields.clear();
			fields2.clear();
			panel.remove(linePanel);
			linePanel = new JPanel(new GridLayout(numberOfEnters, 2));
			for (int i = 0; i < numberOfEnters; i++)
			{
				JTextField field = new JTextFieldLimit(50, 20, true);
				fields.add(field);
				linePanel.add(field);
				field = new JTextFieldLimit(50, 20, true);
				fields2.add(field);
				linePanel.add(field);
			}
			panel.add(linePanel, BorderLayout.NORTH);
			validate();
			pack();
		}

		public void actionPerformed(ActionEvent e)
		{
			optionPane.setValue(s[0]);
		}

		public void propertyChange(PropertyChangeEvent pce)
		{
			String prop = pce.getPropertyName();

			if ((pce.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop)))
			{
				Object value = optionPane.getValue();
				if (value == JOptionPane.UNINITIALIZED_VALUE)
					return;

				optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
				if (s[0].equals(value))
				{
					ArrayList<String> lines = new ArrayList<String>();
					for (JTextField field : fields)
						lines.add(field.getText());
					map.setInteractLine1(lines);
					lines = new ArrayList<String>();
					for (JTextField field : fields2)
						lines.add(field.getText());
					map.setInteractLine2(lines);
					frame.setSelectedTool(SelectedTool.INTERACTION);
					dispose();
				}
				else
					dispose();
			}
		}

		public class JTextFieldLimit extends JTextField
		{
			private static final long	serialVersionUID	= 2L;
			private int					limit;
			private boolean				letters;

			public JTextFieldLimit(int limit, int columns, boolean letters)
			{
				super(columns);
				this.limit = limit;
				this.letters = letters;
			}

			@Override
			protected Document createDefaultModel()
			{
				return new LimitDocument();
			}

			private class LimitDocument extends PlainDocument
			{
				private static final long	serialVersionUID	= 3L;

				@Override
				public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
				{
					if (!letters)
					{
						try
						{
							@SuppressWarnings("unused")
							int test = Integer.parseInt(str);
							if (str == null)
								return;

							if ((getLength() + str.length()) <= limit)
							{
								super.insertString(offset, str, attr);
							}
						}
						catch (Exception noIntE)
						{
							return;
						}
					}
					else
					{
						if ((getLength() + str.length()) <= limit)
						{
							super.insertString(offset, str, attr);
						}
					}
				}
			}
		}
	}
}
