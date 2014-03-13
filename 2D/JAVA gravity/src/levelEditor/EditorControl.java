package levelEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import levelEditor.model.EditorLevelInformation;
import levelEditor.model.EditorObjectType;
import levelEditor.model.HiScoreEntry;

public class EditorControl implements Serializable
{
	private static final long					serialVersionUID	= 1L;
	private Map<String, EditorLevelInformation>	levels;
	private List<HiScoreEntry>					hiScore;
	private boolean								editorEnabled;
	private long								spikeN, wallN, platformN, dPlatformN, blockN, dBlockN, omruN, pcN, doorN;

	public static void main(String[] arg)
	{
		new EditorControl();
	}
	
	public EditorControl(Map<String, EditorLevelInformation> levels, List<HiScoreEntry> hiScore, boolean editorEnabled)
	{
		this.levels = levels;
		this.hiScore = hiScore;
		this.editorEnabled = editorEnabled;
		new EditorFrame(this.levels, this, false);
	}

	public EditorControl()
	{
		levels = new TreeMap<String, EditorLevelInformation>();
		hiScore = new ArrayList<HiScoreEntry>();
		for (int i = 15; i > 0; i--)
		{
			if (i == 15)
				hiScore.add(new HiScoreEntry("-----", 90000, 30));
			else
				hiScore.add(new HiScoreEntry("-----", 360000 - i * 12000, 103 - i * 3));
		}
		new EditorFrame(levels, this, true);
	}

	public Map<String, EditorLevelInformation> getLevels()
	{
		return levels;
	}

	public void setLevels(Map<String, EditorLevelInformation> levels)
	{
		this.levels = levels;
	}

	public List<HiScoreEntry> getHiScore()
	{
		return hiScore;
	}

	public void setHiScore(List<HiScoreEntry> hiScore)
	{
		this.hiScore = hiScore;
	}

	public boolean isEditorEnabled()
	{
		return editorEnabled;
	}

	public void setEditorEnabled(boolean editorEnabled)
	{
		this.editorEnabled = editorEnabled;
	}
	
	public long getNext(EditorObjectType type)
	{
		switch(type)
		{
		case BLOCK:
			blockN++;
			return blockN;
		case DEADLYBLOCK:
			dBlockN++;
			return dBlockN;
		case DEADLYPLATFORM:
			dPlatformN++;
			return dPlatformN;
		case DOOR:
			doorN++;
			return doorN;
		case OMRU:
			omruN++;
			return omruN;
		case PC:
			pcN++;
			return pcN;
		case PLATFORM:
			platformN++;
			return platformN;
		case SPIKE:
			spikeN++;
			return spikeN;
		case WALL:
			wallN++;
			return wallN;
		default:
			break;
		}
		return 0;
	}
}
