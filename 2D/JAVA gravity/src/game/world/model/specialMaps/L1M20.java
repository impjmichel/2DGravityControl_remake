//package game.world.model.specialMaps;
//
//import game.GameFrame;
//import game.world.GameWorld;
//import game.world.model.GameMap;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import levelEditor.model.EditorMapInformation;
//import net.phys2d.math.Vector2f;
//import net.phys2d.raw.Body;
//import net.phys2d.raw.StaticBody;
//import net.phys2d.raw.shapes.Box;
//
//public class L1M20 extends GameMap
//{
//	private static final long	serialVersionUID	= 1L;
//	private Body				b1, b2, b3, b4, b5, b6, b7;
//	private Image pcImage;
//	private int pcX;
//	private int pcCounter;
//	private int closeY, closeCounter;
//
//	public L1M20(GameWorld world, GameFrame frame, EditorMapInformation objects, Vector2f position)
//	{
//		super(world, frame, objects, position);
//		pcX = 0;
//		pcCounter = 0;
//		
//		if(!world.isClosedL1M20())
//			closeY = 170;
//		else
//			closeY = 0;
//
//		b1 = new StaticBody("", new Box(80, 40f));
//		b2 = new StaticBody("", new Box(80, 40f));
//		b3 = new StaticBody("", new Box(120, 40f));
//		b4 = new StaticBody("", new Box(40f, 79));
//		b5 = new StaticBody("", new Box(40f, 79));
//		b6 = new StaticBody("", new Box(40f, 79));
//		b7 = new StaticBody("", new Box(40f, 79));
//		world2D.add(b1);
//		world2D.add(b2);
//		world2D.add(b3);
//		world2D.add(b4);
//		world2D.add(b5);
//		world2D.add(b6);
//		world2D.add(b7);
//		
//		try
//		{
//			pcImage = ImageIO.read(L1M02.class.getResource("/pc93.png"));
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void paintComponent(Graphics g)
//	{
//		super.paintComponent(g);
//		Graphics2D g2 = (Graphics2D) g;
//		
//		g2.setStroke(new BasicStroke(2));
//		g2.setColor(new Color(40,40,40));
//		drawBox(g2, b1);
//		drawBox(g2, b2);
//		drawBox(g2, b3);
//		drawBox(g2, b4);
//		drawBox(g2, b5);
//		drawBox(g2, b6);
//		drawBox(g2, b7);
//		
//		if(pcImage != null)
//		{
//			BufferedImage subImg = ((BufferedImage) pcImage).getSubimage(pcX*93, 0, 93, 94);
//			g2.drawImage(subImg, 410, 150, 80, 80, null);
//		}
//		
//		hero.drawHero(g2);
//	}
//	
//	@Override
//	public void enter()
//	{
//		int x = (int) hero2D.getPosition().getX();
//		int y = (int) hero2D.getPosition().getY();
//		if(x > 410 && x < 490)
//		{
//			if(y > 140 && y < 240)
//				world.setClosedL1M20(false);
//		}
//	}
//	
//	@Override
//	public void actionPerformed(ActionEvent arg0)
//	{
//		closeCounter = (closeCounter+1)%12;
//		if(closeCounter == 0 && !world.isClosedL1M20() && closeY < 170)
//			closeY++;
//		update();
//		pcCounter = (pcCounter+1)%5;
//		if(pcCounter == 4 && world.isClosedL1M20())
//			pcX = (pcX+1)%4;
//		for(int i=0; i<5; i++) 
//		{
//			world2D.step();
//			validate();
//		}
//		super.actionPerformed(arg0);
//	}
//	
//	public void update()
//	{
//		b1.setPosition(390, 290+closeY);
//		b2.setPosition(510, 290+closeY);
//		b3.setPosition(450, 330+closeY);
//		b4.setPosition(370, 270+closeY);
//		b5.setPosition(410, 310+closeY);
//		b6.setPosition(490, 310+closeY);
//		b7.setPosition(530, 270+closeY);
//	}
//}
