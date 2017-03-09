package code.UI;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Options extends JMenu
{
	private JMenuItem escapeTime = null;
	
	private final String itemTitle = "Escape Time";
	
	public Options()
	{
		try
		{
			if(itemTitle != null)
			{
				new JMenu("Options");
				setMnemonic(KeyEvent.VK_F);
				getAccessibleContext().setAccessibleDescription("Change escape");
				escapeTime = new JMenuItem(itemTitle);
				escapeTime.getAccessibleContext().setAccessibleDescription("Change the escape time for the fractal");
				//escapeTime.addActionListener((e)->{ new Fractal.setEscapeDistance(double num); });
				add(escapeTime);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			e.toString();
		}
	}
}
