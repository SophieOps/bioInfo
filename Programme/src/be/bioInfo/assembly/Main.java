package be.bioInfo.assembly;

import be.bioInfo.assembly.view.MainFrame;
/**
 * Call the main user interface for the project.
 * 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 */
public class Main {

	/**
	 * Main method, allows to launch the project
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.setProperty( "file.encoding", "UTF-8" );
		MainFrame mainFrame = new MainFrame();
	}

}
