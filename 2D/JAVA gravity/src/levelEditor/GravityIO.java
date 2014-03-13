package levelEditor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class GravityIO
{
	private EditorControl control;
	
	public GravityIO(EditorControl control)
	{
		this.control = control;
	}
	
	public void load(String filename) throws IOException, ClassNotFoundException
	{	
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{			
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			Object read = in.readObject();
			if(read instanceof EditorControl)
			{
				control.getLevels().clear();
				
				control.getLevels().putAll( ((EditorControl) read).getLevels() );
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			in.close();
		}
	}
	
	public void save(String filename) throws IOException
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(control);
		}
		catch (IOException ex)
		{
			throw ex;
		}
		finally
		{
			out.close();
		}
	}
}
