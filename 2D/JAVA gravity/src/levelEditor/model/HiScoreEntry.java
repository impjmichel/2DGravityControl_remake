package levelEditor.model;

import java.io.Serializable;

public class HiScoreEntry implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	private String				name;
	private int					time;
	private int					deaths;

	public HiScoreEntry(String name, int time, int deaths)
	{
		this.name = name;
		this.time = time;
		this.deaths = deaths;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public int getDeaths()
	{
		return deaths;
	}

	public void setDeaths(int deaths)
	{
		this.deaths = deaths;
	}
}
