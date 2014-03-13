package game.world;

import game.GameFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.swing.Timer;

import levelEditor.model.EditorLevelInformation;
import levelEditor.model.EditorObjectInformation;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

public class GameWorld implements ActionListener
{
	private World					world2D;
	private Vector2f				gravity;
	private GameHero				hero;
	private GameFrame				frame;
	private int						timePlayed, deaths;
	public Timer					time;
	private boolean					gravitySuit;
	private boolean					dead;
	private AudioInputStream		stream;
	private AudioFormat				format;
	private DataLine.Info			info;
	private Clip					clip;
	private boolean					audioMuted	= false;
	private float					volume;
	private FloatControl			volumeControl;
	private Vector2f				savePos;
	private int						saveLvl, saveMap;
	private EditorLevelInformation	currentLevel;
	private Map<String, String>		DoorPCLink;
	private boolean					closedL1M3	= true;

	public GameWorld()
	{
		gravitySuit = false;
		gravity = new Vector2f(.0f, 30.0f);
		world2D = new World(gravity, 10, new QuadSpaceStrategy(200, 5));
		hero = new GameHero(this);
		deaths = 0;
		dead = false;
		time = new Timer(1000 / 100, this);
		saveLvl = 1;
		saveMap = 3;
		savePos = new Vector2f(450, 400);
		DoorPCLink = new TreeMap<String, String>();

		try
		{
			stream = AudioSystem.getAudioInputStream(getClass().getResource("/game/source/8 Bit Portal - Still Alive.wav"));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void flip()
	{
		if (gravitySuit)
		{
			float y = gravity.y * -1;
			world2D.setGravity(0, y);
		}
	}

	public void setFrame(GameFrame frame)
	{
		this.frame = frame;
	}

	public void killHero()
	{
		if (!dead)
		{
			setDead(true);
			deaths++;
			frame.loadMap(saveLvl, saveMap, savePos);
		}
	}

	public World getWorld2D()
	{
		return world2D;
	}

	public void setWorld2D(World world2d)
	{
		world2D = world2d;
	}

	public Vector2f getGravity()
	{
		return gravity;
	}

	public void setGravity(Vector2f gravity)
	{
		this.gravity = gravity;
	}

	public GameHero getHero()
	{
		return hero;
	}

	public float getX()
	{
		return hero.getHeroBody().getPosition().getX();
	}

	public float getY()
	{
		return hero.getHeroBody().getPosition().getY();
	}

	public int getSaveMap()
	{
		return saveMap;
	}

	public boolean isClosed(String pc)
	{
		return currentLevel.getActivatedComps().get(pc);
	}

	public void setClosed(String pc, boolean set)
	{
		currentLevel.getActivatedComps().remove(pc);
		currentLevel.getActivatedComps().put(pc, set);
	}

	public boolean isClosedDoor(String door)
	{
		String tempPC = DoorPCLink.get(door);
		return currentLevel.getActivatedComps().get(tempPC);
	}

	public boolean isGravitySuit()
	{
		return gravitySuit;
	}

	public void setGravitySuit(boolean gravitySuit)
	{
		this.gravitySuit = gravitySuit;
	}

	public void setSaveSpot(Vector2f position)
	{
		savePos = position;
		saveLvl = frame.getCurLvl();
		saveMap = frame.getCurMap();
	}

	public boolean isDead()
	{
		return dead;
	}

	public void setDead(boolean dead)
	{
		this.dead = dead;
	}

	public int getTimePlayed()
	{
		return timePlayed;
	}

	public void setTimePlayed(int timePlayed)
	{
		this.timePlayed = timePlayed;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		frame.requestFocus();
		updateMusic();
		timePlayed++;
	}

	private void updateMusic()
	{
		if (getVolumeInPerCent() == 0)
			audioMuted = true;
		else
			audioMuted = false;
		volumeControl.setValue(volume);
		if (!clip.isActive() && gravitySuit && !audioMuted)
		{
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		if (clip.isActive() && audioMuted)
			clip.stop();
	}

	public String slashPlayed(int time)
	{
		String s = "";
		int hundredth = time % 100;
		int sec = (time / 100) % 60;
		int min = (time / 100 / 60) % 60;
		int hour = time / 100 / 60 / 60;
		s += hour + ":";
		if (min < 10)
			s += "0" + min + ":";
		else
			s += min + ":";
		if (sec < 10)
			s += "0" + sec + ":";
		else
			s += sec + ":";
		if (hundredth < 10)
			s += "0" + hundredth;
		else
			s += hundredth;
		return s;
	}

	public String getDeaths()
	{
		String s = "";
		if (deaths > 0)
			s += deaths;
		return s;
	}

	public String closedDoors()
	{
		int doors = 0;
		Map<String, Boolean> tempMap = currentLevel.getActivatedComps();
		for (Map.Entry<String, Boolean> b : tempMap.entrySet())
		{
			if (b.getValue())
				doors++;
		}
		if (doors == 0)
			return "cleared";
		else
			return "" + doors;
	}

	public int getDeathCount()
	{
		return deaths;
	}

	public boolean isAudioMuted()
	{
		return audioMuted;
	}

	public void setAudioMuted(boolean audioMuted)
	{
		this.audioMuted = audioMuted;
	}

	public int getVolumeInPerCent()
	{
		float volumePerCent = (volume + 54) * 100 / 60;
		return Math.round(volumePerCent);
	}

	public void incrementVolume()
	{
		if (getVolumeInPerCent() < 100)
		{
			int newVolumePerCent = getVolumeInPerCent() + 5;
			setVolume((newVolumePerCent * 60 / 100) - 54);
		}
	}

	public void decreaseVolume()
	{
		if (getVolumeInPerCent() > 0)
		{
			int newVolumePerCent = getVolumeInPerCent() - 5;
			setVolume((newVolumePerCent * 60 / 100) - 54);
		}
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
		updateMusic();
	}

	public EditorLevelInformation getCurrentLevel()
	{
		return currentLevel;
	}

	public void setCurrentLevel(EditorLevelInformation currentLevel)
	{
		this.currentLevel = currentLevel;
		Map<String, List<EditorObjectInformation>> tempMap = currentLevel.getComps();
		if (!tempMap.isEmpty())
		{
			for (List<EditorObjectInformation> tempList : tempMap.values())
			{
				if (tempList.size() > 1)
				{
					for (int i = 1; i < tempList.size(); i++)
						DoorPCLink.put(tempList.get(i).toString(), tempList.get(0).toString());
				}
			}
		}
	}

	public boolean isClosedL1M3()
	{
		return closedL1M3;
	}

	public void setClosedL1M3(boolean closedL1M3)
	{
		this.closedL1M3 = closedL1M3;
	}
}
