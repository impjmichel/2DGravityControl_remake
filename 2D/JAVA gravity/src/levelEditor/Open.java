package levelEditor;

import java.io.File;
import java.io.IOException;

public class Open
{

	public static void main(String[] args)
	{
		Open.open("src/test.html");
	}

	public static void open(String url)
	{
		File file = new File(url);
		String str;
		try
		{
			str = file.getPath();
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + str);
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
