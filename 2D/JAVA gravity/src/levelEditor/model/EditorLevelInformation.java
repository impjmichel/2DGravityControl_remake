package levelEditor.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EditorLevelInformation implements Serializable
{
	private static final long							serialVersionUID	= 1L;
	private Map<String, EditorMapInformation>			maps;
	private Map<String, List<EditorObjectInformation>>	comps;
	private Map<String, Boolean>						activatedComps;

	public EditorLevelInformation(Map<String, EditorMapInformation> maps, Map<String, List<EditorObjectInformation>> comps, Map<String, Boolean> activatedComps)
	{
		super();
		this.maps = maps;
		this.comps = comps;
		this.activatedComps = activatedComps;
	}

	public Map<String, EditorMapInformation> getMaps()
	{
		return maps;
	}

	public void setMaps(Map<String, EditorMapInformation> maps)
	{
		this.maps = maps;
	}

	public Map<String, List<EditorObjectInformation>> getComps()
	{
		return comps;
	}

	public void setComps(Map<String, List<EditorObjectInformation>> comps)
	{
		this.comps = comps;
	}

	public Map<String, Boolean> getActivatedComps()
	{
		return activatedComps;
	}

	public void setActivatedComps(Map<String, Boolean> activatedComps)
	{
		this.activatedComps = activatedComps;
	}
}
